package org.mattlang.jc;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class StopWatch {

    private long start = System.currentTimeMillis();
    private long stop;

    public void start() {
        start = System.currentTimeMillis();
    }

    public void stop() {
        stop = System.currentTimeMillis();
    }

    public long getDuration() {
        return stop - start;
    }

    @Override
    public String toString() {
        Duration d = Duration.of(stop - start, ChronoUnit.MILLIS);
        return d.toString();
    }

    public long getCurrDuration() {
        return System.currentTimeMillis() - start;
    }
}
