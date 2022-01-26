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

    private static final int WINDOW_MARGIN = 50;
    private static final int MAX_WIDENING_PHASES = 0;

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

    /**
     * indicates that alpha side has widened. Without search anomalies only one direction should be widened.
     */
    private boolean alphaWidened;
    /**
     * indicates that beta side has widened. Without search anomalies only one direction should be widened.
     */
    private boolean betaWidened;

    public Window(int alpha, int beta) {
        this.alpha = alpha;
        this.beta = beta;
    }

    public void limitWindow(NegaMaxResult rslt) {
        lastScore = rslt.max;
        alpha = lastScore - WINDOW_MARGIN;
        beta = lastScore + WINDOW_MARGIN;

        wideningPhase = 0;
    }

    public boolean outsideWindow(NegaMaxResult rslt) {
        int score = rslt.max;
        // this is currently a pathological effect, that we sometimes do not find a result
        // in this case we at least stop aspiration search loop and log in the iterative deepening the
        // problematic case
        if (score <= ALPHA_START || score >= BETA_START) {
            LOGGER.fine("Aspiration Window reached outer bounds! score = " + score);
            return false;
        }

        return score <= alpha || score >= beta;
    }

    public void widenWindow(NegaMaxResult rslt) {
        int delta = wideningPhase++ * WINDOW_MARGIN + WINDOW_MARGIN;

        int score = rslt.max;
        lastScore = score;
        if (score <= alpha) {
            alpha = alpha - delta - (alpha - score);
            alphaWidened=true;
            if (betaWidened) {
                LOGGER.warning("Search anomalie: Window widened in two directions!" + descr());
            }
        } else if (score >= beta) {
            beta = beta + delta + (score - beta);
            betaWidened = true;
            if (alphaWidened) {
                /**
                 * a search anomaly: the result of alpha/beta switches between alpha and beta. Normally that should not be the case but can
                 * happen...
                 */
                LOGGER.warning("Search anomalie: Window widened in two directions!" + descr());
            }
        }

        // after n phases, set the full window:
        if (wideningPhase > MAX_WIDENING_PHASES) {
            alpha = ALPHA_START;
            beta = BETA_START;
        }
    }

    public String descr() {
        return "lastscore: " + lastScore + " window[" + alpha + ", " + beta + "]";
    }
}
