package org.mattlang.jc;

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
    public static StopWatch benchmark(
            String benchMarkName,
            Runnable doSomethingToMeasure)
    {
        return benchmark(benchMarkName, doSomethingToMeasure, 10);
    }


    public static StopWatch benchmark(
            String benchMarkName,
            Runnable doSomethingToMeasure, int maxRounds) {

        int rounds = 0;
        long lastMeasuredTime = 0;
        long measuredTime = 0;

        StopWatch minWatch = new StopWatch();

        System.out.println("starting benchmark " + benchMarkName + " ...");
        do {
            lastMeasuredTime = measuredTime;
            rounds++;
            StopWatch watch = new StopWatch();
            watch.start();

            doSomethingToMeasure.run();

            watch.stop();
            measuredTime = watch.getDuration();

            if (measuredTime < lastMeasuredTime || lastMeasuredTime == 0) {
                minWatch = watch;
            }

            System.out.println("round: " + rounds + " last: " + lastMeasuredTime + " current:" + measuredTime);

            if (diffAccepted(lastMeasuredTime, measuredTime) && rounds>=3) {
                break;
            }
        } while (rounds < maxRounds);
        System.out.println("benchmark result " + benchMarkName + ": " + minWatch.toString());
        return minWatch;
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
