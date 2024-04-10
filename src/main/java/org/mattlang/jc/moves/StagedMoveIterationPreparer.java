package org.mattlang.jc.moves;

import static java.util.Objects.requireNonNull;

import java.util.logging.Logger;

import org.mattlang.jc.BuildConstants;
import org.mattlang.jc.Factory;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.CheckChecker;
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
public class StagedMoveIterationPreparer implements MoveIterator {

    public static final Logger LOGGER = Logger.getLogger(StagedMoveIterationPreparer.class.getSimpleName());

    private static final int STAGE_HASH = 1;
    private static final int PREPARE_STAGE_GOOD_CAPTURES = 2;
    private static final int STAGE_GOOD_CAPTURES = 3;
    private static final int STAGE_KILLERS1 = 4;
    private static final int STAGE_KILLERS2 = 5;
    private static final int STAGE_COUNTER = 6;
    private static final int STAGE_QUIESCENCE_HASH = 7;
    private static final int PREPARE_STAGE_QUIESCENCE_REST = 8;
    private static final int STAGE_QUIESCENCE_REST = 9;
    private static final int PREPARE_STAGE_REST = 10;
    private static final int STAGE_REST = 11;

    /* stage names, used for debugging. */
    private static final String[] STAGENAME =
            { "NONE", "STAGE HASH", "PREPARE STAGE GOOD CAPTURES", "STAGE GOOD CAPTURES", "STAGE KILLERS1",
                    "STAGE KILLERS2", "STAGE COUNTER MOVE",
                    "STAGE QUIESCENCE HASH", "PREPARE STAGE QUIESCENCE REST", "STAGE QUIESCENCE REST",
                    "PREPARE STAGE REST", "STAGE REST" };

    /**
     * stages for "normal" negamax iteration.
     * This is currently the best split in stages. Other combinations, e.g. split killers in two stages, etc.
     * have not given any benefits.
     */
    private static final int[] STAGES_NORMAL =
            { STAGE_HASH, PREPARE_STAGE_GOOD_CAPTURES, STAGE_GOOD_CAPTURES, STAGE_KILLERS1, STAGE_KILLERS2,
                    STAGE_COUNTER, PREPARE_STAGE_REST, STAGE_REST };

    /**
     * Stages for quiescence. Actually we only have on stage for quiescence; other experiments have not
     * given any benefit.
     */
    private static final int[] STAGES_QUIESCENCE =
            { /*STAGE_QUIESCENCE_HASH,*/ PREPARE_STAGE_QUIESCENCE_REST, STAGE_QUIESCENCE_REST };

    private MoveList moveList = new MoveList();

    private CheckChecker checkChecker = Factory.getDefaults().checkChecker.instance();

    private PseudoLegalMoveGenerator generator = new PseudoLegalMoveGenerator();

    private MoveBoardIterator moveBoardIterator = new MoveBoardIterator();

    private OrderCalculator orderCalculator;
    private BoardRepresentation board;

    private int stage;

    private int[] stages;

    private int hashMove;
    private Color color;
    private int ply;
    private int parentMove;

    private int captureMargin;
    private SearchThreadContext stc;
    private GenMode mode;

    public void prepare(SearchThreadContext stc, GenMode mode, BoardRepresentation board, Color color,
            int ply, int hashMove, int parentMove) {
        prepare(stc, mode, board, color, ply, hashMove, parentMove, 0);
    }

    public void prepare(SearchThreadContext stc, GenMode mode, BoardRepresentation board, Color color,
            int ply, int hashMove, int parentMove, int captureMargin) {
        moveList.reset(color);
        movelistPos = 0;
        this.stage = 0;
        this.stc = stc;
        this.board = board;
        this.hashMove = hashMove;
        this.color = color;
        this.ply = ply;
        this.parentMove = parentMove;
        this.captureMargin = captureMargin;
        this.orderCalculator = requireNonNull(stc.getOrderCalculator()); // maybe refactor this..
        this.mode = mode;
        stages = mode == GenMode.NORMAL ? STAGES_NORMAL : STAGES_QUIESCENCE;
    }

    private int theNextMove = 0;
    private int theNextOrder = 0;
    private int movelistPos = 0;

