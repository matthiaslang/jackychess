package org.mattlang.jc.uci;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;

/**
 * Simpler splitter of string in substrings to consume during uci parsing.
 */
public class UciStringParser {

    private final StringTokenizer tokenizer;
    private String currToken;

    public static final String END = "\n";

    public UciStringParser(String string) {
        tokenizer = new StringTokenizer(string);
        nextTok();
    }

    private void nextTok() {
        if (tokenizer.hasMoreTokens()) {
            currToken = tokenizer.nextToken();
        } else {
            currToken = END;
        }
    }

    public String getCurr() {
        return currToken;
    }

    public boolean hasNext() {
        return getCurr() != END || tokenizer.hasMoreTokens();
    }

    public boolean match(String tok) {
        if (Objects.equals(tok, getCurr())) {
            nextTok();
            return true;
        }
        return false;
    }

    public String match() {
        String curr = getCurr();
        nextTok();
        return curr;
    }

    public long matchLong() {
        return Long.parseLong(match());
    }

    public int matchInt() {
        return Integer.parseInt(match());
    }

    public String[] collectRestTokens() {
        List<String> tokens = new ArrayList<>();
        while (hasNext()) {
            tokens.add(match());
        }
        return tokens.toArray(new String[tokens.size()]);
    }
}
