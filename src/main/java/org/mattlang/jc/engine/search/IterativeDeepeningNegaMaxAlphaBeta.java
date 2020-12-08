package org.mattlang.jc.engine.search;

import static org.mattlang.jc.engine.search.NegaMaxAlphaBeta.ALPHA_START;
import static org.mattlang.jc.engine.search.NegaMaxAlphaBeta.BETA_START;

import java.util.List;
import java.util.stream.Collectors;

import org.mattlang.jc.StopWatch;
import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.BasicMoveList;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.SearchMethod;
import org.mattlang.jc.uci.UCI;

public class IterativeDeepeningNegaMaxAlphaBeta implements SearchMethod {


    private NegaMaxAlphaBeta negaMaxAlphaBeta = new NegaMaxAlphaBeta();

    private int maxDepth = 6;

    private long timeout = 15 * 1000;


    public Move search(Board currBoard, int depth, Color color) {
        this.maxDepth = depth;
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        int currdepth = 1;
        Move savedMove = null;

        MoveList moves = negaMaxAlphaBeta.generateMoves(currBoard, color);
        while (stopWatch.getDuration() < timeout && currdepth <= maxDepth) {

            List<NegaMaxAlphaBeta.MoveScore> rslt =
                    negaMaxAlphaBeta.searchWithScore(currBoard, currdepth, color, ALPHA_START, BETA_START, moves);

            savedMove = negaMaxAlphaBeta.getSavedMove();
            currdepth++;

            if (savedMove != null) {
                UCI.instance.putCommand("info currmove " + savedMove.toStr());
            }
            moves = reOrderMoves(rslt);
        }

        return savedMove;
    }

    private MoveList reOrderMoves(List<NegaMaxAlphaBeta.MoveScore> rslt) {
        rslt.sort((o1, o2) -> {
            // todo right order?
            return o2.score - o1.score;
       });
        List<Move> list = rslt.stream().map(s -> s.move).collect(Collectors.toList());
        return new BasicMoveList(list);

    }

    private void reset() {


    }

}
