package org.mattlang.jc.engine.evaluation.parameval;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.mattlang.jc.EvalParameterSet;
import org.mattlang.jc.Factory;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BitBoard;

public class ParameterizedEvaluationTest {

    @Test
    public void configParseTest(){

        // try load/parse the default config:
        Factory.getDefaults().getConfig().evaluateParamSet.setValue(EvalParameterSet.DEFAULT);
        ParameterizedEvaluation pe = new ParameterizedEvaluation();

        Factory.getDefaults().getConfig().evaluateParamSet.setValue(EvalParameterSet.CURRENT);
        pe = new ParameterizedEvaluation();

        Factory.getDefaults().getConfig().evaluateParamSet.setValue(EvalParameterSet.EXPERIMENTAL);
        pe = new ParameterizedEvaluation();
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