package org.mattlang.jc.moves;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.search.SearchThreadContext;
import org.mattlang.jc.engine.sorting.MovePicker;
import org.mattlang.jc.engine.sorting.OrderCalculator;
import org.mattlang.jc.movegenerator.GenMode;
import org.mattlang.jc.movegenerator.PseudoLegalMoveGenerator;

/**
 * Old Variant to encapsulates all preparation for move iteration in one stage.
 * Is obsolete due to the new staged move generation.
 */
public class RegularMoveIterationPreparer {

    private MoveList moveList = new MoveList();

    private PseudoLegalMoveGenerator generator = new PseudoLegalMoveGenerator();

    private MovePicker movePicker = new MovePicker();

    private MoveBoardIterator moveBoardIterator = new MoveBoardIterator();

    private OrderCalculator orderCalculator;
    private BoardRepresentation board;

    public void prepare(SearchThreadContext stc, GenMode mode, BoardRepresentation board, Color color,
            int ply, int hashMove, int parentMove) {
        moveList.reset(color);
        this.board = board;
        orderCalculator = stc.getOrderCalculator(); // maybe refactor this..

        generator.generate(mode, board, color, moveList);
        orderCalculator.prepareOrder(color, hashMove, parentMove, ply, board, 0);
        orderCalculator.scoreMoves(moveList);

    }

    public void prepare(SearchThreadContext stc, GenMode mode, BoardRepresentation board, Color color,
            int ply, int hashMove, int parentMove, int captureMargin) {
        moveList.reset(color);
        this.board = board;
        orderCalculator = stc.getOrderCalculator(); // maybe refactor this..

        generator.generate(mode, board, color, moveList);
        orderCalculator.prepareOrder(color, hashMove, parentMove, ply, board, captureMargin);
        orderCalculator.scoreMoves(moveList);

    }

    public MoveBoardIterator iterateMoves() {
        movePicker.init(moveList, 0);
        moveBoardIterator.init(movePicker, board);
        return moveBoardIterator;
    }
}
