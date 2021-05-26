package org.mattlang.jc.engine.search;

import static org.mattlang.jc.engine.search.NegaMaxAlphaBetaPVS.*;

import java.util.logging.Logger;

import org.mattlang.jc.Factory;
import org.mattlang.jc.StopWatch;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.SearchMethod;
import org.mattlang.jc.engine.sorting.OrderHints;
import org.mattlang.jc.uci.GameContext;
import org.mattlang.jc.uci.UCI;

/**
 * mtf implementation.
 * currently very, very slow, because alpha beta results are not save in transpostion table...
 * works also not well when sorting moves (why??)
 */
public class IterativeDeepeningMtdf implements SearchMethod {

    Logger logger = Logger.getLogger("MTDF");

    private NegaMaxAlphaBetaPVS negaMaxAlphaBeta = new NegaMaxAlphaBetaPVS();

    private int maxDepth ;

    private long timeout = Factory.getDefaults().getConfig().timeout.getValue();

    @Override
    public Move search(GameState gameState, GameContext context, int depth) {
        negaMaxAlphaBeta.reset();
        negaMaxAlphaBeta.setDoCaching(true);
        this.maxDepth = depth;
        int currdepth = 1;
        Move savedMove = null;

        long stopTime = System.currentTimeMillis() + timeout;
        StopWatch watch = new StopWatch();
        watch.start();

        OrderHints orderHints = OrderHints.NO_HINTS;
        try {
            int firstguess = 0;
            for (currdepth = 1; currdepth <= maxDepth; currdepth++) {

                NegaMaxResult rslt =
                        mtdf(gameState, currdepth, stopTime, firstguess, orderHints);

                savedMove = negaMaxAlphaBeta.getSavedMove();
                firstguess = rslt.max;

                if (savedMove != null) {
                    IterativeDeepeningPVS.printRoundInfo(rslt, watch, negaMaxAlphaBeta);

                }
                //moves = reOrderMoves(rslt.moveScores);
                negaMaxAlphaBeta.resetCaches();
                orderHints = new OrderHints(rslt.pvList, rslt.moveScores, context.getContext(HISTORY_HEURISTIC));
            }
        } catch (TimeoutException te) {
            return savedMove;
        } finally {
            negaMaxAlphaBeta.reset();
        }

        return savedMove;
    }

    private NegaMaxResult mtdf(GameState gameState, int depth, long stopTime, int f, OrderHints orderHints) {
        int lower = ALPHA_START;
        int upper = BETA_START;
        NegaMaxResult result = null;
        int rounds = 0;
        do {
            rounds++;
            int beta = f;
            if (f == lower) {
                beta += 1;
            }
            //logger.info("alpha: " + (beta -1) + " beta: " + beta);
            result = negaMaxAlphaBeta.searchWithScore(gameState, depth, beta - 1, beta, stopTime, orderHints);
            f = result.max;
            if (f < beta) {
                upper = f;
            }else {
                lower = f;
            }
        } while (lower < upper);

        UCI.instance.putCommand("info depth " + depth + " mtdf: rounds: " + rounds + " score cp:" + f + " lower: " + lower + " upper: " + upper );

        return result;
    }

}
