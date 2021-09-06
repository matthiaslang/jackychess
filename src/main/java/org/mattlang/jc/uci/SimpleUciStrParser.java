package org.mattlang.jc.uci;

/**
 * Helper class to parse uci strings which consist of tokens and values separated by a white space
 */
public class SimpleUciStrParser {

    private String cmdStr;
    private String[] result;
    int i = 0;

    public SimpleUciStrParser(String cmdStr) {
        this.cmdStr = cmdStr;
        result = cmdStr.split("\\s");
    }

    public boolean hasMore() {
        return i < result.length;
    }

    public void expect(String tok) {
        if (!match(tok)){
            throw new RuntimeException("Error parsing uci cmd, expecting " + tok);
        }
    }

    public boolean match(String tok) {
        if (!hasMore()) {
            return false;
        }
        String t = result[i];
         if (tok.equals(t)) {
             i++;
             return true;
         }
         return false;
    }

    /**
     * Expects to match a long at the current position. Fails if there is no long at that position.
     * @return
     */
    public long expectLong() {
        long val=Long.parseLong(result[i]);
        i++;
        return val;
    }

    public int expectInt() {
        int val=Integer.parseInt(result[i]);
        i++;
        return val;
    }


    public String expectStr() {
        if (i< result.length) {
            String rslt = result[i];
            i++;
            return rslt;
        } else {
            throw new RuntimeException("Error String end of " + cmdStr);
        }
    }

    public String getRest() {
        StringBuilder b = new StringBuilder();
        while (hasMore()){
            b.append(expectStr());
            b.append(" ");
        }
        return b.toString();
    }

}
