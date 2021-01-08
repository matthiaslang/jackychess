package org.mattlang.jc.movegenerator;

import java.util.HashMap;
import java.util.Objects;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;

public class BoardCacheImpl<T> implements BoardCache<T> {

    public static final int CAPACITY = 50_000;

    private int cacheHit;
    private int cacheFail;

    private HashMap<BoardRepresentation, T> whitemap = new HashMap<>(CAPACITY);
    private HashMap<BoardRepresentation, T> blackmap = new HashMap<>(CAPACITY);

    private BoardCacheEntryCreator<T> creator;

    public BoardCacheImpl(BoardCacheEntryCreator<T> creator) {
        this.creator = Objects.requireNonNull(creator);
    }

    @Override
    public void setCreator(BoardCacheEntryCreator<T> creator) {
        this.creator = Objects.requireNonNull(creator);
    }

    @Override
    public final T getCache(BoardRepresentation board, Color side) {
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

    @Override
    public final T get(BoardRepresentation board, Color side) {
        switch (side) {
        case WHITE:
            return whitemap.get(board);
        case BLACK:
            return blackmap.get(board);
        }
        throw new IllegalStateException("something is completely wrong here?!");
    }

    @Override
    public final void put(BoardRepresentation board, Color side, T value) {
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
