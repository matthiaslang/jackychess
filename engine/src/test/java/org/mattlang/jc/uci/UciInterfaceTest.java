package org.mattlang.jc.uci;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mattlang.jc.uci.BestmoveExpectation.bestmoveWithPonder;

import java.io.IOException;

import org.junit.jupiter.api.Test;
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

        communication.expectBestmove("g1f3", "b1c3", "d2d4");
        communication.consumeAllInfo();

        communication.write("position startpos moves e2e4 e7e5 g1f3 d7d5");
        communication.write("go infinite");

        Thread.sleep(2000);
        communication.write("stop");
        Thread.sleep(2000);
        communication.expectBestmove("e4d5", "f3e5");

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

        communication.expectBestmove("g1f3", "b1c3", "d2d4");
        communication.consumeAllInfo();

        communication.write("position startpos moves e2e4 e7e5 g1f3 d7d5");
        communication.write("go wtime 1000 btime 1000 winc 0 binc 0 movestogo 10");

        Thread.sleep(2000);
        communication.expectBestmove("e4d5", "e4d5", "f3e5");
        communication.consumeAllInfo();

        communication.write("position startpos moves e2e4 e7e5 g1f3 d7d5");
        communication.write("go depth 1");
        Thread.sleep(2000);
        communication.expectBestmove("e4d5", "e4d5", "f3e5");
        communication.consumeAllInfo();

        communication.write("position startpos moves e2e4 e7e5 g1f3 d7d5");
        communication.write("go nodes 10000");
        Thread.sleep(2000);
        communication.expectBestmove("e4d5", "e4d5", "f3e5");
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
        communication.expectBestmove("d2d4", "g1f3");
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

    @Test
    public void doFisher960() throws InterruptedException, IOException {

        communication.uciStartCommunication();

        communication.write("ucinewgame");
        communication.write("setoption name UCI_AnalyseMode value false");
        communication.write("setoption name UCI_Chess960 value true");
        communication.write(
                "position fen bbnqrnkr/pppppppp/8/8/8/8/PPPPPPPP/BBNQRNKR w HEhe - moves e2e4 b7b5 f2f4 c7c5 f1e3");
        communication.write("go wtime 1000 btime 1000 winc 0 binc 0 movestogo 1");

        Thread.sleep(2000);

        communication.expectBestmove("b8f4", "a8e4");
        communication.consumeAllInfo();

    }

    @Test
    public void doPonderingAndPonderHitAfterThinkTime() throws InterruptedException, IOException {

        // simulate pondering with a ponder hit:
        communication.uciStartCommunication();

        communication.write("ucinewgame");
        communication.write("setoption name UCI_AnalyseMode value false");
        communication.write("setoption name Ponder value true");
        communication.write("position startpos moves e2e4 e7e5");
        communication.write("go ponder wtime 1000 btime 1000 winc 0 binc 0 movestogo 1");

        Thread.sleep(2000);
        communication.write("ponderhit");

        communication.expectBestmove(bestmoveWithPonder("g1f3", "b8c6"),
                bestmoveWithPonder("g1f3", "b8c6"),
                bestmoveWithPonder("b1c3", "b8c6"));
        communication.consumeAllInfo();

    }


    @Test
    public void doPonderingWithoutPonderHit() throws InterruptedException, IOException {

        communication.uciStartCommunication();
        // simulate pondering without a ponder hit:

        communication.write("setoption name Ponder value true");
        communication.write("position startpos moves e2e4 e7e5");
        communication.write("go ponder wtime 1000 btime 1000 winc 0 binc 0 movestogo 1");

        Thread.sleep(2000);
        // on stop there should be no further output from the engine
        communication.write("stop");
        communication.expectBestmove(bestmoveWithPonder("g1f3", "b8c6"),
                bestmoveWithPonder("g1f3", "b8c6"),
                bestmoveWithPonder("b1c3", "b8c6"));

        communication.consumeAllInfo();

        //        Assertions.assertThat(communication.isNoInput()).isTrue();

    }

    @Test
    public void doPonderingWithPonderHitWithinThinktime() throws InterruptedException, IOException {

        communication.uciStartCommunication();
        // simulate pondering with ponderhit earlier than calculated think time:
        communication.write("setoption name Ponder value true");
        communication.write("position startpos moves e2e4 e7e5");
        communication.write("go ponder wtime 1000 btime 1000 winc 0 binc 0 movestogo 1");

        Thread.sleep(300);
        communication.write("ponderhit");
        Thread.sleep(300);
        // it should now calculate a bit further and then return bestmove
        communication.expectBestmove(bestmoveWithPonder("g1f3", "b8c6"),
                bestmoveWithPonder("g1f3", "b8c6"),
                bestmoveWithPonder("b1c3", "b8c6"),
                bestmoveWithPonder("b1c3", "g8f6"));
        communication.consumeAllInfo();

    }

}