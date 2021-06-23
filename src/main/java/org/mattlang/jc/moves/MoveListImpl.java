package org.mattlang.jc.moves;

import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.Figure.*;
import static org.mattlang.jc.board.FigureConstants.B_PAWN;
import static org.mattlang.jc.board.FigureConstants.W_PAWN;
import static org.mattlang.jc.moves.MoveImpl.*;

import java.util.Comparator;
import java.util.Iterator;

import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.FigureConstants;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.sorting.LongSorter;
import org.mattlang.jc.engine.sorting.OrderCalculator;

public class MoveListImpl implements MoveList {

    private LongList moves = new LongList();
    private int[] order = new int[200];

    private boolean subset = false;
    private boolean legal = false;
    private boolean checkMate = false;
    private boolean sorted = false;

    public MoveListImpl() {
    }

    public void genMove(byte figureType, int from, int to, byte capturedFigure) {
        moves.add(createNormalMove(figureType, from, to, capturedFigure, NO_EN_PASSANT_OPTION));
    }

    public void genPawnMove(int from, int to, Color side, byte capturedFigure, int enPassantOption) {

        // todo backward compatibility. should be cleaned up, once we use only this impl
        enPassantOption = enPassantOption == -1? NO_EN_PASSANT_OPTION : enPassantOption;
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
            moves.add(createNormalMove(FigureConstants.FT_PAWN, from, to, capturedFigure, enPassantOption));

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

    @Override
    public void sortMoves(Comparator<Move> moveComparator) {
        throw new IllegalArgumentException("not supported any more... use embedded ordering!");
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
    public boolean isSubset() {
        return subset;
    }

    @Override
    public boolean isLegal() {
        return legal;
    }

    @Override
    public boolean isCheckMate() {
        return checkMate;
    }

    public void setSubset(boolean subset) {
        this.subset = subset;
    }

    public void setLegal(boolean legal) {
        this.legal = legal;
    }

    public void setCheckMate(boolean checkMate) {
        this.checkMate = checkMate;
    }

    @Override
    public Iterator<MoveCursor> iterator() {
        if (sorted) {
            return new LazySortedMoveListIteratorImpl(new LongSorter(moves.getRaw(), moves.size(), order));
        } else {
            return new MoveListIteratorImpl(this);
        }
    }

    public final long get(int i) {
        return moves.get(i);
    }

    public void remove(int index) {
        moves.remove(index);
    }

    public void reset() {
        moves.reset();

        subset = false;
        legal = false;
        checkMate = false;
        sorted = false;
    }
}
