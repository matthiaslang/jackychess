package org.mattlang.jc.engine;

import static org.mattlang.jc.Constants.MAX_MOVES;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.Figure.*;
import static org.mattlang.jc.board.FigureConstants.B_PAWN;
import static org.mattlang.jc.board.FigureConstants.W_PAWN;
import static org.mattlang.jc.engine.sorting.OrderCalculator.isGoodCapture;
import static org.mattlang.jc.engine.sorting.OrderCalculator.isGoodPromotion;
import static org.mattlang.jc.moves.MoveImpl.*;
import static org.mattlang.util.Assertions.assertFieldNum;
import static org.mattlang.util.Assertions.assertFigureCodeOrEmpty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.mattlang.jc.BuildConstants;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.FigureConstants;
import org.mattlang.jc.engine.sorting.OrderCalculator;
import org.mattlang.jc.moves.CastlingMove;
import org.mattlang.jc.moves.MoveImpl;

public final class MoveList {

    private int[] moves = new int[MAX_MOVES];
    private int[] order = new int[MAX_MOVES];

    private int size = 0;

    /**
     * Moves which should be filtered during collecting of moves (used in staged move generation).
     * 4 Places are needed: a hash move, two killers, and a counter move at most.
     */
    private int[] filterMoves = new int[4];

    /**
     * The side to move of the moves in this move list.
     */
    private Color sideToMove;

    public MoveList() {
    }

    public void genMove(byte figureType, int from, int to, byte capturedFigure) {
        addMove(createNormalMove(figureType, from, to, capturedFigure));
    }

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

    public void genPawnMove(int from, int to, byte capturedFigure) {
        if (BuildConstants.ASSERTIONS) {
            assertFieldNum(from);
            assertFieldNum(to);
            assertFigureCodeOrEmpty(capturedFigure);
        }

        if (isOnLastLine(to)) {
            addMove(createPromotionMove(from, to, capturedFigure, sideToMove == WHITE ? W_Queen : B_Queen));
            addMove(createPromotionMove(from, to, capturedFigure, sideToMove == WHITE ? W_Rook : B_Rook));
            addMove(createPromotionMove(from, to, capturedFigure, sideToMove == WHITE ? W_Bishop : B_Bishop));
            addMove(createPromotionMove(from, to, capturedFigure, sideToMove == WHITE ? W_Knight : B_Knight));
        } else {
            addMove(createNormalMove(FigureConstants.FT_PAWN, from, to, capturedFigure));

        }
    }

    public void genPawnQuiets(long targets, int fromOffset) {
        while (targets != 0) {
            final int toIndex = Long.numberOfTrailingZeros(targets);
            genPawnMove(toIndex + fromOffset, toIndex, (byte) 0);
            targets &= targets - 1;
        }
    }

    public void genEnPassant(int from, int to) {
        addMove(createEnPassantMove(from, to, sideToMove == Color.WHITE ? B_PAWN : W_PAWN));
    }

    public void addCastlingMove(CastlingMove castlingMove) {
        addMove(createCastlingMove(castlingMove));
    }

    private MoveImpl moveWrapper = new MoveImpl("a1a2");

    /**
     * scores all moves in the movelist  with usage of a order calculator.
     * The ordercalculator can produce a order number for each move which is then used as search criteria.
     * with the lowest order for the best moves.
     *
     * @param orderCalculator
     */
    public void scoreMoves(OrderCalculator orderCalculator) {
        scoreMoves(orderCalculator, 0);
    }

    /**
     * scores all moves in the movelist  with usage of a order calculator.
     * The ordercalculator can produce a order number for each move which is then used as search criteria.
     * with the lowest order for the best moves.
     *
     * @param orderCalculator
     */
    public void scoreMoves(OrderCalculator orderCalculator, int start) {
        for (int i = start; i < size; i++) {
            int moveInt = moves[i];
            moveWrapper.fromLongEncoded(moveInt);
            order[i] = orderCalculator.calcOrder(moveWrapper, moveInt);
        }
    }

    /**
     * Scores capture moves and returns the number of "good" captures.
     *
     * @param orderCalculator
     * @param start
     * @return
     */
    public int scoreCaptureMoves(OrderCalculator orderCalculator, int start) {
        int goodOnes = 0;
        for (int i = start; i < size; i++) {
            int moveInt = moves[i];
            moveWrapper.fromLongEncoded(moveInt);
            int orderVal = orderCalculator.calcOrderForCaptures(moveWrapper);
            this.order[i] = orderVal;
            if (isGoodCapture(orderVal) || isGoodPromotion(orderVal)) {
                goodOnes++;
            }
        }
        return goodOnes;
    }

    public void scoreQuietMoves(OrderCalculator orderCalculator, int start) {
        for (int i = start; i < size; i++) {
            int moveInt = moves[i];
            moveWrapper.fromLongEncoded(moveInt);
            order[i] = orderCalculator.calcOrderForQuiets(moveWrapper);
        }
    }

    public int size() {
        return size;
    }

    public int get(int i) {
        return moves[i];
    }

    public int getOrder(int i) {
        return order[i];
    }

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

    public boolean isFiltered(int aMove) {
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

    /**
     * Extracts a java util ist with all moves a move objects.
     * This is not used during search as it would be too slow.
     * Its only for debugging purpose in test code since this is more handy
     * to inspect than an encoded int list.
     *
     * @return
     */
    public List<MoveImpl> extractList() {
        ArrayList<MoveImpl> l1 = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            l1.add(new MoveImpl(moves[i]));
        }
        l1.sort(Comparator.comparingInt(MoveImpl::getMoveInt));
        return l1;
    }

}