package org.mattlang.benchmarks;

import java.util.ArrayList;
import java.util.List;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.chesstests.BratKoKopec;
import org.mattlang.jc.chesstests.EpdParser;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;
import org.openjdk.jmh.annotations.*;

public class BenchmarkTryout {

    @State(Scope.Benchmark)
    public static class Constants {

        public int x = 17;
        public int y = 34;

        public ParameterizedEvaluation pe = new ParameterizedEvaluation();
        public BoardRepresentation board = new BitBoard();

        public List<BoardRepresentation> boards = new ArrayList<BoardRepresentation>();

        {
            pe.setCaching(false);

            // add start position:
            board.setFenPosition("position fen rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w - - 0 1");
            boards.add(board);
            // add the brat co test positions:
            for (int i = 0; i < 10000; i++) {
                for (String[] epdTest : EpdParser.getEPDTests(BratKoKopec.BRATKO_KOPEC)) {
                    BoardRepresentation board = new BitBoard();
                    board.setFenPosition("position fen " + epdTest[0] + " 0 0");
                    boards.add(board);
                }
            }
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public int measureEvaluation(Constants constants) {
        int val = 0;
        for (BoardRepresentation board : constants.boards) {
            val += constants.pe.eval(board, Color.WHITE);
        }
        return val;
    }

    //    @Benchmark
    //    @BenchmarkMode(Mode.AverageTime)
    //    public int ToolsDistanceDirekt(Constants constants) {
    //        int sum = 0;
    //        for (int i = 0; i < constants.x; i++) {
    //            for (int j = 0; j < constants.y; j++) {
    //                sum += Tools.calcDistance(i, j);
    //            }
    //        }
    //        return sum;
    //    }
    //
    //    @Benchmark
    //    @BenchmarkMode(Mode.AverageTime)
    //    public int ToolsDistanceCached(Constants constants) {
    //        int sum = 0;
    //        for (int i = 0; i < constants.x; i++) {
    //            for (int j = 0; j < constants.y; j++) {
    //                sum += Tools.distance(i, j);
    //            }
    //        }
    //        return sum;
    //    }
}
