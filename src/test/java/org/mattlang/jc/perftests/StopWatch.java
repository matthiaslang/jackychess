package org.mattlang.jc.perftests;

public class StopWatch {

    private long start = System.currentTimeMillis();
    private long stop;

    public void start() {
        start = System.currentTimeMillis();
    }

    public void stop() {
        stop = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        long diff = stop - start;
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        return String.format("m: %d s: %d", minutes, seconds);
    }
}
