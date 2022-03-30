package org.mattlang.jc.engine.evaluation.parameval.endgame;

import static org.mattlang.jc.engine.evaluation.parameval.MaterialCorrectionRule.rule;

import java.util.Objects;

import org.mattlang.jc.engine.evaluation.parameval.MaterialCorrectionRule;
import org.mattlang.jc.material.Material;

import lombok.Getter;

@Getter
public enum EndGameRules {

    /** King, Queen (and more) against blank King. */
    KQ_VS_K(rule("KQ* Vs K", "Q* ", " "),
            new KxKEndgameFunction()),

    /** King, Rook (and more) against blank King. */
    KR_VS_K(rule("KR* Vs K", "R* ", " "),
            new KxKEndgameFunction()),

    /** King, two Bishops (and more) against blank King. */
    KRB_VS_K(rule("KBB* Vs K", "BB* ", " "),
            new KxKEndgameFunction());

    private MaterialCorrectionRule materialRule;
    private EndgameFunction endgameFunction;

    EndGameRules(MaterialCorrectionRule materialRule,
            EndgameFunction endgameFunction) {
        this.materialRule = Objects.requireNonNull(materialRule);
        this.endgameFunction = Objects.requireNonNull(endgameFunction);
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
