package org.mattlang.jc.engine.search;

import static org.mattlang.jc.engine.search.NegaMaxAlphaBetaPVS.ALPHA_START;
import static org.mattlang.jc.engine.search.NegaMaxAlphaBetaPVS.BETA_START;

import lombok.Getter;

/**
 * Aspiration Window boundaries.
 */
@Getter
public class Window {

    int alpha;
    int beta;

    /**
     * the last score where the window is build around.
     */
    int lastScore;

    /**
     * Widening phase: 0 means not widened, just limited.
     * Each increment means another widening of the window happened.
     */
    int wideningPhase = 0;

    public Window(int alpha, int beta) {
        this.alpha = alpha;
        this.beta = beta;
    }

    public void limitWindow(NegaMaxResult rslt) {
        lastScore = rslt.max;
        alpha = lastScore - 50;
        beta = lastScore + 50;

        wideningPhase = 0;
    }

    public boolean outsideWindow(NegaMaxResult rslt) {
        int score = rslt.max;
        return score <= alpha || score >= beta;
    }

    public void widenWindow(NegaMaxResult rslt) {
        int delta = wideningPhase++ * 50 + 50;

        int score = rslt.max;
        lastScore = score;
        if (score <= alpha) {
            alpha = alpha - delta - (alpha - score);
        } else if (score >= beta) {
            beta = beta + delta + (score - beta);
        }

        // after 3 phases, set the full window:

        // todo test: after first limit set it to full window... may be this is better...
        if (wideningPhase > 0) {
            alpha = ALPHA_START;
            beta = BETA_START;
        }
    }

    public String descr() {
        return "lastscore: " + lastScore + " window[" + alpha + ", " + beta + "]";
    }
}
