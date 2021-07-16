package org.mattlang.jc.engine;

import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.Figure.*;
import static org.mattlang.jc.board.FigureConstants.B_PAWN;
import static org.mattlang.jc.board.FigureConstants.W_PAWN;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.mattlang.jc.board.*;
import org.mattlang.jc.engine.sorting.OrderCalculator;

public class BasicMoveList implements MoveList {

    private List<Move> moves = new ArrayList<>(60);
    private boolean subset = false;
    private boolean legal = false;
    private boolean checkMate = false;

    public BasicMoveList() {
    }

    public BasicMoveList(List<Move> moves) {
        this.moves = moves;
    }

    public void genMove(byte figureType, int from, int to, byte capturedFigure) {
        moves.add(new BasicMove(figureType, from, to, capturedFigure));
    }

    public void genPawnMove(int from, int to, Color side, byte capturedFigure) {
        boolean isOnLastLine = false;
        if (side == WHITE) {
            isOnLastLine = to >= 56 && to <= 63;
        } else {
            isOnLastLine = to >= 0 && to <= 7;
        }
        if (isOnLastLine) {
            moves.add(new PawnPromotionMove(from, to, capturedFigure, side == WHITE ? W_Queen : B_Queen));
            moves.add(new PawnPromotionMove(from, to, capturedFigure, side == WHITE ? W_Rook : B_Rook));
            moves.add(new PawnPromotionMove(from, to, capturedFigure, side == WHITE ? W_Bishop : B_Bishop));
            moves.add(new PawnPromotionMove(from, to, capturedFigure, side == WHITE ? W_Knight : B_Knight));
        } else {
            moves.add(new BasicMove(FigureConstants.FT_PAWN, from, to, capturedFigure));

        }
    }


    @Override
    public void genEnPassant(int from, int to, Color side, int enPassantCapturePos) {
        moves.add(new EnPassantMove(from, to, side == Color.WHITE ? B_PAWN : W_PAWN, enPassantCapturePos));
    }

    @Override
    public void hypotheticalPawnCapture(int from, int to) {
        // not needed here, since this is only a hypothetical theoretical capture.
    }

    @Override
    public void addRochadeLongWhite() {
        moves.add(RochadeMove.CASTLING_WHITE_LONG);
    }

    @Override
    public void addRochadeShortWhite() {
        moves.add(RochadeMove.CASTLING_WHITE_SHORT);
    }

    @Override
    public void addRochadeShortBlack() {
        moves.add(RochadeMove.CASTLING_BLACK_SHORT);
    }

    @Override
    public void addRochadeLongBlack() {
        moves.add(RochadeMove.CASTLING_BLACK_LONG);
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

    public List<Move> getMoves() {
        return moves;
    }

    @Override
    public Iterator<MoveCursor> iterator() {
        return new BasicMoveListIterator(this);
    }

    @Override
    public void close() {
        // dont do anything, since this impl has nothing to do here
    }
}
