package org.mattlang.jc.board;

public class GameState {

    private BoardRepresentation board;
    private Color who2Move;

    private RepetitionChecker repetitionChecker;

    public GameState(BoardRepresentation board, Color who2Move, RepetitionChecker repetitionChecker) {
        this.board = board.copy();
        this.who2Move = who2Move;
        this.repetitionChecker = repetitionChecker;
    }


    public GameState(BoardRepresentation board, Color who2Move) {
        this.board = board.copy();
        this.who2Move = who2Move;
        this.repetitionChecker = new SimpleRepetitionChecker();
    }

    public BoardRepresentation getBoard() {
        return board;
    }

    public Color getWho2Move() {
        return who2Move;
    }

    public RepetitionChecker getRepetitionChecker() {
        return repetitionChecker;
    }
}
