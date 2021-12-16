package org.mattlang.jc.engine.search;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.*;
import org.mattlang.jc.engine.CheckChecker;
import org.mattlang.jc.engine.EvaluateFunction;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.evaluation.PhaseCalculator;
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

    private final BoardRepresentation board;

    private RepetitionChecker repetitionChecker;

    @Getter
    private final boolean openingOrMiddleGame;

    private final TTCacheInterface ttCache;

    private final EvaluateFunction evaluate;

    // null move stuff
    @Getter
    private int nullMoveCounter;
    private int enPassantBeforeNullMove;
    private RepetitionChecker repetitionCheckerBeforeNullMove;

    public SearchContext(GameState gameState, GameContext context,
            EvaluateFunction evaluate,
            int targetDepth, int alpha) {

        this.board = gameState.getBoard();
        repetitionChecker = gameState.getRepetitionChecker();
        openingOrMiddleGame = PhaseCalculator.isOpeningOrMiddleGame(gameState.getBoard());

        this.evaluate = evaluate;

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

    public boolean isRepetition() {
        return repetitionChecker.isRepetition();
    }

    public void doMove(MoveCursor moveCursor) {
        moveCursor.move(board);
        repetitionChecker.push(board);
    }

    public void undoMove(MoveCursor moveCursor) {
        moveCursor.undoMove(board);
        repetitionChecker.pop();
    }

    public void undoNullMove() {
        nullMoveCounter--;
        if (enPassantBeforeNullMove != board.getEnPassantMoveTargetPos()) {
            board.setEnPassantOption(enPassantBeforeNullMove);
        }
//        repetitionChecker = repetitionCheckerBeforeNullMove;
    }

    public void doPrepareNullMove() {
        nullMoveCounter++;
        enPassantBeforeNullMove = board.getEnPassantMoveTargetPos();
        if (enPassantBeforeNullMove != Board3.NO_EN_PASSANT_OPTION) {
            board.setEnPassantOption(Board3.NO_EN_PASSANT_OPTION);
        }

//        repetitionCheckerBeforeNullMove = repetitionChecker;
//        repetitionChecker = new SimpleRepetitionChecker();
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
        ttCache.collectStatistics(ttcacheMap);
        rslts.put("ttcache", ttcacheMap);

    }

    public MoveList generateMoves(Color color) {
        return generator.generate(board, color);
    }

    public MoveList generateNonQuietMoves(Color color) {
        return generator.generateNonQuietMoves(board, color);
    }

    public int eval(Color color) {
        return evaluate.eval(board, color);
    }
}
