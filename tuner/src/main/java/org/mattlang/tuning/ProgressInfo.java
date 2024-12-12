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

    private double overallAdjPerSecond;

    private int lastParamsAdjusted;

    private long lastTime;
    private double adjPerSecond;

    public ProgressInfo(OptParameters optParameters, File outputDir, MarkdownAppender markdownAppender) {
        this.outputDir = outputDir;
        this.updatesInMinutes = optParameters.getProgressUpdatesInMinutes();
        this.markdownAppender = markdownAppender;
        stopWatch.start();

        progressTable =
                new MarkdownTable().header("Duration", "Param Iteration", "Round", "Step", "Params adjustments", "Adj total", "Curr Error"
                        , "Overall AdjPerSecond", "AdjPerSecond");

        markdownAppender.append(w -> {
            progressTable.writeTableHeader(w);
        });
    }

    public void progressInfo(ParameterSet parameterSet, int step, ProgressParams progress) {
        int adjOfProgressInterval = progress.numParamAdjusted - lastParamsAdjusted;

        long secondsOfProgressInterval = (stopWatch.getCurrDuration() - lastTime) / 1000;
        if (secondsOfProgressInterval > 0) {

            adjPerSecond = ((double) adjOfProgressInterval) / secondsOfProgressInterval;
        }
        lastParamsAdjusted = progress.numParamAdjusted;
        lastTime = stopWatch.getCurrDuration();

        long seconds = stopWatch.getCurrDuration() / 1000;
        if (seconds > 0) {
            overallAdjPerSecond = ((double) progress.numParamAdjusted) / seconds;
        }

        String progressInfoTxt =
                stopWatch.getFormattedCurrDuration()
                        + ": paramIteration " + progress.paramIterationRound
                        + ": round " + progress.round +
                        ", step " + step +
                        ", params adjustments: " + adjOfProgressInterval
                        + ", total: " + progress.numParamAdjusted
                        + "; curr Error= " + progress.bestE
                        + ", overall paramsAdjPerSecond= " + overallAdjPerSecond
                        + ", paramsAdjPerSecond= " + adjPerSecond;
        LOGGER.info(progressInfoTxt);
        parameterSet.writeParamDescr(outputDir);

        markdownAppender.append(w -> {
            progressTable.row(stopWatch.getFormattedCurrDuration(), progress.paramIterationRound, progress.round
                    , step, adjOfProgressInterval, progress.numParamAdjusted, progress.bestE, overallAdjPerSecond, adjPerSecond);
            progressTable.writeRows(w);
        });
    }

    public boolean isEnoughTimeElapsed() {
        return stopWatch.timeElapsed(updatesInMinutes * 60000);
    }
}
