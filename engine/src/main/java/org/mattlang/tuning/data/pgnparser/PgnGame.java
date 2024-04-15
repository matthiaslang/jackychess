package org.mattlang.tuning.data.pgnparser;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import lombok.Getter;

public class PgnGame {

    private LinkedHashMap<String, String> tags = new LinkedHashMap<>();

    @Getter
    private List<PgnMove> moves = new ArrayList<>();

    public void addTag(String name, String value) {
        tags.put(name, value);
    }

    public void addMove(PgnMove move) {
        moves.add(move);
    }

    public String getTag(String key) {
        return tags.get(key);
    }

    public Ending getResult() {
        String result = getTag("Result");
        return Ending.match(result);
    }
}
