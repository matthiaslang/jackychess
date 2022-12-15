package org.mattlang.jc.moves;

import static java.util.Objects.requireNonNull;
import static org.mattlang.jc.engine.sorting.OrderCalculator.isGoodCapture;
import static org.mattlang.jc.engine.sorting.OrderCalculator.isGoodPromotion;

import java.util.logging.Logger;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Figure;
import org.mattlang.jc.engine.CheckChecker;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.search.SearchThreadContext;
import org.mattlang.jc.engine.sorting.MovePicker;
import org.mattlang.jc.engine.sorting.OrderCalculator;
import org.mattlang.jc.movegenerator.BBMoveGeneratorImpl2;
import org.mattlang.jc.movegenerator.MoveGenerator;
import org.mattlang.jc.movegenerator.PseudoLegalMoveGenerator;

/**
 * Encapsulates all relevant objects to prepare iteration over moves on the board.
 * The move iteration can be internally staged into several stages for hashmove, captures, non-captures.
 */
public class StagedMoveIterationPreparer implements MoveIterationPreparer, MoveCursor {

    public static final Logger LOGGER = Logger.getLogger(StagedMoveIterationPreparer.class.getSimpleName());

    private static final int NO_STAGE = 0;
    private static final int STAGE_HASH = 1;
    private static final int STAGE_GOOD_CAPTURES = 2;
    private static final int STAGE_KILLERS = 3;
    private static final int STAGE_COUNTER = 4;
    private static final int STAGE_REST = 5;
    private MoveListImpl moveList = new MoveListImpl();

    private CheckChecker checkChecker = Factory.getDefaults().checkChecker.instance();

    private MoveGenerator generator = new PseudoLegalMoveGenerator();
    private BBMoveGeneratorImpl2 gen = new BBMoveGeneratorImpl2();

    private MoveBoardIterator moveBoardIterator = new MoveBoardIterator();

    private OrderCalculator orderCalculator;
    private BoardRepresentation board;

    private int currMove;
    private int orderOfCurrentMove;

    private MoveImpl currMoveObj = new MoveImpl("a1a2");

    private int stage = NO_STAGE;

    private int hashMove;
    private Color color;
    private int ply;
    private int parentMove;

    private MovePicker movePicker = new MovePicker();
    private SearchThreadContext stc;

    public void prepare(SearchThreadContext stc, MoveGenerator.GenMode mode, BoardRepresentation board, Color color,
            int ply, int hashMove, int parentMove) {
        moveList.reset();
        movePicker.init(moveList, 0);
        stage = NO_STAGE;
        this.stc = stc;
        this.board = board;
        this.hashMove = hashMove;
        this.color = color;
        this.ply = ply;
        this.parentMove = parentMove;
        this.orderCalculator = requireNonNull(stc.getOrderCalculator()); // maybe refactor this..

        if (mode == MoveGenerator.GenMode.NORMAL) {
            // do staged move generation in normal mode:
            prepareNextStage();
        } else {
            // quiescence mode: do only one stage:
            generator.generate(mode, orderCalculator, board, color, moveList);
            createSortOrders(0);
            stage = STAGE_REST;
            movePicker.init(moveList, 0);
        }
    }

    private void prepareNextStage() {
        switch (stage) {
        case NO_STAGE:
            if (hashMove != 0 && board.isvalidmove(color, hashMove)) {
                prepareHashStage();
                break;
            } else {
                prepareGoodCaptureStage();
                break;
            }
        case STAGE_HASH:
            prepareGoodCaptureStage();
            break;
        case STAGE_GOOD_CAPTURES:
            prepareKillerStage();
            break;
        case STAGE_KILLERS:
            prepareCounterStage();
            break;
        case STAGE_COUNTER:
            prepareRestStage();
            break;
        case STAGE_REST:
            //            LOGGER.info("preparing end marker stage");
            // end: reset picker to 0 moves
            movePicker.init(moveList, 0, 0);
            break;
        }

    }

