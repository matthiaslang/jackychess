package org.mattlang.jc.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.mattlang.jc.board.*;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.sorting.MoveIteratorImpl;
import org.mattlang.jc.engine.sorting.MvvLva;
import org.mattlang.jc.movegenerator.MoveGeneration;
import org.mattlang.jc.movegenerator.PseudoLegalMoveGenerator;
import org.mattlang.jc.moves.MoveBoardIterator;
import org.mattlang.jc.moves.MoveImpl;
import org.mattlang.jc.uci.FenParser;

/**
 * Helper class for debugging.
 * We currently seem to have a problem that calculated pv moves are not always valid. Cutechess is logging this from
 * time to time.
 * So we check this and log at least the issue for further analysis.
 */
public class MoveValidator {

    private static final Logger LOGGER = Logger.getLogger(MoveValidator.class.getSimpleName());

    private PseudoLegalMoveGenerator movegen = new PseudoLegalMoveGenerator();

    private MoveIteratorImpl moveIterator = new MoveIteratorImpl();

    private MoveBoardIterator moveBoardIterator = new MoveBoardIterator();

    /**
     * reused movelist.
     */
    private MoveList moveList = new MoveList();

    public boolean isLegalMove(BoardRepresentation board, Move move, Color who2Move) {
        return isLegalMove(board, move.getMoveInt(), who2Move);
    }

    public boolean isLegalMove(BoardRepresentation board, int move, Color who2Move) {

        moveList.reset(who2Move);
        // gen the moves for the respective move type to check:
        switch (MoveImpl.getFigureType(move)) {
        case FigureConstants.FT_PAWN:
            MoveGeneration.generatePawnMoves(board, who2Move.ordinal(), moveList);
            break;
        case FigureConstants.FT_ROOK:
            MoveGeneration.generateRookMoves(board, who2Move.ordinal(), moveList);
            break;
        case FigureConstants.FT_BISHOP:
            MoveGeneration.generateBishopMoves(board, who2Move.ordinal(), moveList);
            break;
        case FigureConstants.FT_QUEEN:
            MoveGeneration.generateQueenMoves(board, who2Move.ordinal(), moveList);
            break;
        case FigureConstants.FT_KNIGHT:
            MoveGeneration.generateKnightMoves(board, who2Move.ordinal(), moveList);
            break;
        case FigureConstants.FT_KING:
            MoveGeneration.generateKingMoves(board, who2Move.ordinal(), moveList);
            break;
        default:
            throw new IllegalStateException("Illegal Move to check!");
        }

        try (MoveBoardIterator iterator = iterateMoves(board)) {
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

    public IntList validateAndCorrectPvList(IntList pvs, GameState gameState) {

        // play and validate all pv moves:
        BoardRepresentation board = gameState.getBoard().copy();

        Color who2Move = gameState.getWho2Move();
        MoveImpl moveWrapper = new MoveImpl("a1a1");
        for (int i = 0; i < pvs.size(); i++) {

            int moveI = pvs.get(i);

            boolean legal = isLegalMove(board, moveI, who2Move);
            if (legal) {
                moveWrapper.fromLongEncoded(moveI);
                board.domove(moveWrapper);
            } else {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.warning(
                            "Illegal PV Move encountered during pv enrichment " + moveWrapper.toUCIString(board));
                }
                pvs.cutToSize(i);
                break;
            }
            who2Move = who2Move.invert();
        }

        return pvs;
    }

    /**
     * Tester if legal moves are available for a position
     *
     * @param board
     * @return
     */
    public boolean hasLegalMoves(BoardRepresentation board) {

        moveList.reset(board.getSiteToMove());
        movegen.generate(board, board.getSiteToMove().ordinal(), moveList);

        boolean hasLegalMoves = false;
        try (MoveBoardIterator iterator = iterateMoves(board)) {
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
    public Move findSimpleBestMove(GameState gameState, MoveList legalMovesToSearch) {
        BoardRepresentation board = gameState.getBoard();
        int maxOrder = Integer.MIN_VALUE;
        int bestMove = 0;
        try (MoveBoardIterator iterator = iterateMoves(board, legalMovesToSearch)) {
            while (iterator.doNextValidMove()) {
                int order = MvvLva.calcMMVLVA(iterator);
                if (order > maxOrder) {
                    maxOrder = order;
                    bestMove = iterator.getMoveInt();
                }
            }
        }
        return new MoveImpl(bestMove);
    }

    private MoveBoardIterator iterateMoves(BoardRepresentation board) {
        return iterateMoves(board, moveList);
    }

    private MoveBoardIterator iterateMoves(BoardRepresentation board, MoveList moveList) {
        moveIterator.init(moveList, 0);
        moveBoardIterator.init(moveIterator, board);
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
        generateLegalMoves(resultList, board, color);
        return resultList;
    }

    public void generateLegalMoves(MoveList resultList, BoardRepresentation board, Color color) {
        resultList.reset(color);
        moveList.reset(color);
        movegen.generate(board, color.ordinal(), moveList);

        try (MoveBoardIterator iterator = iterateMoves(board)) {
            while (iterator.doNextValidMove()) {
                resultList.addMove(iterator.getMoveInt());
            }
        }
    }

    public MoveList createLegalMovesToSearch(GameState gameState, String[] searchMoves) {
        MoveList legalMovesToSearch = generateLegalMoves(gameState.getBoard(), gameState.getWho2Move());
        if (searchMoves != null && searchMoves.length > 0) {
            MoveList searchMovesResult = new MoveList();
            for (String searchMove : searchMoves) {
                Move move = FenParser.parseMove(gameState.getBoard(), searchMove);
                if (isLegalMove(gameState.getBoard(), move, gameState.getWho2Move())) {
                    searchMovesResult.addMove(move.getMoveInt());
                }
            }
            if (searchMovesResult.size() > 0) {
                legalMovesToSearch = searchMovesResult;
            }
        }
        return legalMovesToSearch;
    }
}
