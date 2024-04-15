package org.mattlang.tuning.genetic;

import org.mattlang.tuning.DataSet;
import org.mattlang.tuning.TuningParameter;
import org.mattlang.tuning.evaluate.ParamTuneableEvaluateFunction;
import org.mattlang.tuning.evaluate.ParameterSet;

import lombok.Getter;

public class Individual {

    @Getter
    private final ParameterSet parameterSet;
    @Getter
    protected int defaultGeneLength = 0;

    private double fitness = 0;
    private boolean fitnessCalculated;

    public Individual(ParameterSet parameterSet) {
        this.parameterSet = parameterSet;
        defaultGeneLength = parameterSet.getParams().size();
    }

    protected int getSingleGene(int index) {
        return parameterSet.getParams().get(index).getValue();
    }

    protected void setSingleGene(int index, int value) {
        parameterSet.getParams().get(index).setValue(value);
        fitness = 0;
        fitnessCalculated = false;
    }

    public double getFitness() {
        if (!fitnessCalculated) {
            throw new IllegalStateException("calculate fitness first!");
        }
        return fitness;
    }

    public void calcFitness(DataSet dataset, ParamTuneableEvaluateFunction evaluate) {
        fitness = dataset.calcError(evaluate, parameterSet);
        fitnessCalculated = true;
    }

    public TuningParameter getGene(int i) {
        return parameterSet.getParams().get(i);
    }
}
