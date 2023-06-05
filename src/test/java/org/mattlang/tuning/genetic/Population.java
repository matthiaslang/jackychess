package org.mattlang.tuning.genetic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.mattlang.tuning.DataSet;
import org.mattlang.tuning.Intervall;
import org.mattlang.tuning.TuningParameter;
import org.mattlang.tuning.evaluate.ParamTuneableEvaluateFunction;
import org.mattlang.tuning.evaluate.ParameterSet;

import lombok.Data;

@Data
public class Population {

    private List<Individual> individuals;

    private static Random random = new Random(4711L);

    public Population(int size) {
        individuals = new ArrayList<>(size);
    }

    public Population(int size, ParameterSet parameterSet, boolean createNew) {
        individuals = new ArrayList<>();
        if (createNew) {
            createNewRandomizedPopulation(size, parameterSet);
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

    private void createNewRandomizedPopulation(int size, ParameterSet params) {
        // add the original set:
        individuals.add(new Individual(randomize(params.copy())));

        for (int i = 1; i < size; i++) {
            Individual newIndividual = new Individual(randomize(params.copy()));
            individuals.add(newIndividual);
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
