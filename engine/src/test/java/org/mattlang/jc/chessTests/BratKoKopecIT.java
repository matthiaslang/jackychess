package org.mattlang.jc.chessTests;

import static org.mattlang.jc.chesstests.BratKoKopec.BRATKO_KOPEC;
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
 * Bratko Kopec Test Suite.
 * 2 of 24 fail currently.
 */

@Category(ChessTests.class)
@RunWith(Parameterized.class)
public class BratKoKopecIT {

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Iterable<TestPosition> getEPDTests() {
        return parseEPDTests(BRATKO_KOPEC).stream()
                .map(TestPosition::new)
                .collect(Collectors.toList());
    }

    private TestPosition testPosition;

    public BratKoKopecIT(TestPosition testPosition) {
        this.testPosition = testPosition;
    }

    @BeforeClass
    public static void init() throws IOException {
        TestTools.initUciEngineTest();
    }

    @Test
    public void testStable() {
        // create engine
        Engine engine = new Engine();
        ChessTestRun.testPosition(engine, testPosition);
    }

}
