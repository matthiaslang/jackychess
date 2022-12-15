package org.mattlang.jc.moves;

import static org.mattlang.jc.Constants.MAX_MOVES;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.Figure.*;
import static org.mattlang.jc.board.FigureConstants.B_PAWN;
import static org.mattlang.jc.board.FigureConstants.W_PAWN;
import static org.mattlang.jc.moves.MoveImpl.*;

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

    public MoveListImpl() {
    }

    public void genMove(byte figureType, int from, int to, byte capturedFigure) {
        addMove(createNormalMove(figureType, from, to, capturedFigure));
    }

    public static boolean isOnLastLine(Color side, int to) {
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
    public void addCastlingMove(CastlingMove castlingMove) {
        addMove(createCastlingMove(castlingMove));
    }

    private MoveImpl moveWrapper = new MoveImpl("a1a2");

    public void sort(OrderCalculator orderCalculator) {
        sort(orderCalculator, 0);
    }

    public void sort(OrderCalculator orderCalculator, int start) {
        for (int i = start; i < size; i++) {
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
        moveCursor.init(this);
        return moveCursor;
    }

    public MoveCursor iterate(int startPos) {
        moveCursor.init(this, startPos);
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

    public void reset() {
        size = 0;
    }

    @Override
    public void close() {

    }

    public void addMove(int aMove) {
        moves[size] = aMove;
        size++;
    }

    public void addMoveWithOrder(int aMove, int orderVal) {
        moves[size] = aMove;
        order[size] = orderVal;
        size++;
    }

    public void swap(int i, int j) {

        int tmp = order[i];
        order[i] = order[j];
        order[j] = tmp;

        int ttmp = moves[i];
        moves[i] = moves[j];
        moves[j] = ttmp;
    }
}
