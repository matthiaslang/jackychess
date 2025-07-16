package org.mattlang.jc.perft;

import static org.mattlang.jc.perft.SimplePerftIteratorSupplier.SIMPLE_PERFT_ITERATOR_SUPPLIER;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * A Perft Test.
 * It is tested with the a single staged simple move generation.
 * https://www.chessprogramming.org/Perft_Results
 */
@Tag("SlowTests")
public class SimplePerfTests {

    @Test
    public void initialPositionPerformanceLegalMoves() {
        Perft perft = new Perft(SIMPLE_PERFT_ITERATOR_SUPPLIER);
        perft.perftInitialPosition();

    }

    @Test
    public void position2() {

        Perft perft = new Perft(SIMPLE_PERFT_ITERATOR_SUPPLIER);
        perft.position2();
    }

    @Test
    public void position3() {

        Perft perft = new Perft(SIMPLE_PERFT_ITERATOR_SUPPLIER);
        perft.position3();
    }

    @Test
    public void position4() {

        Perft perft = new Perft(SIMPLE_PERFT_ITERATOR_SUPPLIER);
        perft.position4();

    }

}
