package org.mattlang.jc.uci;

import java.util.Objects;
import java.util.StringTokenizer;

/**
 * Simpler splitter of string in substrings to consume during uci parsing.
 */
public class UciStringParser {

    StringTokenizer tokenizer;
    private String currToken;

    public static final String END = "\n";

    public UciStringParser(String string) {
        tokenizer = new StringTokenizer(string);
        nextTok();
    }

    public String nextTok() {
        if (tokenizer.hasMoreTokens()) {
            currToken = tokenizer.nextToken();
        } else {
            currToken = END;
        }
        return getCurr();
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

}
