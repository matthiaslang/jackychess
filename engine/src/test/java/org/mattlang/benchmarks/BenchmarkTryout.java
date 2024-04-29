package org.mattlang.benchmarks;

import org.mattlang.jc.engine.evaluation.Tools;
import org.openjdk.jmh.annotations.*;

public class BenchmarkTryout {

    @State(Scope.Benchmark)
    public static class Constants {

        public int x = 17;
        public int y = 34;
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public int ToolsDistanceDirekt(Constants constants) {
        int sum = 0;
        for (int i = 0; i < constants.x; i++) {
            for (int j = 0; j < constants.y; j++) {
                sum += Tools.calcDistance(i, j);
            }
        }
        return sum;
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public int ToolsDistanceCached(Constants constants) {
        int sum = 0;
        for (int i = 0; i < constants.x; i++) {
            for (int j = 0; j < constants.y; j++) {
                sum += Tools.distance(i, j);
            }
        }
        return sum;
    }
}
