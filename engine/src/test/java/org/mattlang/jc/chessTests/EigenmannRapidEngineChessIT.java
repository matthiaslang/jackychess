package org.mattlang.jc.chessTests;

import static org.mattlang.jc.chesstests.EigenmannRapidEngineChess.EIGENMANN_RAPID;
import static org.mattlang.tuning.data.epdparser.EpdParser.parseEPDTests;

import java.io.IOException;
import java.util.stream.Collectors;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mattlang.jc.TestPosition;
import org.mattlang.jc.TestTools;
import org.mattlang.jc.engine.Engine;

/**
 * Eigenmann Rapid Engine Tests Suite.
 * Most Tests fail..
 * 39 of 111 seem to work fine...
 */

@Category(ChessTests.class)
@RunWith(Parameterized.class)
public class EigenmannRapidEngineChessIT {

    public static final int CHESS_SUITE_TEST_TIMEOUT = 1000 * 15;

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Iterable<TestPosition> getEPDTests() {
        return parseEPDTests(EIGENMANN_RAPID).stream()
                .map(TestPosition::new)
                .collect(Collectors.toList());

    }

    private TestPosition testPosition;

    public EigenmannRapidEngineChessIT(TestPosition testPosition) {
        this.testPosition = testPosition;
    }

    @BeforeClass
    public static void init() throws IOException {
        TestTools.initUciEngineTest();
    }

    @Test
    public void testWithDefaultConfig() {
        // create engine
        Engine engine = new Engine();
        ChessTestRun.testPosition(engine, testPosition);
    }

}
