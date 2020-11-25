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

    public Move(int from, int to) {
        this.fromIndex = from;
        this.toIndex = to;
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

    private String convert(int index) {
        int x = index % 8;
        int y = index / 8;
        StringBuilder b = new StringBuilder();
        b.append((char) ('a' + x));
        b.append((char) ('0' + y + 1));
        return b.toString();
    }

    @Override
    public String toString() {
        return toStr();
    }
}
