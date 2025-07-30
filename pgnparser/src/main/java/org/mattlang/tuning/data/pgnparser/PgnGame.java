package org.mattlang.tuning.data.pgnparser;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import lombok.Getter;

public class PgnGame {

    public static final String TAG_RESULT = "Result";
    public static final String TAG_FEN = "FEN";
    public static final String TAG_SETUP = "SetUp";

    @Getter
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
        String result = getTag(TAG_RESULT);
        return Ending.match(result);
    }

    public String getTagStr() {
       StringBuilder b=new StringBuilder();
       for (String key : tags.keySet()) {
           b.append(key);
           b.append("=");
           b.append(tags.get(key));
           b.append("\n");
       }
       return b.toString();
    }
}
