package org.mattlang.jc.perftests;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mattlang.SlowTests;
import org.mattlang.jc.engine.search.SearchThreadContexts;
import org.mattlang.jc.movegenerator.GenMode;
import org.mattlang.jc.moves.StagedMoveIterationPreparer;
import org.mattlang.jc.perft.Perft;
import org.mattlang.jc.perft.PerftIteratorSupplier;

/**
 * The main Perft Test.
 * It is tested with the real staged move generation code used within the engine.
 * It uses the same structures, therefore also the scoring and sorting of moves.
 * This test is for this reason slower than e.g. the SimplePerftTest which does not take care for move sorting and which
 * uses only one stage.
 * https://www.chessprogramming.org/Perft_Results
 */
@Category(SlowTests.class)
public class PerfTests {

    public static final PerftIteratorSupplier DEFAULT_SUPPLIER = (depth, board, color) -> {
        StagedMoveIterationPreparer moveIterationPreparer =
                SearchThreadContexts.CONTEXTS.getContext(0).getMoveIterationPreparer(depth);
        moveIterationPreparer.prepare(SearchThreadContexts.CONTEXTS.getContext(0), GenMode.NORMAL,
                board, color, 0, 0, 0);
        return moveIterationPreparer.iterateMoves();
    };

    @Test
    public void initialPositionPerformanceLegalMoves() {
        Perft perft = new Perft(DEFAULT_SUPPLIER);
        perft.perftInitialPosition();

    }

    @Test
    public void position2() {

        Perft perft = new Perft(DEFAULT_SUPPLIER);
        perft.position2();
    }

    @Test
    public void position3() {

        Perft perft = new Perft(DEFAULT_SUPPLIER);
        perft.position3();
    }

    @Test
    public void position4() {

        Perft perft = new Perft(DEFAULT_SUPPLIER);
        perft.position4();

    }

}
