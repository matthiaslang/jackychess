package org.mattlang.jc.engine.evaluation.parameval;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class MaterialCorrectionRuleTest {

    @Test
    public void parseRules() {

        MaterialCorrectionRule r = MaterialCorrectionRule.parse("", "R 0P vs B -> Reduce half");
        assertThat(r.getStronger().getMaterial().getPawns()).isEqualTo(0);
        assertThat(r.getStronger().getMaterial().getPieceMat()).isEqualTo(1000);

        assertThat(r.getWeaker().isPawnsUnspecific()).isTrue();
        assertThat(r.getWeaker().getMaterial().getPieceMat()).isEqualTo(100);

        r = MaterialCorrectionRule.parse("", "R 0P vs N -> Reduce half");
        r = MaterialCorrectionRule.parse("", "RB 0P vs R -> Reduce half");
        r = MaterialCorrectionRule.parse("", "RN 0P vs R -> Reduce half");
        r = MaterialCorrectionRule.parse("", "B 0P vs X -> Reduce all");
    }
}