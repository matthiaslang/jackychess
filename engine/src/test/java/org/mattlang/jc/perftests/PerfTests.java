package org.mattlang.jc.perftests;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mattlang.SlowTests;

/**
 * PerfTests
 * https://www.chessprogramming.org/Perft_Results
 */
@Category(SlowTests.class)
public class PerfTests {

    @Test
    public void initialPositionPerformanceLegalMoves() {
        Perft perft = new Perft();
        perft.perftInitialPosition();

    }


    @Test
    public void position2() {

        Perft perft = new Perft();
        perft.position2();
    }


    @Test
    public void position3() {

        Perft perft = new Perft();
        perft.position3();
    }

    @Test
    public void position4() {

        Perft perft = new Perft();
        perft.position4();

    }

}
