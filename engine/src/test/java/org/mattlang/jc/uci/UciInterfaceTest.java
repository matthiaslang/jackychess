package org.mattlang.jc.uci;

import java.io.IOException;

import org.junit.Test;

public class UciInterfaceTest {

    /** hold test communication statically to really ensure, that not multiple instances
     * are created per test. This would lead to errors due to static data (e.g. statically holded main Uci-Gobbler)*/
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
        communication.consumeAllInput();

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

        communication.write("position startpos moves e2e4 e7e5 g1f3 d7d5");
        communication.write("go wtime 1000 btime 1000 winc 0 binc 0 movestogo 10");

        Thread.sleep(2000);
        communication.expectBestmove("e4d5");

        communication.consumeAllInput();
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
        communication.consumeAllInput();
    }

}