package org.mattlang.tuning;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import org.mattlang.jc.StopWatch;
import org.mattlang.jc.tools.MarkdownAppender;

public class LocalOptimizer implements Optimizer {

    private static final Logger LOGGER = Logger.getLogger(LocalOptimizer.class.getSimpleName());

    public static final int[] stepGranularity = { /*20, 15, 10,*/ 5, 3, 1 };
    /**
     * safety delta value to ensure that error is not only better by a minor calculation precision issue.
     */
    public static final double DELTA = 0.00000001;
    private final File outputDir;
    private final MarkdownAppender markdownAppender;
    private DataSet dataSet;

    private TuneableEvaluateFunction evaluate;

    public LocalOptimizer(File outputDir, MarkdownAppender markdownAppender) {
        this.outputDir = outputDir;
        this.markdownAppender = markdownAppender;
    }

    @Override
    public List<TuningParameter> optimize(TuneableEvaluateFunction evaluate, DataSet dataSet) {
        this.dataSet = dataSet;
        this.evaluate = evaluate;
        List<TuningParameter> params = evaluate.getParams();

        markdownAppender.append(w -> w.h2("new optimization round"));

        for (int step : stepGranularity) {
            LOGGER.info("Optimizing with step " + step);
            markdownAppender.append(w -> w.h3("Optimizing with step " + step));
            optimize(params, step);
        }
        return params;
    }

    private void optimize(List<TuningParameter> params, int step) {
        double bestE = e(params);
        LOGGER.info("Error at start: " + bestE);
        final double errorAtStart=bestE;
        markdownAppender.append(w->w.paragraph("Error at start: " + errorAtStart));

        int round = 0;
        StopWatch stopWatch = new StopWatch();

        boolean improved = true;
        while (improved) {
            improved = false;
            for (TuningParameter param : params) {
                round++;
                if (round % 100 == 0 && stopWatch.timeElapsed(5 * 60000)) {
                    progressInfo(step, bestE, round, stopWatch);
                }

                param.change(step);
                double newE = e(params);
                if (newE < bestE - DELTA) {
                    bestE = newE;
                    improved = true;
                } else {
                    param.change(-2 * step);
                    newE = e(params);
                    if (newE < bestE - DELTA) {
                        bestE = newE;
                        improved = true;
                    } else {
                        // reset change:
                        param.change(step);
                    }
                }
            }
        }
    }

    private void progressInfo(int step, double bestE, int round, StopWatch stopWatch) {
        LOGGER.info(stopWatch.getFormattedCurrDuration() + ": round " + round + ", step " + step
                + ", curr Error= " + bestE);
        LOGGER.info(evaluate.collectParamDescr());
        evaluate.writeParamDescr(outputDir);

        markdownAppender.append(w -> {
            w.paragraph(stopWatch.getFormattedCurrDuration() + ": round " + round + ", step " + step
                    + ", curr Error= " + bestE);
        });
    }

    private double e(List<TuningParameter> params) {

        return dataSet.calcError(evaluate, params);
    }
}
