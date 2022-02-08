package org.mattlang.jc.moves;

import static org.mattlang.jc.moves.StagedMoveCursor.Stages.*;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Figure;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.movegenerator.BBMoveGeneratorImpl2;

public class StagedMoveCursor implements MoveCursor {

    private static final BBMoveGeneratorImpl2 stagedGenerator = new BBMoveGeneratorImpl2();

    private final StagedMoveListImpl movelist;

    public StagedMoveCursor(StagedMoveListImpl movelist) {
        this.movelist = movelist;
    }

    enum Stages {
        PV,
        HASH,
        KILLER,
        GOOD_CAPTURES,
        QUIET,
        BAD_CAPTURES,
    }

    enum StageStatus {
        NONE,
        ACTIVE,
        FINISHED
    }

    private Stages[] stages = new Stages[] { PV, HASH, GOOD_CAPTURES, KILLER, QUIET, BAD_CAPTURES };

    private int stageIndex = -1;
    private StageStatus stageStatus = StageStatus.NONE;

    private int currMove;
    private int orderOfCurrentMove;

    private MoveImpl currMoveObj = new MoveImpl("a1a2");

    private StageData currStageData = new StageData();

    class StageData {

        MoveListImpl movesOfStage = new MoveListImpl();
        int index = -1;

        public int next() {
            index++;
            return movesOfStage.get(index);
        }

        public int getOrder() {
            return movesOfStage.getOrder(index);
        }

        public boolean hasNext() {
            return index + 1 < movesOfStage.size();
        }

        public void initMove(int aMove) {
            movesOfStage.reset();
            index = -1;
            movesOfStage.addMove(aMove);
        }

        public void initSomeMoves(int[] someMoves) {
            movesOfStage.reset();
            index = -1;
            for (int aMove : someMoves) {
                movesOfStage.addMove(aMove);
            }
        }

        public void initQuiet() {
            movesOfStage.reset();
            index = -1;
            stagedGenerator.generate(movelist.getBoard(), movelist.getSide(), movesOfStage,
                    BBMoveGeneratorImpl2.GenTypes.QUIET);
        }

        public void initCaptures() {
            movesOfStage.reset();
            index = -1;
            stagedGenerator.generate(movelist.getBoard(), movelist.getSide(), movesOfStage,
                    BBMoveGeneratorImpl2.GenTypes.CAPTURES);
        }

    }

    @Override
    public void move(BoardRepresentation board) {
        board.domove(currMoveObj);
    }

    @Override
    public int getMoveInt() {
        return currMove;
    }

    @Override
    public int getOrder() {
        return orderOfCurrentMove;
    }

    @Override
    public void undoMove(BoardRepresentation board) {
        board.undo(currMoveObj);

    }

    @Override
    public boolean isCapture() {
        return currMoveObj.isCapture();
    }

    @Override
    public boolean isPawnPromotion() {
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
    public void remove() {
        throw new IllegalStateException("remove not supported!");
    }

    public boolean hasNext() {
        if (stageStatus != StageStatus.ACTIVE) {
            nextStage();
            return currStageData.hasNext();
        }
        if (stageStatus == StageStatus.ACTIVE) {
            if (currStageData.hasNext()) {
                return true;
            } else {
                nextStage();
                return currStageData.hasNext();
            }
        }
        return false;
    }

    private void nextStage() {
        while (stageIndex+1 < stages.length) {
            stageIndex++;
            switch (stages[stageIndex]) {
            case PV:
                initPV();
                break;
            case HASH:
                initHash();
                break;
            case KILLER:
                initKiller();
                break;
            case GOOD_CAPTURES:
                initCaptures();
                break;
            case BAD_CAPTURES:
                initBadCaptures();
            case QUIET:
                initQuiet();
                break;
            default:
            }
            // the next chosen stage is itself empty, so choose the next stage recursively
            if (stageStatus == StageStatus.ACTIVE) {
                return;
            }
        }
            stageStatus = StageStatus.FINISHED;

    }

    private void initBadCaptures() {
        stageStatus = StageStatus.FINISHED;
    }

    private void initKiller() {
        //        int[] killers = movelist.getGameContext()
        //                .getKillerMoves()
        //                .getPossibleKillers(movelist.getSide(), movelist.getOrderCalculator().getPly());
        //
        //        currStageData.initSomeMoves(killers);
        //        if (currStageData.hasNext()) {
        //            stageStatus = StageStatus.ACTIVE;
        //        } else {
        stageStatus = StageStatus.FINISHED;
        //        }
    }

    private void initQuiet() {
        currStageData.initQuiet();
        if (currStageData.hasNext()) {
            stageStatus = StageStatus.ACTIVE;
        } else {
            stageStatus = StageStatus.FINISHED;
        }
    }

    private void initCaptures() {
        currStageData.initCaptures();
        if (currStageData.hasNext()) {
            stageStatus = StageStatus.ACTIVE;
        } else {
            stageStatus = StageStatus.FINISHED;
        }
    }

    private void initHash() {
        if (movelist.getOrderCalculator().getHashMove() != 0) {
            if (stagedGenerator.isvalidmove(movelist.getOrderCalculator().getHashMove())) {
                currStageData.initMove(movelist.getOrderCalculator().getHashMove());
                stageStatus = StageStatus.ACTIVE;
                return;
            }
        }
        stageStatus = StageStatus.FINISHED;
    }

    private void initPV() {
        if (movelist.getOrderCalculator().getPvMove() != 0) {
            if (stagedGenerator.isvalidmove(movelist.getOrderCalculator().getPvMove())) {
                currStageData.initMove(movelist.getOrderCalculator().getPvMove());
                stageStatus = StageStatus.ACTIVE;
                return;
            }
        }
        stageStatus = StageStatus.FINISHED;
    }

    public void next() {
        if (stageStatus == StageStatus.NONE || stageStatus == StageStatus.FINISHED) {
            throw new IllegalStateException("!!!");
        } else {
//            nextStage();
            currMove = currStageData.next();
            orderOfCurrentMove = currStageData.getOrder();
            currMoveObj.fromLongEncoded(currMove);
            if (!currStageData.hasNext()) {
                stageStatus = StageStatus.FINISHED;
            }
        }
//        if (stageStatus == StageStatus.FINISHED) {
//            throw new IllegalArgumentException();
//        }
    }
}
