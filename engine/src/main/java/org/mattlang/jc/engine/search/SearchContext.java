package org.mattlang.jc.engine.search;

import static org.mattlang.jc.movegenerator.GenMode.NORMAL;
import static org.mattlang.jc.movegenerator.GenMode.QUIESCENCE;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.EvaluateFunction;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.evaluation.PhaseCalculator;
import org.mattlang.jc.engine.evaluation.Weights;
import org.mattlang.jc.engine.tt.TTCache;
import org.mattlang.jc.engine.tt.TTResult;
import org.mattlang.jc.movegenerator.Captures;
import org.mattlang.jc.moves.MoveBoardIterator;
import org.mattlang.jc.moves.StagedMoveIterationPreparer;
import org.mattlang.jc.uci.GameContext;

import lombok.Getter;

/**
 * Holds Information during a negamax Search.
 * Collects results, has methods to deal with generating moves, etc.
 */

public final class SearchContext {

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

    private final int optionalLastBestMove;

    @Getter
    private final BoardRepresentation board;

    @Getter
    private final boolean openingOrMiddleGame;

    private final TTCache ttCache;

    private final EvaluateFunction evaluate;

    // null move stuff
    @Getter
    private int nullMoveCounter;

    private SearchThreadContext stc;

    private TTResult ttResult = new TTResult();

    private HistoryHeuristic historyHeuristic = null;

    private ContinuationHistoryHeuristic continuationHistoryHeuristic = null;

    private CaptureHeuristic captureHeuristic = null;

    private KillerMoves killerMoves = null;

    private CounterMoveHeuristic counterMoveHeuristic = null;

    private MoveList legalMovesToSearch = null;

    public SearchContext(MoveList legalMovesToSearch,
            Move optionalLastBestMove,
            SearchThreadContext stc, GameState gameState,
            GameContext context,
            int targetDepth, int alpha) {

        this.stc = stc;

        this.optionalLastBestMove = optionalLastBestMove != null ? optionalLastBestMove.getMoveInt() : 0;
        this.legalMovesToSearch = legalMovesToSearch;

        this.board = gameState.getBoard();

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
        continuationHistoryHeuristic = stc.getContinuationHistoryHeuristic();
        captureHeuristic = stc.getCaptureHeuristic();
        counterMoveHeuristic = stc.getCounterMoveHeuristic();
    }

    public void adjustSelDepth(int ply) {
        if (ply > selDepth) {
            selDepth = ply;
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
     *
     * @return
     */
    public boolean isRepetition() {
        return board.isRepetition();
    }

    public void undoNullMove() {
        board.undoNullMove();
    }

    public void resetNullMoveCounter() {
        nullMoveCounter = 0;
    }

    public void doPrepareNullMove() {
        nullMoveCounter++;
        board.doNullMove();
    }

    public boolean isInCheck(Color color) {
        return Captures.canKingCaptured(board, color);
    }

    public void storeTT(int max, int alpha, int beta, int depth,
            int move, int eval) {
        ttCache.storeTTEntry(board, max, alpha, beta, depth, move, eval);
    }

    public void storeTTEval(int depth, int eval) {
        ttCache.storeTTEval(board, depth, eval);
    }

    public TTResult getTTEntry() {
        if (ttCache.findEntry(ttResult, board)) {
            return ttResult;
        }
        return null;
    }

    public int probeTTHashMove() {
        return ttCache.findHashMove(board);
    }

    public MoveBoardIterator genQuiescenceMoves(int ply, Color color, int hashMove,
            int parentMove, int captureMargin) {
        StagedMoveIterationPreparer preparer = stc.getMoveIterationPreparer(ply);
        preparer.prepare(stc, QUIESCENCE, board, color, ply, hashMove, parentMove, captureMargin);
        return preparer.iterateMoves();
    }

    public MoveBoardIterator genRegularMoves(int ply, Color color, int hashMove,
            int parentMove, int captureMargin) {
        StagedMoveIterationPreparer preparer = stc.getMoveIterationPreparer(ply);
        if (ply == 1) {
            int bestMoveForSorting = optionalLastBestMove != 0 ? optionalLastBestMove : hashMove;
            preparer.prepareFirstPly(stc, board, color, legalMovesToSearch, bestMoveForSorting, parentMove,
                    captureMargin);
        } else {
            preparer.prepare(stc, NORMAL, board, color, ply, hashMove, parentMove, captureMargin);
        }
        return preparer.iterateMoves();
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
     * wrong handling of keeping the move history in cloned boards. So at the moement we do simply return the weight of
     * 0.
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
    public void updateCutOffHeuristics(int ply, int depth, Color color, int grandparentMove, int parentMove,
            int bestMove,
            MoveCursor moveCursor) {
        if (!moveCursor.isCapture()) {

            historyHeuristic.update(color, moveCursor, depth);

            continuationHistoryHeuristic.update(color, parentMove, moveCursor, depth);
            continuationHistoryHeuristic.update(color, grandparentMove, moveCursor, depth);

            killerMoves.addKiller(bestMove, ply);

            counterMoveHeuristic.addCounterMove(color.ordinal(), parentMove, bestMove);
        } else {
            captureHeuristic.update(color, moveCursor, depth);
        }
    }

    public void updateBadHeuristic(int depth, Color color, int grandparentMove, int parentMove, MoveCursor moveCursor) {
        if (!moveCursor.isCapture()) {

            historyHeuristic.updateBad(color, moveCursor, depth);

            continuationHistoryHeuristic.updateBad(color, parentMove, moveCursor, depth);
            continuationHistoryHeuristic.updateBad(color, grandparentMove, moveCursor, depth);
        } else {
            captureHeuristic.updateBad(color, moveCursor, depth);
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
    public static boolean isNoZugwang(BoardRepresentation board) {
        return board.getMaterial().hasNonPawnMat(board.getSiteToMove());
    }

    /**
     * Reset Killers on a ply. This is done to keep killers of children local to similar positions.
     *
     * @param ply
     */
    public void resetKillers(int ply) {
        killerMoves.resetAtPly(ply);
    }
}
