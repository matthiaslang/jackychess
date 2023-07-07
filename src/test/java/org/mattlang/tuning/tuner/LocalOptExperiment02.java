package org.mattlang.tuning.tuner;

import static java.util.Arrays.asList;

import java.io.IOException;

public class LocalOptExperiment02 {

    public static final String QUIET_LABELED_EPD = "C:\\projekte\\cygwin_home\\mla\\jackyChessDockerTesting\\tuningdata\\quiet-labeled.epd";
    public static final String CCRL3400 = "C:\\projekte\\cygwin_home\\mla\\jackyChessDockerTesting\\tuningdata\\ccrl-40-15-elo-3400.epd";
    public static final String CCRL3300 = "C:\\projekte\\cygwin_home\\mla\\jackyChessDockerTesting\\tuningdata\\ccrl-40-15-elo-3300.epd";
    public static final String CCRL3200 = "C:\\projekte\\cygwin_home\\mla\\jackyChessDockerTesting\\tuningdata\\ccrl-40-15-elo-3200.epd";
    public static final String CCRL3100 = "C:\\projekte\\cygwin_home\\mla\\jackyChessDockerTesting\\tuningdata\\ccrl-40-15-elo-3100.epd";
    public static final String CCRL3000 = "C:\\projekte\\cygwin_home\\mla\\jackyChessDockerTesting\\tuningdata\\ccrl-40-15-elo-3000.epd";

    public static void main(String[] args) throws IOException {
        /**
         * using the zurich quiet labeled test set using all fens except those using special end game functions.
         */
        OptParameters params = OptParameters.builder()
                .name("ccrl test")
                .evalParamSet("CURRENT")
                .optimizeRecalcOnlyDependendFens(false)
                .resetParametersBeforeTuning(false)
                .adjustK(false)
                .multiThreading(true)
                .threadCount(5)
                .delta(0.00000001)
                .stepGranularity( asList(   10, 5,  3, 1 ))
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
                .inputFiles(asList(QUIET_LABELED_EPD))
                .build();

        LocalOptimizationTuner.run(params);
    }
}
