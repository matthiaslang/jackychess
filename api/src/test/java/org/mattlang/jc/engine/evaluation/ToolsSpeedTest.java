package org.mattlang.jc.engine.evaluation;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mattlang.jc.StopWatch;
import org.mattlang.jc.board.Tools;

@Tag("SlowTests")
public class ToolsSpeedTest {

    public static final int MAXROUNDS = 1000000;

    @Test
    public void speedComparisonDistance() {
        StopWatch watch = new StopWatch();

        long result = 0;
        watch.start();
        for (int i = 0; i < MAXROUNDS; i++) {
            for (int j = 0; j < 64; j++) {
                for (int k = 0; k < 64; k++) {
                    result += Tools.calcDistance(j, k);
                }
            }

        }
        watch.stop();
        System.out.println(watch.toString());
        //        System.out.println(Integer.toString(result));

        watch.start();
        for (int i = 0; i < MAXROUNDS; i++) {
            for (int j = 0; j < 64; j++) {
                for (int k = 0; k < 64; k++) {
                    result += Tools.distance(j, k);
                }
            }
        }
        watch.stop();
        System.out.println(watch.toString());

        System.out.println(Long.toString(result));
    }

    @Test
    public void speedComparisonManhattanDistance() {
        StopWatch watch = new StopWatch();

        long result = 0;
        watch.start();
        for (int i = 0; i < MAXROUNDS; i++) {
            for (int j = 0; j < 64; j++) {
                for (int k = 0; k < 64; k++) {
                    result += Tools.calcManhattanDistance(j, k);
                }
            }

        }
        watch.stop();
        System.out.println(watch.toString());
        //        System.out.println(Integer.toString(result));

        watch.start();
        for (int i = 0; i < MAXROUNDS; i++) {
            for (int j = 0; j < 64; j++) {
                for (int k = 0; k < 64; k++) {
                    result += Tools.manhattanDistance(j, k);
                }
            }
        }
        watch.stop();
        System.out.println(watch.toString());

        System.out.println(Long.toString(result));
    }
}