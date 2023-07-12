package org.mattlang.jc.engine.evaluation.parameval;

import java.util.function.Consumer;

import lombok.Getter;

/**
 * Like MgEgScore but supports change listener support.
 * This is used during tuning to make the handling of mg eg combined values easier.
 */
public class ChangeableMgEgScore {

    private final Consumer<Integer> changeListener;
    private int score;
    @Getter
    private final String propNameMg;

    @Getter
    private final String propNameEg;

    public ChangeableMgEgScore(Consumer<Integer> changeListener, String propNameMg, String propNameEg, int score) {
        this.score = score;
        this.changeListener = changeListener;
        this.propNameMg = propNameMg;
        this.propNameEg = propNameEg;
    }

    public int getMgScore() {
        return MgEgScore.getMgScore(score);
    }

    public int getEgScore() {
        return MgEgScore.getEgScore(score);
    }

    public int getCombinedScore() {
        return score;
    }

    /**
     * Set the mg value. Calls the change callback handler.
     *
     * @param mg
     */
    public void setMg(int mg) {
        score = MgEgScore.setMg(score, mg);
        changeListener.accept(score);
    }

    public void setEg(int eg) {
        score = MgEgScore.setEg(score, eg);
        changeListener.accept(score);
    }
}
