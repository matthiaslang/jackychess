package org.mattlang.jc.uci;

import java.io.*;
import java.util.Optional;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Gobbler for asynchronous reading from an input stream and writing to an output stream.
 */
public class Gobbler {

    Logger logger = Logger.getLogger("Gobbler");

    private LinkedBlockingQueue<String> inQueue = new LinkedBlockingQueue<>();

    private PrintStream out;

    private boolean finished = false;
    private static int gobblerCounter = 0;

    private String name = "noname";

    public Gobbler() {
    }

    public Gobbler(String name) {
        this.name = name;
    }

    public void putCommand(String cmd) {
        logger.info("OUT: " + cmd);
        out.println(cmd);
        out.flush();
    }

    public Optional<String> readCommand() {
        try {
            return Optional.ofNullable(inQueue.poll(1000, TimeUnit.SECONDS));
        } catch (InterruptedException e) {
            return Optional.empty();
        }
    }

    public void attachStreams(final InputStream in, final PrintStream out) throws IOException {
        this.out = out;
        Thread inThread = new Thread(
                () -> {
                    try {
                        gobbleIn(in);
                    } catch (IOException e) {
                        logger.log(Level.WARNING, "failed gobbling!", e);
                    }
                    logger.log(Level.INFO, "stopped gobbling");
                }, "Gobble Thread for " + name + gobblerCounter++);
        inThread.start();
    }

    public void attachStreams(final InputStream in, final OutputStream out) throws IOException {
        attachStreams(in, new PrintStream(out));
    }

    private void gobbleIn(InputStream in) throws IOException {
        LineNumberReader r = new LineNumberReader(new InputStreamReader(in));
        while (!finished) {
            String line = r.readLine();
            logger.info(name + " IN: " + line);
            if (line != null) {
                inQueue.add(line);
            }
        }
    }

    public void quit() {
        finished = true;
        logger.log(Level.INFO, "gobbling got quit message");
    }
}
