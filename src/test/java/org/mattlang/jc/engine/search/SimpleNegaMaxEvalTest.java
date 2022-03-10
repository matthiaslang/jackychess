package org.mattlang.jc.engine.search;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mattlang.jc.Main.initLogging;

import java.io.IOException;

import org.junit.Test;
import org.mattlang.jc.EvalFunctions;
import org.mattlang.jc.EvalParameterSet;
import org.mattlang.jc.Factory;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.tools.LegalMoves;
import org.mattlang.jc.uci.GameContext;
import org.mattlang.jc.uci.UCI;

public class SimpleNegaMaxEvalTest {


    @Test
    public void analyzePattProblem() throws IOException {
        initLogging();
        UCI.instance.attachStreams();
        

        Factory.setDefaults(Factory.createStable());
                Factory.getDefaults().config(c-> c.timeout.setValue(600000));
                Factory.getDefaults().config(c-> c.maxQuiescence.setValue(0));
                Factory.getDefaults().config(c-> c.aspiration.setValue(false));
                Factory.getDefaults().config(c-> c.evluateFunctions.setValue(EvalFunctions.PARAMETERIZED));
                Factory.getDefaults().config(c-> c.evaluateParamSet.setValue(EvalParameterSet.EXPERIMENTAL));


        BoardRepresentation board = Factory.getDefaults().boards.create();
        GameState gameState = board.setFenPosition("position fen 8/8/8/4K3/8/6k1/8/3q4 w - - 14 1 moves e5f5 d1d5 f5f6 g3f2 f6e7 d5e5 e7d7 e5f6 d7c7 f6e6 c7b7 e6d6 b7a7 d6c6 a7b8 c6d7 b8a8");

        System.out.println(board.toUniCodeStr());

        Move move = Factory.getDefaults().searchMethod.create().search(gameState, new GameContext(), 6);
        // since we recognize patts, we avoid moves which make patt:
        board.domove(move);
        // means we should have no patt situation:
        MoveList whiteMoves = LegalMoves.generateLegalMoves(board, Color.WHITE);
        // so white should have possibilities to move:
        assertThat(whiteMoves.size()).isGreaterThan(0);
    }

}