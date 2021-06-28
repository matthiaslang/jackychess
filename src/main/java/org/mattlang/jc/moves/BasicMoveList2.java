package org.mattlang.jc.moves;

import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.Figure.*;
import static org.mattlang.jc.board.FigureConstants.B_PAWN;
import static org.mattlang.jc.board.FigureConstants.W_PAWN;
import static org.mattlang.jc.moves.MoveImpl.NO_EN_PASSANT_OPTION;
import static org.mattlang.jc.moves.MoveImpl.createCastling;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.FigureConstants;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.sorting.OrderCalculator;

public class BasicMoveList2 implements MoveList {

    private List<MoveImpl> moves = new ArrayList<>(60);
    private boolean subset = false;
    private boolean legal = false;
    private boolean checkMate = false;

    public BasicMoveList2() {
    }

 

    public void genMove(byte figureType, int from, int to, byte capturedFigure) {
        moves.add(MoveImpl.createNormal(figureType, from, to, capturedFigure, NO_EN_PASSANT_OPTION));
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
            moves.add(MoveImpl.createPromotion(from, to, capturedFigure, side == WHITE ? W_Queen : B_Queen));
            moves.add(MoveImpl.createPromotion(from, to, capturedFigure, side == WHITE ? W_Rook : B_Rook));
            moves.add(MoveImpl.createPromotion(from, to, capturedFigure, side == WHITE ? W_Bishop : B_Bishop));
            moves.add(MoveImpl.createPromotion(from, to, capturedFigure, side == WHITE ? W_Knight : B_Knight));
        } else {
            moves.add(MoveImpl.createNormal(FigureConstants.FT_PAWN, from, to, capturedFigure, enPassantOption));

        }
    }


    @Override
    public void genEnPassant(int from, int to, Color side, int enPassantCapturePos) {
        moves.add(MoveImpl.createEnPassant(from, to, side == Color.WHITE ? B_PAWN : W_PAWN, enPassantCapturePos));
    }

    @Override
    public void hypotheticalPawnCapture(int from, int to) {
        // not needed here, since this is only a hypothetical theoretical capture.
    }

    @Override
    public void addRochadeLongWhite() {
        moves.add(createCastling(CastlingMove.CASTLING_WHITE_LONG));
    }

    @Override
    public void addRochadeShortWhite() {
        moves.add(createCastling(CastlingMove.CASTLING_WHITE_SHORT));
    }

    @Override
    public void addRochadeShortBlack() {
        moves.add(createCastling(CastlingMove.CASTLING_BLACK_SHORT));
    }

    @Override
    public void addRochadeLongBlack() {
        moves.add(createCastling(CastlingMove.CASTLING_BLACK_LONG));
    }

    @Override
    public void sortMoves(Comparator<Move> moveComparator) {
        moves.sort(moveComparator);
    }

    @Override
    public void sort(OrderCalculator orderCalculator) {
        for (Move move : moves) {
            move.setOrder(orderCalculator.calcOrder(move));
        }
        moves.sort(Comparator.comparingInt(Move::getOrder));
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

    public List<MoveImpl> getMoves() {
        return moves;
    }


    @Override
    public Iterator<MoveCursor> iterator() {
        return new BasicMoveListIterator2(this);
    }

    @Override
    public void close() {
        // dont do anything, since this impl has nothing to do here
    }
}
