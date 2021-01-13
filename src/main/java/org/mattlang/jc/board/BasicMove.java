package org.mattlang.jc.board;

import static org.mattlang.jc.board.IndexConversion.convert;
import static org.mattlang.jc.board.IndexConversion.parsePos;

/**
 * Represents a move on the board.
 * <p>
 * Examples:  e2e4, e7e5, e1g1 (white short castling), e7e8q (for promotion)
 */
public class BasicMove implements Move {

    private int fromIndex;

    private int toIndex;

    private int enPassantOption = -1;

    private byte capturedFigure;

    public BasicMove(String moveStr) {
        fromIndex = parsePos(moveStr.substring(0, 2));
        toIndex = parsePos((moveStr.substring(2, 4)));
    }

    public BasicMove(int from, int to, byte capturedFigure) {
        this.fromIndex = from;
        this.toIndex = to;
        this.capturedFigure = capturedFigure;
    }

    public BasicMove(int from, int to, byte capturedFigure, int enPassantOption) {
        this(from, to, capturedFigure);
        this.enPassantOption = enPassantOption;
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
    public String toString() {
        return toStr();
    }

    @Override
    public void move(BoardRepresentation board) {
        byte override = board.move(getFromIndex(), getToIndex());
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
}
