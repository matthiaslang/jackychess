package org.mattlang.jc.movegenerator;

import static org.mattlang.jc.board.FigureConstants.FT_KING;
import static org.mattlang.jc.board.FigureConstants.FT_QUEEN;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.CheckChecker;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.evaluation.BitmaskProducer;

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
        // simple ordering by capture first, to be a bit bettr in alpha beta pruning
        legalMoves.sortByCapture();
        return legalMoves;
    }

    private MoveList filterLegalMoves(BoardRepresentation currBoard, MoveList moves, Color side) {

        /**
         * if we are already in check, then do the regular way:
         */
        if (checkChecker.isInChess(currBoard, side)) {
            return simpleLegalMovesFiltering(currBoard, moves, side);
        }

        long kingRayMask = produceRayMask(
                side == Color.WHITE ? currBoard.getWhitePieces().getKing() : currBoard.getBlackPieces().getKing());

        MoveList legals = Factory.getDefaults().moveList.create();

        /**
         * find out king pos:
         * check before: are we already in check? well, then do full check... (or can we do something faster...?)
         */
        for (MoveCursor moveCursor : moves) {

            /**
             * for king moves, do the full check;
             * do also full check if the moving figure starts from a king ray:
             */

            Move move = moveCursor.getMove();
            if (move.getFigureType() == FT_KING || (kingRayMask & 1L << move.getFromIndex()) > 0) {
                moveCursor.move(currBoard);
                if (!checkChecker.isInChess(currBoard, side)) {
                    legals.addMove(moveCursor);
                }
                moveCursor.undoMove(currBoard);
            } else {
                // since the moving figure starts not from a king ray, it could not lead into a check situation:
                legals.addMove(moveCursor);
            }
        }
        return legals;
    }

    /**
     * Produce ray mask of the king. means all directions (like a qeen moving) starting from the king.
     * @param pos
     * @return
     */
    private long produceRayMask(int pos) {
        return bitmaskProducer.genMobilityBitMask(pos, FT_QUEEN);
    }

    /**
     * Noive filtering of legal moves: check each move on the board if it puts the king into check.
     * This is done, if no other optimization is possible.
     *
     * @param currBoard
     * @param moves
     * @param side
     * @return
     */
    private MoveList simpleLegalMovesFiltering(BoardRepresentation currBoard, MoveList moves, Color side) {
        MoveList legals = Factory.getDefaults().moveList.create();
        for (MoveCursor moveCursor : moves) {
            moveCursor.move(currBoard);
            if (!checkChecker.isInChess(currBoard, side)) {
                legals.addMove(moveCursor);
            }
            moveCursor.undoMove(currBoard);
        }
        return legals;
    }

}
