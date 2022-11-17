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
import org.mattlang.jc.engine.evaluation.Weights;
import org.mattlang.jc.engine.sorting.OrderCalculator;
import org.mattlang.jc.engine.tt.TTCacheInterface;
import org.mattlang.jc.engine.tt.TTResult;
import org.mattlang.jc.movegenerator.MoveGenerator;
import org.mattlang.jc.movegenerator.MoveGenerator.GenMode;
import org.mattlang.jc.moves.MoveBoardIterator;
import org.mattlang.jc.moves.MoveImpl;
import org.mattlang.jc.uci.GameContext;

import lombok.Getter;

/**
 * Holds Information during a negamax Search.
 * Collects results, has methods to deal with generating moves, etc.
 */

public final class SearchContext {

    private CheckChecker checkChecker = Factory.getDefaults().checkChecker.instance();
    private MoveGenerator generator = Factory.getDefaults().legalMoveGenerator.create();

    private boolean doCaching = Factory.getDefaults().getConfig().useTTCache.getValue();

    private boolean useHistoryHeuristic = Factory.getDefaults().getConfig().useHistoryHeuristic.getValue();
    private boolean useKillerMoves = Factory.getDefaults().getConfig().useKillerMoves.getValue();
    private boolean useCounterMove = Factory.getDefaults().getConfig().useCounterMoves.getValue();

    /**
     * The side that we are.
     */
    private final Color weAre;
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

    private TTResult ttResult = new TTResult();

    private HistoryHeuristic historyHeuristic = null;

    private KillerMoves killerMoves = null;

    private CounterMoveHeuristic counterMoveHeuristic = null;

    public SearchContext(SearchThreadContext stc, GameState gameState,
            GameContext context,
            int targetDepth, int alpha) {

        this.stc = stc;
        this.board = gameState.getBoard();

        this.orderCalculator = stc.getOrderCalculator();

        openingOrMiddleGame = PhaseCalculator.isOpeningOrMiddleGame(gameState.getBoard());
        weAre = gameState.getWho2Move();

        this.evaluate = stc.getEvaluate();

        this.gameState = gameState;
        this.context = context;
        this.targetDepth = targetDepth;
        this.selDepth = targetDepth;

        savedMoveScore = alpha;
        savedMove = 0;

        ttCache = context.getTtCache();

        killerMoves = stc.getKillerMoves();
        historyHeuristic = stc.getHistoryHeuristic();
        counterMoveHeuristic = stc.getCounterMoveHeuristic();
    }

    public void adjustSelDepth(int depth) {
        // depth is negative inside quiescence; now update selDepth to the maximum of quiescence depth we have
        // ever used in this round:
        if (targetDepth - depth > selDepth) {
            selDepth = targetDepth - depth;
        }
    }

    public void updateRootMoveScore(int depth, int move, int score, int numSearchedNodes) {
        if (depth == targetDepth) {
            moveScores.add(new MoveScore(move, score, numSearchedNodes));
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

    public TTResult getTTEntry() {
        if (doCaching && ttCache.findEntry(ttResult, board)) {
            return ttResult;
        }
        return null;
    }

    public int probeTTHashMove() {
        if (doCaching) {
            if (ttCache.findEntry(ttResult, board)) {
                return ttResult.getMove();
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

    public MoveList generateSortedMoves(GenMode mode, int ply, Color color,
            int parentMove) {
        MoveList moveList = stc.getCleanedMoveList(ply);
        generator.generate(mode, orderCalculator, board, color, moveList);
        sortMoves(ply, color, parentMove, moveList);

        return moveList;
    }

    public MoveBoardIterator genSortedMovesIterator(GenMode mode, int ply, Color color,
            int parentMove) {
        MoveList moveList = generateSortedMoves(mode, ply, color, parentMove);
        return moveList.iterateMoves(board, checkChecker);
    }

    /**
     * Sorts the move list by calculating the move order first.
     *
     * @param color
     * @param moves
     */
    private void sortMoves(int ply, Color color, int parentMove, MoveList moves) {
        int hashMove = probeTTHashMove();
        orderCalculator.prepareOrder(color, hashMove, parentMove, ply, board);
        moves.sort(orderCalculator);
    }

    public int eval(Color color) {
        return evaluate.eval(board, color);
    }

    /**
     * Evaluate a repetition. A repetition in general is undesirable as it means we have reached the same position
     * again
     * which means it doesnt bring us any further.
     * It could be also the case that this is the third time the repetition which means it is a draw.
     * So we make here a decision if the draw would be useful or bad which depends if we are the winning side or the
     * loosing side.
     * This is especial useful in endgames where caching of tt values could easily lead into repetition draw situations
     * unintentionally for the winning side.
     *
     * At the moment it seems we do not have to do special handling. the described situations lead from a general
     * wrong handling of keeping the move history in cloned boards. So at the moement we do simply return the weight of 0.
     *
     * @param color
     * @return
     */
    public int evaluateRepetition(Color color) {
        // the issues we had with draws was caused by wrong board copy logic, so we it looks like we do
        // not have to do any special logic here...

        //        int staticEval = eval(color);
//        if (staticEval >= WINNING_WEIGHT && weAre == WHITE) {
//            return -DRAW_IS_VERY_BAD;
//        } else if (staticEval <= -WINNING_WEIGHT && weAre == BLACK) {
//            return +DRAW_IS_VERY_BAD;
//        }
        return Weights.REPETITION_WEIGHT;
    }

    // todo test that not in check because those heuristics make only for quiet pos sense...?
    public void updateCutOffHeuristics(int ply, int depth, Color color, int parentMove, int bestMove,
            MoveCursor moveCursor) {
        if (!moveCursor.isCapture()) {
            if (useHistoryHeuristic) {
                historyHeuristic.update(color, moveCursor, depth);
            }
            if (useKillerMoves) {
                killerMoves.addKiller(color, bestMove, ply);
            }

            if (useCounterMove) {
                counterMoveHeuristic.addCounterMove(color.ordinal(), parentMove, bestMove);
            }
        }
    }

    public void updateBadHeuristic(int depth, Color color, MoveCursor moveCursor) {
        if (useHistoryHeuristic && !moveCursor.isCapture()) {
            historyHeuristic.updateBad(color, moveCursor, depth);
        }
    }

    public boolean isDrawByMaterial() {
        return board.isDrawByMaterial();
    }

    /**
     * Tries to find out if the side is not in zugzwang.
     *
     * This is just a simple approach which states that the side to move has non-pawn material, it doesn´t have
     * zugzwang....
     *
     * @return
     */
    public boolean isNoZugzwang() {
        return isNoZugwang(getBoard());
    }

    /**
     * Tries to find out if the side is not in zugzwang.
     *
     * This is just a simple approach which states that the side to move has non-pawn material, it doesn´t have
     * zugzwang....
     *
     * @return
     */
    public static boolean isNoZugwang(BoardRepresentation board){
        return board.getMaterial().hasNonPawnMat(board.getSiteToMove());
    }
}
