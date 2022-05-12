package org.mattlang.jc.perftests;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mattlang.jc.Factory;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;
import org.mattlang.jc.movegenerator.MoveGenerator;
import org.mattlang.jc.movegenerator.PseudoLegalMoveGenerator;

/**
 * PerfTests to validate the boards.isValid method.
 */
public class PerfIncrementalEvaluationTests {


    ParameterizedEvaluation fullEval = new ParameterizedEvaluation();
    ParameterizedEvaluation incrementalEval = new ParameterizedEvaluation();

    private boolean debug = false;
    private PerftConsumer validtMoveTester = new PerftConsumer() {

        @Override
        public void accept(BoardRepresentation board, Color color, int depth, MoveCursor cursor) {

            int cmpEval= fullEval.eval(board, color);
            int incEval = incrementalEval.eval(board, color);

            // now we can test the validity of the move:
            Assertions.assertThat(incEval).isEqualTo(cmpEval);
//                    .withFailMessage("on Board %s: Move %s is not valid!",
//                            board.toUniCodeStr(),
//                            new MoveImpl(cursor.getMoveInt()).toStr())



        }
    };

    @Test
    public void initialPositionPerformanceLegalMoves() {
        Factory.setDefaults(Factory.createStable());

        MoveGenerator generator = new PseudoLegalMoveGenerator();

        Perft perft = new Perft();
        perft.setVisitor(validtMoveTester);
        perft.setBeforePerftConsumer(board -> {
           incrementalEval.initIncrementalValues(board);
           board.registerIncrementalEval(incrementalEval);
        });

        perft.perftInitialPosition(generator);

    }



    @Test
    public void position2() {
        PseudoLegalMoveGenerator generator = new PseudoLegalMoveGenerator();
        Perft perft = new Perft();
        perft.setVisitor(validtMoveTester);
        perft.setBeforePerftConsumer(board -> {
            incrementalEval.initIncrementalValues(board);
            board.registerIncrementalEval(incrementalEval);
        });
        perft.position2(generator);

    }

    @Test
    public void position3() {
        MoveGenerator generator = new PseudoLegalMoveGenerator();
        Perft perft = new Perft();
        perft.setVisitor(validtMoveTester);
        perft.setBeforePerftConsumer(board -> {
            incrementalEval.initIncrementalValues(board);
            board.registerIncrementalEval(incrementalEval);
        });
        perft.position3(generator);
    }

    @Test
    public void position4() {
        PseudoLegalMoveGenerator generator = new PseudoLegalMoveGenerator();
        Perft perft = new Perft();
        perft.setVisitor(validtMoveTester);
        perft.setBeforePerftConsumer(board -> {
            incrementalEval.initIncrementalValues(board);
            board.registerIncrementalEval(incrementalEval);
        });
        perft.position4(generator);
    }

}