    private int nextMove() {

        while (stage < stages.length) {

            if (BuildConstants.ASSERTIONS) {
                LOGGER.fine("ply " + ply + " try " + STAGENAME[stages[stage]]);
            }

            switch (stages[stage]) {
            case STAGE_HASH:
                stage++;
                if (hashMove != 0 && board.isvalidmove(color, hashMove)) {
                    theNextMove = hashMove;
                    theNextOrder = OrderCalculator.HASHMOVE_SCORE;
                    moveList.addFilter(hashMove);
                    return theNextMove;
                }
                break;
            case PREPARE_STAGE_GOOD_CAPTURES:
                stage++;

                int currSize = moveList.size();
                MoveGeneration.generateAttacks(board, color, moveList);
                if (currSize == moveList.size()) {
                    // not captures at all: overstep next step:
                    stage++;
                } else {
                    createCaptureSortOrders(movelistPos);
                    if (movelistPos < moveList.size()) {
                        theNextMove = sortToFront(movelistPos);
                        if (OrderCalculator.isGoodCapture(moveList.getOrder(movelistPos))) {
                            theNextOrder = moveList.getOrder(movelistPos);
                            return theNextMove;
                        } else {
                            // there are only bad captures: overstep the "stage good captures":
                            stage++;
                        }

                    }
                }
                break;
            case STAGE_GOOD_CAPTURES:
                movelistPos++;
                if (movelistPos < moveList.size()) {
                    theNextMove = sortToFront(movelistPos);
                    if (OrderCalculator.isGoodCapture(moveList.getOrder(movelistPos))) {
                        theNextOrder = moveList.getOrder(movelistPos);
                        return theNextMove;
                    }
                }
                stage++;

                break;
            case STAGE_KILLERS1:
                stage++;
                int[] killers = stc.getKillerMoves().getOrCreateKillerList(ply);

                if (killers[0] != 0 && board.isvalidmove(color, killers[0]) && !moveList.isFiltered(killers[0])) {
                    theNextMove = killers[0];
                    theNextOrder = OrderCalculator.KILLER_SCORE;
                    moveList.addFilter(killers[0]);
                    return theNextMove;
                }
                break;
            case STAGE_KILLERS2:
                stage++;
                killers = stc.getKillerMoves().getOrCreateKillerList(ply);

                if (killers[1] != 0 && board.isvalidmove(color, killers[1]) && !moveList.isFiltered(killers[1])) {
                    theNextMove = killers[1];
                    theNextOrder = OrderCalculator.KILLER_SCORE;
                    moveList.addFilter(killers[1]);
                    return theNextMove;
                }
                break;
            case STAGE_COUNTER:
                stage++;
                int counterMove = stc.getCounterMoveHeuristic().getCounter(color.ordinal(), parentMove);
                if (counterMove != 0 && board.isvalidmove(color, counterMove) && !moveList.isFiltered(counterMove)) {
                    theNextMove = counterMove;
                    theNextOrder = OrderCalculator.KILLER_SCORE;
                    moveList.addFilter(counterMove);
                    return theNextMove;
                }
                break;
            case PREPARE_STAGE_REST:
                stage++;
                int start = moveList.size();
                MoveGeneration.generateQuiets(board, color, moveList);
                createQuietSortOrders(start);
                if (movelistPos < moveList.size()) {
                    theNextMove = sortToFront(movelistPos);
                    theNextOrder = moveList.getOrder(movelistPos);
                    return theNextMove;
                }

                break;
            case STAGE_REST:
            case STAGE_QUIESCENCE_REST:
                movelistPos++;
                if (movelistPos < moveList.size()) {
                    theNextMove = sortToFront(movelistPos);
                    theNextOrder = moveList.getOrder(movelistPos);
                    return theNextMove;
                }
                stage++;

                break;
            case STAGE_QUIESCENCE_HASH:
                stage++;
                if (hashMove != 0
                        && (MoveImpl.isCapture(hashMove) || MoveImpl.isPromotion(hashMove))
                        && board.isvalidmove(color, hashMove)) {
                    theNextMove = hashMove;
                    theNextOrder = OrderCalculator.HASHMOVE_SCORE;
                    return theNextMove;
                }
                break;
            case PREPARE_STAGE_QUIESCENCE_REST:
                stage++;
                start = moveList.size();
                generator.generate(GenMode.QUIESCENCE, orderCalculator, board, color, moveList);
                createSortOrders(start);
                if (movelistPos < moveList.size()) {
                    theNextMove = sortToFront(movelistPos);
                    theNextOrder = moveList.getOrder(movelistPos);
                    return theNextMove;
                }
                break;
            }

        }

        return 0;
    }

    private int sortToFront(int start) {
        int currLowest = -1;
        int currLowestIndex = start;
        for (int i = start; i < moveList.size(); i++) {
            if (moveList.getOrder(i) < currLowest || currLowest == -1) {
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
        moveList.scoreMoves(orderCalculator, currStartPos);
    }

    private void createQuietSortOrders(int currStartPos) {
        orderCalculator.prepareOrder(color, hashMove, parentMove, ply, board, captureMargin);
        moveList.scoreQuietMoves(orderCalculator, currStartPos);
    }

    private int createCaptureSortOrders(int currStartPos) {
        orderCalculator.prepareOrder(color, hashMove, parentMove, ply, board, captureMargin);
        return moveList.scoreCaptureMoves(orderCalculator, currStartPos);
    }

    public MoveBoardIterator iterateMoves() {
        moveBoardIterator.init(this, board, checkChecker);
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

    @Override
    public boolean hasNext() {
        theNextMove = nextMove();
        return theNextMove != 0;
    }
}
