package org.mattlang.tuning.genetic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.mattlang.tuning.DataSet;
import org.mattlang.tuning.Intervall;
import org.mattlang.tuning.TuningParameter;
import org.mattlang.tuning.evaluate.ParamTuneableEvaluateFunction;
import org.mattlang.tuning.evaluate.ParameterSet;
import org.mattlang.tuning.tuner.OptParameters;

import lombok.Data;

@Data
public class Population {

    private List<Individual> individuals;

    private static Random random = new Random(4711L);

    public Population(int size) {
        individuals = new ArrayList<>(size);
    }

    public Population(OptParameters params, ParameterSet exampleConfig, List<ParameterSet> startConfigs, boolean createNew) {
        individuals = new ArrayList<>();
        if (createNew) {
            createNewRandomizedPopulation(params, exampleConfig, startConfigs);
        }
    }

    protected Individual getIndividual(int index) {
        return individuals.get(index);
    }

    protected Individual getFittest() {
        Individual fittest = individuals.get(0);
        for (int i = 0; i < individuals.size(); i++) {
            if (fittest.getFitness() >= getIndividual(i).getFitness()) {
                fittest = getIndividual(i);
            }
        }
        return fittest;
    }

    private void createNewRandomizedPopulation(OptParameters params, ParameterSet exampleConfig, List<ParameterSet> startConfigs) {
        // add start configs:
        for (ParameterSet startConfig : startConfigs) {
            individuals.add(new Individual(startConfig.copy()));
            for (int i = 0; i < params.getMutateStartConfigs(); i++) {
                Individual mutatedIndividual = new Individual(startConfig.copy());
                mutate(params, mutatedIndividual);
                individuals.add(mutatedIndividual);
            }
        }
        int currPopulationCount=individuals.size();

        // fill rest of population with random configs:
        for (int i = currPopulationCount; i < params.getPopulationSize(); i++) {
            Individual newIndividual = new Individual(randomize(exampleConfig.copy()));
            individuals.add(newIndividual);
        }
    }

    public void mutate(OptParameters params, Individual indiv) {
        for (int i = 0; i < indiv.getDefaultGeneLength(); i++) {
            if (Math.random() <= params.getMutationRate()) {
                randomizeParamVal(indiv.getGene(i));
            }
        }
    }

    /**
     * randomize a parameter set.
     *
     * @param parameterSet
     * @return
     */
    private ParameterSet randomize(ParameterSet parameterSet) {
        for (TuningParameter param : parameterSet.getParams()) {
            randomizeParamVal(param);
        }
        return parameterSet;
    }

    public static void randomizeParamVal(TuningParameter parameter) {
        Intervall intervall = parameter.getIntervall();
        parameter.setValue(
                random.nextInt(intervall.getMaxIntVal() - intervall.getMinIntVal()) + intervall.getMinIntVal());
    }

    public void calcFitness(DataSet dataset, ParamTuneableEvaluateFunction evaluate) {
        for (Individual individual : individuals) {
            individual.calcFitness(dataset, evaluate);
        }
    }
}
