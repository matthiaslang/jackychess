package org.mattlang.jc.chessTests;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mattlang.jc.Factory;
import org.mattlang.jc.TestTools;
import org.mattlang.jc.chesstests.EigenmannRapidEngineChess;
import org.mattlang.jc.engine.Engine;

/**
 * Eigenmann Rapid Engine Tests Suite.
 * Most Tests fail..
 * 39 of 111 seem to work fine...
 */

@Category(ChessTests.class)
@RunWith(Parameterized.class)
public class EigenmannRapidEngineChessIT {

    @Parameterized.Parameters(name = "{index}: {2}")
    public static Iterable<String[]> getEPDTests() {
        return EpdParsing.getEPDTests(EigenmannRapidEngineChess.EIGENMANN_RAPID);

    }

    private String position;
    private String expectedBestMove;
    private String testName;

    public EigenmannRapidEngineChessIT(String position, String expectedBestMove, String testName) {
        this.position = position;
        this.expectedBestMove = expectedBestMove;
        this.testName = testName;
    }

    @BeforeClass
    public static void init() throws IOException {
        TestTools.initUciEngineTest();
    }

    @Test
    public void testWithDefaultConfig() {
        // create engine
        Factory.setDefaults(Factory.createStable()
                .config(c -> c.timeout.setValue(99999999))
                .config(c -> c.maxDepth.setValue(25))

        );
        Engine engine = new Engine();
        EpdParsing.testPosition(engine, position, expectedBestMove);
    }

}
