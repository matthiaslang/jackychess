package org.mattlang.jc.movegenerator;

import static java.util.Objects.requireNonNull;

import java.util.Map;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;

public class ZobristBoardCache<T> implements BoardCache<T> {

    public static final int CAPACITY = 50_000;

    private int cacheHit;
    private int cacheFail;

    private LimitedHashMap<Long, T> whitemap = new LimitedHashMap<>(CAPACITY);
    private LimitedHashMap<Long, T> blackmap = new LimitedHashMap<>(CAPACITY);

    private BoardCacheEntryCreator<T> creator;

    public ZobristBoardCache() {
    }

    public ZobristBoardCache(BoardCacheEntryCreator<T> creator) {
        this.creator = requireNonNull(creator);
    }

    @Override
    public void setCreator(BoardCacheEntryCreator<T> creator) {
        this.creator = requireNonNull(creator);
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
            return whitemap.get(board.getZobristHash());
        case BLACK:
            return blackmap.get(board.getZobristHash());
        }
        throw new IllegalStateException("something is completely wrong here?!");
    }

    @Override
    public final void put(BoardRepresentation board, Color side, T value) {
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

    @Override
    public void resetStatistics() {
        cacheHit = 0;
        cacheFail = 0;
    }

    @Override
    public void collectStatistics(Map stats) {
        stats.put("size", whitemap.size() + blackmap.size());
        stats.put("cacheHit", cacheHit);
        stats.put("cacheFail", cacheFail);
        if (cacheHit + cacheFail != 0) {
            stats.put("hit/all", cacheHit * 100 / (cacheHit + cacheFail) + "%");
        }
    }
}
