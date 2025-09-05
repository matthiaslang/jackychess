package org.mattlang.jc.uci;

import org.assertj.core.api.Assertions;
import org.mattlang.jc.AppConfiguration;
import org.mattlang.jc.StopWatch;
import org.mattlang.jc.util.Logging;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import static java.util.stream.Collectors.toSet;

/**
 * Helper class to test the engine via the uci communication.
 * It attaches to the streams so that you can send and receive uci commands.
 *
 * Due to "static" behaviour of the engine, you should only use one instance of this class in all tests,
 * and not "quit" the engine inside tests, as this would exit the java process.
 */
public class UciCommunicationTester {

    private static final Logger LOGGER = Logger.getLogger(UciProcessor.class.getSimpleName());

    private Gobbler gobbler;

    private AtomicInteger uciProtocolCounter = new AtomicInteger(0);

    private LinkedBlockingQueue<String> inQueue = new LinkedBlockingQueue<>();

    private boolean finished = false;

    private PrintStream outputToUciEngine;

    public UciCommunicationTester() {
        LOGGER.info("creating new UciCommunicationTester");
    }

    /**
     * pipe in/out together with our own gobbler and start engine processing.
     *
     * @throws IOException
     */
    public void init() throws IOException {
        if (gobbler != null) {
            return; // already initialized
        }
        LOGGER.info("initialize Uci Communication");
        Logging.initLogging();
        gobbler = new Gobbler("Comm. Tester");

        PipedInputStream input = new PipedInputStream();
        PipedOutputStream output = new PipedOutputStream(input);
        PrintStream out = new PrintStream(output);

        PipedInputStream in2 = new PipedInputStream();
        PipedOutputStream output2 = new PipedOutputStream(in2);
        outputToUciEngine = new PrintStream(output2);

        UCI.instance.attachStreams(in2, out);
        LOGGER.info("starting new UciProcessor instance and communication thread");
        UciProcessor processor = new UciProcessor();
        Thread inThread = new Thread(
                () -> {
                    processor.start();
                });
        inThread.start();
        LOGGER.info("attach piped streams");
        gobbler.attachStreams(input, output2);
        startGobbleCollectThread();
    }

    private void startGobbleCollectThread() {
        Thread inThread = new Thread(
                () -> {
                    while (!finished) {
                        Optional<String> optCmd = gobbler.readCommand();
                        if (optCmd.isPresent()) {
                            String readValue = optCmd.get();
                            inQueue.add(readValue);

                            printUCI("engine", readValue);
                        }
                    }
                }, "Uci Communication Tester Input Gobbler Thread ");
        inThread.start();
    }

    public void write(String ucicmd) throws IOException {
        init();
        outputToUciEngine.println(ucicmd);
        printUCI("client", ucicmd);
    }

    public void expectBestmove(String... bestMoves) throws IOException {
        Set<BestmoveExpectation> theBestMoves =
                Arrays.stream(bestMoves).map(BestmoveExpectation::bestmoveWithPonder).collect(toSet());
        expectBestmove(theBestMoves.toArray(new BestmoveExpectation[0]));
    }

    public void expectBestmove(BestmoveExpectation... bestMoves) throws IOException {
        init();
        Set<String> theBestMoves =
                Arrays.stream(bestMoves).map(BestmoveExpectation::toUCIStringExpectation).collect(toSet());

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        while (stopWatch.getCurrDuration() < 5 * 100000) {
            Optional<String> optCmd = read();
            if (optCmd.isPresent()) {
                String result = optCmd.get();
                if (result.startsWith("info ")) {
                    // overread info
                    continue;
                }
                if (result.startsWith("bestmove ")) {
                    Assertions.assertThat(optCmd.get()).isIn(theBestMoves);
                    return;
                }
            }
        }
        Assertions.fail("no expected one of the bestmoves " + bestMoves);
    }

    /**
     * Read from gobbler input. Handling one timeout which might be occured meanwhile.
     *
     * @return
     */
    private Optional<String> read() {
        try {
            String readResult = inQueue.poll(2000, TimeUnit.MILLISECONDS);
            return Optional.ofNullable(readResult);
        } catch (InterruptedException e) {
            return Optional.empty();
        }
    }

    public void expect(String expectedUciString) throws IOException {
        init();
        Optional<String> readResult = read();
        // now we really expect a result and exactly that string:
        Assertions.assertThat(readResult).isPresent();
        Assertions.assertThat(readResult).contains(expectedUciString);
    }

    public void uciStartCommunication() throws IOException, InterruptedException {
        init();
        reset();

        write("uci");

        String version = AppConfiguration.getAppProps().getProperty("version");

        expect("id name JackyChess " + version);
        expect("id author Matthias Lang");

        expect("option name Threads type spin default 1 min 1 max 128");
        expect("option name UCI_Chess960 type check default false");
        expect("option name UCI_AnalyseMode type check default false");
        expect(
                "option name UCI_EngineAbout type string default JackyChess by Matthias Lang, see https://github.com/matthiaslang/jackychess");
        expect("option name Ponder type check default false");
        expect("option name Hash type spin default 128 min 1 max 32768");

        expect("uciok");

        write("isready");
        Thread.sleep(2000);
        expect("readyok");

        // standard options, no pondering, no analysis:
        write("setoption name Ponder value false");
        write("setoption name UCI_AnalyseMode value false");
    }

    /**
     * Resets its state, cleaning up anything from previous tests:
     */
    private void reset() {
        inQueue.clear();
        uciProtocolCounter.set(0);
    }

    public void consumeAllInfo() throws IOException {
        init();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        while (stopWatch.getCurrDuration() < 5 * 1000) {
            Optional<String> readResult = read();
            if (readResult.isEmpty()) {
                break;
            }
            if (readResult.get().startsWith("info ")) {
                // overread info
                continue;
            } else {
                Assertions.fail("unexpected read command: " + readResult.get());
            }
        }
    }

    /**
     * Write out uci protocol for logging.
     * Writes with a counter to understand better the order of asynchronous protocol communication and a who is the
     * source
     * of the sent protocol.
     *
     * @param who
     * @param uciprotocol
     */
    private void printUCI(String who, String uciprotocol) {
        int counter = uciProtocolCounter.incrementAndGet();
        String formattedCounter = String.format("%03d", counter);
        System.out.println(formattedCounter + " " + who + ": " + uciprotocol);
    }
}
