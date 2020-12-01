package org.mattlang.jc.engine;

import static org.mattlang.jc.board.Color.WHITE;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.mattlang.jc.board.*;

public class BasicMoveList implements MoveList {

    public static final RochadeMove ROCHADE_MOVE_LW = new RochadeMove(4, 2, 0, 3);
    public static final RochadeMove ROCHADE_MOVE_SW = new RochadeMove(4, 6, 7, 5);
    public static final RochadeMove ROCHADE_MOVE_SB = new RochadeMove(60, 62, 63, 61);
    public static final RochadeMove ROCHADE_MOVE_LB = new RochadeMove(60, 58, 56, 59);

    private List<Move> moves = new ArrayList<>(60);

    public void genMove(int from, int to, Figure capturedFigure) {
        moves.add(new BasicMove(from, to, capturedFigure));
    }

    public void genPawnMove(int from, int to, Figure pawn, Figure capturedFigure) {
        boolean isOnLastLine = false;
        if (pawn.color == WHITE) {
            isOnLastLine = to >= 56 && to <= 63;
        } else {
            isOnLastLine = to >= 0 && to <= 7;
        }
        moves.add(isOnLastLine ?
                new PawnPromotionMove(from, to, capturedFigure) :
                new BasicMove(from, to, capturedFigure));
    }

    @Override
    public void addRochadeLongWhite() {
        moves.add(ROCHADE_MOVE_LW);
    }

    @Override
    public void addRochadeShortWhite() {
        moves.add(ROCHADE_MOVE_SW);
    }

    @Override
    public void addRochadeShortBlack() {
        moves.add(ROCHADE_MOVE_SB);
    }

    @Override
    public void addRochadeLongBlack() {
        moves.add(ROCHADE_MOVE_LB);
    }

    @Override
    public void addMove(MoveCursor moveCursor) {
        moves.add(moveCursor.getMove());
    }

    @Override
    public boolean capturesFigure(Figure figure) {
        for (Move move : moves) {
            if (figure == move.getCapturedFigure()) {
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
            int c1 = o1.getCapturedFigure() == null ? 0 : -o1.getCapturedFigure().figureCode;
            int c2 = o2.getCapturedFigure() == null ? 0 : -o2.getCapturedFigure().figureCode;
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
