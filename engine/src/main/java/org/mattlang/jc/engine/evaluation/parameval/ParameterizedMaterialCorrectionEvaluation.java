package org.mattlang.jc.engine.evaluation.parameval;

import java.util.List;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.material.Material;

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

    public int correct(BoardRepresentation board, int result) {

        Material currMaterial = board.getMaterial();
        if (result > 0) {
            // white is stronger
            matStronger.setMaterial(currMaterial.getWhiteMat());
            matWeaker.setMaterial(currMaterial.getBlackAsWhitePart());
        } else {
            // black is stronger
            matStronger.setMaterial(currMaterial.getBlackAsWhitePart());
            matWeaker.setMaterial(currMaterial.getWhiteMat());
        }

        for (MaterialCorrectionRule rule : rules) {
            if (rule.matches(matStronger, matWeaker)) {
                result = rule.adjust(result);
                break;
            }
        }

        return result;
    }
}
