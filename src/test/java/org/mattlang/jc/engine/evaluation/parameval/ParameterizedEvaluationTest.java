package org.mattlang.jc.engine.evaluation.parameval;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
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

public class ParameterizedEvaluationTest {

    @Test
    public void configParseTest() {

        Factory.getDefaults().getConfig().evaluateParamSet.setValue(EvalParameterSet.CURRENT);
        ParameterizedEvaluation pe = new ParameterizedEvaluation();

        // try load all existing parametersets:
        for (EvalParameterSet paramSet : Arrays.asList(EvalParameterSet.CURRENT/*, EvalParameterSet.TUNED01*/)) {
            Factory.getDefaults().getConfig().evaluateParamSet.setValue(paramSet);
            pe = new ParameterizedEvaluation();
        }
    }

    @Test
    public void testBlockages() {
        Factory.getDefaults().getConfig().evaluateParamSet.setValue(EvalParameterSet.CURRENT);
        ParameterizedEvaluation pe = new ParameterizedEvaluation();

        BoardRepresentation board = new BitBoard();

        // start position should evaluate to one (tempo):
        board.setFenPosition("position fen rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w - - 0 1");
        assertThat(pe.eval(board, Color.WHITE)).isEqualTo(1);

        // test a blockage:

//        board.setFenPosition("position fen rnbqkbnr/pppppppp/8/8/8/3N4/PPPPPPPP/RNBQKBNR w - - 0 1");
//        assertThat(pe.eval(board, Color.WHITE)).isEqualTo(370);
    }

    @Test
    public void testBackwardPawn() {
        Factory.getDefaults().getConfig().evaluateParamSet.setValue(EvalParameterSet.CURRENT);
        ParameterizedEvaluation pe = new ParameterizedEvaluation();

        //        pe.getPawnEvaluation().setBackwardedPawnPenaltyMgEg(10000);
        //        pe.getPawnEvaluation().setIsolatedPawnPenaltyMgEg(1);
        //        pe.getPawnEvaluation().setAttackedPawnPenaltyMgEg(10);
        //        pe.getPawnEvaluation().setDoublePawnPenaltyMgEg(100);

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