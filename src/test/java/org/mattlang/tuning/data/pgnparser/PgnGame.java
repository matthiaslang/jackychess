package org.mattlang.tuning.data.pgnparser;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class PgnGame {

    private LinkedHashMap<String, String> tags = new LinkedHashMap<>();

    private List<PgnMove> moves = new ArrayList<>();

    public void addTag(String name, String value) {
        tags.put(name, value);
    }

    public void addMove(PgnMove move) {
        moves.add(move);
    }
}
