package org.mattlang.jc.engine.evaluation;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mattlang.jc.EvalParameterSet;
import org.mattlang.jc.Factory;
import org.mattlang.jc.TestPosition;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.chessTests.EigenmannRapidEngineChessIT;
import org.mattlang.jc.chessTests.EpdParsing;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;
import org.mattlang.jc.util.FenFlip;

@RunWith(Parameterized.class)
public class SymmetryOfEvaluationTest {

    private final TestPosition testPosition;

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Iterable<TestPosition> getEPDTests() {
        List<TestPosition> positions = EpdParsing.convertTests(EigenmannRapidEngineChessIT.eret);
        return positions;
    }

    public SymmetryOfEvaluationTest(TestPosition testPosition) {
        this.testPosition = testPosition;
    }

    /**
     * Tests Symmetry of Evaluation: Evaluation for white must be equal to evaluation for black for a flipped board.
     * currently fails for 3 tests, (Blockage of knight blocking queen pawns), but that seems to be a test issue
     * because of wrong flipping the board. We need a mirroring, to have same queenside/kingside behaviour.
     *
     */
    @Test
    public void testSymmetry() {
        Factory.getDefaults().getConfig().evaluateParamSet.setValue(EvalParameterSet.EXPERIMENTAL);

        ParameterizedEvaluation evaluation = new ParameterizedEvaluation();

        BoardRepresentation board = new BitBoard();
        FenFlip fenflip = new FenFlip();

        System.out.println(testPosition.getName() + ": " + testPosition.getFen());

        board.setFenPosition(testPosition.getFenPosition());
        System.out.println(board.toUniCodeStr());


        int scoreWhite = evaluation.eval(board, Color.WHITE);

        String flippedFen = fenflip.flipFen(testPosition.getFenPosition());
        System.out.println("flippedFen=" + flippedFen);

        board.setFenPosition(flippedFen);
        System.out.println(board.toUniCodeStr());

        int scoreBlack = evaluation.eval(board, Color.BLACK);

        Assertions.assertThat(scoreWhite).isEqualTo(scoreBlack);

    }
}
