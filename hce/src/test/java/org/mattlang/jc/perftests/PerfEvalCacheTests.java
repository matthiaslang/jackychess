package org.mattlang.jc.perftests;

import static org.mattlang.jc.perft.SimplePerftIteratorSupplier.SIMPLE_PERFT_ITERATOR_SUPPLIER;

import org.assertj.core.api.Assertions;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mattlang.SlowTests;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;
import org.mattlang.jc.perft.Perft;
import org.mattlang.jc.perft.PerftConsumer;

/**
 * PerfTests to evaluate each position with and without cache to compare results.
 *
 * Currently we have issues with the eval cache since it is not really synchronized and there may happen collisions
 * afer a few million entries....
 * So this test will fail after some million cache hits... therefore it is not activated in pipeline
 */
@Category(SlowTests.class)
@Ignore
public class PerfEvalCacheTests {

    private final ParameterizedEvaluation evaluation = new ParameterizedEvaluation();
    private final ParameterizedEvaluation evaluationCacheDisabled = new ParameterizedEvaluation();

    {
        evaluationCacheDisabled.setCaching(false);
        evaluation.setCaching(true);
    }

    private boolean debug = false;
    private PerftConsumer perftEvaluator = new PerftConsumer() {

        @Override
        public void accept(BoardRepresentation board, Color color, int depth, MoveCursor cursor) {
            //            System.out.println("zobrist: " + board.getZobristHash()+ " pawn zobrist: " + board.getPawnZobristHash());
            //            System.out.println("Move:" + cursor.toStr() + " capture" + cursor.isCapture() + "; " + cursor.getCapturedFigure());

            int evalUsingCache = evaluation.eval(board, color);
            int evalUsingNoCache = evaluationCacheDisabled.eval(board, color);
            if (evalUsingCache != evalUsingNoCache) {
                System.out.println(
                        "Move:" + cursor.toStr() + "capture" + cursor.isCapture() + "; " + cursor.getCapturedFigure());
                board.println();
            }

            Assertions.assertThat(evalUsingCache).isEqualTo(evalUsingNoCache);
        }
    };

    @Test
    public void initialPositionPerformanceLegalMoves() {

        BoardRepresentation board = new BitBoard();
        board.setStartPosition();

        Perft perft = new Perft(SIMPLE_PERFT_ITERATOR_SUPPLIER);
        perft.setVisitor(perftEvaluator);

        perft.perftInitialPosition();

    }

    @Test
    public void position2() {
        Perft perft = new Perft(SIMPLE_PERFT_ITERATOR_SUPPLIER);
        perft.setVisitor(perftEvaluator);

        perft.position2();

    }

    @Test
    public void position3() {
        Perft perft = new Perft(SIMPLE_PERFT_ITERATOR_SUPPLIER);
        perft.setVisitor(perftEvaluator);

        perft.position3();
    }

    @Test
    public void position4() {
        Perft perft = new Perft(SIMPLE_PERFT_ITERATOR_SUPPLIER);
        perft.setVisitor(perftEvaluator);

        perft.position4();
    }

}
