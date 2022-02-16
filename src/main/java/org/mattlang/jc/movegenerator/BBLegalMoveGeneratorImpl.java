package org.mattlang.jc.movegenerator;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.CheckChecker;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.sorting.OrderCalculator;
import org.mattlang.jc.moves.MoveCursorImpl;
import org.mattlang.jc.moves.MoveListImpl;
import org.mattlang.jc.uci.GameContext;

public class BBLegalMoveGeneratorImpl implements LegalMoveGenerator {

    BBMoveGeneratorImpl generator = new BBMoveGeneratorImpl();

    BBCheckCheckerImpl checkChecker = new BBCheckCheckerImpl();

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

        MoveCursorImpl moveCursor = new MoveCursorImpl(((MoveListImpl) moves));
        while (moveCursor.hasNext()) {
            moveCursor.next();

            moveCursor.move(currBoard);

            if (checkChecker.isInChess(currBoard, side)) {
                moveCursor.remove();
            }
            moveCursor.undoMove(currBoard);
        }
        return moves;
    }

}
