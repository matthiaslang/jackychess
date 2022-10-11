package org.mattlang.jc.moves;

import static org.mattlang.jc.Constants.MAX_MOVES;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.Figure.*;
import static org.mattlang.jc.board.FigureConstants.B_PAWN;
import static org.mattlang.jc.board.FigureConstants.W_PAWN;
import static org.mattlang.jc.moves.MoveImpl.*;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.FigureConstants;
import org.mattlang.jc.engine.CheckChecker;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.sorting.OrderCalculator;
import org.mattlang.jc.movegenerator.MoveGenerator;

public final class MoveListImpl implements MoveList {

    private int[] moves = new int[MAX_MOVES];
    private int[] order = new int[MAX_MOVES];

    private int size = 0;

    private LazySortedMoveCursorImpl moveCursor = new LazySortedMoveCursorImpl(this);

    private MoveBoardIterator moveBoardIterator = new MoveBoardIterator();

    private MoveGenerator generator = Factory.getDefaults().legalMoveGenerator.create();

    public MoveListImpl() {
    }

    public void genMove(byte figureType, int from, int to, byte capturedFigure) {
        addMove(createNormalMove(figureType, from, to, capturedFigure));
    }

    public static final boolean isOnLastLine(Color side, int to) {
        if (side == WHITE) {
            return to >= 56 && to <= 63;
        } else {
            return to >= 0 && to <= 7;
        }
    }

    public void genPawnMove(int from, int to, Color side, byte capturedFigure) {

        if (isOnLastLine(side, to)) {
            addMove(createPromotionMove(from, to, capturedFigure, side == WHITE ? W_Queen : B_Queen));
            addMove(createPromotionMove(from, to, capturedFigure, side == WHITE ? W_Rook : B_Rook));
            addMove(createPromotionMove(from, to, capturedFigure, side == WHITE ? W_Bishop : B_Bishop));
            addMove(createPromotionMove(from, to, capturedFigure, side == WHITE ? W_Knight : B_Knight));
        } else {
            addMove(createNormalMove(FigureConstants.FT_PAWN, from, to, capturedFigure));

        }
    }

    @Override
    public void genEnPassant(int from, int to, Color side, int enPassantCapturePos) {
        addMove(createEnPassantMove(from, to, side == Color.WHITE ? B_PAWN : W_PAWN, enPassantCapturePos));
    }

    @Override
    public void addRochadeLongWhite() {
        addMove(createCastlingMove(CastlingMove.CASTLING_WHITE_LONG));
    }

    @Override
    public void addRochadeShortWhite() {
        addMove(createCastlingMove(CastlingMove.CASTLING_WHITE_SHORT));
    }

    @Override
    public void addRochadeShortBlack() {
        addMove(createCastlingMove(CastlingMove.CASTLING_BLACK_SHORT));
    }

    @Override
    public void addRochadeLongBlack() {
        addMove(createCastlingMove(CastlingMove.CASTLING_BLACK_LONG));
    }

    private MoveImpl moveWrapper = new MoveImpl("a1a2");

    @Override
    public void generate(MoveGenerator.GenMode mode, OrderCalculator orderCalculator, BoardRepresentation board,
            Color side, int hashMove, int parentMove, int ply) {
        generator.generate(mode, orderCalculator, board, side, this);
        orderCalculator.prepareOrder(side, hashMove, parentMove, ply, board);
        sort(orderCalculator);
    }

    public void sort(OrderCalculator orderCalculator) {
        for (int i = 0; i < size; i++) {
            moveWrapper.fromLongEncoded(moves[i]);
            order[i] = orderCalculator.calcOrder(moveWrapper);
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public MoveCursor iterate() {
        moveCursor.init(moves, size, order);
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

    public final int getOrder(int i) {
        return order[i];
    }

    public void reset() {
        size = 0;
    }

    public void addMove(int aMove) {
        moves[size] = aMove;
        size++;
    }

    public void addMoveWithOrder(int aMove, int orderValue) {
        moves[size] = aMove;
        order[size] = orderValue;
        size++;
    }

    /**
     * Sorts the moves to swap the highes ordered move to the currIndex Position.
     * (Highest Prio means lowes order value)
     *
     * @param currIndex curr Index where to start the search (till end of list) to find the highest prio move
     *
     *                  As a result the hightes prio move is swapped to the currIndex Position.
     */
    public void sort(int currIndex) {
        if (currIndex >= size - 1) {
            return;
        }

        int currLowest = Integer.MAX_VALUE;
        int currLowestIndex = -1;
        for (int i = currIndex; i < size; i++) {
            if (order[i] < currLowest) {
                currLowest = order[i];
                currLowestIndex = i;
            }
        }
        if (currLowestIndex != currIndex) {
            swap(currIndex, currLowestIndex);
        }

    }

    private void swap(int i, int j) {
        int tmp = order[i];
        order[i] = order[j];
        order[j] = tmp;

        tmp = moves[i];
        moves[i] = moves[j];
        moves[j] = tmp;
    }
    
}
