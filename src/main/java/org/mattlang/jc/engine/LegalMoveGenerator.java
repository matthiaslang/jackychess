package org.mattlang.jc.engine;

import java.util.ArrayList;
import java.util.List;

import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Figure;
import org.mattlang.jc.board.Move;

public class LegalMoveGenerator {

    public List<Move> generate(Board board, Color side) {
        MoveGenerator generator = new MoveGenerator();
        List<Move> moves = generator.generate(board, side);
        moves = filterLegalMoves(board, moves, side == Color.WHITE? Color.BLACK: Color.WHITE);
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
