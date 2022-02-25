package org.mattlang.jc.engine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mattlang.jc.Main.initLogging;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;
import org.mattlang.jc.EvalParameterSet;
import org.mattlang.jc.Factory;
import org.mattlang.jc.MoveListImpls;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;
import org.mattlang.jc.movegenerator.StagedLegalMoveGenerator;
import org.mattlang.jc.uci.UCI;

public class StagedMoveGenTest {

    @Test
    @Ignore
    public void testStagedMoveGen() throws IOException {

        initLogging();
        UCI.instance.attachStreams();
        Factory.setDefaults(Factory.createBitboard()
                .moveList.set(MoveListImpls.STAGED.createSupplier())
                .legalMoveGenerator.set(() -> new StagedLegalMoveGenerator())
                .evaluateFunction.set(() -> new ParameterizedEvaluation())
                .config(c -> c.timeout.setValue(18000000))
                .config(c -> c.maxDepth.setValue(9))
                .config(c -> c.evaluateParamSet.setValue(EvalParameterSet.EXPERIMENTAL)));
        // now starting engine:
        Engine engine = new Engine();
        engine.getBoard().setStartPosition();
        System.out.println(engine.getBoard().toUniCodeStr());
        Move move = engine.go();

        System.out.println(move.toStr());

        // with the evaluation function it should yield e7e6:
        assertThat(move.toStr()).isEqualTo("e7e6");
    }

}