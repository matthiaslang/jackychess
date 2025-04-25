package org.mattlang.jc.engine.evaluation;

import static org.mattlang.tuning.data.epdparser.EpdParser.parseEPDTests;

import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mattlang.jc.TestPosition;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.chesstests.EigenmannRapidEngineChess;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;
import org.mattlang.jc.tools.FenFlip;
import org.mattlang.tuning.data.epdparser.EpdWithOpCodes;

@RunWith(Parameterized.class)
public class SymmetryOfEvaluationTest {

    private final TestPosition testPosition;

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Iterable<TestPosition> getEPDTests() {
        List<EpdWithOpCodes> epds = parseEPDTests(EigenmannRapidEngineChess.EIGENMANN_RAPID);
        return epds.stream()
                .map(TestPosition::new)
                .collect(Collectors.toList());

    }

    public SymmetryOfEvaluationTest(TestPosition testPosition) {
        this.testPosition = testPosition;
    }

    @Before
    public void adjustEval() {
        // override tempo parameter which is the only one breaking the symmetry:
        //        System.setProperty("tempoMg", "0");
        //        System.setProperty("tempoEg", "0");
    }

    @After
    public void resetEval() {
        // override tempo parameter which is the only one breaking the symmetry:
        //        System.setProperty("opt.tempoMg", "1");
        //        System.setProperty("opt.tempoEg", "1");
    }

    /**
     * Tests Symmetry of Evaluation: Evaluation for white must be equal to evaluation for black for a flipped board.
     */
    @Test
    public void testSymmetry() {

        //        System.setProperty("tempoMg", "0");
        //        System.setProperty("tempoEg", "0");

        ParameterizedEvaluation evaluation = new ParameterizedEvaluation();

        BoardRepresentation board = new BitBoard();
        FenFlip fenflip = new FenFlip();

        System.out.println(testPosition.getName() + ": " + testPosition.getFen());

        board.setFenPosition(testPosition.getFenPosition());
        System.out.println(board.toUniCodeStr());

        int scoreWhite = evaluation.eval(board, Color.WHITE);

        String flippedFen = fenflip.mirrorHorizontalFen(testPosition.getFenPosition());
        System.out.println("flippedFen=" + flippedFen);

        board.setFenPosition(flippedFen);
        System.out.println(board.toUniCodeStr());

        int scoreBlack = evaluation.eval(board, Color.BLACK);

        Assertions.assertThat(scoreWhite).isEqualTo(scoreBlack);

    }
}
