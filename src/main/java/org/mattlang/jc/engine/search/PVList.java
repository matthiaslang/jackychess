package org.mattlang.jc.engine.search;

import static java.util.stream.Collectors.joining;

import java.util.ArrayList;

import org.mattlang.jc.board.Move;

public class PVList {

    private ArrayList<Move> list = new ArrayList<>();

    public final void clear() {
        list.clear();
    }

    public final void add(PVList otherList) {
        list.addAll(otherList.list);
    }

    public void set(Move move) {
        list.clear();
        list.add(move);
    }

    public Move get(int index) {
        if (index < list.size()) {
            return list.get(index);
        }
        return null;
    }

    public String toPvStr() {
        return list.stream()
                .map(m -> m.toStr())
                .collect(joining(" "));
    }
}
