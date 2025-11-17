package org.mattlang.jc.moves;

import static java.util.Objects.requireNonNull;
import static org.mattlang.jc.moves.Stage.*;

import java.util.logging.Logger;

import org.mattlang.jc.BuildConstants;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.search.SearchThreadContext;
import org.mattlang.jc.engine.sorting.MoveIterator;
import org.mattlang.jc.engine.sorting.OrderCalculator;
import org.mattlang.jc.movegenerator.GenMode;
import org.mattlang.jc.movegenerator.MoveGeneration;
import org.mattlang.jc.movegenerator.PseudoLegalMoveGenerator;

/**
 * Encapsulates all relevant objects to prepare iteration over moves on the board.
 * The move iteration can be internally staged into several stages for hashmove, captures, non-captures, etc.
 *
 * A lot of experiments have been made with different stage configurations, so this is the result of the
 * "best" variant so far.
 * A code-wise much nicer variant using static Method-References instead of tumb switch cases has also been evaluated,
 * but this gives a much worse performance: the JIT compiler seems in that case not be able to do many optimizations
 * on the code, so this variant has been discarded.
 * Another variant using special code for one-move-stages (like hashmoves, killers) has also been evaluated, but has
 * also not brought any benefit.
 */
public final class StagedMoveIterationPreparer implements MoveIterator {

    public static final Logger LOGGER = Logger.getLogger(StagedMoveIterationPreparer.class.getSimpleName());

    /**
     * stages for "normal" negamax iteration.
     * This is currently the best split in stages. Other combinations, e.g. split killers in two stages, etc.
     * have not given any benefits.
     */
    private static final Stage[] STAGES_NORMAL =
            { STAGE_HASH, PREPARE_STAGE_GOOD_CAPTURES, STAGE_GOOD_CAPTURES, STAGE_KILLERS1, STAGE_KILLERS2,
                    STAGE_COUNTER, PREPARE_STAGE_REST, STAGE_REST };

    /**
     * Stages for quiescence. Actually we only have on stage for quiescence; other experiments have not
     * given any benefit.
     */
    private static final Stage[] STAGES_QUIESCENCE =
            { /*STAGE_QUIESCENCE_HASH,*/ PREPARE_STAGE_QUIESCENCE_REST, STAGE_QUIESCENCE_REST };

    private static final Stage[] SINGLE_STATIC_STAGE = { STAGE_STATIC_ALL };

    private MoveList moveList = new MoveList();

    private PseudoLegalMoveGenerator generator = new PseudoLegalMoveGenerator();

    private MoveBoardIterator moveBoardIterator = new MoveBoardIterator();

    private OrderCalculator orderCalculator;
    private BoardRepresentation board;

    private int stageIndex;

    private Stage[] stages;

    private int hashMove;
    private int color;
    private int ply;
    private int parentMove;

    private int captureMargin;
    private SearchThreadContext stc;
    private Stage currStage;

    /**
     * Moves which should be filtered during collecting of moves (used in staged move generation).
     * 4 Places are needed: a hash move, two killers, and a counter move at most.
     */
    private int[] filterMoves = new int[4];
    private int filterCount = 0;

    public void prepare(SearchThreadContext stc, GenMode mode, BoardRepresentation board, int color,
            int ply, int hashMove, int parentMove) {
        prepare(stc, mode, board, color, ply, hashMove, parentMove, 0);
    }

    public void prepare(SearchThreadContext stc, GenMode mode, BoardRepresentation board, int color,
            int ply, int hashMove, int parentMove, int captureMargin) {
        moveList.reset(color);
        filterCount = 0;
        movelistPos = 0;
        this.stageIndex = 0;
        this.stc = stc;
        this.board = board;
        this.hashMove = hashMove;
        this.color = color;
        this.ply = ply;
        this.parentMove = parentMove;
        this.captureMargin = captureMargin;
        this.orderCalculator = requireNonNull(stc.getOrderCalculator()); // maybe refactor this..
        stages = mode == GenMode.NORMAL ? STAGES_NORMAL : STAGES_QUIESCENCE;
        currStage = stages[stageIndex];
    }

