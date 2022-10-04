package org.mattlang.jc.engine.search;

import java.util.Map;

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
    }

    public void countCutOff(MoveCursor moveCursor) {
        cutOff++;

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
    }
}
