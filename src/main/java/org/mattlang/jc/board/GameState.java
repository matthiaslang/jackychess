package org.mattlang.jc.board;

public class GameState {

    private BoardRepresentation board;

    private RepetitionChecker repetitionChecker;

    public GameState(BoardRepresentation board,RepetitionChecker repetitionChecker) {
        this.board = board.copy();
        this.repetitionChecker = repetitionChecker;
    }

    public GameState(BoardRepresentation board) {
        this.board = board.copy();
        this.repetitionChecker = new SimpleRepetitionChecker();
    }

    public BoardRepresentation getBoard() {
        return board;
    }

    public Color getWho2Move() {
        return board.getSiteToMove();
    }

    public RepetitionChecker getRepetitionChecker() {
        return repetitionChecker;
    }
}
