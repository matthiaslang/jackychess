package org.mattlang.jc.engine.parameval;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.mattlang.jc.StopWatch;
import org.mattlang.jc.TestPosition;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.chesstests.EigenmannRapidEngineChess;
import org.mattlang.jc.chesstests.EpdParser;
import org.mattlang.jc.engine.evaluation.parameval.MgEgScore;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;

public class ParameterizedEvaluationTest {

    @Test
    public void configParseTest() {

        ParameterizedEvaluation pe = new ParameterizedEvaluation();

    }

    @Test
    public void testTempo() {
        ParameterizedEvaluation pe = new ParameterizedEvaluation();

        int tempoMg = MgEgScore.getMgScore(pe.getAdjustments().getTempo());

        BoardRepresentation board = new BitBoard();

        // start position should evaluate to one (tempo):
        board.setFenPosition("position fen rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w - - 0 1");
        assertThat(pe.eval(board, Color.WHITE)).isEqualTo(tempoMg);

    }

    @Test
    public void testBackwardPawn() {
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

        // get an evaluation for tuning: no caching activated:
        ParameterizedEvaluation pe = ParameterizedEvaluation.createForTuning();

        List<TestPosition> positions = EpdParser.convertTests(EigenmannRapidEngineChess.EIGENMANN_RAPID);

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