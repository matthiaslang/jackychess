package org.mattlang.jc.moves;

import java.util.function.Predicate;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Figure;
import org.mattlang.jc.engine.MoveCursor;

public class FilteredMoveCursor implements MoveCursor {

    private MoveCursor delegate;

    private boolean cachedNext = false;

    private Predicate<MoveCursor> filterFunction;

    public void init(MoveCursor delegate, Predicate<MoveCursor> filterFunction) {
        cachedNext = false;
        this.delegate = delegate;
        this.filterFunction = filterFunction;
    }

    @Override
    public void move(BoardRepresentation board) {
        delegate.move(board);
    }

    @Override
    public int getMoveInt() {
        return delegate.getMoveInt();
    }

    @Override
    public int getEnPassantCapturePos() {
        return delegate.getEnPassantCapturePos();
    }

    @Override
    public byte getPromotedFigureByte() {
        return delegate.getPromotedFigureByte();
    }

    @Override
    public CastlingMove getCastlingMove() {
        return delegate.getCastlingMove();
    }

    @Override
    public int getOrder() {
        return delegate.getOrder();
    }

    @Override
    public void undoMove(BoardRepresentation board) {
        delegate.undoMove(board);
    }

    @Override
    public boolean isCapture() {
        return delegate.isCapture();
    }

    @Override
    public boolean isPromotion() {
        return delegate.isPromotion();
    }

    @Override
    public boolean isEnPassant() {
        return delegate.isEnPassant();
    }

    @Override
    public boolean isCastling() {
        return delegate.isCastling();
    }

    @Override
    public byte getCapturedFigure() {
        return delegate.getCapturedFigure();
    }

    @Override
    public Figure getPromotedFigure() {
        return delegate.getPromotedFigure();
    }

    @Override
    public byte getFigureType() {
        return delegate.getFigureType();
    }

    @Override
    public int getFromIndex() {
        return delegate.getFromIndex();
    }

    @Override
    public int getToIndex() {
        return delegate.getToIndex();
    }

    @Override
    public String toStr() {
        return delegate.toStr();
    }

    @Override
    public void next() {
        if (cachedNext) {
            // dont do anything, the delegating cursor is already on the "next":
            cachedNext = false;
        } else {
            delegate.next();
        }
    }

    @Override
    public boolean hasNext() {
        if (cachedNext) {
            return true;
        }
        while (!cachedNext && delegate.hasNext()) {
            delegate.next();
            boolean isOk = filterFunction.test(delegate);
            if (isOk) {
                cachedNext = true;
            }
        }
        return cachedNext;
    }

}
