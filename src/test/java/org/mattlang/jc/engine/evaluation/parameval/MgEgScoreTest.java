package org.mattlang.jc.engine.evaluation.parameval;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class MgEgScoreTest {

    @Test
    public void testArithmetic() {

        MgEgScore val1 = new MgEgScore(500, 300);
        assertThat(val1.getMgScore()).isEqualTo(500);
        assertThat(val1.getEgScore()).isEqualTo(300);

        val1 = new MgEgScore(-500, 300);
        assertThat(val1.getMgScore()).isEqualTo(-500);
        assertThat(val1.getEgScore()).isEqualTo(300);

        val1 = new MgEgScore(500, -300);
        assertThat(val1.getMgScore()).isEqualTo(500);
        assertThat(val1.getEgScore()).isEqualTo(-300);

        val1.addMg(250);
        val1.addEg(-150);
        assertThat(val1.getMgScore()).isEqualTo(750);
        assertThat(val1.getEgScore()).isEqualTo(-450);

        MgEgScore val2 = new MgEgScore(50, 30);

        val1.addMult(val2, 1);
        assertThat(val1.getMgScore()).isEqualTo(800);
        assertThat(val1.getEgScore()).isEqualTo(-420);

        val1.setMg(234);
        assertThat(val1.getMgScore()).isEqualTo(234);
        assertThat(val1.getEgScore()).isEqualTo(-420);

        val1.setEg(789);
        assertThat(val1.getMgScore()).isEqualTo(234);
        assertThat(val1.getEgScore()).isEqualTo(789);
    }

    @Test
    public void testMultArithmetic() {
        MgEgScore val1 = new MgEgScore(500, 1500);
        MgEgScore val2 = new MgEgScore(100, 300);

        assertThat(val1.getCombinedScore()).isEqualTo(val2.getCombinedScore() * 5);
    }
}