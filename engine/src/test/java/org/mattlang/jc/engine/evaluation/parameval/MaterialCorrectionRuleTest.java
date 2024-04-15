package org.mattlang.jc.engine.evaluation.parameval;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mattlang.jc.engine.evaluation.parameval.MaterialCorrectionRule.parse;
import static org.mattlang.jc.engine.evaluation.parameval.MaterialDescription.parseDescr;
import static org.mattlang.jc.material.Material.W_BISHOP_VAL;
import static org.mattlang.jc.material.Material.W_ROOK_VAL;

import java.util.List;

import org.junit.Test;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.engine.evaluation.parameval.endgame.EndGameRules;
import org.mattlang.jc.material.Material;

public class MaterialCorrectionRuleTest {

    Material onlyKing = matOf("position fen k7/8/8/8/K7/8/8/8 w - - 2 17 ");
    Material kingAndQueen = matOf("position fen k7/8/8/8/KQ6/8/8/8 w - - 2 17 ");
    Material kingAndRook = matOf("position fen k7/8/8/8/KR6/8/8/8 w - - 2 17 ");
    Material kingAndBishop = matOf("position fen k7/8/8/8/KB6/8/8/8 w - - 2 17 ");
    Material kingAndRookPawn = matOf("position fen k7/8/8/8/KRP5/8/8/8 w - - 2 17 ");
    Material kingPPP = matOf("position fen k7/8/8/8/KPPP4/8/8/8 w - - 2 17 ");

    @Test
    public void parseRules() {

        MaterialCorrectionRule r = parse("", "R  vs B* -> Reduce half");
        assertThat(r.getStronger().getMaterial().getPawnsMat()).isEqualTo(0);
        assertThat(r.getStronger().getMaterial().getPieceMat()).isEqualTo(W_ROOK_VAL);

        assertThat(r.getWeaker().getComparison()).isEqualTo(MaterialComparison.MORE_OR_EQUAL);
        assertThat(r.getWeaker().getMaterial().getPieceMat()).isEqualTo(W_BISHOP_VAL);

        MaterialCorrectionRule r2 = parse("", "R vs N* -> Reduce half");
        MaterialCorrectionRule r3 = parse("", "RB vs R* -> Reduce half");
        MaterialCorrectionRule r4 = parse("", "RN vs R* -> Reduce half");
        MaterialCorrectionRule r5 = parse("", "B vs X -> Reduce all");

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

    @Test
    public void parseEndGameRules() {
        EndGameRules[] rules = EndGameRules.values();

        for (EndGameRules rule : rules) {
            MaterialCorrectionRule matRule = rule.getMaterialRule();
            System.out.println(
                    matRule.getRuleName() + ": "
                            + matRule.getStronger().toString() + " vs "
                            + matRule.getWeaker().toString());
        }
    }

    @Test
    public void testMatComparisons() {

        assertThat(kingAndQueen.hasMoreWhiteMat(onlyKing)).isTrue();
        assertThat(kingAndQueen.hasMoreWhiteMat(kingAndRook)).isFalse();
        assertThat(kingAndQueen.hasMoreWhiteMat(kingAndQueen)).isFalse();

        assertThat(kingAndRookPawn.hasMoreWhiteMat(kingAndRook)).isTrue();
        assertThat(kingAndRookPawn.hasMoreWhiteMat(kingAndQueen)).isFalse();
        assertThat(kingAndQueen.hasMoreWhiteMat(kingAndRookPawn)).isFalse();

        assertThat(kingAndBishop.hasMoreWhiteMat(kingAndRookPawn)).isFalse();
        assertThat(kingAndBishop.hasMoreWhiteMat(kingPPP)).isFalse();
        assertThat(kingAndBishop.hasMoreWhiteMat(kingAndQueen)).isFalse();

        assertThat(kingPPP.hasMoreWhiteMat(kingAndBishop)).isFalse();

    }

    private Material matOf(String fen) {
        BitBoard board = new BitBoard();
        board.setFenPosition(fen);
        return board.getMaterial();
    }

    @Test
    public void testMaterialDescriptions() {
        assertThat(parseDescr("Q").matches(kingAndQueen)).isTrue();
        assertThat(parseDescr("R").matches(kingAndRook)).isTrue();
        assertThat(parseDescr("R+").matches(kingAndRookPawn)).isTrue();
        assertThat(parseDescr("R*").matches(kingAndRookPawn)).isTrue();
        assertThat(parseDescr("R*").matches(kingAndRook)).isTrue();
        assertThat(parseDescr("R+").matches(kingAndRook)).isFalse();
    }
}