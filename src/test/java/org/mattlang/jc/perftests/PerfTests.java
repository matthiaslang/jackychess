package org.mattlang.jc.perftests;

import org.junit.Test;
import org.mattlang.jc.movegenerator.MoveGenerator;
import org.mattlang.jc.movegenerator.PseudoLegalMoveGenerator;

/**
 * PerfTests
 * https://www.chessprogramming.org/Perft_Results
 */
public class PerfTests {

    @Test
    public void initialPositionPerformanceLegalMoves() {
        MoveGenerator generator = new PseudoLegalMoveGenerator();

        Perft perft = new Perft();
        perft.perftInitialPosition(generator);

    }


    @Test
    public void position2() {

        PseudoLegalMoveGenerator generator = new PseudoLegalMoveGenerator();
        Perft perft = new Perft();

        perft.position2(generator);
    }


    @Test
    public void position3() {

        MoveGenerator generator = new PseudoLegalMoveGenerator();
        Perft perft = new Perft();
        perft.position3(generator);
    }

    @Test
    public void position4() {

        PseudoLegalMoveGenerator generator = new PseudoLegalMoveGenerator();
        Perft perft = new Perft();
        perft.position4(generator);

    }

}
