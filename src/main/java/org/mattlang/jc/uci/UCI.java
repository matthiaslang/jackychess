package org.mattlang.jc.uci;

import java.io.*;
import java.util.Optional;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class UCI {

    Logger logger = Logger.getLogger("UCILOG");

    private LinkedBlockingQueue<String> inQueue = new LinkedBlockingQueue<>();
    private LinkedBlockingQueue<String> outQueue = new LinkedBlockingQueue<>();

    private  PrintStream out;

    private boolean finished = false;

    public static final UCI instance = new UCI();

    private UCI() {
    }

    public void putCommand(String cmd) {
        logger.info("OUT: " + cmd);
        out.println(cmd);
    }

    public Optional<String> readCommand() {
        try {
            return Optional.ofNullable(inQueue.poll(1000, TimeUnit.SECONDS));
        } catch (InterruptedException e) {
            return Optional.empty();
        }
    }

    public void attachStreams() throws IOException {
        attachStreams(System.in, System.out);
    }

    public void attachStreams(final InputStream in, final PrintStream out) throws IOException {
        this.out = out;
        Thread inThread = new Thread(
                () -> {
                    try {
                        gobbleIn(in);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        inThread.start();
    }

    private void gobbleIn(InputStream in) throws IOException {
        LineNumberReader r = new LineNumberReader(new InputStreamReader(in));
        while (!finished) {
            String line = r.readLine();
            logger.info("IN: " + line);
            inQueue.add(line);
        }
    }

    public void quit() {
        finished = true;
    }
}
