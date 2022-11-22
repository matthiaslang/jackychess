package org.mattlang.jc.engine.evaluation.parameval;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.mattlang.jc.EvalParameterSet;
import org.mattlang.jc.Factory;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.engine.evaluation.parameval.functions.ArrayFunction;
import org.mattlang.jc.engine.evaluation.parameval.functions.Function;

public class ParameterizedEvaluationTest {

    @Test
    public void convertFormulasTest() {

        Factory.getDefaults().getConfig().evaluateParamSet.setValue(EvalParameterSet.CURRENT);
        ParameterizedEvaluation pe = new ParameterizedEvaluation();

        System.out.println(convertFunction(14 + 1, pe.getMobEvaluation().getParamsRook().mobilityMG));

    }

    private String convertFunction(int max, Function function) {
        int[] data = new int[max];
        for (int i = 0; i < max; i++) {
            data[i] = function.calc(i);
        }
        ArrayFunction arrayFunction = new ArrayFunction(data);

        return arrayFunction.convertDataToString();

    }

    @Test
    public void configParseTest() {

        // try load/parse the default config:
        Factory.getDefaults().getConfig().evaluateParamSet.setValue(EvalParameterSet.DEFAULT);
        ParameterizedEvaluation pe = new ParameterizedEvaluation();

        Factory.getDefaults().getConfig().evaluateParamSet.setValue(EvalParameterSet.CURRENT);
        pe = new ParameterizedEvaluation();

        Factory.getDefaults().getConfig().evaluateParamSet.setValue(EvalParameterSet.EXPERIMENTAL);
        pe = new ParameterizedEvaluation();

        Factory.getDefaults().getConfig().evaluateParamSet.setValue(EvalParameterSet.TUNED01);
        pe = new ParameterizedEvaluation();
        pe.getMobEvaluation();
    }

    @Test
    public void testBlockages() {
        Factory.getDefaults().getConfig().evaluateParamSet.setValue(EvalParameterSet.EXPERIMENTAL);
        ParameterizedEvaluation pe = new ParameterizedEvaluation();

        BoardRepresentation board = new BitBoard();

        // start position should evaluate to zero:
        board.setFenPosition("position fen rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w - - 0 1");
        assertThat(pe.eval(board, Color.WHITE)).isEqualTo(0);

        // test a blockage:

        board.setFenPosition("position fen rnbqkbnr/pppppppp/8/8/8/3N4/PPPPPPPP/RNBQKBNR w - - 0 1");
        assertThat(pe.eval(board, Color.WHITE)).isEqualTo(322);
    }
}