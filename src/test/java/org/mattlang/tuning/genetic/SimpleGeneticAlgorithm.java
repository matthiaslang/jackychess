package org.mattlang.tuning.genetic;

import java.io.File;
import java.util.logging.Logger;

import org.mattlang.jc.StopWatch;
import org.mattlang.jc.tools.MarkdownAppender;
import org.mattlang.tuning.DataSet;
import org.mattlang.tuning.evaluate.ParamTuneableEvaluateFunction;
import org.mattlang.tuning.evaluate.ParameterSet;
import org.mattlang.tuning.tuner.OptParameters;

import lombok.Data;

/**
 * got it from:
 * https://github.com/eugenp/tutorials/blob/master/algorithms-modules/algorithms-genetic/
 */

@Data
public class SimpleGeneticAlgorithm {

    private static final Logger LOGGER = Logger.getLogger(SimpleGeneticAlgorithm.class.getSimpleName());

    private static final double uniformRate = 0.5;
    private static final double mutationRate = 0.025;
    private static final int tournamentSize = 5;
    private static final boolean elitism = true;

    private final File outputDir;
    private final OptParameters params;
    private final DataSet dataset;
    private final MarkdownAppender markdownAppender;
    private final ParamTuneableEvaluateFunction evaluate;
    private final double delta;
    private int maxGenCount = 100000;
    private ParameterSet bestSolutionSoFar;

    public SimpleGeneticAlgorithm(File outputDir, OptParameters params, ParamTuneableEvaluateFunction evaluate,
            DataSet dataset,
            MarkdownAppender markdownAppender) {
        this.outputDir = outputDir;
        this.params = params;
        this.evaluate = evaluate;
        this.dataset = dataset;
        this.delta = params.getDelta();
        this.markdownAppender = markdownAppender;
    }
    //    private static byte[] solution = new byte[64];

    public ParameterSet runAlgorithm(int populationSize, ParameterSet parameterSet) {

        bestSolutionSoFar = parameterSet.copy();
        //        setSolution(solution);
        Population myPop = new Population(populationSize, bestSolutionSoFar, true);
        myPop.calcFitness(dataset, evaluate);

        double currError = e(bestSolutionSoFar);
        System.out.println("Starting gene: : " + currError);
        markdownAppender.append(w -> w.paragraph("Genetic Error at start: " + currError));

        int generationCount = 1;
        StopWatch stopWatch = new StopWatch();

        while (generationCount < maxGenCount
                && myPop.getFittest().getFitness() > delta
            //                && myPop.getFittest(dataset, evaluate).getFitness(dataset, evaluate) < currError - delta
        ) {

            if (stopWatch.timeElapsed(5 * 60000)) {
                progressInfo(parameterSet, myPop.getFittest().getFitness(), generationCount, stopWatch);
            }

            String generationInfoMsg =
                    "Generation: " + generationCount + " Correct genes found: " + myPop.getFittest().getFitness();
            System.out.println(generationInfoMsg);
            markdownAppender.append(w -> w.paragraph(generationInfoMsg));

            myPop = evolvePopulation(myPop);
            myPop.calcFitness(dataset, evaluate);

            generationCount++;
        }
        System.out.println("Solution found!");
        System.out.println("Generation: " + generationCount);
        System.out.println("Genes: ");
        System.out.println(myPop.getFittest());
        return myPop.getFittest().getParameterSet();
    }

    private void progressInfo(ParameterSet parameterSet, double bestE, int round, StopWatch stopWatch) {
        LOGGER.info(stopWatch.getFormattedCurrDuration() + ": round " + round
                + ", curr Error= " + bestE);
        LOGGER.info(parameterSet.collectParamDescr());
        parameterSet.writeParamDescr(outputDir);

        markdownAppender.append(w -> {
            w.paragraph(stopWatch.getFormattedCurrDuration() + ": round " + round
                    + ", curr Error= " + bestE);
        });
    }

    public Population evolvePopulation(Population pop) {
        int elitismOffset;
        Population newPopulation = new Population(pop.getIndividuals().size());

        if (elitism) {
            newPopulation.getIndividuals().add(pop.getFittest());
            elitismOffset = 1;
        } else {
            elitismOffset = 0;
        }

        for (int i = elitismOffset; i < pop.getIndividuals().size(); i++) {
            Individual indiv1 = tournamentSelection(pop);
            Individual indiv2 = tournamentSelection(pop);
            Individual newIndiv = crossover(indiv1, indiv2);
            newPopulation.getIndividuals().add(i, newIndiv);
        }

        for (int i = elitismOffset; i < newPopulation.getIndividuals().size(); i++) {
            mutate(newPopulation.getIndividual(i));
        }

        return newPopulation;
    }

    private Individual crossover(Individual indiv1, Individual indiv2) {
        Individual newSol = new Individual(indiv1.getParameterSet().copy());
        for (int i = 0; i < newSol.getDefaultGeneLength(); i++) {
            if (Math.random() <= uniformRate) {
                newSol.setSingleGene(i, indiv1.getSingleGene(i));
            } else {
                newSol.setSingleGene(i, indiv2.getSingleGene(i));
            }
        }
        return newSol;
    }

    private void mutate(Individual indiv) {
        for (int i = 0; i < indiv.getDefaultGeneLength(); i++) {
            if (Math.random() <= mutationRate) {

                Population.randomizeParamVal(indiv.getGene(i));
            }
        }
    }

    private Individual tournamentSelection(Population pop) {
        Population tournament = new Population(tournamentSize);
        for (int i = 0; i < tournamentSize; i++) {
            int randomId = (int) (Math.random() * pop.getIndividuals().size());
            tournament.getIndividuals().add(i, pop.getIndividual(randomId));
        }
        Individual fittest = tournament.getFittest();
        return fittest;
    }

    private double e(ParameterSet parameterSet) {
        return dataset.calcError(evaluate, parameterSet);
    }
}
