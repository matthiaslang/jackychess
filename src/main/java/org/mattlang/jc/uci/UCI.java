package org.mattlang.jc.uci;

import java.io.*;
import java.util.Optional;
import java.util.concurrent.LinkedBlockingQueue;

public class UCI {

    private LinkedBlockingQueue<String> inQueue = new LinkedBlockingQueue<>();
    private LinkedBlockingQueue<String> outQueue = new LinkedBlockingQueue<>();

    private boolean finished = false;

    public static final UCI instance = new UCI();

    private UCI() {
    }

    public void putCommand(String cmd) {
        outQueue.add(cmd);
    }

    public Optional<String> readCommand() {
        return Optional.ofNullable(inQueue.poll());
    }

    public void attachStreams(final InputStream in, final PrintStream out) throws IOException {
        Thread inThread = new Thread(
                () -> {
                    try {
                        gobbleIn(in);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        inThread.start();
        Thread outThread = new Thread(() -> {
            try {
                gobbleOut(out);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        outThread.start();
    }

    private void gobbleOut(PrintStream out) throws InterruptedException {
        while (!finished) {
            String line = outQueue.take();
            out.println(line);
        }
    }

    private void gobbleIn(InputStream in) throws IOException {
        LineNumberReader r = new LineNumberReader(new InputStreamReader(in));
        while (!finished) {
            String line = r.readLine();
            inQueue.add(line);
        }
    }
}
