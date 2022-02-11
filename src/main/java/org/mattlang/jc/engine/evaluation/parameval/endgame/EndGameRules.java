package org.mattlang.jc.engine.evaluation.parameval.endgame;

import static org.mattlang.jc.engine.evaluation.parameval.MaterialCorrectionRule.rule;

import org.mattlang.jc.engine.evaluation.parameval.Material;
import org.mattlang.jc.engine.evaluation.parameval.MaterialCorrectionRule;

import lombok.Getter;

@Getter
public enum EndGameRules {

    KQ_VS_K(rule("KQ Vs K", "Q ", "0 0P"),
            new KxKEndgameFunction()),

    KR_VS_K(rule("KR Vs K", "R ", "0 0P"),
            new KxKEndgameFunction()),

    KRR_VS_K(rule("KRR Vs K", "RR ", "0 0P"),
            new KxKEndgameFunction());

    private MaterialCorrectionRule materialRule;
    private EndgameFunction endgameFunction;

    EndGameRules(MaterialCorrectionRule materialRule,
            EndgameFunction endgameFunction) {
        this.materialRule = materialRule;
        this.endgameFunction = endgameFunction;
    }

    public static EndGameRules findRule(Material matStronger, Material matWeaker) {
        for (EndGameRules rule : EndGameRules.values()) {
            if (rule.getMaterialRule().matches(matStronger, matWeaker)) {
                return rule;
            }
        }
        return null;
    }
}
