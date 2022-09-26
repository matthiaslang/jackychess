package org.mattlang.jc.moves;

import static org.mattlang.jc.moves.Stage.*;

import java.util.logging.Logger;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Figure;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.sorting.OrderCalculator;
import org.mattlang.jc.movegenerator.MoveGenerator;

public class StagedMoveCursor implements MoveCursor {

    private static final Logger LOGGER = Logger.getLogger(StagedMoveCursor.class.getSimpleName());

    private final StagedMoveListImpl movelist;

    public StagedMoveCursor(StagedMoveListImpl movelist) {
        this.movelist = movelist;
    }

    // testwise simpler stage config (as someting with the good/bad capture handling does not work..)
    //
    private static Stage[] STAGES_NORMAL =
            new Stage[] { HASH, ALL };
    private static Stage[] STAGES_QUIESCENCE =
            new Stage[] { PROMOTIONS, ALL_CAPTURES };

    //    private static Stage[] STAGES_NORMAL =
    //            new Stage[] { HASH, PV, KILLER1, KILLER2, COUNTER_MOVE, ALL };
    //    private static Stage[] STAGES_QUIESCENCE =
    //            new Stage[] { HASH, PV, ALL_CAPTURES, PROMOTIONS };

    //    private static Stage[] STAGES_NORMAL =
    //            new Stage[] { HASH, PV, ALL_CAPTURES, KILLER1, KILLER2, /*COUNTER_MOVE,*/ QUIET };
    //    private static Stage[] STAGES_QUIESCENCE =
    //            new Stage[] { HASH, PV, ALL_CAPTURES, PROMOTIONS };

    //    private static Stage[] STAGES_NORMAL =
    //            new Stage[] { HASH, PV, GOOD_CAPTURES, KILLER1, KILLER2, QUIET, BAD_CAPTURES };
    //    private static Stage[] STAGES_QUIESCENCE =
    //            new Stage[] { HASH, PV, GOOD_CAPTURES, PROMOTIONS, BAD_CAPTURES };

    private Stage[] stages = STAGES_NORMAL;

    private int stageIndex = -1;
    //    private StageStatus stageStatus = StageStatus.NONE;

    private int currMove;
    private int orderOfCurrentMove;

    private MoveImpl currMoveObj = new MoveImpl("a1a2");

    private StageData currStageData = new StageData();

    private OrderCalculator orderCalculator;

    public void init(MoveGenerator.GenMode mode, OrderCalculator orderCalculator) {
        stageIndex = -1;
        switch (mode) {
        case NORMAL:
            stages = STAGES_NORMAL;
            break;
        case QUIESCENCE:
            stages = STAGES_QUIESCENCE;
            break;
        }
        if (this.orderCalculator == null) {
            this.orderCalculator = new OrderCalculator(orderCalculator);
        } else {
            this.orderCalculator.init(orderCalculator);
        }
        currStageData.reset(movelist.getBoard(), movelist.getSide(), orderCalculator);
    }

    @Override
    public void move(BoardRepresentation board) {
//        LOGGER.info("Do Move " + currMoveObj.toLongAlgebraic());
        // test code:
//        if (!board.isvalidmove(currMove)) {
//            throw new IllegalStateException("AAAHHH!");
        //        }

        board.domove(currMoveObj);
    }

    @Override
    public int getMoveInt() {
        return currMove;
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
    public CastlingMove getCastlingMove() {
        return currMoveObj.getCastlingMove();
    }

    @Override
    public int getOrder() {
        return orderOfCurrentMove;
    }

    @Override
    public void undoMove(BoardRepresentation board) {
        //        LOGGER.info("undo Move " + currMoveObj.toLongAlgebraic());
        board.undo(currMoveObj);

    }

    @Override
    public boolean isCapture() {
        return currMoveObj.isCapture();
    }

    @Override
    public boolean isPromotion() {
        return currMoveObj.isPromotion();
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
    public byte getCapturedFigure() {
        return currMoveObj.getCapturedFigure();
    }

    @Override
    public Figure getPromotedFigure() {
        return currMoveObj.getPromotedFigure();
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
    public boolean hasNext() {
        while (!currStageData.hasNext() && !isLastStage()) {
            nextStage();
        }

        // test code
        //        if (stageIndex >= 0) {
        //            LOGGER.info("hasnext(): result " + currStageData.hasNext() + " stage " + stages[stageIndex]);
//        } else {
//            LOGGER.info("hasnext(): result " + currStageData.hasNext() + " stage still -1!! ");
//        }
        return currStageData.hasNext();
    }

    private boolean isLastStage() {
        return stageIndex == stages.length - 1;
    }

    private void nextStage() {
        while (stageIndex + 1 < stages.length) {
            stageIndex++;
//            LOGGER.info("checking next stage " + stages[stageIndex]);
            switch (stages[stageIndex]) {
            case HASH:
                initHash();
                break;
            case KILLER1:
                initKiller1();
                break;
            case KILLER2:
                initKiller2();
                break;
            case COUNTER_MOVE:
                initCounterMove();
                break;
            case GOOD_CAPTURES:
                currStageData.initGoodCaptures();
                break;
            case ALL_CAPTURES:
                currStageData.initAllCaptures();
                break;
            case PROMOTIONS:
                currStageData.initPromotions();
                break;
            case BAD_CAPTURES:
                currStageData.initBadCaptures();
                break;
            case QUIET:
                currStageData.initQuiet();
                break;
            case ALL:
                currStageData.initAll();
                break;
            default:
            }

            // the next chosen stage is itself empty, so choose the next stage recursively
            if (currStageData.hasNext()) {
                return;
            }
        }
    }

    private int getKiller(int index) {
        if (orderCalculator.getKillerMoves() != null) {
            int[] killers = orderCalculator.getKillerMoves()
                    .getOrCreateKillerList(orderCalculator.getColor(), orderCalculator.getPly());
            if (killers.length > index) {
                return killers[index];
            }
        }
        return 0;
    }

    private void initKiller1() {
        int killer1 = getKiller(0);
        currStageData.prepareMoveIfValid(killer1);
    }

    private void initKiller2() {
        int killer2 = getKiller(1);
        currStageData.prepareMoveIfValid(killer2);
    }

    private void initCounterMove() {
        int counterMove = orderCalculator.getCounterMove();
        currStageData.prepareMoveIfValid(counterMove);
    }

    private void initHash() {
        int hashMove = orderCalculator.getHashMove();
        currStageData.prepareMoveIfValid(hashMove);
    }

    @Override
    public void next() {

        currMove = currStageData.next();
        orderOfCurrentMove = currStageData.getOrder();
        currMoveObj.fromLongEncoded(currMove);

        // test data:
//        LOGGER.info("next(): " + currMoveObj.toLongAlgebraic() + " from stage " + stages[stageIndex]);
//        if (currMove == 0){
//            LOGGER.severe("Error : next() has unitialized state!");
//        }

    }
}
