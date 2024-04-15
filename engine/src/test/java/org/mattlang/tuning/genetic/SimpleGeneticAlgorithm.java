package org.mattlang.tuning.genetic;

import java.io.File;
import java.util.List;
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

    private final File outputDir;
    private final OptParameters params;
    private final DataSet dataset;
    private final MarkdownAppender markdownAppender;
    private final ParamTuneableEvaluateFunction evaluate;
    private final double delta;


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

    public ParameterSet runAlgorithm(ParameterSet exampleConfig, List<ParameterSet> startConfigs) {

        Population myPop = new Population(params, exampleConfig, startConfigs, true);
        myPop.calcFitness(dataset, evaluate);
        double currError = myPop.getFittest().getFitness();

        System.out.println("Starting gene: : " + currError);
        markdownAppender.append(w -> w.paragraph("Genetic Error at start: " + currError));

        int generationCount = 1;
        StopWatch stopWatch = new StopWatch();

        while (generationCount < params.getGeneticParams().getMaxGenCount()
                && myPop.getFittest().getFitness() > delta
            //                && myPop.getFittest(dataset, evaluate).getFitness(dataset, evaluate) < currError - delta
        ) {

            if (stopWatch.timeElapsed(5 * 60000)) {
                progressInfo(myPop.getFittest().getParameterSet(), myPop.getFittest().getFitness(), generationCount,
                        stopWatch);
            }

            String generationInfoMsg = stopWatch.getFormattedCurrDuration() +
                    ": Generation: " + generationCount + " best genes found: " + myPop.getFittest().getFitness();
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
        myPop.getFittest().getParameterSet().writeParamDescr(outputDir);

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

        if (params.getGeneticParams().isElitism()) {
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
            newPopulation.mutate(params, newPopulation.getIndividual(i));
        }

        return newPopulation;
    }

    private Individual crossover(Individual indiv1, Individual indiv2) {
        Individual newSol = new Individual(indiv1.getParameterSet().copy());
        for (int i = 0; i < newSol.getDefaultGeneLength(); i++) {
            if (Math.random() <= params.getGeneticParams().getUniformRate()) {
                newSol.setSingleGene(i, indiv1.getSingleGene(i));
            } else {
                newSol.setSingleGene(i, indiv2.getSingleGene(i));
            }
        }
        return newSol;
    }

    private Individual tournamentSelection(Population pop) {
        Population tournament = new Population(params.getGeneticParams().getTournamentSize());
        for (int i = 0; i < params.getGeneticParams().getTournamentSize(); i++) {
            int randomId = (int) (Math.random() * pop.getIndividuals().size());
            tournament.getIndividuals().add(i, pop.getIndividual(randomId));
        }
        Individual fittest = tournament.getFittest();
        return fittest;
    }

}
