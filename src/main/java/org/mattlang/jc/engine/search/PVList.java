package org.mattlang.jc.engine.search;

import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.List;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.moves.MoveImpl;

/**
 * Result Container for the pv list.
 */
public final class PVList {

    private List<Integer> pvs;

    public PVList(List<Integer> pvs) {
        this.pvs = pvs;
    }

    public String toPvStr(BoardRepresentation boardRepresentation) {
        return getPvMoves().stream()
                .map(m -> m.toUCIString(boardRepresentation))
                .collect(joining(" "));
    }

    public String toPvLogStr() {
        return getPvMoves().stream()
                .map(m -> m.toStr())
                .collect(joining(" "));
    }

    public List<Move> getPvMoves() {
        List<Move> list = new ArrayList<>();
        for (Integer move : pvs) {
            list.add(new MoveImpl(move));
        }
        return list;
    }

}