    public void prepareFirstPly(SearchThreadContext stc, BoardRepresentation board, int color,
            MoveList legalMovesToSearch, int hashMove, int parentMove, int captureMargin) {
        moveList.reset(color);
        filterCount = 0;
        movelistPos = 0;
        this.stageIndex = 0;
        this.stc = stc;
        this.board = board;
        this.hashMove = hashMove;
        this.color = color;
        this.ply = 1;
        this.parentMove = parentMove;
        this.captureMargin = captureMargin;
        this.orderCalculator = requireNonNull(stc.getOrderCalculator()); // maybe refactor this..
        stages = STAGES_NORMAL;
        if (legalMovesToSearch != null && legalMovesToSearch.size() > 0) {
            stages = SINGLE_STATIC_STAGE;
            moveList.initFrom(legalMovesToSearch);
            createSortOrders(0);
        }
        currStage = stages[stageIndex];
    }

    private int theNextMove = 0;
    private int theNextOrder = 0;
    private int movelistPos = 0;

    @Override
    public boolean hasNext() {

        while (stageIndex < stages.length) {

            if (BuildConstants.ASSERTIONS) {
                LOGGER.fine("ply " + ply + " try " + currStage);
            }

            switch (currStage) {
            case STAGE_HASH:
                nextStage();
                if (hashMove != 0 && board.isvalidmove(color, hashMove)) {
                    theNextMove = hashMove;
                    theNextOrder = OrderCalculator.HASHMOVE_SCORE;
                    addFilter(hashMove);
                    return true;
                }
                break;
            case PREPARE_STAGE_GOOD_CAPTURES:
                nextStage();

                int currSize = moveList.size();
                MoveGeneration.generateAttacks(board, color, moveList);
                if (currSize == moveList.size()) {
                    // not captures at all: overstep next step:
                    nextStage();
                } else {
                    createCaptureSortOrders(movelistPos);
                    if (movelistPos < moveList.size()) {
                        theNextMove = sortToFront(movelistPos);
                        if (OrderCalculator.isGoodCapture(moveList.getOrder(movelistPos))) {
                            theNextOrder = moveList.getOrder(movelistPos);
                            return true;
                        } else {
                            // there are only bad captures: overstep the "stage good captures":
                            nextStage();
                        }

                    }
                }
                break;
            case STAGE_GOOD_CAPTURES:
                movelistPos++;
                if (sortToFrontSkippingFiltered()) {
                    if (OrderCalculator.isGoodCapture(moveList.getOrder(movelistPos))) {
                        theNextOrder = moveList.getOrder(movelistPos);
                        return true;
                    }
                }
                nextStage();

                break;
            case STAGE_KILLERS1:
                nextStage();
                int[] killers = stc.getKillerMoves().getOrCreateKillerList(ply);

                if (killers[0] != 0 && board.isvalidmove(color, killers[0]) && isUnfilteredMove(killers[0])) {
                    theNextMove = killers[0];
                    theNextOrder = OrderCalculator.KILLER_SCORE;
                    addFilter(killers[0]);
                    return true;
                }
                break;
            case STAGE_KILLERS2:
                nextStage();
                killers = stc.getKillerMoves().getOrCreateKillerList(ply);

                if (killers[1] != 0 && board.isvalidmove(color, killers[1]) && isUnfilteredMove(killers[1])) {
                    theNextMove = killers[1];
                    theNextOrder = OrderCalculator.KILLER_SCORE;
                    addFilter(killers[1]);
                    return true;
                }
                break;
            case STAGE_COUNTER:
                nextStage();
                int counterMove = stc.getCounterMoveHeuristic().getCounter(color, parentMove);
                if (counterMove != 0 && board.isvalidmove(color, counterMove) && isUnfilteredMove(counterMove)) {
                    theNextMove = counterMove;
                    theNextOrder = OrderCalculator.KILLER_SCORE;
                    addFilter(counterMove);
                    return true;
                }
                break;
            case PREPARE_STAGE_REST:
                nextStage();
                int start = moveList.size();
                MoveGeneration.generateQuiets(board, color, moveList);
                createQuietSortOrders(start);
                if (sortToFrontSkippingFiltered()) {
                    theNextOrder = moveList.getOrder(movelistPos);
                    return true;
                }

                break;
            case STAGE_REST:
            case STAGE_QUIESCENCE_REST:
                movelistPos++;
                if (sortToFrontSkippingFiltered()) {
                    theNextOrder = moveList.getOrder(movelistPos);
                    return true;
                }
                nextStage();

                break;
            case STAGE_QUIESCENCE_HASH:
                nextStage();
                if (hashMove != 0
                    && (MoveImpl.isCapture(hashMove) || MoveImpl.isPromotion(hashMove))
                    && board.isvalidmove(color, hashMove)) {
                    theNextMove = hashMove;
                    theNextOrder = OrderCalculator.HASHMOVE_SCORE;
                    return true;
                }
                break;
            case PREPARE_STAGE_QUIESCENCE_REST:
                nextStage();
                start = moveList.size();
                generator.generate(GenMode.QUIESCENCE, board, color, moveList);
                createSortOrders(start);
                if (sortToFrontSkippingFiltered()) {
                    theNextOrder = moveList.getOrder(movelistPos);
                    return true;
                }
                break;
            case STAGE_STATIC_ALL:
                if (sortToFrontSkippingFiltered()) {
                    theNextOrder = moveList.getOrder(movelistPos);
                    movelistPos++;
                    return true;
                }
                nextStage();
                break;
            }

        }

        return false;
    }

