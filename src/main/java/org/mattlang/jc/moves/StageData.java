package org.mattlang.jc.moves;

import static org.mattlang.jc.moves.Stage.GOOD_CAPTURES;

import java.util.function.Predicate;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.sorting.OrderCalculator;
import org.mattlang.jc.movegenerator.BBMoveGeneratorImpl2;

public class StageData implements Predicate<MoveCursor> {

    private static final BBMoveGeneratorImpl2 stagedGenerator = new BBMoveGeneratorImpl2();

    private MoveListImpl movesOfStage = new MoveListImpl();
    private MoveListImpl captures = new MoveListImpl();
    private boolean capturesInitialized = false;

    private FilteredMoveCursor cursor = new FilteredMoveCursor();

    private int nextFilteredMove;

    private int filteredMoveAppendIndex = 0;
    private int[] filteredMoves = new int[5];

    private BoardRepresentation board;
    private Color side;
    private OrderCalculator orderCalculator;

    private Stage currStage;

    public StageData() {
        initEmpty();
    }

    public int next() {
        cursor.next();
        return cursor.getMoveInt();
    }

    public int getOrder() {
        return cursor.getOrder();
    }

    public boolean hasNext() {
        return cursor.hasNext();
    }

    public void prepareMoveIfValid(int aMove) {
        if (aMove != 0 && board.isvalidmove(aMove) && !isFiltered(aMove)) {
            initMove(aMove);
            addFilteredMoveForNextStage(aMove);
        }
    }

    public void initMove(int aMove) {
        currStage = null;
        movesOfStage.reset();
        movesOfStage.addMove(aMove);
        prepareStagedNextFilteredMove();
        cursor.init(movesOfStage.iterate(), this);
    }

    public void initEmpty() {
        currStage = null;
        movesOfStage.reset();
        prepareStagedNextFilteredMove();
        cursor.init(movesOfStage.iterate(), this);
    }

    public void initQuiet() {
        currStage = null;
        movesOfStage.reset();
        prepareStagedNextFilteredMove();
        stagedGenerator.generate(board, side, movesOfStage,
                BBMoveGeneratorImpl2.GenTypes.QUIET);
        movesOfStage.sort(orderCalculator);
        cursor.init(movesOfStage.iterate(), this);
    }

    public void initPromotions() {
        currStage = null;
        movesOfStage.reset();
        prepareStagedNextFilteredMove();
        stagedGenerator.genPawnMoves(board.getBoard(), movesOfStage, side, true);
        movesOfStage.sort(orderCalculator);
        cursor.init(movesOfStage.iterate(), this);
    }

    public void initAll() {
        currStage = null;
        movesOfStage.reset();
        prepareStagedNextFilteredMove();
        stagedGenerator.generate(board, side, movesOfStage,
                BBMoveGeneratorImpl2.GenTypes.ALL);
        movesOfStage.sort(orderCalculator);
        cursor.init(movesOfStage.iterate(), this);
    }

    public void initAllCaptures() {
        currStage = null;
        movesOfStage.reset();
        prepareStagedNextFilteredMove();
        prepareCaptures();
        cursor.init(captures.iterate(), this);
    }

    public void initGoodCaptures() {
        currStage = GOOD_CAPTURES;
        movesOfStage.reset();
        prepareStagedNextFilteredMove();
        prepareCaptures();
        cursor.init(captures.iterate(), this);
    }

    private void prepareCaptures() {
        if (!capturesInitialized) {
            stagedGenerator.generate(board, side, captures,
                    BBMoveGeneratorImpl2.GenTypes.CAPTURES);
            captures.sort(orderCalculator);
            capturesInitialized = true;
        }
    }

    public void initBadCaptures() {
        currStage = Stage.BAD_CAPTURES;
        movesOfStage.reset();
        prepareStagedNextFilteredMove();

        prepareCaptures();
        cursor.init(captures.iterate(), this);
    }

    @Override
    public boolean test(MoveCursor cursor) {
        return !isFiltered(cursor.getMoveInt()) && matchesCaptureStage(cursor.getOrder());
    }

    private boolean matchesCaptureStage(int order) {
        if (currStage == null) {
            return true;
        }
        if (currStage == GOOD_CAPTURES) {
            return OrderCalculator.isGoodCapture(order);
        } else if (currStage == Stage.BAD_CAPTURES) {
            return !OrderCalculator.isGoodCapture(order);
        }
        return true;
    }

    private boolean isFiltered(int moveInt) {
        if (filteredMoveAppendIndex == 0) {
            return false;
        }
        for (int i = 0; i < filteredMoveAppendIndex; i++) {
            if (filteredMoves[i] == moveInt) {
                return true;
            }
        }
        return false;
    }

    public void reset(BoardRepresentation board, Color side, OrderCalculator orderCalculator) {
        this.board = board;
        this.side = side;
        this.orderCalculator = orderCalculator;
        nextFilteredMove = 0;
        filteredMoves[0] = 0;
        filteredMoveAppendIndex = 0;
        currStage = null;
        initEmpty();
        captures.reset();
        capturesInitialized = false;
    }

    public void addFilteredMoveForNextStage(int nextFilteredMove) {
        this.nextFilteredMove = nextFilteredMove;
    }

    private void prepareStagedNextFilteredMove() {
        if (nextFilteredMove != 0) {
            filteredMoves[filteredMoveAppendIndex] = nextFilteredMove;
            filteredMoveAppendIndex++;
            nextFilteredMove = 0;
        }
    }

}
