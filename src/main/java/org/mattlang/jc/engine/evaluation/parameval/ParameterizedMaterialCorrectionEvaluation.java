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

    public ParameterizedMaterialCorrectionEvaluation(EvalConfig config) {
        rules = config.parseMaterialRules();
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

        Material matWeaker = Material.fromBoard(bb, weaker);
        Material matStronger = Material.fromBoard(bb, stronger);

        for (MaterialCorrectionRule rule : rules) {
            if (rule.matches(matStronger, matWeaker)) {
                result = rule.adjust(result);
                break;
            }
        }
        return result;
    }
}
