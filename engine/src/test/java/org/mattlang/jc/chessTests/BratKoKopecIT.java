package org.mattlang.jc.chessTests;

import static org.mattlang.jc.chesstests.BratKoKopec.BRATKO_KOPEC;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mattlang.jc.Factory;
import org.mattlang.jc.TestTools;
import org.mattlang.jc.engine.Engine;

/**
 * Bratko Kopec Test Suite.
 * 2 of 24 fail currently.
 */

@Category(ChessTests.class)
@RunWith(Parameterized.class)
public class BratKoKopecIT {

    @Parameterized.Parameters(name = "{index}: {2}")
    public static Iterable<String[]> getEPDTests() {
        return EpdParsing.getEPDTests(BRATKO_KOPEC);
    }

    private String position;
    private String expectedBestMove;
    private String testName;

    public BratKoKopecIT(String position, String expectedBestMove, String testName) {
        this.position = position;
        this.expectedBestMove = expectedBestMove;
        this.testName = testName;
    }

    @BeforeClass
    public static void init() throws IOException {
        TestTools.initUciEngineTest();
    }

    @Test
    public void testStable() {
        // create engine
        Factory.setDefaults(Factory.createStable()
                .config(c -> c.timeout.setValue(99999999))
                .config(c -> c.maxDepth.setValue(25))

        );
        Engine engine = new Engine();
        EpdParsing.testPosition(engine, position, expectedBestMove);
    }

}
