package org.mattlang.jc.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Figure;
import org.mattlang.jc.board.Move;

public class LegalMoveGenerator {

    /**
     * First simple try to order moves for alpha beta cut off relevace: "best" guessed moves should be
     * processed first to early cut off. First try: sort by capture first moves:
     */
    private static final Comparator<Move> CMP = new Comparator<Move>() {

        @Override
        public int compare(Move o1, Move o2) {
            int c1 = o1.getCapturedFigure() == null ? 0 : -o1.getCapturedFigure().figureCode;
            int c2 = o2.getCapturedFigure() == null ? 0 : -o2.getCapturedFigure().figureCode;
            return c1 - c2;
        }
    };

    public List<Move> generate(Board board, Color side) {
        MoveGenerator generator = new MoveGenerator();
        List<Move> moves = generator.generate(board, side);
        moves = filterLegalMoves(board, moves, side == Color.WHITE ? Color.BLACK : Color.WHITE);
        // simple ordering by capture first, to be a bit bettr in alpha beta pruning
        Collections.sort(moves, CMP);
        return moves;
    }


    private List<Move> filterLegalMoves(Board currBoard, List<Move> moves, Color color) {
        Figure king = color== Color.WHITE? Figure.B_King: Figure.W_King;

        ArrayList<Move> legals = new ArrayList<>(moves.size());
        for (Move move : moves) {
            Move undo = currBoard.move(move);
            MoveGenerator generator = new MoveGenerator();
            List<Move> responseMoves = generator.generate(currBoard, color);
            currBoard.move(undo);

            if (!containsTarget(responseMoves, king)){
                 legals.add(move)                           ;
            }

        }
        return legals;
    }

    private boolean containsTarget(List<Move> moves, Figure figure) {
        for (Move move : moves) {
            if (figure == move.getCapturedFigure()){
                return true;
            }
        }
        return false;
    }
}
