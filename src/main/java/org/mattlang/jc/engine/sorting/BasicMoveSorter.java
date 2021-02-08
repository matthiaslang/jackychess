package org.mattlang.jc.engine.sorting;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.BasicMoveList;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.search.MoveScore;

/**
 * Simple move sorter using scores from previous run for root level and pvs for deeper levels.
 */
public class BasicMoveSorter implements MoveSorter {

    @Override
    public MoveList sort(MoveList movelist, OrderHints orderHints, int depth, int targetDepth) {
        int index = targetDepth - depth;
        // if we are at the root and have scores from a previous run, lets take them:
        if (index == 0 && orderHints.moveScores != null) {
            // todo assert that the first pvs should be the highest score...
            return reOrderMoves(orderHints.moveScores);
        }
        movelist.sortMoves(new SimpleMoveComparator(orderHints.prevPvlist, depth, targetDepth));
        return movelist;
    }

    public static MoveList reOrderMoves(List<MoveScore> rslt) {
        // order highest scores for us first:
        rslt.sort((o1, o2) -> o2.score - o1.score);
        List<Move> list = rslt.stream().map(s -> s.move).collect(toList());
        return new BasicMoveList(list);
    }
}
