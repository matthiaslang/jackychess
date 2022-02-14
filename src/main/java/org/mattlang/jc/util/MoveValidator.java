package org.mattlang.jc.util;

import java.util.List;
import java.util.logging.Logger;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.search.NegaMaxResult;
import org.mattlang.jc.engine.tt.IntCache;
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

    private BBLegalMoveGeneratorImpl legalMoveGen = new BBLegalMoveGeneratorImpl();

    /** reused movelist. */
    private MoveList moveList= Factory.getDefaults().moveList.create();

    public void validate(GameState gameState, NegaMaxResult rslt) {
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

    private boolean isLegalMove(BoardRepresentation board, Move move, Color who2Move) {
        return isLegalMove(board, move.toInt(), who2Move);
    }

    private boolean isLegalMove(BoardRepresentation board, int move, Color who2Move) {

        moveList.reset();
        legalMoveGen.generate(board, who2Move, moveList);

        // todo clean up and make nicer way to check this...
        for (MoveCursor legalMove : moveList) {
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

    public List<Integer> enrichPVList(List<Integer> pvs, GameState gameState, IntCache pvCache,
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
            int tte = pvCache.find(board.getZobristHash());
            if (tte != IntCache.NORESULT) {
                Move move = new MoveImpl(tte);
                boolean legal = isLegalMove(board, move, who2Move);

                if (legal) {
                    board.domove(move);
                    pvs.add(tte);
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
