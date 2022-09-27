package org.mattlang.jc.moves;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.CheckChecker;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.sorting.OrderCalculator;
import org.mattlang.jc.movegenerator.MoveGenerator;

import lombok.Getter;

@Getter
public class StagedMoveListImpl implements MoveList {

    private MoveBoardIterator moveBoardIterator = new MoveBoardIterator();

    private StagedMoveCursor moveCursor = new StagedMoveCursor(this);

    private OrderCalculator orderCalculator;
    private BoardRepresentation board;
    private Color side;
    private MoveGenerator.GenMode mode;

    public StagedMoveListImpl() {
    }

    public void genMove(byte figureType, int from, int to, byte capturedFigure) {
        throw new IllegalStateException("illegal state!");
    }

    public void genPawnMove(int from, int to, Color side, byte capturedFigure) {
        throw new IllegalStateException("illegal state!");
    }

    @Override
    public void genEnPassant(int from, int to, Color side, int enPassantCapturePos) {
        throw new IllegalStateException("illegal state!");
    }

    @Override
    public void addRochadeLongWhite() {
        throw new IllegalStateException("illegal state!");
    }

    @Override
    public void addRochadeShortWhite() {
        throw new IllegalStateException("illegal state!");
    }

    @Override
    public void addRochadeShortBlack() {
        throw new IllegalStateException("illegal state!");
    }

    @Override
    public void addRochadeLongBlack() {
        throw new IllegalStateException("illegal state!");
    }

    public void sort(OrderCalculator orderCalculator) {
        if (orderCalculator == null) {
            this.orderCalculator = new OrderCalculator(orderCalculator);
        } else {
            this.orderCalculator.init(orderCalculator);
        }
    }

    @Override
    public int size() {
        throw new IllegalStateException("illegal state!");
    }

    public void reset() {
        orderCalculator = null;
        board = null;
        side = null;
    }

    @Override
    public MoveCursor iterate() {
        moveCursor.init(mode, orderCalculator);
        return moveCursor;
    }

    @Override
    public MoveBoardIterator iterateMoves(BoardRepresentation board, CheckChecker checkChecker) {
        MoveCursor moveCursor = iterate();
        moveBoardIterator.init(moveCursor, board, checkChecker);
        return moveBoardIterator;
    }

    public void init(MoveGenerator.GenMode mode, OrderCalculator orderCalculator,
            BoardRepresentation board, Color side) {
        this.mode = mode;
        this.orderCalculator = orderCalculator;
        this.board = board;
        this.side = side;
        moveCursor.init(mode, orderCalculator);
    }

    @Override
    public void close() {
    }
}
