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
     * King, Queen vs King Pawn.
     */
    KQ_VS_KP(rule("KQ Vs KP", "Q ", "P "),
            new KQvsKPEndgameFunction()),

    /**
     * King, Rook vs King Bishop.
     */
    KR_VS_KP(rule("KR Vs KP", "R ", "P "),
            new KRvsKPEndgameFunction()),

    /**
     * King, Rook vs King Bishop.
     */
    KR_VS_KB(rule("KR Vs KB", "R ", "B "),
            new KRvsKNEndgameFunction()),

    /**
     * King, Rook vs King Knight.
     */
    KR_VS_KN(rule("KR Vs KN", "R ", "N "),
            new KRvsKBEndgameFunction()),

    /**
     * King, Queen (and more) against blank King.
     */
    KQ_VS_K(rule("KQ* Vs K", "Q* ", " "),
            new KxKEndgameFunction()),

    /**
     * King, Rook (and more) against blank King.
     */
    KR_VS_K(rule("KR* Vs K", "R* ", " "),
            new KxKEndgameFunction()),

    /**
     * King, two Bishops (and more) against blank King.
     */
    KBB_VS_K(rule("KBB* Vs K", "BB* ", " "),
            new KxKEndgameFunction()),

    /**
     * King, Bishop and Knight against blank King.
     */
    KBN_VS_K(rule("KBB* Vs K", "BK ", " "),
            new KBNvsKEndgameFunction());

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
