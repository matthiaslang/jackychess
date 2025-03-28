package org.mattlang.tuning.tuner;

import static java.util.Arrays.asList;

import java.io.IOException;

public class LocalOptExperiment03 {

    public static final String QUIET_LABELED_EPD = "C:\\projekte\\cygwin_home\\mla\\jackyChessDockerTesting\\tuningdata\\quiet-labeled.epd";
    public static final String LICHESS = "C:\\projekte\\cygwin_home\\mla\\jackyChessDockerTesting\\tuningdata\\lichess-big3-resolved.book";

    public static final String CCRLBLITZPREPARED = "C:\\projekte\\cygwin_home\\mla\\chessdata\\prepared\\ccrlblitz-prepared.epd";


    public static void main(String[] args) throws IOException {
        /**
         * using the zurich quiet labeled test set using all fens except those using special end game functions.
         */
        OptParameters params = OptParameters.builder()
                .name("tune new mob params")
                .evalParamSet("CURRENT")
                .optimizeMode(false)
                .resetParametersBeforeTuning(true)
                .adjustK(false)
                .k(1.43)
                .multiThreading(true)
                .threadCount(5)
                .delta(0.000001)
                .stepGranularity( asList(   1 ))
                .removeDuplicateFens(true)
                .tuneParams("mob.special.*|mob.positional.ext.*|mob.positional.center.*|mob.positional.rookP.*")
                .tunePst(false)
                .tuneMaterial(false)
                .tuneAdjustments(false)
                .tuneMobility(false)
                .tunePositional(false)
                .tunePawnEval(false)
                .tunePassedPawnEval(false)
                .tuneKingAttack(false)
                .tuneKingSafety(false)
                .tuneThreats(false)
                .tuneComplexity(false)
                .tuneMobilityTropism(false)
//                .inputFiles(asList(QUIET_LABELED_EPD))
                .inputFiles(asList(LICHESS))
                .build();

        LocalOptimizationTuner.run(params);
    }
}
