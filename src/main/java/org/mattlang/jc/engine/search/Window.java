package org.mattlang.jc.engine.search;

import static org.mattlang.jc.engine.search.NegaMaxAlphaBetaPVS.ALPHA_START;
import static org.mattlang.jc.engine.search.NegaMaxAlphaBetaPVS.BETA_START;

import java.util.logging.Logger;

import lombok.Getter;

/**
 * Aspiration Window boundaries.
 */
@Getter
public class Window {

    private static final Logger LOGGER = Logger.getLogger(Window.class.getSimpleName());

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
        // this is currently a pathological effect, that we sometimes do not find a result
        // in this case we at least stop aspiration search loop and log in the iterative deepening the
        // problematic case
        if (score <= ALPHA_START || score >= BETA_START) {
            LOGGER.info("Aspiration Window reached outer bounds! score = " + score);
            return false;
        }

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
