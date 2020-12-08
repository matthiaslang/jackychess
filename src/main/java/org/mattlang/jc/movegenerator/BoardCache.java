package org.mattlang.jc.movegenerator;

import java.util.HashMap;
import java.util.Objects;

import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.Color;

public class BoardCache<T> {

    public static final int CAPACITY = 50_000_000;

    private HashMap<Board, T> whitemap = new HashMap<>(CAPACITY);
    private HashMap<Board, T> blackmap = new HashMap<>(CAPACITY);

    private BoardCacheEntryCreator<T> creator;

    public BoardCache(BoardCacheEntryCreator<T> creator) {
        this.creator = Objects.requireNonNull(creator);
    }

    public T getCache(Board board, Color side) {
        T result = get(board, side);
        if (result != null) {
            return result;
        }
        T value = creator.createEntry(board, side);
        put(board, side, value);
        return value;
    }

    private T get(Board board, Color side) {
        switch (side) {
        case WHITE:
            return whitemap.get(board);
        case BLACK:
            return blackmap.get(board);
        }
        throw new IllegalStateException("something is completely wrong here?!");
    }

    private void put(Board board, Color side, T value) {
        Board key = board.copy();
        switch (side) {
        case WHITE:
            whitemap.put(key, value);
            break;
        case BLACK:
            blackmap.put(key, value);
            break;
        }
    }
}
