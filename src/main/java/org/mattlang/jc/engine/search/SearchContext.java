package org.mattlang.jc.engine.search;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.engine.CheckChecker;
import org.mattlang.jc.engine.EvaluateFunction;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.evaluation.PhaseCalculator;
import org.mattlang.jc.engine.sorting.OrderCalculator;
import org.mattlang.jc.engine.tt.TTCacheInterface;
import org.mattlang.jc.engine.tt.TTEntry;
import org.mattlang.jc.movegenerator.LegalMoveGenerator;
import org.mattlang.jc.moves.MoveImpl;
import org.mattlang.jc.uci.GameContext;

import lombok.Getter;

/**
 * Holds Information during a negamax Search.
 * Collects results, has methods to deal with generating moves, etc.
 */

public final class SearchContext {

    private CheckChecker checkChecker = Factory.getDefaults().checkChecker.instance();
    private LegalMoveGenerator generator = Factory.getDefaults().legalMoveGenerator.create();

    private boolean doCaching = Factory.getDefaults().getConfig().useTTCache.getValue();

    private GameState gameState;
    private GameContext context;

    /**
     * target search depth.
     */
    @Getter
    private int targetDepth;

    /**
     * selective search depth due to quiescence search
     */
    @Getter
    private int selDepth;

    @Getter
    private int savedMove = 0;

    @Getter
    private int savedMoveScore = 0;

    /**
     * Movescores on ply 1 for each searched move.
     */
    @Getter
    private ArrayList<MoveScore> moveScores = new ArrayList<>();

    @Getter
    private final BoardRepresentation board;

    @Getter
    private final boolean openingOrMiddleGame;

    private final TTCacheInterface ttCache;

    private final EvaluateFunction evaluate;

    private OrderCalculator orderCalculator;

    // null move stuff
    @Getter
    private int nullMoveCounter;

    private SearchThreadContext stc;

    public SearchContext(SearchThreadContext stc, GameState gameState,
            GameContext context,
            OrderCalculator orderCalculator, EvaluateFunction evaluate,
            int targetDepth, int alpha) {

        this.stc=stc;
        this.board = gameState.getBoard();

        openingOrMiddleGame = PhaseCalculator.isOpeningOrMiddleGame(gameState.getBoard());

        this.evaluate = evaluate;
        this.orderCalculator = orderCalculator;

        this.gameState = gameState;
        this.context = context;
        this.targetDepth = targetDepth;
        this.selDepth = targetDepth;

        savedMoveScore = alpha;
        savedMove = 0;

        ttCache = context.getTtCache();
    }

    public void adjustSelDepth(int depth) {
        // depth is negative inside quiescence; now update selDepth to the maximum of quiescence depth we have
        // ever used in this round:
        if (targetDepth - depth > selDepth) {
            selDepth = targetDepth - depth;
        }
    }

    public void updateRootMoveScore(int depth, int move, int score) {
        if (depth == targetDepth) {
            moveScores.add(new MoveScore(move, score));
        }
    }

    public void updateRootBestMove(int depth, int bestMove, int score) {
        if (depth == targetDepth) {
            savedMove = bestMove;
            savedMoveScore = score;
        }
    }

    /**
     * Return true, if this position is a repetition in the search path.
     * We treat this all the time as "draw" (not only after 3 times), means we treat repetitions not as beneficial,
     * since it means we have chosen this before and it has not made the situation better.
     * @return
     */
    public boolean isRepetition() {
        return board.isRepetition();
    }

    public void doMove(MoveCursor moveCursor) {
        moveCursor.move(board);
    }

    public void undoMove(MoveCursor moveCursor) {
        moveCursor.undoMove(board);
    }

    public void undoNullMove() {
        nullMoveCounter--;
        board.undoNullMove();
    }

    public void doPrepareNullMove() {
        nullMoveCounter++;
        board.doNullMove();
    }

    public boolean isInCheck(Color color) {
        return checkChecker.isInChess(board, color);
    }

    public void storeTT(Color color, int max, int alpha, int beta, int depth,
            int move) {
        if (doCaching) {
            ttCache.storeTTEntry(board, color, max, alpha, beta, depth, move);
        }
    }

    public TTEntry getTTEntry(Color color) {
        if (doCaching) {
            return ttCache.getTTEntry(board, color);
        } else {
            return null;
        }
    }

    public int probeTTHashMove(Color color, int depth) {
        if (doCaching) {
            TTEntry entry = ttCache.getTTEntry(board, color);
            // todo should the depth influence the search of the hash move?
            // any hash move may be better than no hash move...?
            if (entry != null) {
                return entry.getMove();
            }
        }
        return 0;

    }

    public void collectStatistics(Map stats) {

        Map rslts = new LinkedHashMap();
        stats.put("negamax alpha/beta", rslts);
        rslts.put("savedMove", new MoveImpl(getSavedMove()));
        rslts.put("savedMoveScore", getSavedMoveScore());
        Map ttcacheMap = new LinkedHashMap();
//        ttCache.collectStatistics(ttcacheMap);
        rslts.put("ttcache", ttcacheMap);

    }

    public MoveList generateMoves(int ply, Color color) {
        MoveList moveList=stc.getCleanedMoveList(ply);
        generator.generate(context, orderCalculator, board, color, moveList);
        return moveList;
    }

    public int eval(Color color) {
        return evaluate.eval(board, color);
    }
}
