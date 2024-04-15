package org.mattlang.jc.uci;

import static java.util.logging.Level.FINE;

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

    public static final String GOBBLER_LOGNAME = "Gobbler";
    Logger logger = Logger.getLogger(GOBBLER_LOGNAME);

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




    /**
     * put a uci command .

     * @param cmd
     */
    public void putCommand(String cmd) {
        if (logger.isLoggable(FINE)) {
            logger.log(FINE, "OUT: " + cmd);
        }
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
                    logger.log(FINE, "stopped gobbling");
                }, "Gobble Thread for " + name + gobblerCounter++);
        inThread.start();
    }

    public void attachStreams(final InputStream in, final OutputStream out) throws IOException {
        attachStreams(in, new PrintStream(out));
    }

    private void gobbleIn(InputStream in) throws IOException {
        LineNumberReader r = new LineNumberReader(new InputStreamReader(in));
        while (!finished && !Thread.currentThread().isInterrupted()) {
            String line = r.readLine();
            logger.fine(name + " IN: " + line);
            if (line != null) {
                inQueue.add(line);
            }
        }
    }

    public void quit() {
        finished = true;
        logger.log(FINE, "gobbling got quit message");
    }
}
