package org.mattlang.jc.poc.qbbSpeedComparison;

/*
 This perft implementation is based on QBBEngine by Fabio Gobbato and ported to Java by Emanuel Torres.

 The purpose is to compare the speed differences of various languages for a chess programming workload.

 Used to compare this code with the speed of jacky chess perft to analyse for probably optimizations.

 This is the QbbPerft code modified to delegate to jackies move iteration preparer to compare the speed.
 The jacky logic has overhead compared to the qbb implementation (roughly ~50%-60% slower without move ordering); it is unclear to me where the difference come from.



*/

import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.search.SearchThreadContext;
import org.mattlang.jc.engine.search.SearchThreadContexts;
import org.mattlang.jc.movegenerator.BBCheckCheckerImpl;
import org.mattlang.jc.movegenerator.GenMode;
import org.mattlang.jc.moves.MoveImpl;
import org.mattlang.jc.moves.StagedMoveIterationPreparer;

class QbbPerft3 {

    private static MoveList[] moveLists = new MoveList[256];

    static {
        for (int i = 0; i < 256; i++) {
            moveLists[i] = new MoveList();
        }
    }

    private static BBCheckCheckerImpl checkChecker = new BBCheckCheckerImpl();

    static int pPosition;

    private static BitBoard board;

    /*
    Load a position starting from a fen.
    This function doesn't check the correctness of the fen.
    */
    static BitBoard LoadPosition(String fen) {

        BitBoard board = new BitBoard();
        board.setFenPosition(fen);
        return board;
    }

    static long Perft(int depth) {

        long tot = 0;
        Color siteToMove = board.getSiteToMove();
        SearchThreadContext context = SearchThreadContexts.CONTEXTS.getContext(1);
        StagedMoveIterationPreparer mip = context.getMoveIterationPreparer(depth);
        mip.prepare(context, GenMode.NORMAL, board, board.getSiteToMove(), depth, 0, 0);
        MoveImpl wrapper = new MoveImpl("a1a1");
        while (mip.hasNext()) {
            int move = mip.next();
            wrapper.fromLongEncoded(move);
            board.domove(wrapper);
            if (!checkChecker.isInChess(board, siteToMove)) {
                if (depth > 1) {
                    tot += Perft(depth - 1);
                } else
                    tot++;
            }
            board.undo(wrapper);
        }

        //        try (MoveBoardIterator iterator = mip.iterateMoves()) {
        //
        //            while (iterator.nextMove()) {
        //                if (iterator.doValidMove()) {
        //                    if (depth > 1) {
        //                        tot += Perft(depth - 1);
        //                    } else
        //                        tot++;
        //                }
        //            }
        //        }

        return tot;
    }

    static class PerftSettings {

        String fen;
        int depth;
        long expectedCount;

        PerftSettings(String fen, int depth, long expectedCount) {
            this.fen = fen;
            this.depth = depth;
            this.expectedCount = expectedCount;
        }
    }

    /* Run the Perft with this 6 test positions */
    static void TestPerft() {
        PerftSettings[] Test =
                { new PerftSettings("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", 6, 119060324),
                        new PerftSettings("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1", 5,
                                193690690),
                        new PerftSettings("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1", 7, 178633661),
                        new PerftSettings("r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1", 6,
                                706045033),
                        new PerftSettings("rnbqkb1r/pp1p1ppp/2p5/4P3/2B5/8/PPP1NnPP/RNBQK2R w KQkq - 0 6", 3, 53392),
                        new PerftSettings("r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10", 5,
                                164075551) };

        long totalCount = 0;
        long totalTime = 0;

        for (PerftSettings test : Test) {
            board = LoadPosition("position fen " + test.fen);
            long time = System.nanoTime();
            long actualCount = Perft(test.depth);
            time = System.nanoTime() - time;
            totalCount += actualCount;
            totalTime += time;
            System.out.printf("%5.0f ms, %.0f knps%s\n", time * 1e-6, actualCount * 1e6 / time,
                    actualCount == test.expectedCount ?
                            "" :
                            (" -- ERROR: expected " + test.expectedCount + " got " + actualCount));
        }

        System.out.printf("Total: %.0f ms, %.0f knps\n", totalTime * 1e-6, totalCount * 1e6 / totalTime);
    }

    public static void main(String[] args) {
        System.out.println("QBB Perft in Java");
        TestPerft();
    }
}