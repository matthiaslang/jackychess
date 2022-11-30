package org.mattlang.jc.engine.evaluation.parameval;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvalScore {

    int midScore;

    int endScore;

    public static final EvalScore SCORE(int midScore, int endScore) {
        return new EvalScore(midScore, endScore);
    }

    public void reset() {
        midScore = 0;
        endScore = 0;
    }

    public EvalScore plus(EvalScore score) {
        this.midScore += score.getMidScore();
        this.endScore += score.getEndScore();
        return this;
    }

    public EvalScore plusMg(int mg) {
        this.midScore += mg;
        return this;
    }

    public EvalScore plusEg(int eg) {
        this.endScore += eg;
        return this;
    }

    public EvalScore plusMult(EvalScore score, int count) {
        this.midScore += score.getMidScore() * count;
        this.endScore += score.getEndScore() * count;
        return this;
    }
}
