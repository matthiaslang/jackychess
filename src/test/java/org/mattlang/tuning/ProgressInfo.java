package org.mattlang.tuning;

import java.io.File;
import java.util.logging.Logger;

import org.mattlang.jc.StopWatch;
import org.mattlang.jc.tools.MarkdownAppender;
import org.mattlang.tuning.evaluate.ParameterSet;

public class ProgressInfo {

    private static final Logger LOGGER = Logger.getLogger(ProgressInfo.class.getSimpleName());

    private final File outputDir;
    private final MarkdownAppender markdownAppender;

    private StopWatch stopWatch = new StopWatch();

    private double overallAdjPerSecond;

    private int lastParamsAdjusted;

    private long lastTime;
    private double adjPerSecond;

    public ProgressInfo(File outputDir, MarkdownAppender markdownAppender) {
        this.outputDir = outputDir;
        this.markdownAppender = markdownAppender;
        stopWatch.start();
    }

    public void progressInfo(ParameterSet parameterSet, int step, double bestE, int round, int numParamAdjusted) {

        long secondsOfProgressInterval = (stopWatch.getCurrDuration() - lastTime) / 1000;
        if (secondsOfProgressInterval > 0) {
            int adjOfProgressInterval = numParamAdjusted - lastParamsAdjusted;
            adjPerSecond = ((double) adjOfProgressInterval) / secondsOfProgressInterval;
        }
        lastParamsAdjusted = numParamAdjusted;
        lastTime = stopWatch.getCurrDuration();

        long seconds = stopWatch.getCurrDuration() / 1000;
        if (seconds > 0) {
            overallAdjPerSecond = ((double) numParamAdjusted) / seconds;
        }


        String progressInfoTxt =
                stopWatch.getFormattedCurrDuration() + ": round " + round +
                        ", step " + step +
                        ", params adjusted: " + numParamAdjusted
                        + ", curr Error= " + bestE
                        + ", overall paramsAdjPerSecond= " + overallAdjPerSecond
                        + ", paramsAdjPerSecond= " + adjPerSecond;
        LOGGER.info(progressInfoTxt);
        LOGGER.info(parameterSet.collectParamDescr());
        parameterSet.writeParamDescr(outputDir);

        markdownAppender.append(w -> {
            w.paragraph(progressInfoTxt);
        });
    }

    public boolean isEnoughTimeElapsed() {
        return stopWatch.timeElapsed(5 * 60000);
    }
}
