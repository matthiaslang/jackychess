package org.mattlang.jc.board;

/**
 * A Move which undos another move.
 */
public class UndoMove extends BasicMove {

    /**
     * the overriden field/figure by the move, which needs to be reset during the undoing.
     */
    public final byte overriddenFig;

    public UndoMove(int from, int to, byte overriddenFig) {
        super(from, to, (byte)0);
        this.overriddenFig = overriddenFig;
    }

    @Override
    public Move move(BoardRepresentation board) {
        super.move(board);
        board.setPos(getFromIndex(), overriddenFig);
        return null;
    }
}
