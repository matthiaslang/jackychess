package org.mattlang.jc.board;

import org.mattlang.jc.engine.Configurator;

import lombok.Getter;

/**
 * Holds the current board status during uci processing.
 */
@Getter
public class GameState {

    private String fenStr;

    private String goCmd;

    private BoardRepresentation board;

    public GameState(BoardRepresentation board, String fenStr) {
        this.board = board.copy();
        this.fenStr = fenStr;
    }

    public GameState(BoardRepresentation board, String fenStr, String goCmd) {
        this.fenStr = fenStr;
        this.goCmd = goCmd;
        this.board = board;
    }

    public GameState(BoardRepresentation board) {
        this.board = board.copy();
    }

    public Color getWho2Move() {
        return board.getSiteToMove();
    }

    public GameState copy() {
        return new GameState(board.copy(), fenStr, goCmd);
    }

    public void appendGoCmd(String cmdStr) {
        this.goCmd = cmdStr;
    }

    public static GameState posFrom(String fenStr) {
        BoardRepresentation aboard = Configurator.createBoard();
        aboard.setFenPosition(fenStr);
        return new GameState(aboard, fenStr);
    }

    public static GameState startPos() {
        BoardRepresentation aboard = Configurator.createBoard();
        aboard.setStartPosition();
        return new GameState(aboard, null);
    }
}
