package org.mattlang.jc.moves;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.CheckChecker;
import org.mattlang.jc.engine.search.SearchThreadContext;
import org.mattlang.jc.engine.sorting.OrderCalculator;
import org.mattlang.jc.movegenerator.MoveGenerator;
import org.mattlang.jc.movegenerator.PseudoLegalMoveGenerator;

/**
 * Encapsulates all relevant objects to prepare iteration over moves on the board.
 */
public class RegularMoveIterationPreparer implements MoveIterationPreparer {

    private MoveListImpl moveList = new MoveListImpl();

    private CheckChecker checkChecker = Factory.getDefaults().checkChecker.instance();
    private MoveGenerator generator = new PseudoLegalMoveGenerator();

    private OrderCalculator orderCalculator;
    private BoardRepresentation board;

    public void prepare(SearchThreadContext stc, MoveGenerator.GenMode mode, BoardRepresentation board, Color color,
            int ply, int hashMove, int parentMove) {
        moveList.reset(color);
        this.board = board;
        orderCalculator = stc.getOrderCalculator(); // maybe refactor this..

        generator.generate(mode, orderCalculator, board, color, moveList);
        orderCalculator.prepareOrder(color, hashMove, parentMove, ply, board, 0);
        moveList.scoreMoves(orderCalculator);

    }

    public void prepare(SearchThreadContext stc, MoveGenerator.GenMode mode, BoardRepresentation board, Color color,
                        int ply, int hashMove, int parentMove, int captureMargin) {
        moveList.reset(color);
        this.board = board;
        orderCalculator = stc.getOrderCalculator(); // maybe refactor this..

        generator.generate(mode, orderCalculator, board, color, moveList);
        orderCalculator.prepareOrder(color, hashMove, parentMove, ply, board, captureMargin);
        moveList.scoreMoves(orderCalculator);

    }

    public MoveBoardIterator iterateMoves() {
        return moveList.iterateMoves(board, checkChecker);
    }
}
