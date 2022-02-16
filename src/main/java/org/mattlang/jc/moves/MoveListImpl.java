package org.mattlang.jc.moves;

import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.Figure.*;
import static org.mattlang.jc.board.FigureConstants.B_PAWN;
import static org.mattlang.jc.board.FigureConstants.W_PAWN;
import static org.mattlang.jc.moves.MoveImpl.*;

import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.FigureConstants;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.sorting.OrderCalculator;

public class MoveListImpl implements MoveList {

    private IntList moves = new IntList();
    private int[] order = new int[200];

    private LazySortedMoveCursorImpl moveCursor = new LazySortedMoveCursorImpl();

    public MoveListImpl() {
    }

    public void genMove(byte figureType, int from, int to, byte capturedFigure) {
        moves.add(createNormalMove(figureType, from, to, capturedFigure));
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
            moves.add(createPromotionMove(from, to, capturedFigure, side == WHITE ? W_Queen : B_Queen));
            moves.add(createPromotionMove(from, to, capturedFigure, side == WHITE ? W_Rook : B_Rook));
            moves.add(createPromotionMove(from, to, capturedFigure, side == WHITE ? W_Bishop : B_Bishop));
            moves.add(createPromotionMove(from, to, capturedFigure, side == WHITE ? W_Knight : B_Knight));
        } else {
            moves.add(createNormalMove(FigureConstants.FT_PAWN, from, to, capturedFigure));

        }
    }

    @Override
    public void genEnPassant(int from, int to, Color side, int enPassantCapturePos) {
        moves.add(createEnPassantMove(from, to, side == Color.WHITE ? B_PAWN : W_PAWN, enPassantCapturePos));
    }

    @Override
    public void hypotheticalPawnCapture(int from, int to) {
        // not needed here, since this is only a hypothetical theoretical capture.
    }

    @Override
    public void addRochadeLongWhite() {
        moves.add(createCastlingMove(CastlingMove.CASTLING_WHITE_LONG));
    }

    @Override
    public void addRochadeShortWhite() {
        moves.add(createCastlingMove(CastlingMove.CASTLING_WHITE_SHORT));
    }

    @Override
    public void addRochadeShortBlack() {
        moves.add(createCastlingMove(CastlingMove.CASTLING_BLACK_SHORT));
    }

    @Override
    public void addRochadeLongBlack() {
        moves.add(createCastlingMove(CastlingMove.CASTLING_BLACK_LONG));
    }

    private MoveImpl moveWrapper = new MoveImpl("a1a2");

    public void sort(OrderCalculator orderCalculator) {

        for (int i = 0; i < moves.size(); i++) {
            moveWrapper.fromLongEncoded(moves.get(i));
            order[i] = orderCalculator.calcOrder(moveWrapper);
        }
    }

    @Override
    public int size() {
        return moves.size();
    }

    @Override
    public MoveCursor iterate() {
        moveCursor.init(moves.getRaw(), moves.size(), order);
        return moveCursor;
    }

    public final int get(int i) {
        return moves.get(i);
    }

    public final int getOrder(int i) {
        return order[i];
    }

    public void remove(int index) {
        moves.remove(index);
    }

    public void reset() {
        moves.reset();
    }

    @Override
    public void close() {

    }

    public void addMove(int aMove) {
        moves.add(aMove);
    }
}
