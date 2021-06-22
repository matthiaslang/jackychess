package org.mattlang.jc.board;

import static org.mattlang.jc.board.IndexConversion.convert;
import static org.mattlang.jc.board.IndexConversion.parsePos;

import java.util.Objects;

/**
 * Represents a move on the board.
 * <p>
 * Examples:  e2e4, e7e5, e1g1 (white short castling), e7e8q (for promotion)
 */
public class BasicMove implements Move {

    public static final int NOT_SORTED = Integer.MAX_VALUE;
    private byte figureType;

    private byte fromIndex;

    private byte toIndex;

    private byte enPassantOption = -1;

    private byte capturedFigure;

    private int order = NOT_SORTED;

    public BasicMove(String moveStr) {
        fromIndex = parsePos(moveStr.substring(0, 2));
        toIndex = parsePos((moveStr.substring(2, 4)));
    }

    public BasicMove(byte figureType, int from, int to, byte capturedFigure) {
        this.figureType=figureType;
        this.fromIndex = (byte) from;
        this.toIndex = (byte) to;
        this.capturedFigure = capturedFigure;
    }

    public BasicMove(byte figureType, int from, int to, byte capturedFigure, int enPassantOption) {
        this(figureType, from, to, capturedFigure);
        this.enPassantOption = (byte) enPassantOption;
    }

    public int getFromIndex() {
        return fromIndex;
    }

    public int getToIndex() {
        return toIndex;
    }

    public String toStr() {
        return convert(fromIndex) + convert(toIndex);
    }

    @Override
    public boolean isEnPassant() {
        return false;
    }

    @Override
    public boolean isCastling() {
        return false;
    }

    @Override
    public boolean isPromotion() {
        return false;
    }

    @Override
    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public String toString() {
        return toStr();
    }

    @Override
    public void move(BoardRepresentation board) {
        board.move(getFromIndex(), getToIndex());
        if (enPassantOption>=0) {
           board.setEnPassantOption(enPassantOption);
        }
    }

    @Override
    public void undo(BoardRepresentation board) {
        board.move(getToIndex(), getFromIndex());
        if (capturedFigure != 0) {
            board.setPos(getToIndex(), capturedFigure);
        }
    }

    @Override
    public byte getCapturedFigure() {
        return capturedFigure;
    }

    @Override
    public boolean isCapture() {
        return getCapturedFigure() != 0;
    }

    @Override
    public byte getFigureType() {
        return figureType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        BasicMove basicMove = (BasicMove) o;
        return figureType == basicMove.figureType && fromIndex == basicMove.fromIndex && toIndex == basicMove.toIndex
                && enPassantOption == basicMove.enPassantOption && capturedFigure == basicMove.capturedFigure;
    }

    @Override
    public int hashCode() {
        return Objects.hash(figureType, fromIndex, toIndex, enPassantOption, capturedFigure);
    }

}
