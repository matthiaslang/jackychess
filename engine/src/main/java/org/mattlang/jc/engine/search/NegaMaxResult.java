package org.mattlang.jc.engine.search;

import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.List;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.moves.MoveImpl;
import org.mattlang.jc.moves.MoveToStringConverter;
import org.mattlang.jc.util.IntList;

/**
 * Result from a negamax search run.
 * Immutable object.
 */
public final class NegaMaxResult {

    /**
     * direct score result from negamax. (could be theoretically different to max).
     */
    public final int directScore;
    /**
     * max score from negamax.
     */
    public final int max;
    /**
     * Best move from negamax.
     */
    public final Move savedMove;

    /**
     * pv list from negamax search.
     */
    private final IntList pvList;

    /**
     * the target depth.
     */
    public final int targetDepth;

    /**
     * selected depth due to quiescence search.
     */
    public final int selDepth;

    public final int nodesVisited;
    public final int quiescenceNodesVisited;

    public final int ponderMove;

    public NegaMaxResult(int directScore, IntList pvMoves,
            SearchContext searchContext, int nodesVisited, int quiescenceNodesVisited) {
        this.directScore = directScore;
        this.max = searchContext.getSavedMoveScore();
        if (searchContext.getSavedMove() != 0) {
            this.savedMove = new MoveImpl(searchContext.getSavedMove());
        } else {
            this.savedMove = null;
        }
        // should we take the best move also from the pv, analogue like the ponder move?
        this.ponderMove = pvMoves.size() >= 2 ? pvMoves.get(1) : 0;
        this.pvList = pvMoves;

        this.targetDepth = searchContext.getTargetDepth();
        this.selDepth = searchContext.getSelDepth();

        this.nodesVisited = nodesVisited;
        this.quiescenceNodesVisited = quiescenceNodesVisited;
    }

    public NegaMaxResult(Move bestMove) {
        this.savedMove = bestMove;

        this.directScore = 0;
        this.max = 0;
        this.ponderMove =0;
        this.pvList = null;
        this.targetDepth =0;
        this.selDepth = 0;
        this.nodesVisited = 0;
        this.quiescenceNodesVisited = 0;
    }


    public String toPvStr(BoardRepresentation boardRepresentation) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < pvList.size(); i++) {
            int move =  pvList.get(i);
            if (builder.length() > 0) {
                builder.append(' ');
            }
            builder.append(MoveToStringConverter.toUCIString(move, boardRepresentation));
        }
        return builder.toString();
    }

    public String toPvLogStr() {
        return getPvMoves().stream()
                .map(m -> m.toStr())
                .collect(joining(" "));
    }

    public List<Move> getPvMoves() {
        List<Move> list = new ArrayList<>();
        for (int i = 0; i < pvList.size(); i++) {
            int move = pvList.get(i);
            list.add(new MoveImpl(move));
        }
        return list;
    }


    @Override
    public String toString() {
        return "NegaMaxResult{" +
               "directScore=" + directScore +
               ", max=" + max +
               ", savedMove=" + (savedMove != null ? savedMove.toStr() : "") +
               ", pvList=" + toPvLogStr() +
               ", targetDepth=" + targetDepth +
               ", selDepth=" + selDepth +
               '}';
    }

    public String toLogString() {
        return "[" +
               "directScore=" + directScore +
               ", max=" + max +
               ", savedMove=" + (savedMove != null ? savedMove.toStr() : "") +
               ", pv=" + toPvLogStr() +
               ", depth=" + targetDepth + "/" + selDepth +
               ", nodes=" + nodesVisited + "/" + quiescenceNodesVisited +
               ']';
    }
}
