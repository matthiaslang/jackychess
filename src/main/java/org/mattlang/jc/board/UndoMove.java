package org.mattlang.jc.board;

/**
 * A Move which undos another move.
 */
public class UndoMove extends Move {

    public final byte originField;

    public UndoMove(int from, int to, byte originField) {
        super(from, to);
        this.originField = originField;
    }
}
