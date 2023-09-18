package org.mattlang.jc.perftests;

import org.junit.Test;

/**
 * PerfTests
 * https://www.chessprogramming.org/Perft_Results
 */
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
