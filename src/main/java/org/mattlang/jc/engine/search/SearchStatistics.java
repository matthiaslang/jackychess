package org.mattlang.jc.engine.search;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.mattlang.jc.BuildConstants;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.sorting.OrderCalculator;

/**
 * Some statistic values collected during negamax search for debugging purpose.
 */
public class SearchStatistics {

    private static final Logger LOGGER = Logger.getLogger(SearchStatistics.class.getSimpleName());

    public int nodesVisited = 0;
    public int quiescenceNodesVisited = 0;

    /** number of cut offs.*/
    public int cutOff;
    /** number of times we have not found a cut off.*/
    public int noCutOffFoundCount;

    public int drawByMaterialDetected;
    public int drawByRepetionDetected;
    public int mateDistancePruningCount;
    public int ttPruningCount;
    public int staticNullMovePruningCount;

    public int nullMoveTryCount;
    public int nullMovePruningCount;
    public int razoringTryCount;
    public int razoringPruningCount;
    public int iterativeDeepeningCount;
    public int futilityPruningCount;
    public int lateMovePruningCount;
    public int seePruningCount;

    public int cutOffByHashMoveCount;

    public int cutOffByKillerCount;

    public int cutOffByCounterMoveCount;

    public int cutOffByGoodCaptureCount;

    public int cutOffByBadCaptureCount;

    public int cutOffByHistoryCount;

    public int cutOffByQuietCount;

    public int deltaCutoffCount;

    public int skipLowPromotionsCount;

    public int[] searchedMoveIndexCount = new int[30];


    public void resetStatistics() {
        nodesVisited = 0;
        quiescenceNodesVisited = 0;
        cutOff = 0;
        noCutOffFoundCount=0;
        drawByMaterialDetected = 0;
        drawByRepetionDetected = 0;
        mateDistancePruningCount = 0;
        ttPruningCount = 0;
        staticNullMovePruningCount = 0;
        nullMoveTryCount = 0;
        nullMovePruningCount = 0;
        razoringPruningCount = 0;
        razoringTryCount=0;
        iterativeDeepeningCount = 0;
        futilityPruningCount = 0;
        lateMovePruningCount = 0;
        seePruningCount = 0;
        cutOffByHashMoveCount = 0;
        cutOffByKillerCount = 0;
        cutOffByCounterMoveCount = 0;
        cutOffByGoodCaptureCount = 0;
        cutOffByBadCaptureCount = 0;
        cutOffByHistoryCount = 0;
        cutOffByQuietCount = 0;
        deltaCutoffCount = 0;
        skipLowPromotionsCount = 0;
        Arrays.fill(searchedMoveIndexCount, 0);
    }

    public void countCutOff(MoveCursor moveCursor, int searchedMoves) {
        if (!BuildConstants.STATS_ACTIVATED) {
            return;
        }
        cutOff++;

        if (searchedMoves < searchedMoveIndexCount.length) {
            searchedMoveIndexCount[searchedMoves]++;
        }

//        if (LOGGER.isLoggable(Level.INFO)) {
            int order = moveCursor.getOrder();
            if (OrderCalculator.isHashMove(order)) {
                cutOffByHashMoveCount++;
            } else if (OrderCalculator.isKillerMove(order)) {
                cutOffByKillerCount++;
            } else if (OrderCalculator.isCounterMove(order)) {
                cutOffByCounterMoveCount++;
            } else if (moveCursor.isCapture()) {
                if (OrderCalculator.isGoodCapture(order)) {
                    cutOffByGoodCaptureCount++;
                } else {
                    cutOffByBadCaptureCount++;
                }
            } else {
                if (OrderCalculator.isHistory(order)) {
                    cutOffByHistoryCount++;
                } else {
                    cutOffByQuietCount++;
                }

            }
        //        }
    }

    public void logStatistics() {
        logStats(LOGGER, Level.INFO, "Game statistics");
    }

    public void logStats(Logger logger, Level level, String msg) {
        if (logger.isLoggable(level)) {
            if (BuildConstants.STATS_ACTIVATED && hasStatistics()) {
                String completeMsg = formatStats(msg);
                logger.log(level, completeMsg);
            } else {
                logger.log(level, "no statistics ...");
            }
        }
    }

    private boolean hasStatistics() {
        // we have no statistics if we havent at least the node visited counter set:
        return nodesVisited != 0;
    }

