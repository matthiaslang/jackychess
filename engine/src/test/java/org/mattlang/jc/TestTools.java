package org.mattlang.jc;

import java.io.IOException;

import org.mattlang.jc.engine.search.SearchThreadContexts;
import org.mattlang.jc.engine.tt.Caching;
import org.mattlang.jc.uci.UCI;
import org.mattlang.jc.util.Logging;

public class TestTools {

    /**
     * Initialisations for Tests where we do an engine run, which uses most of the internal structures.
     * Therefor we want to reset all caches (TT cache, eval cache, pawn cache) to have a deterministic result.
     *
     * We also want to have logging activated for the test and UCI protocol output directed to standard output.
     *
     * @throws IOException
     */
    public static void initUciEngineTest() throws IOException {
        System.setProperty("jacky.logging.activate", "true");
        Logging.initLogging();
        Caching.CACHING.getTtCache().reset();
        SearchThreadContexts.CONTEXTS.reset();
        UCI.instance.attachStreams();
    }
}
