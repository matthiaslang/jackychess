package org.mattlang.jc.moves;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.CheckChecker;
import org.mattlang.jc.engine.search.SearchThreadContext;
import org.mattlang.jc.engine.sorting.OrderCalculator;
import org.mattlang.jc.movegenerator.MoveGenerator;

/**
 * Encapsulates all relevant objects to prepare iteration over moves on the board.
 */
public class MoveIterationPreparer {

    private MoveListImpl moveList = new MoveListImpl();

    private CheckChecker checkChecker = Factory.getDefaults().checkChecker.instance();
    private MoveGenerator generator = Factory.getDefaults().legalMoveGenerator.create();

    private OrderCalculator orderCalculator;
    private BoardRepresentation board;

    public void prepare(SearchThreadContext stc, MoveGenerator.GenMode mode, BoardRepresentation board, Color color,
            int ply, int hashMove, int parentMove) {
        moveList.reset();
        this.board = board;
        orderCalculator = stc.getOrderCalculator(); // maybe refactor this..

        generator.generate(mode, orderCalculator, board, color, moveList);
        orderCalculator.prepareOrder(color, hashMove, parentMove, ply, board);
        moveList.sort(orderCalculator);

    }

    public MoveBoardIterator iterateMoves() {
        return moveList.iterateMoves(board, checkChecker);
    }
}
