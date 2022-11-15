package org.mattlang.jc.engine.search;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.sorting.OrderCalculator;

/**
 * Some statistic values collected during negamax search.
 */
public class SearchStatistics {

    private static final Logger LOGGER = Logger.getLogger(SearchStatistics.class.getSimpleName());

    public int nodesVisited = 0;
    public int quiescenceNodesVisited = 0;

    public int cutOff;

    public int drawByMaterialDetected;
    public int drawByRepetionDetected;
    public int mateDistancePruningCount;
    public int ttPruningCount;
    public int staticNullMovePruningCount;
    public int nullMovePruningCount;
    public int razoringPruningCount;
    public int iterativeDeepeningCount;
    public int futilityPruningCount;

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
        drawByMaterialDetected = 0;
        drawByRepetionDetected = 0;
        mateDistancePruningCount = 0;
        ttPruningCount = 0;
        staticNullMovePruningCount = 0;
        nullMovePruningCount = 0;
        razoringPruningCount = 0;
        iterativeDeepeningCount = 0;
        futilityPruningCount = 0;
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
        cutOff++;

        if (searchedMoves < searchedMoveIndexCount.length) {
            searchedMoveIndexCount[searchedMoves]++;
        }

        if (LOGGER.isLoggable(Level.INFO)) {
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
        }
    }

    public void logStatistics() {
        logStats(LOGGER, Level.INFO, "Game statistics");
    }

    public void logStats(Logger logger, Level level, String msg) {

        if (logger.isLoggable(level)) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PrintWriter w = new PrintWriter(bos);
            w.println(msg);

            w.printf("nodesVisited               %d\n", nodesVisited);

            w.printf("quiescenceNodesVisited     %d\n", quiescenceNodesVisited);
            w.printf("deltaCutoffCount           %d\n", deltaCutoffCount);
            w.printf("skipLowPromotionsCount     %d\n", skipLowPromotionsCount);

            w.printf("cutoff                     %d\n", cutOff);

            w.printf("drawByMaterialDetected     %d\n", drawByMaterialDetected);
            w.printf("drawByRepetionDetected     %d\n", drawByRepetionDetected);
            w.printf("mateDistancePruningCount   %d\n", mateDistancePruningCount);
            w.printf("ttPruningCount             %d\n", ttPruningCount);
            w.printf("staticNullMovePruningCount %d\n", staticNullMovePruningCount);
            w.printf("nullMovePruningCount       %d\n", nullMovePruningCount);
            w.printf("razoringPruningCount       %d\n", razoringPruningCount);
            w.printf("iterativeDeepeningCount    %d\n", iterativeDeepeningCount);
            w.printf("futilityPruningCount       %d\n", futilityPruningCount);


            w.printf("cutOffByHashMoveCount      %d\n", cutOffByHashMoveCount);
            w.printf("cutOffByKillerCount        %d\n", cutOffByKillerCount);
            w.printf("cutOffByGoodCaptureCount   %d\n", cutOffByGoodCaptureCount);
            w.printf("cutOffByBadCaptureCount    %d\n", cutOffByBadCaptureCount);
            w.printf("cutOffByCounterMoveCount   %d\n", cutOffByCounterMoveCount);
            w.printf("cutOffByHistoryCount       %d\n", cutOffByHistoryCount);
            w.printf("cutOffByQuietCount         %d\n", cutOffByQuietCount);
            for (int i = 1; i < searchedMoveIndexCount.length; i++) {
                double percents = cutOff != 0 ? ((double) searchedMoveIndexCount[i]) / cutOff : 0.0;
                w.printf("searchedMoveIndexCount[%d]    %d    %f\n", i, searchedMoveIndexCount[i], percents);
            }
            w.flush();
            w.close();
            String completeMsg = bos.toString();
            logger.log(level, completeMsg);
        }
    }

    public void collectStatistics(Map rslts) {

        rslts.put("nodesVisited", nodesVisited);

        rslts.put("quiescenceNodesVisited", quiescenceNodesVisited);

        rslts.put("cutoff", cutOff);
        rslts.put("deltacutoffCount", deltaCutoffCount);
        rslts.put("skipLowPromotionsCount", skipLowPromotionsCount);

        rslts.put("drawByMaterialDetected", drawByMaterialDetected);
        rslts.put("drawByRepetionDetected", drawByRepetionDetected);
        rslts.put("mateDistancePruningCount", mateDistancePruningCount);
        rslts.put("ttPruningCount", ttPruningCount);
        rslts.put("staticNullMovePruningCount", staticNullMovePruningCount);
        rslts.put("nullMovePruningCount", nullMovePruningCount);
        rslts.put("razoringPruningCount", razoringPruningCount);
        rslts.put("iterativeDeepeningCount", iterativeDeepeningCount);
        rslts.put("futilityPruningCount", futilityPruningCount);
        rslts.put("cutOffByHashMoveCount", cutOffByHashMoveCount);
        rslts.put("cutOffByKillerCount", cutOffByKillerCount);
        rslts.put("cutOffByGoodCaptureCount", cutOffByGoodCaptureCount);
        rslts.put("cutOffByBadCaptureCount", cutOffByBadCaptureCount);
        rslts.put("cutOffByCounterMoveCount", cutOffByCounterMoveCount);
        rslts.put("cutOffByHistoryCount", cutOffByHistoryCount);
        rslts.put("cutOffByQuietCount", cutOffByQuietCount);
        for (int i = 1; i < searchedMoveIndexCount.length; i++) {
            rslts.put("searchedMoveIndexCount[" + i + "]", searchedMoveIndexCount[i]);
        }
    }

    public void add(SearchStatistics statistics) {
        nodesVisited += statistics.nodesVisited;
        quiescenceNodesVisited += statistics.quiescenceNodesVisited;
        cutOff += statistics.cutOff;
        deltaCutoffCount += statistics.deltaCutoffCount;
        skipLowPromotionsCount += statistics.skipLowPromotionsCount;
        drawByMaterialDetected += statistics.drawByMaterialDetected;
        drawByRepetionDetected += statistics.drawByRepetionDetected;
        mateDistancePruningCount += statistics.mateDistancePruningCount;
        ttPruningCount += statistics.ttPruningCount;
        staticNullMovePruningCount += statistics.staticNullMovePruningCount;
        nullMovePruningCount += statistics.nullMovePruningCount;
        razoringPruningCount += statistics.razoringPruningCount;
        iterativeDeepeningCount += statistics.iterativeDeepeningCount;
        futilityPruningCount += statistics.futilityPruningCount;
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
