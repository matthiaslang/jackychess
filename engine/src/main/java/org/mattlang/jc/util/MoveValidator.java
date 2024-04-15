package org.mattlang.jc.util;

import static java.util.logging.Level.SEVERE;
import static org.mattlang.jc.util.LoggerUtils.fmtSevere;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.CheckChecker;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.search.NegaMaxResult;
import org.mattlang.jc.engine.sorting.MovePicker;
import org.mattlang.jc.engine.sorting.MvvLva;
import org.mattlang.jc.movegenerator.BBCheckCheckerImpl;
import org.mattlang.jc.movegenerator.PseudoLegalMoveGenerator;
import org.mattlang.jc.moves.MoveBoardIterator;
import org.mattlang.jc.moves.MoveImpl;

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

    private MovePicker movePicker = new MovePicker();

    private MoveBoardIterator moveBoardIterator = new MoveBoardIterator();

    /**
     * reused movelist.
     */
    private MoveList moveList = new MoveList();

    public void validate(GameState gameState, NegaMaxResult rslt) {
        if (LOGGER.isLoggable(Level.WARNING)) {
            BoardRepresentation board = gameState.getBoard().copy();

            Color who2Move = gameState.getWho2Move();
            for (Move move : rslt.pvList.getPvMoves()) {

                boolean legal = isLegalMove(board, move, who2Move);

                if (legal) {
                    board.domove(move);
                } else {
                    LOGGER.warning(
                            "depth: " + rslt.targetDepth + " Illegal PV Move " + move.toUCIString(board) + " in "
                                    + rslt.toLogString());
                    break;

                }
                who2Move = who2Move.invert();
            }
        }

        // test the best move itself:
        if (LOGGER.isLoggable(SEVERE)) {
            BoardRepresentation board = gameState.getBoard().copy();
            boolean legal = isLegalMove(board, rslt.savedMove, gameState.getWho2Move());
            if (!legal) {
                LOGGER.log(SEVERE, fmtSevere(gameState, "Illegal Best Move " + rslt.savedMove.toUCIString(board)));
            }
        }
    }

    public boolean isLegalMove(BoardRepresentation board, Move move, Color who2Move) {
        return isLegalMove(board, move.getMoveInt(), who2Move);
    }

    public boolean isLegalMove(BoardRepresentation board, int move, Color who2Move) {

        moveList.reset(who2Move);
        movegen.generate(board, who2Move, moveList);

        try (MoveBoardIterator iterator = iterateMoves(board, checkChecker)) {
            while (iterator.doNextValidMove()) {
                if (iterator.getMoveInt() == move) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks the pv list if it contains valid moves and shorten it if there are invalid moves.
     *
     * There seems to be an issue where cutechess reports invalid pv moves from our output.
     * This method tries to correct this (as long as we do not know the real reason).
     *
     * @return
     */

    public List<Integer> validateAndCorrectPvList(List<Integer> pvs, GameState gameState) {

        // play and validate all pv moves:
        BoardRepresentation board = gameState.getBoard().copy();

        Color who2Move = gameState.getWho2Move();
        ArrayList<Integer> validatedPvs = new ArrayList<>();

        for (int moveI : pvs) {

            boolean legal = isLegalMove(board, moveI, who2Move);
            MoveImpl move = new MoveImpl(moveI);
            if (legal) {
                board.domove(move);
                validatedPvs.add(moveI);
            } else {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.warning("Illegal PV Move encountered during pv enrichment " + move.toUCIString(board));
                }
                break;
            }
            who2Move = who2Move.invert();
        }

        return validatedPvs;
    }

    /**
     * Tester if legal moves are available for a position
     *
     * @param board
     * @return
     */
    public boolean hasLegalMoves(BoardRepresentation board) {

        moveList.reset(board.getSiteToMove());
        movegen.generate(board, board.getSiteToMove(), moveList);

        boolean hasLegalMoves = false;
        try (MoveBoardIterator iterator = iterateMoves(board, checkChecker)) {
            while (iterator.doNextValidMove()) {
                hasLegalMoves = true;
                break;
            }
        }
        return hasLegalMoves;

    }

    /**
     * Finds a first "best move" by ordering the moves by mmv lva.
     * This is used to have a first proper best move before we even start our search.
     *
     * @param gameState
     * @return
     */
    public Move findSimpleBestMove(GameState gameState) {
        BoardRepresentation board = gameState.getBoard();
        moveList.reset(board.getSiteToMove());
        movegen.generate(board, board.getSiteToMove(), moveList);

        int minOrder = Integer.MAX_VALUE;
        int bestMove = 0;
        try (MoveBoardIterator iterator = iterateMoves(board, checkChecker)) {
            while (iterator.doNextValidMove()) {
                int order = MvvLva.calcMMVLVA(iterator);
                if (order < minOrder) {
                    minOrder = order;
                    bestMove = iterator.getMoveInt();
                }
            }
        }
        return new MoveImpl(bestMove);
    }

    private MoveBoardIterator iterateMoves(BoardRepresentation board, CheckChecker checkChecker) {
        movePicker.init(moveList, 0);
        moveBoardIterator.init(movePicker, board, checkChecker);
        return moveBoardIterator;
    }

    /**
     * Delivers a move list with all legal moves for a position and a side to move.
     * This uses the PseudoLegalMoveGenerator + iterator + checkchecker to filter all legal moves.
     *
     * @param board
     * @param color
     * @return
     */
    public MoveList generateLegalMoves(BoardRepresentation board, Color color) {
        MoveList resultList = new MoveList();
        resultList.reset(color);
        moveList.reset(color);
        movegen.generate(board, board.getSiteToMove(), moveList);

        try (MoveBoardIterator iterator = iterateMoves(board, checkChecker)) {
            while (iterator.doNextValidMove()) {
                resultList.addMove(iterator.getMoveInt());
            }
        }
        return resultList;
    }
}