    public String formatStats(String msg) {
        if (!BuildConstants.STATS_ACTIVATED) {
            return "";
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrintWriter w = new PrintWriter(bos);
        w.println(msg);

        w.printf("nodesVisited               %12d\n", nodesVisited);
        w.printf("---------------------------------------------------------------------\n");
        w.printf("quiescenceNodesVisited     %12d\n", quiescenceNodesVisited);
        w.printf("deltaCutoffCount           %12d     %7.8f\n", deltaCutoffCount,
                ratio(deltaCutoffCount, quiescenceNodesVisited));
        w.printf("skipLowPromotionsCount     %12d     %7.8f\n", skipLowPromotionsCount, ratio(skipLowPromotionsCount, quiescenceNodesVisited));


        w.printf("---------------------------------------------------------------------\n");
        w.printf("drawByMaterialDetected     %12d\n", drawByMaterialDetected);
        w.printf("drawByRepetionDetected     %12d\n", drawByRepetionDetected);
        w.printf("mateDistancePruningCount   %12d\n", mateDistancePruningCount);
        w.printf("ttPruningCount             %12d\n", ttPruningCount);
        w.printf("---------------------------------------------------------------------\n");
        w.printf("staticNullMovePruningCount %12d\n", staticNullMovePruningCount);
        w.printf("nullMoveTryCount           %12d\n", nullMoveTryCount);
        w.printf("nullMovePruningCount       %12d     %7.8f\n", nullMovePruningCount, ratio(nullMovePruningCount, nullMoveTryCount));

        w.printf("---------------------------------------------------------------------\n");
        w.printf("razoringTryCount           %12d\n", razoringTryCount);
        w.printf("razoringPruningCount       %12d     %7.8f\n", razoringPruningCount, ratio(razoringPruningCount, razoringTryCount));
        w.printf("---------------------------------------------------------------------\n");
        w.printf("iterativeDeepeningCount    %12d\n", iterativeDeepeningCount);
        w.printf("futilityPruningCount       %12d\n", futilityPruningCount);
        w.printf("lateMovePruningCount       %12d\n", lateMovePruningCount);
        w.printf("seePruningCount            %12d\n", seePruningCount);
        w.printf("---------------------------------------------------------------------\n");


        w.printf("noCutOffFoundCount         %12d\n", noCutOffFoundCount);
        w.printf("cutoff                     %12d     %7.8f\n", cutOff, ratio(cutOff, noCutOffFoundCount));

        w.printf("cutOffByHashMoveCount      %12d     %7.8f\n", cutOffByHashMoveCount, ratio(cutOffByHashMoveCount, cutOff));
        w.printf("cutOffByKillerCount        %12d     %7.8f\n", cutOffByKillerCount, ratio(cutOffByKillerCount, cutOff));
        w.printf("cutOffByGoodCaptureCount   %12d     %7.8f\n", cutOffByGoodCaptureCount, ratio(cutOffByGoodCaptureCount, cutOff));
        w.printf("cutOffByBadCaptureCount    %12d     %7.8f\n", cutOffByBadCaptureCount, ratio(cutOffByBadCaptureCount, cutOff));
        w.printf("cutOffByCounterMoveCount   %12d     %7.8f\n", cutOffByCounterMoveCount, ratio(cutOffByCounterMoveCount, cutOff));
        w.printf("cutOffByHistoryCount       %12d     %7.8f\n", cutOffByHistoryCount, ratio(cutOffByHistoryCount, cutOff));
        w.printf("cutOffByQuietCount         %12d     %7.8f\n", cutOffByQuietCount, ratio(cutOffByQuietCount, cutOff));
        for (int i = 1; i < searchedMoveIndexCount.length; i++) {
            w.printf("searchedMoveIndexCount[%2d]    %12d    %7.8f\n", i, searchedMoveIndexCount[i], ratio(searchedMoveIndexCount[i], cutOff));
        }
        w.flush();
        w.close();
        String completeMsg = bos.toString();
        return completeMsg;
    }

    private double ratio(int divident, int divisor){
        double percents = divisor != 0 ? ((double) divident) / divisor : 0.0;
        return percents;
    }

    public void add(SearchStatistics statistics) {
        if (!BuildConstants.STATS_ACTIVATED){
            return;
        }

        nodesVisited += statistics.nodesVisited;
        quiescenceNodesVisited += statistics.quiescenceNodesVisited;
        cutOff += statistics.cutOff;
        noCutOffFoundCount += statistics.noCutOffFoundCount;
        deltaCutoffCount += statistics.deltaCutoffCount;
        skipLowPromotionsCount += statistics.skipLowPromotionsCount;
        drawByMaterialDetected += statistics.drawByMaterialDetected;
        drawByRepetionDetected += statistics.drawByRepetionDetected;
        mateDistancePruningCount += statistics.mateDistancePruningCount;
        ttPruningCount += statistics.ttPruningCount;
        staticNullMovePruningCount += statistics.staticNullMovePruningCount;
        nullMovePruningCount += statistics.nullMovePruningCount;
        nullMoveTryCount += statistics.nullMoveTryCount;
        razoringPruningCount += statistics.razoringPruningCount;
        razoringTryCount += statistics.razoringTryCount;
        iterativeDeepeningCount += statistics.iterativeDeepeningCount;
        futilityPruningCount += statistics.futilityPruningCount;
        seePruningCount += statistics.seePruningCount;
        cutOffByHashMoveCount += statistics.cutOffByHashMoveCount;
        cutOffByKillerCount += statistics.cutOffByKillerCount;
        cutOffByCounterMoveCount += statistics.cutOffByCounterMoveCount;
        cutOffByGoodCaptureCount += statistics.cutOffByGoodCaptureCount;
        cutOffByQuietCount += statistics.cutOffByQuietCount;
        cutOffByHistoryCount += statistics.cutOffByHistoryCount;
        for (int i = 0; i < searchedMoveIndexCount.length; i++) {
            searchedMoveIndexCount[i] += statistics.searchedMoveIndexCount[i];
        }
    }
}
