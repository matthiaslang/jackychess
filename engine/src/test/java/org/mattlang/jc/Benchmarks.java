package org.mattlang.jc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class Benchmarks {

    /**
     * Does a benchmark of some code reducing warm up of JIT by rerunning the code
     * till its duration stabilizes.
     * The code to benchmark should be some fairly huge piece of work to let the measurement
     * work properly.
     *
     * The test code is at least 3 times executed, at max 10 times till its execution time stabilizes by 5% differences.
     * The method returns the minimum execution time of all rounds.
     *
     * @param doSomethingToMeasure
     * @return
     */
    public static <T> StopWatch benchmark(
            String benchMarkName,
            Callable<T> doSomethingToMeasure) {
        return benchmark(benchMarkName, doSomethingToMeasure, 10).getWatch();
    }

    public static StopWatch benchmark(
            String benchMarkName,
            Runnable doSomethingToMeasure) {
        return benchmark(benchMarkName, wrap(doSomethingToMeasure), 10).getWatch();
    }

    public static <T> BenchmarkResults benchmarkWithResults(
            String benchMarkName,
            Callable<T> doSomethingToMeasure) {
        ExecResults<T> bmResult = benchmark(benchMarkName, doSomethingToMeasure, 10);
        return new BenchmarkResults<T>(benchMarkName, bmResult, null, null);
    }

    public static BenchmarkResults benchmarkWithResults(
            String benchMarkName,
            Runnable doSomethingToMeasure) {
        ExecResults bmResult = benchmark(benchMarkName, wrap(doSomethingToMeasure), 10);
        return new BenchmarkResults(benchMarkName, bmResult, null, null);
    }

    private static Callable wrap(Runnable doSomethingToMeasure) {
        return () -> {
            doSomethingToMeasure.run();
            return null;
        };
    }

    public static <T> ExecResults<T> benchmark(
            String benchMarkName,
            Callable<T> doSomethingToMeasure, int maxRounds) {

        int rounds = 0;
        long lastMeasuredTime = 0;
        long measuredTime = 0;

        StopWatch minWatch = new StopWatch();

        System.out.println("starting benchmark " + benchMarkName + " ...");
        List<T> results = new ArrayList<>();
        do {
            lastMeasuredTime = measuredTime;
            rounds++;
            StopWatch watch = new StopWatch();
            watch.start();
            try {
                results.add(doSomethingToMeasure.call());
            } catch (Exception e) {
                watch.stop();
                return new ExecResults<>(watch, results, e);
            }
            watch.stop();
            measuredTime = watch.getDuration();

            if (measuredTime < lastMeasuredTime || lastMeasuredTime == 0) {
                minWatch = watch;
            }

            System.out.println("round: " + rounds + " last: " + lastMeasuredTime + " current:" + measuredTime);

            if (diffAccepted(lastMeasuredTime, measuredTime) && rounds >= 3) {
                break;
            }
        } while (rounds < maxRounds);
        System.out.println("benchmark result " + benchMarkName + ": " + minWatch.toString());
        return new ExecResults<>(minWatch, results);
    }

    private static boolean diffAccepted(long lastMeasuredTime, long measuredTime) {
        if (lastMeasuredTime == 0 || measuredTime == 0) {
            return false;
        }
        long diff = Math.abs(lastMeasuredTime - measuredTime);
        long pct = diff * 100 / lastMeasuredTime;
        return pct <= 5;
    }


}
