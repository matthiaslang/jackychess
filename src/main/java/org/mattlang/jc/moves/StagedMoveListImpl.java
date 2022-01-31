package org.mattlang.jc.moves;

import java.util.Iterator;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.sorting.OrderCalculator;
import org.mattlang.jc.uci.GameContext;

import lombok.Getter;

@Getter
public class StagedMoveListImpl implements MoveList {

    private IntList moves = new IntList();
    private int[] order = new int[200];

    private boolean checkMate = false;

    private GameContext gameContext;
    private OrderCalculator orderCalculator;
    private BoardRepresentation board;
    private Color side;

    public StagedMoveListImpl() {
    }

    public void genMove(byte figureType, int from, int to, byte capturedFigure) {
        throw new IllegalStateException("illegal state!");
        //        moves.add(createNormalMove(figureType, from, to, capturedFigure));
    }

    public void genPawnMove(int from, int to, Color side, byte capturedFigure) {
        throw new IllegalStateException("illegal state!");
        //        boolean isOnLastLine = false;
        //        if (side == WHITE) {
        //            isOnLastLine = to >= 56 && to <= 63;
        //        } else {
        //            isOnLastLine = to >= 0 && to <= 7;
        //        }
        //        if (isOnLastLine) {
        //            moves.add(createPromotionMove(from, to, capturedFigure, side == WHITE ? W_Queen : B_Queen));
        //            moves.add(createPromotionMove(from, to, capturedFigure, side == WHITE ? W_Rook : B_Rook));
        //            moves.add(createPromotionMove(from, to, capturedFigure, side == WHITE ? W_Bishop : B_Bishop));
        //            moves.add(createPromotionMove(from, to, capturedFigure, side == WHITE ? W_Knight : B_Knight));
        //        } else {
        //            moves.add(createNormalMove(FigureConstants.FT_PAWN, from, to, capturedFigure));
        //
        //        }
    }

    @Override
    public void genEnPassant(int from, int to, Color side, int enPassantCapturePos) {
        throw new IllegalStateException("illegal state!");
        //        moves.add(createEnPassantMove(from, to, side == Color.WHITE ? B_PAWN : W_PAWN, enPassantCapturePos));
    }

    @Override
    public void hypotheticalPawnCapture(int from, int to) {
        // not needed here, since this is only a hypothetical theoretical capture.
        throw new IllegalStateException("illegal state!");
    }

    @Override
    public void addRochadeLongWhite() {
        throw new IllegalStateException("illegal state!");
        //        moves.add(createCastlingMove(CastlingMove.CASTLING_WHITE_LONG));
    }

    @Override
    public void addRochadeShortWhite() {
        throw new IllegalStateException("illegal state!");
        //        moves.add(createCastlingMove(CastlingMove.CASTLING_WHITE_SHORT));
    }

    @Override
    public void addRochadeShortBlack() {
        throw new IllegalStateException("illegal state!");
        //        moves.add(createCastlingMove(CastlingMove.CASTLING_BLACK_SHORT));
    }

    @Override
    public void addRochadeLongBlack() {
        throw new IllegalStateException("illegal state!");
        //        moves.add(createCastlingMove(CastlingMove.CASTLING_BLACK_LONG));
    }

    public void sort(OrderCalculator orderCalculator) {
        this.orderCalculator = orderCalculator;
    }

    @Override
    public int size() {
        throw new IllegalStateException("illegal state!");
        //        return moves.size();
    }

    @Override
    public Iterator<MoveCursor> iterator() {
        return new StagedMoveListIteratorImpl(this);
    }

    public void reset() {
        moves.reset();

        checkMate = false;
        gameContext = null;
        orderCalculator = null;
        board = null;
        side = null;
    }

    public void init(GameContext gameContext, OrderCalculator orderCalculator, BoardRepresentation board, Color side) {
        this.gameContext = gameContext;
        this.orderCalculator = orderCalculator;
        this.board = board;
        this.side = side;
    }

    @Override
    public void close() {
        //MoveListPool.instance.dispose(this);
    }
}
