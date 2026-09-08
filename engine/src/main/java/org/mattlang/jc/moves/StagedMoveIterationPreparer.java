package org.mattlang.jc.moves;

import org.mattlang.jc.BuildConstants;
import org.mattlang.jc.ConfigValues;
import org.mattlang.jc.ConfigurationListener;
import org.mattlang.jc.UciConfigParam;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.search.SearchThreadContext;
import org.mattlang.jc.engine.sorting.MoveIterator;
import org.mattlang.jc.engine.sorting.MovePicker;
import org.mattlang.jc.engine.sorting.OrderCalculator;
import org.mattlang.jc.movegenerator.GenMode;
import org.mattlang.jc.movegenerator.MoveGeneration;
import org.mattlang.jc.movegenerator.PseudoLegalMoveGenerator;

import java.util.logging.Logger;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static org.mattlang.jc.moves.Stage.*;

/**
 * Encapsulates all relevant objects to prepare iteration over moves on the board.
 * The move iteration can be internally staged into several stages for hashmove, captures, non-captures, etc.
 * <p>
 * A lot of experiments have been made with different stage configurations, so this is the result of the
 * "best" variant so far.
 * A code-wise much nicer variant using static Method-References instead of tumb switch cases has also been evaluated,
 * but this gives a much worse performance: the JIT compiler seems in that case not be able to do many optimizations
 * on the code, so this variant has been discarded.
 * Another variant using special code for one-move-stages (like hashmoves, killers) has also been evaluated, but has
 * also not brought any benefit.
 */
public final class StagedMoveIterationPreparer implements MoveIterator, ConfigurationListener {

    public static final Logger LOGGER = Logger.getLogger(StagedMoveIterationPreparer.class.getSimpleName());

    /**
     * stages for "normal" negamax iteration.
     * This is currently the best split in stages. Other combinations, e.g. split killers in two stages, etc.
     * have not given any benefits.
     */
    private static final Stage[] STAGES_NORMAL =
            {STAGE_HASH, PREPARE_STAGE_GOOD_CAPTURES, STAGE_GOOD_CAPTURES, STAGE_KILLERS1, STAGE_KILLERS2,
                    STAGE_COUNTER, PREPARE_STAGE_QUIET, STAGE_GOOD_QUIET, STAGE_BAD_CAPTURES, STAGE_BAD_QUIET};

    private Stage[] initializedStagesNormal = STAGES_NORMAL;
    /**
     * Stages for quiescence. Actually we only have on stage for quiescence; other experiments have not
     * given any benefit.
     */
    private static final Stage[] STAGES_QUIESCENCE =
            { /*STAGE_QUIESCENCE_HASH,*/ PREPARE_STAGE_QUIESCENCE_REST, STAGE_GOOD_CAPTURES, STAGE_BAD_CAPTURES};

    private static final Stage[] SINGLE_STATIC_STAGE = {STAGE_GOOD_CAPTURES};

    private final MoveList moveListGen = new MoveList();

    private final MovePicker pickerGoodCapt = new MovePicker();
    private final MovePicker pickerBadCapt = new MovePicker();
    private final MovePicker pickerGoodQuiet = new MovePicker();
    private final MovePicker pickerBadQuiet = new MovePicker();

    private final PseudoLegalMoveGenerator generator = new PseudoLegalMoveGenerator();

    private final MoveBoardIterator moveBoardIterator = new MoveBoardIterator();

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
    private final int[] filterMoves = new int[4];
    private int filterCount = 0;

    @UciConfigParam
    private String stagesNormal;

    public StagedMoveIterationPreparer() {
        ConfigValues.getConfigValues().registerConfigurableListeningObject(this);
    }

    public void prepare(SearchThreadContext stc, GenMode mode, BoardRepresentation board, int color,
                        int ply, int hashMove, int parentMove) {
        prepare(stc, mode, board, color, ply, hashMove, parentMove, 0);
    }

    public void prepare(SearchThreadContext stc, GenMode mode, BoardRepresentation board, int color,
                        int ply, int hashMove, int parentMove, int captureMargin) {

        pickerGoodCapt.reset();
        pickerBadCapt.reset();
        pickerGoodQuiet.reset();
        pickerBadQuiet.reset();

        filterCount = 0;
        this.stageIndex = 0;
        this.stc = stc;
        this.moveBoardIterator.setEvaluate(stc.getEvaluate());
        this.board = board;
        this.hashMove = hashMove;
        this.color = color;
        this.ply = ply;
        this.parentMove = parentMove;
        this.captureMargin = captureMargin;
        this.orderCalculator = requireNonNull(stc.getOrderCalculator()); // maybe refactor this..
        stages = mode == GenMode.NORMAL ? initializedStagesNormal : STAGES_QUIESCENCE;
        currStage = stages[stageIndex];
    }

    public void configChanged() {
        if (stagesNormal == null) {
            initializedStagesNormal = STAGES_NORMAL;
        } else {
            initializedStagesNormal = stream(stagesNormal.split(",")).map(Stage::valueOf).toArray(Stage[]::new);
        }
    }

