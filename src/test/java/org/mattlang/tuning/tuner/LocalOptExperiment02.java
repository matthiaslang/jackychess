package org.mattlang.tuning.tuner;

import static java.util.Arrays.asList;

import java.io.IOException;

public class LocalOptExperiment02 {

    public static void main(String[] args) throws IOException {
        /**
         * using the zurich quiet labeled test set using all fens except those using special end game functions.
         */
        OptParameters params = OptParameters.builder()
                .evalParamSet("TUNED01")
                .adjustK(false)
                .multiThreading(true)
                .threadCount(5)
                .delta(0.00000001)
                .stepGranularity( asList( /*20, 15, 10, 5, */3, 1 ))
                .removeDuplicateFens(true)
                .tunePst(true)
                .tuneMaterial(false)
                .tuneAdjustments(true)
                .tuneMobility(true)
                .tunePositional(true)
                .tunePawnEval(true)
                .tuneKingAttack(true)
                .tuneThreats(true)
                .inputFiles(asList(
                        "C:\\cygwin64\\home\\mla\\jackyChessDockerTesting\\tuningdata\\quiet-labeled.epd"))
                .build();

        LocalOptimizationTuner.run(params);
    }
}
