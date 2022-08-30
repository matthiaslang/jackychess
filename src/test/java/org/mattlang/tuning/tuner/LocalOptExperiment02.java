package org.mattlang.tuning.tuner;

import java.io.IOException;
import java.util.Arrays;

public class LocalOptExperiment02 {

    public static void main(String[] args) throws IOException {
        /**
         * using the zurich quiet labeled test set using all fens except those using special end game functions.
         */
        OptParameters params = OptParameters.builder()
                .evalParamSet("TUNED01")
                .adjustK(false)
                .multiThreading(true)
                .threadCount(7)
                .removeDuplicateFens(true)
                .tunePst(true)
                .tuneMaterial(true)
                .tuneAdjustments(true)
                .inputFiles(Arrays.asList(
                        "C:\\cygwin64\\home\\mla\\jackyChessDockerTesting\\tuningdata\\quiet-labeled.epd"))
                .build();

        LocalOptimizationTuner.run(params);
    }
}