    public void prepareFirstPly(SearchThreadContext stc, BoardRepresentation board, int color,
                                MoveList legalMovesToSearch, int hashMove, int parentMove, int captureMargin) {

        pickerGoodCapt.reset();
        pickerBadCapt.reset();
        pickerGoodQuiet.reset();
        pickerBadQuiet.reset();

        filterCount = 0;
        this.stageIndex = 0;
        this.stc = stc;
        this.moveBoardIterator.setEvaluate(stc.getEvaluate());
        this.board = board;
        this.hashMove = hashMove;
        this.color = color;
        this.ply = 1;
        this.parentMove = parentMove;
        this.captureMargin = captureMargin;
        this.orderCalculator = requireNonNull(stc.getOrderCalculator()); // maybe refactor this..
        stages = initializedStagesNormal;
        if (legalMovesToSearch != null && legalMovesToSearch.size() > 0) {
            stages = SINGLE_STATIC_STAGE;
            orderCalculator.prepareOrder(color, hashMove, parentMove, ply, board, captureMargin);
            pickerGoodCapt.reset();
            orderCalculator.scoreMoves(legalMovesToSearch, pickerGoodCapt, pickerGoodCapt);

        }
        currStage = stages[stageIndex];
    }

    private int theNextMove = 0;
    private int theNextOrder = 0;

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

                    moveListGen.reset(color);
                    MoveGeneration.generateAttacks(board, color, moveListGen);
                    createCaptureSortOrders();
                    break;
                case STAGE_GOOD_CAPTURES:
                    if (sortToFrontSkippingFiltered(pickerGoodCapt)) {
                        theNextOrder = pickerGoodCapt.getOrder();
                        return true;
                    }
                    nextStage();

                    break;
                case STAGE_BAD_CAPTURES:
                    if (sortToFrontSkippingFiltered(pickerBadCapt)) {
                        theNextOrder = pickerBadCapt.getOrder();
                        return true;
                    }
                    nextStage();

                    break;
                case STAGE_KILLERS1:
                    nextStage();
                    if (isValidSpecialMove(getKiller(0))) {
                        theNextOrder = OrderCalculator.KILLER_SCORE;
                        return true;
                    }
                    break;
                case STAGE_KILLERS2:
                    nextStage();
                    if (isValidSpecialMove(getKiller(1))) {
                        theNextOrder = OrderCalculator.KILLER_SCORE;
                        return true;
                    }
                    break;
                case STAGE_COUNTER:
                    nextStage();
                    int counterMove = stc.getCounterMoveHeuristic().getCounter(color, parentMove);
                    if (isValidSpecialMove(counterMove)) {
                        theNextOrder = OrderCalculator.COUNTER_MOVE_SCORE;
                        return true;
                    }
                    break;
                case PREPARE_STAGE_QUIET:
                    nextStage();
                    moveListGen.reset(color);
                    MoveGeneration.generateQuiets(board, color, moveListGen);
                    createQuietSortOrders();

                    break;
                case STAGE_GOOD_QUIET:
                    if (sortToFrontSkippingFiltered(pickerGoodQuiet)) {
                        theNextOrder = pickerGoodQuiet.getOrder();
                        return true;
                    }
                    nextStage();

                    break;
                case STAGE_BAD_QUIET:
                    if (sortToFrontSkippingFiltered(pickerBadQuiet)) {
                        theNextOrder = pickerBadQuiet.getOrder();
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
                    moveListGen.reset(color);
                    generator.generate(GenMode.QUIESCENCE, board, color, moveListGen);
                    createCaptureSortOrders();

                    break;
            }

        }

        return false;
    }

    private int getKiller(int killerNum) {
        int[] killers = stc.getKillerMoves().getOrCreateKillerList(ply);
        return killers[killerNum];
    }

    private boolean isValidSpecialMove(int specialMove) {
        if (specialMove != 0 && board.isvalidmove(color, specialMove) && isUnfilteredMove(specialMove)) {
            theNextMove = specialMove;
            addFilter(specialMove);
            return true;
        }
        return false;
    }

    /**
     * sorts the next move to front of the list, skipping all special handled
     * moves like hashmove, killers, etc.
     * this is used for stages handling quiet moves since there all special moves
     * could be part of it.
     *
     * @param picker
     * @return
     */
    private boolean sortToFrontSkippingFiltered(MovePicker picker) {
        while (picker.hasNext()) {
            theNextMove = picker.next();
            if (isUnfilteredMove(theNextMove)) {
                return true;
            }
        }
        return false;
    }

    private void createQuietSortOrders() {
        pickerGoodQuiet.reset();
        pickerBadQuiet.reset();
        orderCalculator.prepareOrder(color, hashMove, parentMove, ply, board, captureMargin);
        orderCalculator.scoreQuietMoves(moveListGen, pickerGoodQuiet, pickerBadQuiet);
    }

    private void createCaptureSortOrders() {
        pickerGoodCapt.reset();
        pickerBadCapt.reset();
        orderCalculator.prepareOrder(color, hashMove, parentMove, ply, board, captureMargin);
        orderCalculator.scoreCaptureMoves(moveListGen, pickerGoodCapt, pickerBadCapt);
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
