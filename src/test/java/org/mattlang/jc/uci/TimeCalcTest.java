package org.mattlang.jc.uci;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mattlang.jc.uci.TimeCalc.determineTime;

import org.junit.Test;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.bitboard.BitBoard;

public class TimeCalcTest {

    @Test
    public void testProblem() {
        BoardRepresentation board = new BitBoard();
        board.setStartPosition();

        GameState gameState = new GameState(board);
        gameState = gameState.getBoard()
                .setFenPosition(
                        "position startpos moves d2d4 g8f6 c2c4 e7e6 g1f3 b7b6 a2a3 c8a6 d1c2 c7c5 d4d5 e6d5 c4d5 d7d6 b1c3 f8e7\n"
                                + " e2e4 a6f1 h1f1 e8g8 c1f4 d8d7 e1c1 f6h5 f4g3 b6b5 f1e1 b5b4 c3e2 b4a3 b2a3 c5c4 d1d4 c4c3 e4e5 h5g3 h2g3 f8c8 e5e6 f7e6\n"
                                + " d5e6 d7e6 e2c3 e6d7 c2b3 d6d5 e1e7 d7e7 b3d5 g8h8 d5a8 e7a3 c1b1 a3c3 d4d2 b8d7 a8d5 c3a3 d2b2 d7f6 d5f5 c8d8 f3e5 d8d1\n"
                                + " b1c2 a3a4 b2b3 d1d5 e5f7 h8g8 f7h6 g8f8 f5f3 a4a2 c2c1 a2d2 c1b1 d2h6 b3b8 f8e7 f3a3 e7e6 a3a6 e6f5 a6c8 f6d7 c8c2 f5e6\n"
                                + " c2c6 d5d6 c6e4 e6f6 e4f3 f6e7 f3e4 h6e6 e4h4 d7f6 b8b7 d6d7 b7d7 e6d7 h4g5 d7d3 b1b2 d3d4 b2c1 d4f2 g5g7 e7e6 c1b1 a7a5");
        GoParameter goparams = new GoParameter();
        goparams.wtime = 19527;
        goparams.btime = 21579;
        goparams.winc = 1000;
        goparams.binc = 1000;
        long result = TimeCalc.determineCalculationTime(gameState, goparams);
        assertThat(result).isEqualTo(957L);
        System.out.println(result);
    }

    @Test
    public void spendABitMoreTimeOnOpeningAndMiddleGame() {
        BoardRepresentation board = new BitBoard();
        board.setStartPosition();

        GameState gameState = new GameState(board);
        GoParameter goparams = new GoParameter();
        goparams.wtime = 60000;
        goparams.btime = 60000;
        goparams.winc = 1000;
        goparams.binc = 1000;
        long result = TimeCalc.determineCalculationTime(gameState, goparams);

        assertThat(result).isGreaterThan(1000);
        assertThat(result).isLessThan(2000);

    }

    @Test
    public void spendABitMoreTimeIfWeHaveMoreTimeThanOpponent() {
        BoardRepresentation board = new BitBoard();
        board.setStartPosition();

        GameState gameState = new GameState(board);
        GoParameter goparams = new GoParameter();
        goparams.wtime = 60000;
        goparams.btime = 30000;
        goparams.winc = 1000;
        goparams.binc = 1000;
        long result = TimeCalc.determineCalculationTime(gameState, goparams);

        assertThat(result).isEqualTo(8400);

    }

    @Test
    public void dontSpendMoreTimeThanWeHaveDependingOnExpectedFutureMoves() {
        BoardRepresentation board = new BitBoard();
        board.setStartPosition();

        GameState gameState = new GameState(board);
        GoParameter goparams = new GoParameter();
        goparams.wtime = 900;
        goparams.btime = 800;
        goparams.winc = 1000;
        goparams.binc = 1000;
        long result = TimeCalc.determineCalculationTime(gameState, goparams);

        assertThat(result).isEqualTo(500);

    }

    @Test
    public void dontSpendMoreTimeThanWeHaveDependingOnExpectedFutureMovesEvenIfOpponentHasMuchLessTime() {
        BoardRepresentation board = new BitBoard();
        board.setStartPosition();

        GameState gameState = new GameState(board);
        GoParameter goparams = new GoParameter();
        goparams.wtime = 900;
        goparams.btime = 100;
        goparams.winc = 1000;
        goparams.binc = 1000;
        long result = TimeCalc.determineCalculationTime(gameState, goparams);

        assertThat(result).isEqualTo(691);

    }

