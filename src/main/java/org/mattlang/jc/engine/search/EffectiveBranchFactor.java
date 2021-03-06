package org.mattlang.jc.engine.search;

import static org.mattlang.jc.Constants.MAX_PLY_INDEX;

import java.util.Arrays;

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

    private EBF[] ebfs = new EBF[MAX_PLY_INDEX];

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
        Arrays.fill(ebfs, null);
    }

    public String report() {
        StringBuilder b = new StringBuilder();
        for (int i = 1; i < ebfs.length; i++) {
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
