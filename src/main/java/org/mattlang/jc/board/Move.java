package org.mattlang.jc.board;

/**
 * Represents a move on the board.
 *
 * Examples:  e2e4, e7e5, e1g1 (white short castling), e7e8q (for promotion)
 */
public class Move {

    private int fromIndex;

    private int toIndex;

    public Move(String moveStr) {
        int fromXIndex = moveStr.charAt(0) - 'a';
        int fromYIndex = moveStr.charAt(1) - '0' - 1;

        int toXIndex = moveStr.charAt(2) - 'a';
        int toYIndex = moveStr.charAt(3) - '0' - 1;

        fromIndex = fromYIndex * 8 + fromXIndex;
        toIndex = toYIndex * 8 + toXIndex;
    }

    public int getFromIndex() {
        return fromIndex;
    }

    public int getToIndex() {
        return toIndex;
    }
}
