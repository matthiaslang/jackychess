package org.mattlang.attic.movegenerator;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.CheckChecker;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.sorting.OrderCalculator;
import org.mattlang.jc.movegenerator.LegalMoveGenerator;
import org.mattlang.jc.movegenerator.MoveGenerator;
import org.mattlang.jc.uci.GameContext;

public class LegalMoveGeneratorImpl3 implements LegalMoveGenerator {

    MoveGenerator generator = Factory.getDefaults().moveGenerator.instance();

    CheckChecker checkChecker = Factory.getDefaults().checkChecker.instance();

    @Override
    public MoveList generate(BoardRepresentation board, Color side) {
        MoveList moves = generator.generate(board, side);
        MoveList legalMoves = filterLegalMoves(board, checkChecker, moves, side);
        return legalMoves;
    }

    @Override
    public void generate(BoardRepresentation board, Color side, MoveList moveList) {
        generator.generate(board, side, moveList);
        filterLegalMoves(board, checkChecker, moveList, side);
    }

    @Override
    public void generate(GenMode mode, GameContext gameContext, OrderCalculator orderCalculator, BoardRepresentation board,
            Color side, MoveList moveList) {
        generator.generate(board, side, moveList);
        filterLegalMoves(board, checkChecker, moveList, side);
    }

    public static MoveList filterLegalMoves(BoardRepresentation currBoard, CheckChecker checkChecker, MoveList moves,
            Color side) {

        for (MoveCursor moveCursor : moves) {
            moveCursor.move(currBoard);

            if (checkChecker.isInChess(currBoard, side)) {
                moveCursor.remove();
            }
            moveCursor.undoMove(currBoard);
        }
        return moves;
    }

}
