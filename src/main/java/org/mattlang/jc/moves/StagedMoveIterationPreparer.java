package org.mattlang.jc.moves;

import static java.util.Objects.requireNonNull;

import java.util.logging.Logger;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.CheckChecker;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.search.SearchThreadContext;
import org.mattlang.jc.engine.sorting.MoveIterator;
import org.mattlang.jc.engine.sorting.MovePicker;
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
    private static final int STAGE_GOOD_CAPTURES = 2;
    private static final int STAGE_KILLERS = 3;
    private static final int STAGE_COUNTER = 4;
    private static final int STAGE_QUIESCENCE_HASH = 5;
    private static final int STAGE_QUIESCENCE_REST = 6;
    private static final int STAGE_REST = 7;

    /**
     * stages for "normal" negamax iteration.
     * This is currently the best split in stages. Other combinations, e.g. split killers in two stages, etc.
     * have not given any benefits.
     */
    private static final int[] STAGES_NORMAL =
            { STAGE_HASH, STAGE_GOOD_CAPTURES, STAGE_KILLERS, STAGE_COUNTER, STAGE_REST };

    /**
     * Stages for quiescence. Actually we only have on stage for quiescence; other experiments have not
     * given any benefit.
     */
    private static final int[] STAGES_QUIESCENCE = { /*STAGE_QUIESCENCE_HASH,*/ STAGE_QUIESCENCE_REST };

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
    private MovePicker movePicker = new MovePicker();
    private SearchThreadContext stc;
    private GenMode mode;

    public void prepare(SearchThreadContext stc, GenMode mode, BoardRepresentation board, Color color,
            int ply, int hashMove, int parentMove) {
        prepare(stc, mode, board, color, ply, hashMove, parentMove, 0);
    }

    public void prepare(SearchThreadContext stc, GenMode mode, BoardRepresentation board, Color color,
            int ply, int hashMove, int parentMove, int captureMargin) {
        moveList.reset(color);
        movePicker.init(moveList, 0);
        this.stage = -1;
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

        prepareNextStage();
    }

    /**
     * Prepares the next stage according to the stages list.
     * Therefore, loops over the list of stages until a stage delivers moves.
     *
     * If no moves at all are delivered it stops if all stages are iterated.
     */
    private void prepareNextStage() {
        boolean nextStagePrepared = false;
        while (!nextStagePrepared && stage < stages.length - 1) {
            stage++;

            switch (stages[stage]) {
            case STAGE_HASH:
                if (hashMove != 0 && board.isvalidmove(color, hashMove)) {
                    nextStagePrepared = prepareHashStage();
                }
                break;
            case STAGE_GOOD_CAPTURES:
                nextStagePrepared = prepareGoodCaptureStage();
                break;
            case STAGE_KILLERS:
                nextStagePrepared = prepareKillerStage();
                break;
            case STAGE_COUNTER:
                nextStagePrepared = prepareCounterStage();
                break;
            case STAGE_REST:
                nextStagePrepared = prepareRestStage();
                break;
            case STAGE_QUIESCENCE_HASH:
                nextStagePrepared = prepareQuiescenceHashStage();
                break;
            case STAGE_QUIESCENCE_REST:
                nextStagePrepared = prepareQuiescenceRestStage();
                break;
            }

        }
    }

    private boolean prepareKillerStage() {
        int currIndex = movePicker.getCurrentIndex();
        int currStartPos = moveList.size();
        int[] killers = stc.getKillerMoves().getOrCreateKillerList(color, ply);
        for (int killer : killers) {
            if (killer != 0 && board.isvalidmove(color, killer)) {
                moveList.addMoveWithOrder(killer, OrderCalculator.KILLER_SCORE);
                moveList.addFilter(killer);
            }
        }
        if (moveList.size() > currStartPos) {
            int killerStageSize = moveList.size() - currStartPos;
            movePicker.init(moveList, currIndex + 1, killerStageSize);
            return true;
        } else {
            return false;
        }
    }

    private boolean prepareCounterStage() {
        int currIndex = movePicker.getCurrentIndex();
        int currStartPos = moveList.size();
        int counterMove = stc.getCounterMoveHeuristic().getCounter(color.ordinal(), parentMove);
        if (counterMove != 0 && board.isvalidmove(color, counterMove)) {
            moveList.addMoveWithOrder(counterMove, OrderCalculator.COUNTER_MOVE_SCORE);
            moveList.addFilter(counterMove);
        }

        if (moveList.size() > currStartPos) {
            int counterStageSize = moveList.size() - currStartPos;
            movePicker.init(moveList, currIndex + 1, counterStageSize);
            return true;
        } else {
            return false;
        }
    }

    // rest: quiets + promotions
    private boolean prepareRestStage() {
        //        LOGGER.info("preparing rest stage");
        int currIndex = movePicker.getCurrentIndex();
        int currStartPos = moveList.size();
        MoveGeneration.generateQuiets(board, color, moveList);
        createQuietSortOrders(currStartPos);

        movePicker.init(moveList, currIndex + 1);
        return true;
    }

    private boolean prepareQuiescenceHashStage() {

        if (hashMove != 0
                && (MoveImpl.isCapture(hashMove) || MoveImpl.isPromotion(hashMove))
                && board.isvalidmove(color, hashMove)) {
            moveList.addMoveWithOrder(hashMove, OrderCalculator.HASHMOVE_SCORE);
            moveList.addFilter(hashMove);
            movePicker.init(moveList, 0);
            return true;
        } else {
            return false;
        }
    }

    private boolean prepareQuiescenceRestStage() {
        //        LOGGER.info("preparing rest stage");
        int currIndex = movePicker.getCurrentIndex();
        int currStartPos = moveList.size();
        generator.generate(GenMode.QUIESCENCE, orderCalculator, board, color, moveList);
        createSortOrders(currStartPos);

        movePicker.init(moveList, currIndex + 1);
        return true;
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

    /**
     * Prepares the stage with good captures.
     * This creates all capture moves together with "bad" captures, but the size of the picker will be reduced
     * to iterate them then later on in other stages.
     */
    private boolean prepareGoodCaptureStage() {
        //        LOGGER.info("preparing good capture stage");
        int currIndex = movePicker.getCurrentIndex();
        int currStartPos = moveList.size();
        MoveGeneration.generateAttacks(board, color, moveList);

        if (moveList.size() == currStartPos) {
            return false;
        } else {
            /* create sort vals and count number of good capture moves in the list. this is the size of the picker for this stage:
             * */
            int stageSize = createCaptureSortOrders(currStartPos);

            if (stageSize != 0) {
                // init picker with size to only iterate good captures:
                movePicker.init(moveList, currIndex + 1, stageSize);
                return true;
            } else {
                // no good moves, jump to next stage:
                return false;
            }
        }
    }

    private boolean prepareHashStage() {
        //        LOGGER.info("preparing hash stage");
        moveList.addMoveWithOrder(hashMove, OrderCalculator.HASHMOVE_SCORE);
        moveList.addFilter(hashMove);
        movePicker.init(moveList, 0);

        return true;
    }

    public MoveBoardIterator iterateMoves() {
        moveBoardIterator.init(this, board, checkChecker);
        return moveBoardIterator;
    }

    @Override
    public int getOrder() {
        return movePicker.getOrder();
    }

    @Override
    public int next() {
        return movePicker.next();
    }

    @Override
    public boolean hasNext() {
        if (movePicker.hasNext()) {
            return true;
        }
        prepareNextStage();
        return movePicker.hasNext();
    }
}
