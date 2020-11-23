package org.mattlang.jc.board;

/**
 * Represents a move on the board.
 *
 * Examples:  e2e4, e7e5, e1g1 (white short castling), e7e8q (for promotion)
 */
public class Move {

    public Move(String moveStr) {
       int fromXIndex = 'a' - moveStr.charAt(0);
       int fromYIndex = '0' - moveStr.charAt(1) - 1;

        int toXIndex = 'a' - moveStr.charAt(2);
        int toYIndex = '0' - moveStr.charAt(3) - 1;

    }
}
