package org.mattlang.jc.engine.evaluation.parameval;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mattlang.jc.engine.evaluation.parameval.MobLinFun.parse;

import org.junit.Test;

public class MobLinFunTest {

    @Test
    public void testParsing() {
        MobLinFun fun = parse("3*(mob-14)");
        assertThat(fun.getFactor()).isEqualTo(3);
        assertThat(fun.getKoeff()).isEqualTo(-14);

        assertThat(fun.calc(14)).isEqualTo(0);
        assertThat(fun.calc(15)).isEqualTo(3);
        assertThat(fun.calc(16)).isEqualTo(6);

    }

    @Test
    public void testParsing2() {
        MobLinFun fun = parse("3 * (mob - 14 )");
        assertThat(fun.getFactor()).isEqualTo(3);
        assertThat(fun.getKoeff()).isEqualTo(-14);

        assertThat(fun.calc(14)).isEqualTo(0);
        assertThat(fun.calc(15)).isEqualTo(3);
        assertThat(fun.calc(16)).isEqualTo(6);

    }
}