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

    public int cutOffByEqualCaptureCount;

    public int cutOffByBadCaptureCount;

    public int cutOffByHistoryCount;

    public int cutOffByQuietCount;

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
        cutOffByEqualCaptureCount = 0;
        cutOffByBadCaptureCount = 0;
        cutOffByHistoryCount = 0;
        cutOffByQuietCount = 0;
        Arrays.fill(searchedMoveIndexCount, 0);
    }

    public void countCutOff(MoveCursor moveCursor, int searchedMoves) {
        cutOff++;

        if (searchedMoves < searchedMoveIndexCount.length) {
            searchedMoveIndexCount[searchedMoves]++;
        }

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
            } else if (OrderCalculator.isEqualCapture(order)) {
                cutOffByEqualCaptureCount++;
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

    public void logStats(Logger logger, Level level, String msg) {

        if (logger.isLoggable(level)) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PrintWriter w = new PrintWriter(bos);
            w.println(msg);

            w.printf("nodesVisited               %1\n", nodesVisited);

            w.printf("quiescenceNodesVisited     %1\n", quiescenceNodesVisited);

            w.printf("cutoff                     %1\n", cutOff);

            w.printf("drawByMaterialDetected     %1\n", drawByMaterialDetected);
            w.printf("drawByRepetionDetected     %1\n", drawByRepetionDetected);
            w.printf("mateDistancePruningCount   %1\n", mateDistancePruningCount);
            w.printf("ttPruningCount             %1\n", ttPruningCount);
            w.printf("staticNullMovePruningCount %1\n", staticNullMovePruningCount);
            w.printf("nullMovePruningCount       %1\n", nullMovePruningCount);
            w.printf("razoringPruningCount       %1\n", razoringPruningCount);
            w.printf("iterativeDeepeningCount    %1\n", iterativeDeepeningCount);
            w.printf("futilityPruningCount       %1\n", futilityPruningCount);
            w.printf("cutOffByHashMoveCount      %1\n", cutOffByHashMoveCount);
            w.printf("cutOffByKillerCount        %1\n", cutOffByKillerCount);
            w.printf("cutOffByGoodCaptureCount   %1\n", cutOffByGoodCaptureCount);
            w.printf("cutOffByEqualCaptureCount  %1\n", cutOffByEqualCaptureCount);
            w.printf("cutOffByBadCaptureCount    %1\n", cutOffByBadCaptureCount);
            w.printf("cutOffByCounterMoveCount   %1\n", cutOffByCounterMoveCount);
            w.printf("cutOffByHistoryCount       %1\n", cutOffByHistoryCount);
            w.printf("cutOffByQuietCount         %1\n", cutOffByQuietCount);
            for (int i = 1; i < searchedMoveIndexCount.length; i++) {
                double percents = cutOff != 0 ? searchedMoveIndexCount[i] / cutOff : 0;
                w.printf("searchedMoveIndexCount[" + i + "]    %1    %2\n", searchedMoveIndexCount[i], percents);
            }

            w.close();
            String completeMsg = bos.toString();
            logger.log(level, completeMsg);
        }
    }

    public void collectStatistics(Map rslts) {

        rslts.put("nodesVisited", nodesVisited);

        rslts.put("quiescenceNodesVisited", quiescenceNodesVisited);

        rslts.put("cutoff", cutOff);

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
        rslts.put("cutOffByEqualCaptureCount", cutOffByEqualCaptureCount);
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
        cutOffByEqualCaptureCount += statistics.cutOffByEqualCaptureCount;
        cutOffByQuietCount += statistics.cutOffByQuietCount;
        cutOffByHistoryCount += statistics.cutOffByHistoryCount;
        for (int i = 0; i < searchedMoveIndexCount.length; i++) {
            searchedMoveIndexCount[i] += statistics.searchedMoveIndexCount[i];
        }
    }
}
