package org.mattlang.jc.engine.tt;

import java.util.ArrayList;
import java.util.Optional;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.search.PVList;
import org.mattlang.jc.movegenerator.MoveGeneratorImpl3;

/**
 * Experimental pv extraction from tt cache. Does not work currently...
 */
public class PVExtraction {

    private Optional<Move> findPvMove(BoardRepresentation rootBoard, Color color, TTCache cache) {
        MoveGeneratorImpl3 moveGen = new MoveGeneratorImpl3();
        MoveList moves = moveGen.generate(rootBoard, color);
        for (MoveCursor moveCursor : moves) {
            moveCursor.move(rootBoard);
            TTEntry entry = cache.getTTEntry(rootBoard, color);
            moveCursor.undoMove(rootBoard);
            if (entry != null && entry.type == TTEntry.TTType.EXACT_VALUE) {
                return Optional.of(moveCursor.getMove());
            }
        }
        return Optional.empty();
    }

    public Optional<PVList> extractFromTT(BoardRepresentation rootBoard, int pathDepth, Color color, TTCache cache) {
        ArrayList<Move> pvs = new ArrayList<>();

        for (int d = 0; d < pathDepth; d++) {
            Optional<Move> optMove = findPvMove(rootBoard, color, cache);
            if (optMove.isPresent()) {
                pvs.add(optMove.get());
                optMove.get().move(rootBoard);
                color = color.invert();
            } else {
                return Optional.empty();
            }
        }
        return Optional.of(new PVList(pvs));
    }

}
