package org.mattlang.jc.movegenerator;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;

import java.util.HashMap;

import static java.util.Objects.requireNonNull;

public class ZobristBoardCache<T> {

    public static final int CAPACITY = 5_0_000;

    private int cacheHit;
    private int cacheFail;

    private HashMap<Long, T> whitemap = new HashMap<>(CAPACITY);
    private HashMap<Long, T> blackmap = new HashMap<>(CAPACITY);

    private BoardCacheEntryCreator<T> creator;

    public ZobristBoardCache(BoardCacheEntryCreator<T> creator) {
        this.creator = requireNonNull(creator);
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

    public T get(BoardRepresentation board, Color side) {
        switch (side) {
        case WHITE:
            return whitemap.get(board.getZobristHash());
        case BLACK:
            return blackmap.get(board.getZobristHash());
        }
        throw new IllegalStateException("something is completely wrong here?!");
    }

    public void put(BoardRepresentation board, Color side, T value) {
        long key = board.getZobristHash();
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
