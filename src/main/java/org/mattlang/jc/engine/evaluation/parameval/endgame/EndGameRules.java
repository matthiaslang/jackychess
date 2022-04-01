package org.mattlang.jc.engine.evaluation.parameval.endgame;

import static org.mattlang.jc.engine.evaluation.parameval.MaterialCorrectionRule.rule;

import java.util.Objects;

import org.mattlang.jc.engine.evaluation.parameval.MaterialCorrectionRule;
import org.mattlang.jc.material.Material;

import lombok.Getter;

/**
 * All endgame evaluation functions.
 * Note they must be ordered correctly, to select the correct rule by its descriptions.
 */
@Getter
public enum EndGameRules {

    /**
     * King, Knight,Knight vs King Pawn.
     */
    KNN_VS_KP(rule("KNN vs KP"), new KNNvsKPEndgameFunction()),

    /**
     * King, Queen vs King Rook.
     */
    KQ_VS_KR(rule("KQ vs KR"), new KQvsKREndgameFunction()),

    /**
     * King, Queen vs King Pawn.
     */
    KQ_VS_KP(rule("KQ vs KP"), new KQvsKPEndgameFunction()),

    /**
     * King, Rook vs King Bishop.
     */
    KR_VS_KP(rule("KR vs KP"), new KRvsKPEndgameFunction()),

    /**
     * King, Rook vs King Bishop.
     */
    KR_VS_KB(rule("KR vs KB"), new KRvsKBEndgameFunction()),

    /**
     * King, Rook vs King Knight.
     */
    KR_VS_KN(rule("KR vs KN"), new KRvsKNEndgameFunction()),

    /**
     * King, Queen (and more) against blank King.
     */
    KQ_VS_K(rule("KQ* vs K"), new KxKEndgameFunction()),

    /**
     * King, Rook (and more) against blank King.
     */
    KR_VS_K(rule("KR* vs K"), new KxKEndgameFunction()),

    /**
     * King, two Bishops (and more) against blank King.
     */
    KBB_VS_K(rule("KBB* vs K"), new KxKEndgameFunction()),

    /**
     * King, Bishop and Knight against blank King.
     */
    KBN_VS_K(rule("KBN* vs K"), new KBNvsKEndgameFunction());

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