    private boolean sortToFrontSkippingFiltered() {
        while (movelistPos < moveList.size()) {
            theNextMove = sortToFront(movelistPos);
            if (isUnfilteredMove(theNextMove)) {
                return true;
            }
            movelistPos++;
        }
        return false;
    }

    private int sortToFront(int start) {
        int currLowest = moveList.getOrder(start);
        int currLowestIndex = start;
        for (int i = start + 1; i < moveList.size(); i++) {
            if (moveList.getOrder(i) < currLowest) {
                currLowest = moveList.getOrder(i);
                currLowestIndex = i;
            }
        }

        if (currLowestIndex != start) {
            moveList.swap(start, currLowestIndex);
        }
        return moveList.get(start);
    }

    private void createSortOrders(int currStartPos) {
        orderCalculator.prepareOrder(color, hashMove, parentMove, ply, board, captureMargin);
        orderCalculator.scoreMoves(moveList, currStartPos);
    }

    private void createQuietSortOrders(int currStartPos) {
        orderCalculator.prepareOrder(color, hashMove, parentMove, ply, board, captureMargin);
        orderCalculator.scoreQuietMoves(moveList, currStartPos);
    }

    private void createCaptureSortOrders(int currStartPos) {
        orderCalculator.prepareOrder(color, hashMove, parentMove, ply, board, captureMargin);
        orderCalculator.scoreCaptureMoves(moveList, currStartPos);
    }

    public MoveBoardIterator iterateMoves() {
        moveBoardIterator.init(this, board);
        return moveBoardIterator;
    }

    @Override
    public int getOrder() {
        return theNextOrder;
    }

    @Override
    public int next() {
        return theNextMove;
    }

    private void nextStage() {
        stageIndex++;
        if (stageIndex < stages.length) {
            currStage = stages[stageIndex];
        }
    }

    private boolean isUnfilteredMove(int aMove) {
        for (int i = 0; i < filterCount; i++) {
            if (filterMoves[i] == aMove) {
                return false;
            }
        }
        return true;
    }

    private void addFilter(int filterMove) {
        filterMoves[filterCount] = filterMove;
        filterCount++;
    }
}
