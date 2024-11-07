package org.mattlang.jc.perftests;

import static org.mattlang.jc.perftests.PerfTests.DEFAULT_SUPPLIER;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mattlang.SlowTests;
import org.mattlang.jc.Factory;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.engine.Configurator;
import org.mattlang.jc.engine.EvaluateFunction;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.perft.Perft;
import org.mattlang.jc.perft.PerftConsumer;

/**
 * PerfTests to evaluate each position. Used to measure eval optimizations.
 */
@Category(SlowTests.class)
public class PerfEvalTests {

    private final EvaluateFunction evaluation = Configurator.createConfiguredEvaluateFunction();

    private boolean debug = false;
    private PerftConsumer perftEvaluator = new PerftConsumer() {

        @Override
        public void accept(BoardRepresentation board, Color color, int depth, MoveCursor cursor) {
            evaluation.eval(board, color);
        }
    };

    @Test
    public void initialPositionPerformanceLegalMoves() {
        Factory.setDefaults(Factory.createStable());
        BoardRepresentation board = new BitBoard();
        board.setStartPosition();

        Perft perft = new Perft(DEFAULT_SUPPLIER);
        perft.setVisitor(perftEvaluator);

        perft.perftInitialPosition();

    }

    @Test
    public void position2() {
        Perft perft = new Perft(DEFAULT_SUPPLIER);
        perft.setVisitor(perftEvaluator);

        perft.position2();

    }

    @Test
    public void position3() {
        Perft perft = new Perft(DEFAULT_SUPPLIER);
        perft.setVisitor(perftEvaluator);

        perft.position3();
    }

    @Test
    public void position4() {
        Perft perft = new Perft(DEFAULT_SUPPLIER);
        perft.setVisitor(perftEvaluator);

        perft.position4();
    }

}
