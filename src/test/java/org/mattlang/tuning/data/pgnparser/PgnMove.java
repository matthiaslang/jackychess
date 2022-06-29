package org.mattlang.tuning.data.pgnparser;

public class PgnMove {

    private MoveDescr white;
    private MoveDescr black;

    public PgnMove(MoveDescr white, MoveDescr black) {
        this.white = white;
        this.black = black;
    }
}
