package org.mattlang.tuning.tuner;

import static java.util.Arrays.asList;

import java.io.IOException;

public class LocalOptExperiment02 {

    public static final String testingProject = "C:\\projekte\\cygwin_home\\mla\\jackyChessDockerTesting\\";

    public static final String SF_PGN = testingProject + "results\\tuningdata\\sf\\run20230914_1415\\tournament.pgn";

    public static final String QUIET_LABELED_DEBUG =
            testingProject + "tuningdata\\quiet-labeled_debug.epd";

    public static final String QUIET_LABELED_EPD =
            testingProject + "tuningdata\\quiet-labeled.epd";

    /** not working...*/
    public static final String CCRL3400 =
            testingProject + "tuningdata\\ccrl-40-15-elo-3400.epd";

    /** not working...*/
    public static final String CCRL3300 =
            testingProject + "tuningdata\\ccrl-40-15-elo-3300.epd";

    /** not working...*/
    public static final String CCRL3200 =
            testingProject + "tuningdata\\ccrl-40-15-elo-3200.epd";

    /** not working...*/
    public static final String CCRL3100 =
            testingProject + "tuningdata\\ccrl-40-15-elo-3100.epd";

    /** not working...*/
    public static final String CCRL3000 =
            testingProject + "tuningdata\\ccrl-40-15-elo-3000.epd";

    /**
     * 60000 games with random openings between version 0.12.0 and 0.10.0 with very short time (depth=3, quiescence=1)
     */
    static String[] tournamentFiles;

    static {

        tournamentFiles = new String[] {
                testingProject + "results\\tuningdata\\veryshorttime\\run20220720_0949\\tournament.pgn",
                testingProject + "results\\tuningdata\\veryshorttime\\run20220720_1559\\tournament.pgn",
                testingProject + "results\\tuningdata\\veryshorttime\\run20220803_1456\\tournament.pgn",
                testingProject + "results\\tuningdata\\veryshorttime\\run20220804_1004\\tournament.pgn"
        };
    }

    static String[] tournamentFilesSFchess22k = new String[] {
            testingProject + "results\\tuningdata\\sf\\run20230914_1415\\tournament.pgn"
    };

    public static void main(String[] args) throws IOException {
        /**
         * using the zurich quiet labeled test set using all fens except those using special end game functions.
         */
        OptParameters params = OptParameters.builder()
                .name("king safety tuning after bugfixes")
                .evalParamSet("CURRENT")
                .optimizeRecalcOnlyDependendFens(false)
                .resetParametersBeforeTuning(true)
                .adjustK(false)
                .multiThreading(true)
                .threadCount(8)
                .delta(0.00000001)
                .stepGranularity(asList(10, 5, 3, 1))
                .removeDuplicateFens(true)
                .tunePst(false)
                .tuneMaterial(false)
                .tuneAdjustments(false)
                .tuneMobility(false)
                .tunePositional(false)
                .tunePawnEval(false)
                .tunePassedPawnEval(false)
                .tuneKingAttack(false)
                .tuneKingSafety(true)
                .tuneComplexity(true)
                .tuneThreats(false)
                //                .inputFiles(asList(QUIET_LABELED_EPD))
                .inputFiles(asList(QUIET_LABELED_DEBUG))
                .build();

        LocalOptimizationTuner.run(params);
    }
}
