package org.mattlang.jc.board;

/**
 * Represents a move on the board.
 *
 * Examples:  e2e4, e7e5, e1g1 (white short castling), e7e8q (for promotion)
 */
public class BasicMove implements Move {

    private int fromIndex;

    private int toIndex;


    private Figure capturedFigure;

    public BasicMove(String moveStr) {
        fromIndex = parsePos(moveStr.substring(0, 2));
        toIndex = parsePos((moveStr.substring(2, 4)));
    }


    public BasicMove(int from, int to, Figure capturedFigure) {
        this.fromIndex = from;
        this.toIndex = to;
        this.capturedFigure = capturedFigure;
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

    public static int parsePos(String pos) {
        int x = pos.charAt(0) - 'a';
        int y = pos.charAt(1) - '0' - 1;
        return y * 8 + x;
    }

    @Override
    public Move move(Board board) {
        byte override = board.move(getFromIndex(), getToIndex());
        return new UndoMove(getToIndex(), getFromIndex(), override);
    }

    @Override
    public Figure getCapturedFigure() {
        return capturedFigure;
    }
}
