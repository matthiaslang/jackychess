package org.mattlang.tuning.genetic;

import static java.util.Arrays.asList;

import java.io.IOException;
import java.util.List;

import org.mattlang.tuning.tuner.OptParameters;

public class GeneticOptimizing {

    public static final String QUIET_LABELED_DEBUG_EPD = "C:\\projekte\\cygwin_home\\mla\\jackyChessDockerTesting\\tuningdata\\quiet-labeled_debug.epd";
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
                .name("genetic tuning test")
                .populationSize(50)
                .startEvalConfigs(List.of("TUNED01" , /*"v_0_12_0", "v_0_13_0",*/ "v_0_14_3"))
                .mutateStartConfigs(10)
                .mutationRate(0.025)
                .uniformRate(0.5)
                .tournamentSize(5)
                .elitism(true)
                .maxGenCount(100000)

                .evalParamSet("TUNED01")
                .adjustK(false)
                .multiThreading(true)
                .threadCount(5)
                .delta(0.00000001)

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
//                .inputFiles(asList(QUIET_LABELED_EPD))
                .inputFiles(asList(QUIET_LABELED_EPD))
                .build();

        GeneticTuner.run(params);
    }
}
