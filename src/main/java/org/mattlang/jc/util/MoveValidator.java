package org.mattlang.jc.util;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.CheckChecker;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.search.NegaMaxResult;
import org.mattlang.jc.engine.tt.IntCache;
import org.mattlang.jc.movegenerator.BBCheckCheckerImpl;
import org.mattlang.jc.movegenerator.PseudoLegalMoveGenerator;
import org.mattlang.jc.moves.MoveBoardIterator;
import org.mattlang.jc.moves.MoveImpl;
import org.mattlang.jc.moves.MoveListImpl;

/**
 * Helper class for debugging.
 * We currently seem to have a problem that calculated pv moves are not always valid. Cutechess is logging this from
 * time to time.
 * So we check this and log at least the issue for further analysis.
 */
public class MoveValidator {

    private static final Logger LOGGER = Logger.getLogger(MoveValidator.class.getSimpleName());

    private PseudoLegalMoveGenerator movegen = new PseudoLegalMoveGenerator();

    private CheckChecker checkChecker = new BBCheckCheckerImpl();

    /**
     * reused movelist.
     */
    private MoveList moveList = new MoveListImpl();

    public void validate(GameState gameState, NegaMaxResult rslt) {
        BoardRepresentation board = gameState.getBoard().copy();

        Color who2Move = gameState.getWho2Move();
        for (Move move : rslt.pvList.getPvMoves()) {

            boolean legal = isLegalMove(board, move, who2Move);

            if (legal) {
                board.domove(move);
            } else {
                LOGGER.warning("depth: "+ rslt.targetDepth+ " Illegal PV Move " + move.toStr() + " in " + rslt.toLogString());
                break;

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

    public boolean isLegalMove(BoardRepresentation board, Move move, Color who2Move) {
        return isLegalMove(board, move.toInt(), who2Move);
    }

    public boolean isLegalMove(BoardRepresentation board, int move, Color who2Move) {

        moveList.reset();
        movegen.generate(board, who2Move, moveList);

        try (MoveBoardIterator iterator = moveList.iterateMoves(board, checkChecker)) {
            while (iterator.doNextMove()) {
                if (iterator.getMoveInt() == move) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Since the pv list extracted from the triangular may be shortened if tt cach hits have been used,
     * we try to fill missing entries via entries from the tt cache if they exist.
     *
     * The pv array as well as the pv cache might not fully contain the relevant pv moves so the enrichment could
     * be incomplete.
     *
     * @return
     */

    public List<Integer> enrichPVList(List<Integer> pvs, GameState gameState, IntCache pvCache,
            int depth) {

        if (pvs.size() == depth) {
            LOGGER.fine("pv == expected depth. everything ok");
            return pvs; // nothing to enrich
        }

        // play all pv moves:
        BoardRepresentation board = gameState.getBoard().copy();

        Color who2Move = gameState.getWho2Move();
        ArrayList<Integer> validatedPvs=new ArrayList<>();

        for (int moveI : pvs) {

            boolean legal = isLegalMove(board, moveI, who2Move);
            MoveImpl move = new MoveImpl(moveI);
            if (legal) {
                board.domove(move);
                validatedPvs.add(moveI);
            } else {
                LOGGER.fine("Illegal PV Move encountered during pv enrichment " + move.toStr());
                break;
            }
            who2Move = who2Move.invert();
        }

        enrichWithPVCache(validatedPvs, pvCache, depth, board, who2Move);

        if (validatedPvs.size() == depth) {
            LOGGER.fine("pv enrichment successful");
        } else {
            LOGGER.info("could not enrich pv with pv cache!");
        }

        return validatedPvs;
    }

    private void enrichWithPVCache(List<Integer> pvs, IntCache pvCache, int depth, BoardRepresentation board, Color who2Move) {
        // now enrich the missing ones:
        int size = pvs.size();
        int extended=0;
        for (int d = size; d < depth; d++) {
            int tte = pvCache.find(board.getZobristHash());
            if (tte != IntCache.NORESULT) {
                Move move = new MoveImpl(tte);
                boolean legal = isLegalMove(board, move, who2Move);

                if (legal) {
                    board.domove(move);
                    pvs.add(tte);
                    extended++;
                } else {
                    // old or weird entry... stop here
                    LOGGER.fine("stopped extending pv: found non legal pv cache move! extended " + extended + " of " + (
                            depth -size));
                    break;

                }
                who2Move = who2Move.invert();
            } else {
                LOGGER.fine("stopped extending pv: no pv cache entry found! extended " + extended + " of " + (depth -size));
                break; // stop here...
            }

        }
    }
}
