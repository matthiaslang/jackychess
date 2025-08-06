package org.mattlang.jc.engine.search;

import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.List;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.moves.MoveImpl;
import org.mattlang.jc.moves.MoveToStringConverter;
import org.mattlang.jc.util.IntList;

/**
 * Result Container for the pv list.
 */
public final class PVList {

    private IntList pvs;

    public PVList(IntList pvs) {
        this.pvs = pvs;
    }

    public String toPvStr(BoardRepresentation boardRepresentation) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < pvs.size(); i++) {
            int move =  pvs.get(i);
            if (builder.length() > 0) {
                builder.append(' ');
            }
            builder.append(MoveToStringConverter.toUCIString(move, boardRepresentation));
        }
        return builder.toString();
    }

    public String toPvLogStr() {
        return getPvMoves().stream()
                .map(m -> m.toStr())
                .collect(joining(" "));
    }

    public List<Move> getPvMoves() {
        List<Move> list = new ArrayList<>();
        for (int i = 0; i < pvs.size(); i++) {
            int move = pvs.get(i);
            list.add(new MoveImpl(move));
        }
        return list;
    }

}
