package org.mattlang.jc;

import lombok.Getter;

@Getter
public class TestPosition {

    public final String fen;

    public final String fenPosition;

    public final String name;

    public final String expectedBestMove;

    public TestPosition(String fenPosition) {
        fen = null;
        this.fenPosition = fenPosition;
        this.name = null;
        this.expectedBestMove = null;
    }

    public TestPosition(String fen, String expectedBestMove, String name) {
        this.fen = fen;
        this.fenPosition = "position fen " + fen + " 0 0";
        this.expectedBestMove = expectedBestMove;
        this.name = name;
    }

}