    private void prepareKillerStage() {
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
            stage = STAGE_KILLERS;
            int killerStageSize = moveList.size() - currStartPos;
            movePicker.init(moveList, currIndex + 1, killerStageSize);
        } else {
            prepareCounterStage();
        }
    }

    private void prepareCounterStage() {
        int currIndex = movePicker.getCurrentIndex();
        int currStartPos = moveList.size();
        int counterMove = stc.getCounterMoveHeuristic().getCounter(color.ordinal(), parentMove);
        if (counterMove != 0 && board.isvalidmove(color, counterMove)) {
            moveList.addMoveWithOrder(counterMove, OrderCalculator.COUNTER_MOVE_SCORE);
            moveList.addFilter(counterMove);
        }

        if (moveList.size() > currStartPos) {
            stage = STAGE_COUNTER;
            int counterStageSize = moveList.size() - currStartPos;
            movePicker.init(moveList, currIndex + 1, counterStageSize);
        } else {
            prepareRestStage();
        }
    }

    // rest: quiets + promotions
    private void prepareRestStage() {
        //        LOGGER.info("preparing rest stage");
        int currIndex = movePicker.getCurrentIndex();
        int currStartPos = moveList.size();
        gen.generate(board, color, moveList, BBMoveGeneratorImpl2.GenTypes.QUIET);
        createSortOrders(currStartPos);
        stage = STAGE_REST;
        movePicker.init(moveList, currIndex + 1);
    }

    private void createSortOrders(int currStartPos) {
        orderCalculator.prepareOrder(color, hashMove, parentMove, ply, board);
        moveList.sort(orderCalculator, currStartPos);
    }

    /**
     * Prepares the stage with good captures.
     * This creates all capture moves together with "bad" captures, but the size of the picker will be reduced
     * to iterate them then later on in other stages.
     */
    private void prepareGoodCaptureStage() {
//        LOGGER.info("preparing good capture stage");
        int currIndex = movePicker.getCurrentIndex();
        int currStartPos = moveList.size();
        //        gen.genPawnMoves(board.getBoard(), moveList, color, true);
        gen.generate(board, color, moveList, BBMoveGeneratorImpl2.GenTypes.CAPTURES);

        if (moveList.size() == currStartPos) {
            prepareKillerStage();
        } else {
            createSortOrders(currStartPos);
            stage = STAGE_GOOD_CAPTURES;

            /* count number of good capture moves in the list. this is the size of the picker for this stage:
             * */
            int stageSize = countGoodCapturesAndPromotions(moveList, currIndex + 1);

            if (stageSize!=0) {
                // init picker with size to only iterate good captures:
                movePicker.init(moveList, currIndex + 1, stageSize);
            } else {
                // no good moves, jump to next stage:
                prepareKillerStage();
            }
        }
    }

    /**
     * Count number of good captures and good promotions in a move list.
     *
     * @param moveList
     * @param start
     * @return
     */
    private int countGoodCapturesAndPromotions(MoveListImpl moveList, int start) {
        int goodOnes = 0;
        for (int i = start; i < moveList.size(); i++) {
            if (isGoodCapture(moveList.getOrder(i)) || isGoodPromotion(moveList.getOrder(i))) {
                goodOnes++;
            }
        }
        return goodOnes;
    }

    private void prepareHashStage() {
//        LOGGER.info("preparing hash stage");
        moveList.addMoveWithOrder(hashMove, OrderCalculator.HASHMOVE_SCORE);
        moveList.addFilter(hashMove);
        movePicker.init(moveList, 0);
        stage = STAGE_HASH;
    }

    public MoveBoardIterator iterateMoves() {
        moveBoardIterator.init(this, board, checkChecker);
        return moveBoardIterator;
    }

    @Override
    public byte getCapturedFigure() {
        return currMoveObj.getCapturedFigure();
    }

    @Override
    public boolean isCapture() {
        return currMoveObj.isCapture();
    }

    @Override
    public byte getFigureType() {
        return currMoveObj.getFigureType();
    }

    @Override
    public int getFromIndex() {
        return currMoveObj.getFromIndex();
    }

    @Override
    public int getToIndex() {
        return currMoveObj.getToIndex();
    }

    @Override
    public String toStr() {
        return currMoveObj.toStr();
    }

    @Override
    public boolean isEnPassant() {
        return currMoveObj.isEnPassant();
    }

    @Override
    public boolean isCastling() {
        return currMoveObj.isCastling();
    }

    @Override
    public boolean isPromotion() {
        return currMoveObj.isPromotion();
    }

    @Override
    public Figure getPromotedFigure() {
        return currMoveObj.getPromotedFigure();
    }

    @Override
    public int getMoveInt() {
        return currMoveObj.getMoveInt();
    }

    @Override
    public int getEnPassantCapturePos() {
        return currMoveObj.getEnPassantCapturePos();
    }

    @Override
    public byte getPromotedFigureByte() {
        return currMoveObj.getPromotedFigureByte();
    }

    @Override
    public byte getCastlingType() {
        return currMoveObj.getCastlingType();
    }

    @Override
    public void move(BoardRepresentation board) {
        board.domove(currMoveObj);
    }

    @Override
    public void undoMove(BoardRepresentation board) {
        board.undo(currMoveObj);
    }

    @Override
    public int getOrder() {
        return orderOfCurrentMove;
    }

    @Override
    public void next() {
        currMove = movePicker.next();
        orderOfCurrentMove = movePicker.getOrder();
        currMoveObj.fromLongEncoded(currMove);
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
