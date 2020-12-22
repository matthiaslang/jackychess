package org.mattlang.jc.movegenerator;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;

import java.util.HashMap;
import java.util.Objects;

public class BoardCache<T> {

    public static final int CAPACITY = 50_000_000;

    private int cacheHit;
    private int cacheFail;

    private HashMap<BoardRepresentation, T> whitemap = new HashMap<>(CAPACITY);
    private HashMap<BoardRepresentation, T> blackmap = new HashMap<>(CAPACITY);

    private BoardCacheEntryCreator<T> creator;

    public BoardCache(BoardCacheEntryCreator<T> creator) {
        this.creator = Objects.requireNonNull(creator);
    }

    public T getCache(BoardRepresentation board, Color side) {
        T result = get(board, side);
        if (result != null) {
            cacheHit++;
            return result;
        }
        cacheFail++;
        T value = creator.createEntry(board, side);
        put(board, side, value);
        return value;
    }

    private T get(BoardRepresentation board, Color side) {
        switch (side) {
        case WHITE:
            return whitemap.get(board);
        case BLACK:
            return blackmap.get(board);
        }
        throw new IllegalStateException("something is completely wrong here?!");
    }

    private void put(BoardRepresentation board, Color side, T value) {
        BoardRepresentation key = board.copy();
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
