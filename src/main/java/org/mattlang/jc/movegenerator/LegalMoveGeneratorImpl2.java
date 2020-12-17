package org.mattlang.jc.movegenerator;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.FigureType;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.evaluation.BoardStats;
import org.mattlang.jc.engine.evaluation.SimpleBoardStatsGenerator;

public class LegalMoveGeneratorImpl2 implements LegalMoveGenerator {

    MoveGenerator generator = Factory.getDefaults().moveGenerator.create();

    SimpleBoardStatsGenerator statsGenerator = new SimpleBoardStatsGenerator();
    @Override
    public MoveList generate(Board board, Color side) {
        MoveList moves = generator.generate(board, side);
        MoveList legalMoves = filterLegalMoves(board, moves, side.invert());
        // simple ordering by capture first, to be a bit bettr in alpha beta pruning
        legalMoves.sortByCapture();
        return legalMoves;
    }

    private MoveList filterLegalMoves(Board currBoard, MoveList moves, Color color) {
        Color otherColor = color.invert();

        byte kingFigureCode = (byte) (FigureType.King.figureCode | otherColor.code);

        MoveList legals = Factory.getDefaults().moveList.create();
        for (MoveCursor moveCursor : moves) {
            moveCursor.move(currBoard);

            int kingPos = currBoard.findPosOfFigure(kingFigureCode);
            long kingBitBoardPos = (1L << kingPos);

            BoardStats responseStats = statsGenerator.gen(currBoard, color);
            moveCursor.undoMove(currBoard);

            if ((responseStats.mobilityBitBoard & kingBitBoardPos) == 0) {
                legals.addMove(moveCursor);
            }

        }
        return legals;
    }

}
