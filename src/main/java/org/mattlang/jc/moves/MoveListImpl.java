package org.mattlang.jc.moves;

import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.Figure.*;
import static org.mattlang.jc.board.FigureConstants.B_PAWN;
import static org.mattlang.jc.board.FigureConstants.W_PAWN;
import static org.mattlang.jc.moves.MoveImpl.*;

import java.util.Iterator;

import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.FigureConstants;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.sorting.LongSorter;
import org.mattlang.jc.engine.sorting.OrderCalculator;

public class MoveListImpl implements MoveList {

    private IntList moves = new IntList();
    private int[] order = new int[200];

    private boolean sorted = false;

    public final static MoveListPool<MoveListImpl> POOL = new MoveListPool<>(() -> new MoveListImpl());

    public MoveListImpl() {
    }

    public void genMove(byte figureType, int from, int to, byte capturedFigure) {
        moves.add(createNormalMove(figureType, from, to, capturedFigure));
    }

    public void genPawnMove(int from, int to, Color side, byte capturedFigure) {

        boolean isOnLastLine = false;
        if (side == WHITE) {
            isOnLastLine = to >= 56 && to <= 63;
        } else {
            isOnLastLine = to >= 0 && to <= 7;
        }
        if (isOnLastLine) {
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

    public void sort(OrderCalculator orderCalculator) {
        MoveImpl move = new MoveImpl("a1a2");
        for (int i = 0; i < moves.size(); i++) {
            move.fromLongEncoded(moves.get(i));
            order[i] = orderCalculator.calcOrder(move);
        }
        sorted = true;
    }

    @Override
    public int size() {
        return moves.size();
    }

    @Override
    public Iterator<MoveCursor> iterator() {
        if (sorted) {
            return new LazySortedMoveListIteratorImpl(new LongSorter(moves.getRaw(), moves.size(), order));
        } else {
            return new MoveListIteratorImpl(this);
        }
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
        sorted = false;
    }

    @Override
    public void close() {
        POOL.dispose(this);
    }

    public void addMove(int aMove) {
        moves.add(aMove);
    }
}
