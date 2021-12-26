package org.mattlang.jc.engine.search;

import lombok.AllArgsConstructor;

/**
 * Calcs the effective branch factor during iterative deepening.
 */
public class EffectiveBranchFactor {

    @AllArgsConstructor
    static class EBF {

        int currdepth;
        long duration;
        int nodesVisited;
        double factor;
    }

    private final static int MAX=64;
    private EBF[] ebfs = new EBF[MAX];

    public void update(int currdepth, long duration, int nodesVisited) {
        double factor;
        int nodesVisitedPrevious = 1;
        if (ebfs[currdepth - 1] != null) {
            nodesVisitedPrevious = ebfs[currdepth - 1].nodesVisited;
        }
        factor = ((double) nodesVisited) / nodesVisitedPrevious;
        ebfs[currdepth] = new EBF(currdepth, duration, nodesVisited, factor);
    }

    public void clear() {
        for (int i = 0; i < MAX; i++) {
            ebfs[i] = null;
        }
    }

    public String report() {
        StringBuilder b = new StringBuilder();
        for (int i = 1; i < MAX; i++) {
            if (ebfs[i] != null) {
                report(b, i, ebfs[i]);
            } else {
                return b.toString();
            }
        }
        return b.toString();
    }

    private void report(StringBuilder b, int depth, EBF ebf) {
        b.append(String.format(" %s: %4.2f", depth, ebf.factor));
    }
}
