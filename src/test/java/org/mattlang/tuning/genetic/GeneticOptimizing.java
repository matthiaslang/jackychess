package org.mattlang.tuning.genetic;

import static java.util.Arrays.asList;
import static org.mattlang.tuning.tuner.LocalOptExperiment02.QUIET_LABELED_EPD;

import java.io.IOException;
import java.util.List;

import org.mattlang.tuning.tuner.GeneticParams;
import org.mattlang.tuning.tuner.OptParameters;

public class GeneticOptimizing {

    public static void main(String[] args) throws IOException {

        GeneticParams geneticParams = GeneticParams.builder()
                .populationSize(50)
                .mutateStartConfigs(10)
                .mutationRate(0.025)
                .uniformRate(0.5)
                .tournamentSize(5)
                .elitism(true)
                .maxGenCount(100000)
                .startEvalConfigs(List.of("CURRENT" /*"v_0_12_0", "v_0_13_0","v_0_14_3"*/ ))
                .mutateStartConfigs(10)
                .build();

        /**
         * using the zurich quiet labeled test set using all fens except those using special end game functions.
         */
        OptParameters params = OptParameters.builder()
                .name("genetic tuning test")
                .geneticParams(geneticParams)
                .evalParamSet("CURRENT")
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
