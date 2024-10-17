package org.mattlang.jc.engine.evaluation.parameval;

import org.junit.Test;

public class EvalResultTest {

    /**
     * Some stupid arithmetic tests to ensure my understanding of the tapered evals.
     * Proves that we can put non-tapered scores on both mid and end game values and taper it, instead of
     * afterwards add it to the tapered values (simple mathematical distrbutive property)
     *
     * So this means we could refactor the evaluation to use everywhere separated mid an end game values (combined in an int value as in MgEgScore class)
     * .
     */
    @Test
    public void doSomeArithmeticTests() {
        double factor = 0.2;
        int midGame = 500;
        int endGame = 600;
        int common = 300;
        int score = (int) (factor * midGame + (1 - factor) * endGame) + common;

        System.out.println(score);

        midGame += common;
        endGame += common;
        common = 0;

        score = (int) (factor * midGame + (1 - factor) * endGame) + common;

        System.out.println(score);

    }
}