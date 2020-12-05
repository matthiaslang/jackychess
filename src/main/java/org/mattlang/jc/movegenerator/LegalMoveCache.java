package org.mattlang.jc.movegenerator;

import java.util.HashMap;

import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.MoveList;

public class LegalMoveCache {


    public static final int CAPACITY = 50_000_000;

    private HashMap<Board, MoveList> whitemap = new HashMap<>(CAPACITY);
    private HashMap<Board, MoveList> blackmap = new HashMap<>(CAPACITY);

    public LegalMoveCache() {
    }

    public MoveList get(Board board, Color side) {
        switch (side) {
        case WHITE:
            return whitemap.get(board);
        case BLACK:
            return blackmap.get(board);
        }
        throw new IllegalStateException("something is completely wrong here?!");
    }

    public void put(Board board, Color side, MoveList legalMoves) {
        Board key = board.copy();
        switch (side) {
        case WHITE:
            whitemap.put(key, legalMoves); break;
        case BLACK:
            blackmap.put(key, legalMoves); break;
        }
    }
}
