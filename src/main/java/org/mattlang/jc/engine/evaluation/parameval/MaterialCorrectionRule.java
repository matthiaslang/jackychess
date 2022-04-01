package org.mattlang.jc.engine.evaluation.parameval;

import static org.mattlang.jc.engine.evaluation.parameval.MaterialDescription.parseDescr;

import org.mattlang.jc.material.Material;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * A Rule to correct the score by low material.
 * guarding against an illusory material advantage.
 *
 * This represents a configured rule which is read via the property files.
 *
 * Examples:
 *
 * materialRule.rookVsBishopIsDrawish=R 0P vs B -> Reduce half
 * materialRule.rookVsKnightIsDrawish=R 0P vs N -> Reduce half
 * materialRule.rookAndBishopVsRookIsDrawish=RB 0P vs R -> Reduce half
 * materialRule.rookAndKnightVsRookIsDrawish=RN 0P vs R -> Reduce half
 *
 * materialRule.bareBishopCanNotWin=B 0P vs X -> Reduce all
 * materialRule.bareKnightCanNotWin=N 0P vs X -> Reduce all
 * materialRule.twoKnightsCannotWin=NN 0P vs X -> Reduce all
 */
@AllArgsConstructor
@Getter
public class MaterialCorrectionRule {

    private String ruleName;

    private MaterialDescription stronger;

    private MaterialDescription weaker;

    private ReductionRule reductionRule;

    public static MaterialCorrectionRule rule(String rule) {
        Descriptions descriptions = parseMaterialDescriptions(rule);
        return new MaterialCorrectionRule(rule, descriptions.stronger, descriptions.weaker, null);
    }

    @AllArgsConstructor
    @Getter
    static class Descriptions {

        MaterialDescription stronger;
        MaterialDescription weaker;
    }

    public static Descriptions parseMaterialDescriptions(String ruleStr) {
        String[] split = ruleStr.split(" vs ");
        String strongerStr = split[0];
        String weakerStr = split[1];

        if (weakerStr.contains("->")) {
            weakerStr = weakerStr.substring(0, weakerStr.indexOf("->"));
        }

        MaterialDescription stronger = parseDescr(strongerStr);
        MaterialDescription weaker = parseDescr(weakerStr);

        return new Descriptions(stronger, weaker);
    }

    public static MaterialCorrectionRule parse(String ruleName, String ruleStr) {
        Descriptions descriptions = parseMaterialDescriptions(ruleStr);

        String[] split2 = ruleStr.split("->");
        String redruleStr = split2[1];
        ReductionRule rule = ReductionRule.findByTxt(redruleStr);

        return new MaterialCorrectionRule(ruleName, descriptions.stronger, descriptions.weaker, rule);
    }

    public boolean matches(Material matStronger, Material matWeaker) {
        return stronger.matches(matStronger) && weaker.matches(matWeaker);
    }

    public int adjust(int result) {
        return reductionRule.adjust(result);
    }
}
