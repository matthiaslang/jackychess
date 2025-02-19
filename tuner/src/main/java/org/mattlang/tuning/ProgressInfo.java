package org.mattlang.tuning;

import java.io.File;
import java.util.logging.Logger;

import org.mattlang.jc.StopWatch;
import org.mattlang.jc.tools.MarkdownAppender;
import org.mattlang.jc.tools.MarkdownTable;
import org.mattlang.tuning.evaluate.ParameterSet;
import org.mattlang.tuning.tuner.OptParameters;

public class ProgressInfo {

    private static final Logger LOGGER = Logger.getLogger(ProgressInfo.class.getSimpleName());
    public static final int DEFAULT_UPDATE_MINUTES = 5;

    private final int updatesInMinutes;

    private final File outputDir;
    private final MarkdownAppender markdownAppender;
    private final MarkdownTable progressTable;

    private StopWatch stopWatch = new StopWatch();

    private double overallAdjPerHour;

    private int lastParamsAdjusted;

    private long lastTime;
    private double adjPerHour;
    private boolean tooLessProgress = false;

    public ProgressInfo(OptParameters optParameters, File outputDir, MarkdownAppender markdownAppender) {
        this.outputDir = outputDir;
        this.updatesInMinutes = optParameters.getProgressUpdatesInMinutes();
        this.markdownAppender = markdownAppender;
        stopWatch.start();

        progressTable =
                new MarkdownTable().header("Duration", "Param Iteration", "Round", "Step", "Params adjustments",
                        "Adj total", "Curr Error"
                        , "Overall AdjPerHour", "AdjPerHour");

        markdownAppender.append(w -> {
            progressTable.writeTableHeader(w);
        });
    }

    public void progressInfo(ParameterSet parameterSet, int step, ProgressParams progress) {
        int adjOfProgressInterval = progress.numParamAdjusted - lastParamsAdjusted;

        tooLessProgress = adjOfProgressInterval < 5;

        long secondsOfProgressInterval = (stopWatch.getCurrDuration() - lastTime) / 1000;
        if (secondsOfProgressInterval > 0) {

            adjPerHour = ((double) adjOfProgressInterval) / secondsOfProgressInterval * 60 * 60;
        }
        lastParamsAdjusted = progress.numParamAdjusted;
        lastTime = stopWatch.getCurrDuration();

        long seconds = stopWatch.getCurrDuration() / 1000;
        if (seconds > 0) {
            overallAdjPerHour = ((double) progress.numParamAdjusted) / seconds * 60 * 60;
        }

        String progressInfoTxt =
                stopWatch.getFormattedCurrDuration()
                        + ": paramIteration " + progress.paramIterationRound
                        + ": round " + progress.round +
                        ", step " + step +
                        ", params adjustments: " + adjOfProgressInterval
                        + ", total: " + progress.numParamAdjusted
                        + "; curr Error= " + progress.bestE
                        + ", overall paramsAdjPerHour= " + overallAdjPerHour
                        + ", paramsAdjPerHour= " + adjPerHour;
        LOGGER.info(progressInfoTxt);
        parameterSet.writeParamDescr(outputDir);

        markdownAppender.append(w -> {
            progressTable.row(stopWatch.getFormattedCurrDuration(), progress.paramIterationRound, progress.round
                    , step, adjOfProgressInterval, progress.numParamAdjusted, progress.bestE, overallAdjPerHour,
                    adjPerHour);
            progressTable.writeRows(w);
        });
    }

    public boolean isEnoughTimeElapsed() {
        return stopWatch.timeElapsed(updatesInMinutes * 60000);
    }

    public boolean hasEnoughProgress(int step) {
        if (step == 1) {
            return true;
        }
        // in higher steps we check that we make a minimum of progress, otherwise continue with the next smaller step:
        return !tooLessProgress;
    }
}
