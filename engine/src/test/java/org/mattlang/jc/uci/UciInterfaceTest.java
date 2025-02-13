package org.mattlang.jc.uci;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;
import org.mattlang.jc.ConfigValues;
import org.mattlang.jc.engine.tt.Caching;

public class UciInterfaceTest {

    /**
     * hold test communication statically to really ensure, that not multiple instances
     * are created per test. This would lead to errors due to static data (e.g. statically holded main Uci-Gobbler)
     */
    private static UciCommunicationTester communication = new UciCommunicationTester();

    @Test
    public void doAnalysis() throws InterruptedException, IOException {

        communication.uciStartCommunication();

        communication.write("ucinewgame");
        communication.write("setoption name UCI_AnalyseMode value true");
        communication.write("position startpos moves e2e4 e7e5");
        communication.write("go infinite");

        Thread.sleep(2000);
        communication.write("stop");
        Thread.sleep(2000);

        communication.expectBestmove("g1f3");
        communication.consumeAllInfo();

        communication.write("position startpos moves e2e4 e7e5 g1f3 d7d5");
        communication.write("go infinite");

        Thread.sleep(2000);
        communication.write("stop");
        Thread.sleep(2000);
        communication.expectBestmove("e4d5");

        communication.consumeAllInfo();
    }

    @Test
    public void doSomePlaying() throws InterruptedException, IOException {

        communication.uciStartCommunication();

        communication.write("ucinewgame");
        communication.write("setoption name UCI_AnalyseMode value false");
        communication.write("position startpos moves e2e4 e7e5");
        communication.write("go wtime 1000 btime 1000 winc 0 binc 0 movestogo 1");

        Thread.sleep(2000);

        communication.expectBestmove("g1f3");
        communication.consumeAllInfo();

        communication.write("position startpos moves e2e4 e7e5 g1f3 d7d5");
        communication.write("go wtime 1000 btime 1000 winc 0 binc 0 movestogo 10");

        Thread.sleep(2000);
        communication.expectBestmove("e4d5");
        communication.consumeAllInfo();

        communication.write("position startpos moves e2e4 e7e5 g1f3 d7d5");
        communication.write("go depth 1");
        Thread.sleep(2000);
        communication.expectBestmove("e4d5");
        communication.consumeAllInfo();

        communication.write("position startpos moves e2e4 e7e5 g1f3 d7d5");
        communication.write("go nodes 10000");
        Thread.sleep(2000);
        communication.expectBestmove("e4d5");
        communication.consumeAllInfo();
    }

    @Test
    public void doAnalysisWithSearchMoves() throws InterruptedException, IOException {

        communication.uciStartCommunication();

        communication.write("ucinewgame");
        communication.write("setoption name UCI_AnalyseMode value true");
        communication.write("position startpos");
        communication.write("go infinite searchmoves d2d4 g1f3 b2b3");

        Thread.sleep(2000);

        communication.write("stop");
        Thread.sleep(2000);
        communication.expectBestmove("d2d4");
        communication.consumeAllInfo();
    }

    @Test
    public void changeHashCache() throws InterruptedException, IOException {

        communication.uciStartCommunication();

        communication.write("ucinewgame");
        communication.write("setoption name Hash value 16");

        Thread.sleep(2000);
        assertThat(Caching.CACHING.getTtCache().getCacheSize()).isEqualTo(16 * 1024 * 1024);
        communication.write("setoption name Hash value 64");
        Thread.sleep(2000);
        assertThat(Caching.CACHING.getTtCache().getCacheSize()).isEqualTo(64 * 1024 * 1024);
        communication.write("setoption name Hash value 32");
        Thread.sleep(2000);
        assertThat(Caching.CACHING.getTtCache().getCacheSize()).isEqualTo(32 * 1024 * 1024);
        ConfigValues.resetConfigValues();
        communication.consumeAllInfo();
    }

}