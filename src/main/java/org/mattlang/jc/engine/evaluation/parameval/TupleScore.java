package org.mattlang.jc.engine.evaluation.parameval;

public class TupleScore {

    int score;

    public TupleScore(int mg, int eg) {
        score = (mg << 16) + eg;
    }

    public int getMgScore() {
        return (score + 0x8000) >> 16;
    }

    public int getEgScore() {
        return (short) (score & 0xffff);
    }
}
