package org.mattlang.tuning.tuner;

import static java.util.Arrays.asList;

import java.io.IOException;

public class LocalOptExperiment03 {

    public static final String QUIET_LABELED_EPD = "C:\\projekte\\cygwin_home\\mla\\jackyChessDockerTesting\\tuningdata\\quiet-labeled.epd";
    public static final String LICHESS = "C:\\projekte\\cygwin_home\\mla\\jackyChessDockerTesting\\tuningdata\\lichess-big3-resolved.book";

    public static void main(String[] args) throws IOException {
        /**
         * using the zurich quiet labeled test set using all fens except those using special end game functions.
         */
        OptParameters params = OptParameters.builder()
                .name("lichess test3")
                .evalParamSet("CURRENT")
                .optimizeRecalcOnlyDependendFens(false)
                .resetParametersBeforeTuning(false)
                .adjustK(false)
                .k(1.43)
                .multiThreading(true)
                .threadCount(5)
                .delta(0.000000001)
                .stepGranularity( asList(   1 ))
                .removeDuplicateFens(true)
                .tunePst(true)
                .tuneMaterial(false)
                .tuneAdjustments(true)
                .tuneMobility(true)
                .tunePositional(true)
                .tunePawnEval(true)
                .tunePassedPawnEval(true)
                .tuneKingAttack(true)
                .tuneThreats(true)
                .tuneComplexity(true)
                .tuneKingSafety(true)
                .tuneMobilityTropism(true)
//                .inputFiles(asList(QUIET_LABELED_EPD))
                .inputFiles(asList(LICHESS))
                .build();

        LocalOptimizationTuner.run(params);
    }
}
