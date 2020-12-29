package org.mattlang.jc.engine;

import org.mattlang.jc.board.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.Figure.*;
import static org.mattlang.jc.board.FigureConstants.B_PAWN;
import static org.mattlang.jc.board.FigureConstants.W_PAWN;

public class BasicMoveList implements MoveList {

    private List<Move> moves = new ArrayList<>(60);

    public BasicMoveList() {
    }

    public BasicMoveList(List<Move> moves) {
        this.moves = moves;
    }

    public void genMove(byte castlingRightsBefore, int from, int to, byte capturedFigure) {
        moves.add(new BasicMove(castlingRightsBefore, from, to, capturedFigure));
    }

    public void genPawnMove(int from, int to, Color side, byte capturedFigure, int enPassantOption) {
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
            moves.add(new BasicMove(from, to, capturedFigure, enPassantOption));

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
    public void addRochadeLongWhite(byte castlingRightsBefore) {
        moves.add(RochadeMove.createCastlingWhiteLong(castlingRightsBefore));
    }

    @Override
    public void addRochadeShortWhite(byte castlingRightsBefore) {
        moves.add(RochadeMove.createCastlingWhiteShort(castlingRightsBefore));
    }

    @Override
    public void addRochadeShortBlack(byte castlingRightsBefore) {
        moves.add(RochadeMove.createCastlingBlackShort(castlingRightsBefore));
    }

    @Override
    public void addRochadeLongBlack(byte castlingRightsBefore) {
        moves.add(RochadeMove.createCastlingBlackLong(castlingRightsBefore));
    }

    @Override
    public void addMove(MoveCursor moveCursor) {
        moves.add(moveCursor.getMove());
    }

    @Override
    public boolean capturesFigure(Figure figure) {
        byte searchedFigCode = figure.figureCode;
        for (Move move : moves) {
            if (searchedFigCode == move.getCapturedFigure()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void sortByCapture() {
        moves.sort(CMP);
    }

    private static final Comparator<Move> CMP = new Comparator<Move>() {

        @Override
        public int compare(Move o1, Move o2) {
            int c1 = o1.getCapturedFigure() == FigureConstants.FT_EMPTY ? 0 : -o1.getCapturedFigure();
            int c2 = o2.getCapturedFigure() == FigureConstants.FT_EMPTY ? 0 : -o2.getCapturedFigure();
            return c1 - c2;
        }
    };

    @Override
    public int size() {
        return moves.size();
    }

    public List<Move> getMoves() {
        return moves;
    }

    public void setMoves(List<Move> moves) {
        this.moves = moves;
    }

    @Override
    public Iterator<MoveCursor> iterator() {
        return new BasicMoveListIterator(this);
    }

}
