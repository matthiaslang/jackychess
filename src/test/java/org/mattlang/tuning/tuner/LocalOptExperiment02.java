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
                .adjustK(true)
                .multiThreading(true)
                .threadCount(5)
                .delta(0.00000001)
                .stepGranularity( asList(   /*10, 5, 3,*/ 1 ))
                .removeDuplicateFens(true)
                .tunePst(false)
                .tuneMaterial(false)
                .tuneAdjustments(false)
                .tuneMobility(false)
                .tunePositional(false)
                .tunePawnEval(false)
                .tunePassedPawnEval(true)
                .tuneKingAttack(false)
                .tuneThreats(false)
                .inputFiles(asList(
                        "C:\\cygwin64\\home\\mla\\jackyChessDockerTesting\\tuningdata\\quiet-labeled.epd"))
                .build();

        LocalOptimizationTuner.run(params);
    }
}
