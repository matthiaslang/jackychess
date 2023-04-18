package org.mattlang.jc.engine.evaluation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.mattlang.jc.StopWatch;

public class ToolsTest {

    public static final int MAXROUNDS = 1000000;

    @Test
    public void testManhattenDistance() {
        assertThat(Tools.calcManhattanDistance(0, 63)).isEqualTo(14);
        assertThat(Tools.calcManhattanDistance(0, 1)).isEqualTo(1);
        assertThat(Tools.calcManhattanDistance(0, 8)).isEqualTo(1);
        assertThat(Tools.calcManhattanDistance(0, 9)).isEqualTo(2);
        assertThat(Tools.calcManhattanDistance(0, 10)).isEqualTo(3);

    }


    @Test
    public void speedComparisonDistance() {
        StopWatch watch = new StopWatch();

        long result = 0;
        watch.start();
        for (int i = 0; i < MAXROUNDS; i++) {
            for (int j = 0; j < 64; j++) {
                for (int k = 0; k < 64; k++) {
                    result += Tools.calcDistance(j,k);
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
                    result += Tools.distance(j,k);
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
                    result += Tools.calcManhattanDistance(j,k);
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
                    result += Tools.manhattanDistance(j,k);
                }
            }
        }
        watch.stop();
        System.out.println(watch.toString());

        System.out.println(Long.toString(result));
    }
}