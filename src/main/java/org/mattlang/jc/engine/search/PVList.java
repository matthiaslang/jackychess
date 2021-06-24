package org.mattlang.jc.engine.search;

import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.List;

import org.mattlang.jc.board.Move;

public final class PVList {

    private ArrayList<Move> list = null;

    public PVList() {

    }

    public PVList(ArrayList<Move> pvs) {
        getLazyList().addAll(pvs);
    }

    public final void clear() {
        list = null;
    }

    public final void add(PVList otherList) {
        if (otherList.list != null) {
            getLazyList().addAll(otherList.getLazyList());
        }
    }

    public void set(Move move) {
        list = null;
        getLazyList().add(move);
    }

    public Move get(int index) {
        if (index < getLazyList().size()) {
            return getLazyList().get(index);
        }
        return null;
    }

    public String toPvStr() {
        return getLazyList().stream()
                .map(m -> m.toStr())
                .collect(joining(" "));
    }

    public List<Move> getPvMoves() {
        return new ArrayList<>(getLazyList());
    }

    private List<Move> getLazyList() {
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }
}
