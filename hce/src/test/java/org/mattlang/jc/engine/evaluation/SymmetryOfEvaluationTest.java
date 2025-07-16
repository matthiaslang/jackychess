package org.mattlang.jc.engine.evaluation;

import static org.mattlang.tuning.data.epdparser.EpdParser.parseEPDTests;

import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedClass;
import org.junit.jupiter.params.provider.MethodSource;
import org.mattlang.jc.TestPosition;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.chesstests.EigenmannRapidEngineChess;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;
import org.mattlang.jc.tools.FenFlip;
import org.mattlang.tuning.data.epdparser.EpdWithOpCodes;

@ParameterizedClass
@MethodSource("getEPDTests")
public class SymmetryOfEvaluationTest {

    @org.junit.jupiter.params.Parameter(0)
    private TestPosition testPosition;

    public static Iterable<TestPosition> getEPDTests() {
        List<EpdWithOpCodes> epds = parseEPDTests(EigenmannRapidEngineChess.EIGENMANN_RAPID);
        return epds.stream()
                .map(TestPosition::new)
                .collect(Collectors.toList());

    }

    /**
     * Tests Symmetry of Evaluation: Evaluation for white must be equal to evaluation for black for a flipped board.
     */

    @Test
    public void testSymmetry() {

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
