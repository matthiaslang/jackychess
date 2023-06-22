package org.mattlang.jc.engine.evaluation.parameval;

/**
 * Score class which combines MG and EG score encoded in a single int.
 * (Therefore each value must be smaller than a short)
 * Using this class lets us calc some separated eg/mg evaluations within one step instead of two.
 */
public final class MgEgScore {

    private int score;

    public MgEgScore() {
        score = 0;
    }

    public MgEgScore(int mg, int eg) {
        score = (mg << 16) + eg;
    }

    public static int createMgEgScore(int mg, int eg) {
        return (mg << 16) + eg;
    }

    public int getMgScore() {
        return (score + 0x8000) >> 16;
    }

    public int getEgScore() {
        return (short) (score & 0xffff);
    }

    public int getCombinedScore() {
        return score;
    }

    public void clear() {
        score = 0;
    }

    public void set(int mg, int eg) {
        score = (mg << 16) + eg;
    }

    public void addMult(MgEgScore toAdd, int factor) {
        score += toAdd.score * factor;
    }

    public void addMg(int mg) {
        score += mg << 16;
    }

    public void addEg(int eg) {
        score += eg;
    }

    public void add(int val) {
        score += val;
    }
}