    @Test
    public void testDetermineTimeWithIncrements() {
        // in opening we get a bit more than winc:
        assertThat(determineTime(60000, 60000, 1000, 1000, 0, 1)).isEqualTo(1400);
        // if we have more rest time than opponent, we get in relation a bit more:
        assertThat(determineTime(60000, 50000, 1000, 1000, 0, 1)).isEqualTo(4733);
        // if we have nearly no time left, get at least halve of the winc time:
        assertThat(determineTime(50, 50, 1000, 1000, 0, 1)).isEqualTo(500);
        // same if we have no time left:
        assertThat(determineTime(0, 0, 1000, 1000, 0, 1)).isEqualTo(500);
        // regardless if we are in endgame/middlegame, etc:
        assertThat(determineTime(0, 0, 1000, 1000, 0, 0)).isEqualTo(500);
        // in endgame we get in relation a bit more time, since we expect to have fewer moves till game end:
        assertThat(determineTime(60000, 60000, 1000, 1000, 0, 0)).isEqualTo(3400);
        // and also if we have more time than opponent we again get a bit more extra time:
        assertThat(determineTime(60000, 50000, 1000, 1000, 0, 0)).isEqualTo(3733);
    }

    @Test
    public void testDetermineTimeWithoutIncrements() {
        // in opening we get time calculated by estimated moves:
        assertThat(determineTime(60000, 60000, 0, 0, 0, 1)).isEqualTo(1000);
        // if we have more rest time than opponent, we get in relation more:
        assertThat(determineTime(60000, 50000, 0, 0, 0, 1)).isEqualTo(4233);
        // if we have nearly no time left, than its anyway nearly over, get rest time:
        assertThat(determineTime(50, 50, 0, 0, 0, 1)).isEqualTo(25);
        // if we have nothing, we couldnt to anything:
        assertThat(determineTime(0, 0, 0, 0, 0, 1)).isEqualTo(0);
        // same in endgame:
        assertThat(determineTime(0, 0, 0, 0, 0, 0)).isEqualTo(0);
        // in endgame we get in relation a bit more time, since we expect to have fewer moves till game end:
        assertThat(determineTime(60000, 60000, 0, 0, 0, 0)).isEqualTo(2900);
        // and also if we have more time than opponent we again get a bit more extra time:
        assertThat(determineTime(60000, 50000, 0, 0, 0, 0)).isEqualTo(3233);
    }

    @Test
    public void testBug() {
        // in opening we get a bit more than winc:
        assertThat(determineTime(359781, 723623, 0, 0, 20, 1)).isEqualTo(17889);
        assertThat(determineTime(1812, 559782, 0, 0, 12, 1)).isEqualTo(151);
        assertThat(determineTime(658594, 794764, 0, 0, 25, 1)).isEqualTo(26243);
        //
        // bug where we calculated a "malus" when we have less time than the opponent, but the malus has accidently increased our calculated think time instead decreasing it...
        // the "malus" idea was just a crazy try which doesnt bring anyting, so I have removed the code for it...
        // (contrariry to a "bonus" if we have really more time than the opponent which we still use and is reasonable.
        //
        //        (JackyChess 0.12.0 64-bit) -> go movestogo 32 wtime 960000 btime 960000
        //        (JackyChess 0.12.0 64-bit) -> go movestogo 31 wtime 931078 btime 929703
        //        (JackyChess 0.12.0 64-bit) -> go movestogo 30 wtime 921859 btime 899125
        //        (JackyChess 0.12.0 64-bit) -> go movestogo 29 wtime 914093 btime 861469
        //        (JackyChess 0.12.0 64-bit) -> go movestogo 28 wtime 848530 btime 814250
        //        (JackyChess 0.12.0 64-bit) -> go movestogo 27 wtime 841451 btime 773781
        //        (JackyChess 0.12.0 64-bit) -> go movestogo 26 wtime 831123 btime 722641
        //        (JackyChess 0.12.0 64-bit) -> go movestogo 25 wtime 794764 btime 658594
        //        (JackyChess 0.12.0 64-bit) -> go movestogo 24 wtime 774795 btime 619844
        //        (JackyChess 0.12.0 64-bit) -> go movestogo 23 wtime 765623 btime 573313
        //        (JackyChess 0.12.0 64-bit) -> go movestogo 22 wtime 747576 btime 512844
        //        (JackyChess 0.12.0 64-bit) -> go movestogo 21 wtime 735842 btime 436766
        //        (JackyChess 0.12.0 64-bit) -> go movestogo 20 wtime 723623 btime 359781
        //        (JackyChess 0.12.0 64-bit) -> go movestogo 19 wtime 712686 btime 256453
        //        (JackyChess 0.12.0 64-bit) -> go movestogo 18 wtime 694624 btime 116500
        //        (JackyChess 0.12.0 64-bit) -> go movestogo 17 wtime 666280 btime 58110
        //        (JackyChess 0.12.0 64-bit) -> go movestogo 16 wtime 647046 btime 28922
        //        (JackyChess 0.12.0 64-bit) -> go movestogo 15 wtime 625421 btime 14422
        //        (JackyChess 0.12.0 64-bit) -> go movestogo 14 wtime 605000 btime 7187
        //        (JackyChess 0.12.0 64-bit) -> go movestogo 13 wtime 584407 btime 3578
        //        (JackyChess 0.12.0 64-bit) -> go movestogo 12 wtime 559782 btime 1812
        //        (JackyChess 0.12.0 64-bit) -> go movestogo 11 wtime 525688 btime 640
        //        (JackyChess 0.12.0 64-bit) -> go movestogo 10 wtime 500126 btime -95

    }
}