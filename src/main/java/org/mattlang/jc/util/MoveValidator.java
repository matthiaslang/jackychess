package org.mattlang.jc.util;

import java.util.List;
import java.util.logging.Logger;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.search.NegaMaxResult;
import org.mattlang.jc.engine.tt.TTCacheInterface;
import org.mattlang.jc.engine.tt.TTEntry;
import org.mattlang.jc.movegenerator.BBLegalMoveGeneratorImpl;
import org.mattlang.jc.moves.MoveImpl;

/**
 * Helper class for debugging.
 * We currently seem to have a problem that calculated pv moves are not always valid. Cutechess is logging this from
 * time to time.
 * So we check this and log at least the issue for further analysis.
 */
public class MoveValidator {

    private static final Logger LOGGER = Logger.getLogger(MoveValidator.class.getSimpleName());

    private static BBLegalMoveGeneratorImpl legalMoveGen = new BBLegalMoveGeneratorImpl();

    public static void validate(GameState gameState, NegaMaxResult rslt) {
        BoardRepresentation board = gameState.getBoard().copy();

        Color who2Move = gameState.getWho2Move();
        for (Move move : rslt.pvList.getPvMoves()) {

            boolean legal = isLegalMove(board, move, who2Move);

            if (legal) {
                board.domove(move);
            } else {
                LOGGER.warning("Illegal PV Move " + move.toStr());

            }
            who2Move = who2Move.invert();
        }

        // test the best move itself:
        board = gameState.getBoard().copy();
        boolean legal = isLegalMove(board, rslt.savedMove, gameState.getWho2Move());
        if (!legal) {
            LOGGER.severe("Illegal Best Move " + rslt.savedMove.toStr());
        }
    }

    private static boolean isLegalMove(BoardRepresentation board, Move move, Color who2Move) {
        return isLegalMove(board, move.toInt(), who2Move);
    }

    private static boolean isLegalMove(BoardRepresentation board, int move, Color who2Move) {

        MoveList legalMoves =
                legalMoveGen.generate(board, who2Move);

        // todo clean up and make nicer way to check this...
        for (MoveCursor legalMove : legalMoves) {
            if (legalMove.getMoveInt() == move) {
                return true;
            }
        }
        //        String moves = "";
        //        for (MoveCursor legalMove : legalMoves) {
        //            moves += new MoveImpl(legalMove.getMoveInt()).toStr();
        //            moves += ",";
        //
        //        }
        return false;
    }

    /**
     * Since the pv list extracted from the triangular may be shortened if tt cach hits have been used,
     * we try to fill missing entries via entries from the tt cache.
     *
     * It is of course a good questions if this is the best method. We could of course reconstruct the pv list all the
     * time
     * from the tt cache, but this may also not work always, if entries get overridden.
     *
     * @return
     */

    public static List<Integer> enrichPVList(List<Integer> pvs, GameState gameState, TTCacheInterface ttCache,
            int depth) {

        if (pvs.size() == depth) {
            return pvs; // nothing to enrich
        }

        // play all pv moves:
        BoardRepresentation board = gameState.getBoard().copy();

        Color who2Move = gameState.getWho2Move();
        for (int moveI : pvs) {

            boolean legal = isLegalMove(board, moveI, who2Move);
            MoveImpl move = new MoveImpl(moveI);
            if (legal) {
                board.domove(move);
            } else {
                LOGGER.warning("Illegal PV Move " + move.toStr());
                return pvs;
            }
            who2Move = who2Move.invert();
        }

        // now enrich the missing ones:
        int size = pvs.size();
        for (int d = size; d < depth; d++) {
            TTEntry tte = ttCache.getTTEntry(board, who2Move);
            if (tte != null && tte.getMove() != 0) {
                Move move = new MoveImpl(tte.getMove());
                boolean legal = isLegalMove(board, move, who2Move);

                if (legal) {
                    board.domove(move);
                    pvs.add(tte.getMove());
                } else {
                    // old or weird entry... stop here
                    break;

                }
                who2Move = who2Move.invert();
            } else {
                break; // stop here...
            }

        }
        return pvs;
    }
}
