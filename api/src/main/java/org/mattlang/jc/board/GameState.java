package org.mattlang.jc.board;

import org.mattlang.jc.engine.MoveList;

import lombok.Getter;

@Getter
public class GameState {

    private String fenStr;

    private BoardRepresentation board;

    /** an optional list with legal moves  when coming from async engine. If searchmoves is set, it contains only the
     * legal search moves.
     * It may be null in test cases. */
    private MoveList legalMovesToSearch;

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

    public void setLegalMovesToSearch(MoveList legalMovesToSearch) {
        this.legalMovesToSearch = legalMovesToSearch;
    }

    public MoveList getLegalMovesToSearch() {
        return legalMovesToSearch;
    }
}
