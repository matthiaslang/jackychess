package org.mattlang.attic.movegenerator;

import static org.mattlang.jc.board.FigureConstants.FT_KING;
import static org.mattlang.jc.board.FigureConstants.FT_QUEEN;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.CheckChecker;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.sorting.OrderCalculator;
import org.mattlang.jc.movegenerator.LegalMoveGenerator;
import org.mattlang.jc.movegenerator.MoveGenerator;
import org.mattlang.jc.moves.MoveImpl;
import org.mattlang.jc.uci.GameContext;

/**
 * Experimental legal move generator with some optimizations.
 * Saves in perft tests ~ 20%. in real life the savings are much lower... ~ 3%.
 *
 */
public class LegalMoveGeneratorImpl4 implements LegalMoveGenerator {

    MoveGenerator generator = Factory.getDefaults().moveGenerator.instance();

    CheckChecker checkChecker = Factory.getDefaults().checkChecker.instance();

    BitmaskProducer bitmaskProducer = new BitmaskProducer();

    @Override
    public MoveList generate(BoardRepresentation board, Color side) {
        MoveList moves = generator.generate(board, side);
        MoveList legalMoves = filterLegalMoves(board, moves, side);
        return legalMoves;
    }

    @Override
    public void generate(GameContext gameContext, OrderCalculator orderCalculator, BoardRepresentation board,
            Color side, MoveList moveList) {
        generator.generate(board, side, moveList);
        filterLegalMoves(board, moveList, side);
    }

    private MoveList filterLegalMoves(BoardRepresentation currBoard, MoveList moves, Color side) {

        /**
         * if we are already in check, then do the regular way:
         */
        if (checkChecker.isInChess(currBoard, side)) {
            return LegalMoveGeneratorImpl3.filterLegalMoves(currBoard, checkChecker, moves, side);
        }

        long kingRayMask = produceRayMask(
                side == Color.WHITE ? currBoard.getWhitePieces().getKing() : currBoard.getBlackPieces().getKing());

        /**
         * find out king pos:
         * check before: are we already in check? well, then do full check... (or can we do something faster...?)
         */
        for (MoveCursor moveCursor : moves) {

            /**
             * for king moves, do the full check;
             * do also full check if the moving figure starts from a king ray:
             */
            int move = moveCursor.getMoveInt();
            byte figureType = MoveImpl.getFigureType(move);
            byte from = MoveImpl.getFromIndex(move);
            if (figureType == FT_KING || (kingRayMask & 1L << from) > 0) {
                moveCursor.move(currBoard);
                if (checkChecker.isInChess(currBoard, side)) {
                    moveCursor.remove();
                }
                moveCursor.undoMove(currBoard);
            }
        }
        return moves;
    }

    /**
     * Produce ray mask of the king. means all directions (like a qeen moving) starting from the king.
     * @param pos
     * @return
     */
    private long produceRayMask(int pos) {
        return bitmaskProducer.genMobilityBitMask(pos, FT_QUEEN);
    }

}
