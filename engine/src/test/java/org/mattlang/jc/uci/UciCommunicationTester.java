package org.mattlang.jc.uci;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.Optional;
import java.util.logging.Logger;

import org.assertj.core.api.Assertions;
import org.mattlang.jc.StopWatch;
import org.mattlang.jc.util.Logging;

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
//        System.setProperty(LOGGING_ACTIVATE, "true");
//        System.setProperty(LOG_UCI, "true");
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
    }

    public void write(String ucicmd) throws IOException {
        init();
        System.out.println("UCI> " + ucicmd);
        outputToUciEngine.println(ucicmd);
    }

    public void expectOverread(String expectedUciString) throws IOException {
        init();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        while (stopWatch.getCurrDuration() < 5 * 100000) {
            Optional<String> optCmd = gobbler.readCommand();
            if (optCmd.isPresent()) {
                printLogOutput(optCmd.get());
                if (optCmd.get().equals(expectedUciString)) {
                    return;
                }
            }
        }
        Assertions.fail("no expected uci string " + expectedUciString);
    }

    private void printLogOutput(String uci) {
        System.out.println("UCI< " + uci);
    }

    public void expect(String expectedUciString) throws IOException {
        init();
        Optional<String> readResult = gobbler.readCommand();
        Assertions.assertThat(readResult).isPresent();
        printLogOutput(readResult.get());
        Assertions.assertThat(readResult.get()).isEqualTo(expectedUciString);
    }

    public void uciStartCommunication() throws IOException, InterruptedException {

        write("uci");

        expect("id name JackyChess 25.01");
        expect("id author Matthias Lang");

        expect("option name thinktime type spin default 15000 min 1000 max 600000");
        expect("option name maxdepth type spin default 63 min 3 max 63");
        expect("option name quiescence type spin default 63 min 0 max 63");
        expect("option name maxThreads type spin default 1 min 1 max 8");
        expect("option name UCI_Chess960 type check default false");
        expect("option name UCI_AnalyseMode type check default false");
        expect(
                "option name UCI_EngineAbout type string default JackyChess by Matthias Lang, see https://github.com/matthiaslang/jackychess");
        expect("option name Hash type spin default 128 min 1 max 2048");
        expect("option name searchalg type combo default MULTITHREAD var SINGLETHREAD var MULTITHREAD");

        expect("uciok");

        write("isready");
        Thread.sleep(2000);
        expect("readyok");
    }

    public void consumeAllInput() throws IOException {
        init();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        while (stopWatch.getCurrDuration() < 5 * 1000) {
            Optional<String> readResult = gobbler.readCommand();
            while (readResult.isPresent()) {
                readResult = gobbler.readCommand();
                if (readResult.isPresent()) {
                    printLogOutput(readResult.get());
                }
               if (stopWatch.getCurrDuration() < 5 * 1000){
                   break;
               }
            }
        }
    }
}
