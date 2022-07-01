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

    public String getFormattedDuration(){
        Duration d = Duration.of(stop - start, ChronoUnit.MILLIS);
        return formatDuration(d);
    }

    public String getFormattedCurrDuration(){
        Duration d = Duration.of(getCurrDuration(), ChronoUnit.MILLIS);
        return formatDuration(d);
    }

    public static String formatDuration(Duration duration) {

       int millis= duration.getNano()/1_000_000;
        long seconds = duration.getSeconds();
        long absSeconds = Math.abs(seconds);
        String positive = String.format(
                "%d:%02d:%02d,%03d",
                absSeconds / 3600,
                (absSeconds % 3600) / 60,
                absSeconds % 60,
                millis);
        return seconds < 0 ? "-" + positive : positive;
    }
}
