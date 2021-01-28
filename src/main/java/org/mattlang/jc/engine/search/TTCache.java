package org.mattlang.jc.engine.search;

import static org.mattlang.jc.engine.search.TTEntry.TTType.*;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.movegenerator.ZobristBoardCache;

public class TTCache {
    private ZobristBoardCache<TTEntry> ttCache = new ZobristBoardCache<>((board, side) -> null);

    public final TTEntry getTTEntry(BoardRepresentation currBoard, Color side) {
        return ttCache.get(currBoard, side);
    }


    public final void storeTTEntry(BoardRepresentation board, Color side, int eval, TTEntry.TTType tpe, int depth) {
        // only store entries with lower depth:
        TTEntry existing = ttCache.get(board, side);
        if (existing == null || existing.depth > depth) {
            ttCache.put(board, side, new TTEntry(eval, tpe, depth));
        }
    }


    public final void storeTTEntry(BoardRepresentation currBoard, Color color, int max, int alpha, int beta, int depth) {
        if(max <= alpha) // a lowerbound value
            storeTTEntry(currBoard, color, max, LOWERBOUND, depth);
        else if(max >= beta) // an upperbound value
            storeTTEntry(currBoard, color, max, UPPERBOUND, depth);
        else // a true minimax value
            storeTTEntry(currBoard, color, max, EXACT_VALUE, depth);

    }
}
