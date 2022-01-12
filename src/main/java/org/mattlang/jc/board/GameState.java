package org.mattlang.jc.board;

import lombok.Getter;

@Getter
public class GameState {

    private String fenStr;

    private BoardRepresentation board;

    private RepetitionChecker repetitionChecker;

    public GameState(BoardRepresentation board, RepetitionChecker repetitionChecker, String fenStr) {
        this.board = board.copy();
        this.repetitionChecker = repetitionChecker;
        this.fenStr = fenStr;
    }

    public GameState(BoardRepresentation board) {
        this.board = board.copy();
        this.repetitionChecker = new SimpleRepetitionChecker();
    }

    public Color getWho2Move() {
        return board.getSiteToMove();
    }
}
