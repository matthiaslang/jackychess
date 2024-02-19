package org.mattlang.jc.moves;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.CheckChecker;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.search.SearchThreadContext;
import org.mattlang.jc.engine.sorting.OrderCalculator;
import org.mattlang.jc.movegenerator.GenMode;
import org.mattlang.jc.movegenerator.PseudoLegalMoveGenerator;

/**
 * Old Variant to encapsulates all preparation for move iteration in one stage.
 * Is obsolete due to the new staged move generation.
 */
public class RegularMoveIterationPreparer {

    private MoveList moveList = new MoveList();

    private CheckChecker checkChecker = Factory.getDefaults().checkChecker.instance();
    private PseudoLegalMoveGenerator generator = new PseudoLegalMoveGenerator();

    private OrderCalculator orderCalculator;
    private BoardRepresentation board;

    public void prepare(SearchThreadContext stc, GenMode mode, BoardRepresentation board, Color color,
            int ply, int hashMove, int parentMove) {
        moveList.reset(color);
        this.board = board;
        orderCalculator = stc.getOrderCalculator(); // maybe refactor this..

        generator.generate(mode, orderCalculator, board, color, moveList);
        orderCalculator.prepareOrder(color, hashMove, parentMove, ply, board, 0);
        moveList.scoreMoves(orderCalculator);

    }

    public void prepare(SearchThreadContext stc, GenMode mode, BoardRepresentation board, Color color,
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
