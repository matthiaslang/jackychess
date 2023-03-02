package org.mattlang.jc.engine.evaluation.parameval;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.mattlang.jc.EvalParameterSet;
import org.mattlang.jc.Factory;
import org.mattlang.jc.StopWatch;
import org.mattlang.jc.TestPosition;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.chessTests.EigenmannRapidEngineChessIT;
import org.mattlang.jc.chessTests.EpdParsing;
import org.mattlang.jc.engine.evaluation.parameval.functions.ArrayFunction;
import org.mattlang.jc.engine.evaluation.parameval.functions.Function;

public class ParameterizedEvaluationTest {

    @Test
    public void convertFormulasTest() {

        Factory.getDefaults().getConfig().evaluateParamSet.setValue(EvalParameterSet.CURRENT);
        ParameterizedEvaluation pe = new ParameterizedEvaluation();

        System.out.println(convertFunction(12, pe.getMobEvaluation().getParamsKing().kingAtt));

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

        // try load all existing parametersets:
        for (EvalParameterSet paramSet : EvalParameterSet.values()) {
            Factory.getDefaults().getConfig().evaluateParamSet.setValue(paramSet);
            pe = new ParameterizedEvaluation();
        }
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


    @Test
    public void testBackwardPawn() {
        Factory.getDefaults().getConfig().evaluateParamSet.setValue(EvalParameterSet.TUNED01);
        ParameterizedEvaluation pe = new ParameterizedEvaluation();

        BoardRepresentation board = new BitBoard();


        // test a backward pawn:

        board.setFenPosition("position fen k7/8/2p5/3p4/3P4/8/8/K7 w - - 0 1");
        System.out.println(board.toUniCodeStr());
        System.out.println(pe.eval(board, Color.BLACK));
    }

//    @Test
    public void testPerformance() {
        Factory.getDefaults().getConfig().evaluateParamSet.setValue(EvalParameterSet.CURRENT);

        // get an evaluation for tuning: no caching activated:
        ParameterizedEvaluation pe = ParameterizedEvaluation.createForTuning();

        List<TestPosition> positions = EpdParsing.convertTests(EigenmannRapidEngineChessIT.eret);

        List<BoardRepresentation> boards = positions.stream()
                .map(p -> {
                    BoardRepresentation board = new BitBoard();
                    board.setFenPosition(p.getFenPosition());
                    return board;
                }).collect(Collectors.toList());

        StopWatch watch = new StopWatch();
        watch.start();
        int evals = 0;
        for (int i = 0; i < 250000; i++) {
            for (BoardRepresentation board : boards) {
                evals += pe.eval(board, Color.WHITE);
            }
        }
        watch.stop();
        System.out.println(evals);
        System.out.println(watch.toString());
    }
}