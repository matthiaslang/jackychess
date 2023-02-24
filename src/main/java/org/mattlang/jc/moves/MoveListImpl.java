package org.mattlang.jc.moves;

import static org.mattlang.jc.Constants.MAX_MOVES;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.Figure.*;
import static org.mattlang.jc.board.FigureConstants.B_PAWN;
import static org.mattlang.jc.board.FigureConstants.W_PAWN;
import static org.mattlang.jc.moves.MoveImpl.*;

import java.util.Arrays;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.FigureConstants;
import org.mattlang.jc.engine.CheckChecker;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.sorting.OrderCalculator;

public final class MoveListImpl implements MoveList {

    private int[] moves = new int[MAX_MOVES];
    private int[] order = new int[MAX_MOVES];

    private int size = 0;

    private LazySortedMoveCursorImpl moveCursor = new LazySortedMoveCursorImpl();

    private MoveBoardIterator moveBoardIterator = new MoveBoardIterator();

    /**
     * Moves which should be filtered during collecting of moves (used in staged move generation).
     * 4 Places are needed: a hash move, two killers, and a counter move at most.
     */
    private int[] filterMoves = new int[4];

    /**
     * The side to move of the moves in this move list.
     */
    private Color sideToMove;

    public MoveListImpl() {
    }

    @Override
    public void genMove(byte figureType, int from, int to, byte capturedFigure) {
        addMove(createNormalMove(figureType, from, to, capturedFigure));
    }

    @Override
    public void genQuietMoves(byte figType, int from, long quietTargets) {
        while (quietTargets != 0) {
            final int toIndex = Long.numberOfTrailingZeros(quietTargets);
            genMove(figType, from, toIndex, (byte) 0);
            quietTargets &= quietTargets - 1;
        }
    }

    public static boolean isOnLastLine(int to) {
        return to >= 56 && to <= 63 || to >= 0 && to <= 7;
    }

    @Override
    public void genPawnMove(int from, int to, byte capturedFigure) {

        if (isOnLastLine(to)) {
            addMove(createPromotionMove(from, to, capturedFigure, sideToMove == WHITE ? W_Queen : B_Queen));
            addMove(createPromotionMove(from, to, capturedFigure, sideToMove == WHITE ? W_Rook : B_Rook));
            addMove(createPromotionMove(from, to, capturedFigure, sideToMove == WHITE ? W_Bishop : B_Bishop));
            addMove(createPromotionMove(from, to, capturedFigure, sideToMove == WHITE ? W_Knight : B_Knight));
        } else {
            addMove(createNormalMove(FigureConstants.FT_PAWN, from, to, capturedFigure));

        }
    }

    @Override
    public void genPawnQuiets(long targets, int fromOffset) {
        while (targets != 0) {
            final int toIndex = Long.numberOfTrailingZeros(targets);
            genPawnMove(toIndex + fromOffset, toIndex, (byte) 0);
            targets &= targets - 1;
        }
    }

    @Override
    public void genEnPassant(int from, int to, int enPassantCapturePos) {
        addMove(createEnPassantMove(from, to, sideToMove == Color.WHITE ? B_PAWN : W_PAWN, enPassantCapturePos));
    }

    @Override
    public void addCastlingMove(CastlingMove castlingMove) {
        addMove(createCastlingMove(castlingMove));
    }

    private MoveImpl moveWrapper = new MoveImpl("a1a2");

    public void scoreMoves(OrderCalculator orderCalculator) {
        scoreMoves(orderCalculator, 0);
    }

    public void scoreMoves(OrderCalculator orderCalculator, int start) {
        for (int i = start; i < size; i++) {
            int moveInt = moves[i];
            moveWrapper.fromLongEncoded(moveInt);
            order[i] = orderCalculator.calcOrder(moveWrapper, moveInt);
        }
    }

    public void scoreCaptureMoves(OrderCalculator orderCalculator, int start) {
        for (int i = start; i < size; i++) {
            int moveInt = moves[i];
            moveWrapper.fromLongEncoded(moveInt);
            order[i] = orderCalculator.calcOrderForCaptures(moveWrapper);
        }
    }

    public void scoreQuietMoves(OrderCalculator orderCalculator, int start) {
        for (int i = start; i < size; i++) {
            int moveInt = moves[i];
            moveWrapper.fromLongEncoded(moveInt);
            order[i] = orderCalculator.calcOrderForQuiets(moveWrapper);
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public MoveCursor iterate() {
        moveCursor.init(this);
        return moveCursor;
    }

    @Override
    public MoveBoardIterator iterateMoves(BoardRepresentation board, CheckChecker checkChecker) {
        MoveCursor moveCursor = iterate();
        moveBoardIterator.init(moveCursor, board, checkChecker);
        return moveBoardIterator;
    }

    public int get(int i) {
        return moves[i];
    }

    public int getOrder(int i) {
        return order[i];
    }

    @Override
    public void reset(Color sideToMove) {
        this.sideToMove = sideToMove;
        size = 0;
        Arrays.fill(filterMoves, 0);
    }

    public void addMove(int aMove) {
        if (!isFiltered(aMove)) {
            moves[size] = aMove;
            size++;
        }
    }

    public void addMoveWithOrder(int aMove, int orderVal) {
        if (!isFiltered(aMove)) {
            moves[size] = aMove;
            order[size] = orderVal;
            size++;
        }
    }

    private boolean isFiltered(int aMove) {
        for (int i = 0; i < filterMoves.length; i++) {
            int filterMove = filterMoves[i];
            // no more filtered moves set, so we kan skip
            if (filterMove == 0) {
                return false;
            }
            if (filterMove == aMove) {
                return true;
            }

        }
        return false;
    }

    public void swap(int i, int j) {

        int tmp = order[i];
        order[i] = order[j];
        order[j] = tmp;

        int ttmp = moves[i];
        moves[i] = moves[j];
        moves[j] = ttmp;
    }

    public void addFilter(int filterMove) {
        for (int i = 0; i < filterMoves.length; i++) {
            if (filterMoves[i] == 0) {
                filterMoves[i] = filterMove;
                return;
            }
        }
        throw new IllegalStateException("no free filter move!");
    }
}
