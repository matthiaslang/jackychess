package org.mattlang.jc.engine.evaluation.parameval;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mattlang.jc.engine.evaluation.parameval.functions.MobLinFun.parse;

import org.junit.Test;
import org.mattlang.jc.StopWatch;
import org.mattlang.jc.engine.evaluation.parameval.functions.ArrayFunction;
import org.mattlang.jc.engine.evaluation.parameval.functions.MobLinFun;

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

    @Test
    public void speedComparison() {
        StopWatch watch = new StopWatch();

        int result=0;
        MobLinFun fun = parse("2 * (mob-14)");
        watch.start();
        for (int i = 0; i < 1_000_000_000; i++) {
            result += fun.calc(i % 28);
        }
        watch.stop();
        System.out.println(watch.toString());
//        System.out.println(Integer.toString(result));
        
        ArrayFunction fun2 = ArrayFunction.parse(
                "-2, -13, -74, -60, -80, -70, -40, -20, -6, -13, 3, 15, 19, 27, 33, 43, 40, 38, 36, 48, 40, 38, 26, 24, 10, 22, 27, 12, 28");
        watch.start();
        for (int i = 0; i < 1_000_000_000; i++) {
            result += fun2.calc(i % 28);
        }
        watch.stop();
        System.out.println(watch.toString());

        System.out.println(Integer.toString(result));
    }
}