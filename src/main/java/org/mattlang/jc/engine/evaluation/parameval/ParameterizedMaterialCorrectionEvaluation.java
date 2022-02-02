package org.mattlang.jc.engine.evaluation.parameval;

import static org.mattlang.jc.board.bitboard.BitChessBoard.nBlack;
import static org.mattlang.jc.board.bitboard.BitChessBoard.nWhite;

import java.util.List;

import org.mattlang.jc.board.bitboard.BitChessBoard;

/**
 * Paremeterized Low Material Correction.
 */
public class ParameterizedMaterialCorrectionEvaluation {

    private final List<MaterialCorrectionRule> rules;

    private Material matWeaker = new Material();
    private Material matStronger = new Material();

    public ParameterizedMaterialCorrectionEvaluation(EvalConfig config) {
        rules = config.parseMaterialRules();
    }

    public ParameterizedMaterialCorrectionEvaluation(List<MaterialCorrectionRule> rules) {
        this.rules = rules;
    }

    public int correct(BitChessBoard bb, int result) {

        int stronger;
        int weaker;
        if (result > 0) {
            stronger = nWhite;
            weaker = nBlack;
        } else {
            stronger = nBlack;
            weaker = nWhite;
        }

        Material.fromBoard(matWeaker, bb, weaker);
        Material.fromBoard(matStronger, bb, stronger);

        for (MaterialCorrectionRule rule : rules) {
            if (rule.matches(matStronger, matWeaker)) {
                result = rule.adjust(result);
                break;
            }
        }
        return result;
    }
}
