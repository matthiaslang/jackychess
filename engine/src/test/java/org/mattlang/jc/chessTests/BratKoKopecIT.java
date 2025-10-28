package org.mattlang.jc.chessTests;

import static org.mattlang.jc.chesstests.BratKoKopec.BRATKO_KOPEC;
import static org.mattlang.tuning.data.epdparser.EpdParser.parseEPDTests;

import java.io.IOException;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.BeforeParameterizedClassInvocation;
import org.junit.jupiter.params.ParameterizedClass;
import org.junit.jupiter.params.provider.MethodSource;
import org.mattlang.jc.TestPosition;
import org.mattlang.jc.TestTools;
import org.mattlang.jc.engine.Engine;

/**
 * Bratko Kopec Test Suite.
 * 2 of 24 fail currently.
 */

@ParameterizedClass
@MethodSource("getEPDTests")
public class BratKoKopecIT {

    public static Iterable<TestPosition> getEPDTests() {
        return parseEPDTests(BRATKO_KOPEC).stream()
                .map(TestPosition::new)
                .collect(Collectors.toList());
    }

    @org.junit.jupiter.params.Parameter(0)
    private TestPosition testPosition;

    @BeforeParameterizedClassInvocation
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
