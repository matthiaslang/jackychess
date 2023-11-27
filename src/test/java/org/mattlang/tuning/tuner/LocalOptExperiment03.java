package org.mattlang.tuning.tuner;

import static java.util.Arrays.asList;

import java.io.IOException;

public class LocalOptExperiment03 {

    public static final String QUIET_LABELED_EPD = "C:\\projekte\\cygwin_home\\mla\\jackyChessDockerTesting\\tuningdata\\quiet-labeled.epd";
    public static final String SF_PGN = "C:\\projekte\\cygwin_home\\mla\\jackyChessDockerTesting\\results\\tuningdata\\sf\\run20230914_1415\\tournament.pgn";

    public static void main(String[] args) throws IOException {
        /**
         * using the zurich quiet labeled test set using all fens except those using special end game functions.
         */
        OptParameters params = OptParameters.builder()
                .name("sf test")
                .evalParamSet("CURRENT")
                .optimizeRecalcOnlyDependendFens(false)
                .resetParametersBeforeTuning(false)
                .adjustK(true)
                .multiThreading(true)
                .threadCount(5)
                .delta(0.00000001)
                .stepGranularity( asList(   /*10, 5,  3,*/ 1 ))
                .removeDuplicateFens(true)
                .tunePst(true)
                .tuneMaterial(false)
                .tuneAdjustments(false)
                .tuneMobility(false)
                .tunePositional(false)
                .tunePawnEval(false)
                .tunePassedPawnEval(false)
                .tuneKingAttack(false)
                .tuneThreats(false)
//                .inputFiles(asList(QUIET_LABELED_EPD))
                .inputFiles(asList(SF_PGN))
                .build();

        LocalOptimizationTuner.run(params);
    }
}
