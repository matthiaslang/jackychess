package org.mattlang.jc.board;

import lombok.Getter;

@Getter
public class GameState {

    private String fenStr;

    private BoardRepresentation board;

    public GameState(BoardRepresentation board, String fenStr) {
        this.board = board.copy();
        this.fenStr = fenStr;
    }

    public GameState(BoardRepresentation board) {
        this.board = board.copy();
    }

    public Color getWho2Move() {
        return board.getSiteToMove();
    }

    public GameState copy() {
        return new GameState(board.copy(), fenStr);
    }
}
