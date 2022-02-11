package org.mattlang.jc.engine.evaluation.parameval;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mattlang.jc.engine.evaluation.parameval.MaterialCorrectionRule.parse;

import java.util.List;

import org.junit.Test;
import org.mattlang.jc.board.bitboard.BitBoard;

public class MaterialCorrectionRuleTest {

    @Test
    public void parseRules() {

        MaterialCorrectionRule r = parse("", "R 0P vs B -> Reduce half");
        assertThat(r.getStronger().getMaterial().getPawns()).isEqualTo(0);
        assertThat(r.getStronger().getMaterial().getPieceMat()).isEqualTo(1000);

        assertThat(r.getWeaker().isPawnsUnspecific()).isTrue();
        assertThat(r.getWeaker().getMaterial().getPieceMat()).isEqualTo(100);

        MaterialCorrectionRule r2 = parse("", "R 0P vs N -> Reduce half");
        MaterialCorrectionRule r3 = parse("", "RB 0P vs R -> Reduce half");
        MaterialCorrectionRule r4 = parse("", "RN 0P vs R -> Reduce half");
        MaterialCorrectionRule r5 = parse("", "B 0P vs X -> Reduce all");

        List<MaterialCorrectionRule> rules = asList(r, r2, r3, r4, r5);

        BitBoard board = new BitBoard();
        board.setFenPosition("position fen k7/4q3/8/8/4K3/8/8/B7 w - - 2 17 ");

        ParameterizedMaterialCorrectionEvaluation matCorr = new ParameterizedMaterialCorrectionEvaluation(
                rules);

        assertThat(matCorr.correct(board, 500)).isEqualTo(0);

        board = new BitBoard();
        board.setFenPosition("position fen k7/4pn2/8/8/4K3/8/8/R7 w - - 2 17 ");

        assertThat(matCorr.correct(board, 500)).isEqualTo(250);

        board = new BitBoard();
        board.setFenPosition("position fen k7/4pr2/8/8/4K3/8/8/RB6 w - - 2 17 ");

        assertThat(matCorr.correct(board, 500)).isEqualTo(250);

        board = new BitBoard();
        board.setFenPosition("position fen k7/4pr2/8/8/4K3/8/8/RBP5 w - - 2 17 ");

        assertThat(matCorr.correct(board, 500)).isEqualTo(500);
    }
}