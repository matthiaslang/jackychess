package org.mattlang.tuning.tuner;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GeneticParams {

    /**
     * Genetic: Eval Configs to start from. Each of them will build one Individual of the start population.
     * The Rest of the population will be filled with random start data.
     */
    private List<String> startEvalConfigs;

    /**
     * Genetic: population size.
     */
    private int populationSize = 50;

    /**
     * creates for each start config n mutated clones.
     */
    private int mutateStartConfigs = 0;

    /**
     * mutation rate of genes.
     */
    private double mutationRate = 0.025;

    private double uniformRate = 0.5;
    private int tournamentSize = 5;
    private boolean elitism = true;

    private int maxGenCount = 100000;
}
