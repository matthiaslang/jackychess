package org.mattlang.jc.poc.qbbSpeedComparison;

/*
 This perft implementation is based on QBBEngine by Fabio Gobbato and ported to Java by Emanuel Torres.

 The purpose is to compare the speed differences of various languages for a chess programming workload.

  Used to compare this code with the speed of jacky chess perft to analyse for probably optimizations.

  This is the QbbPerft code modified to delegate to jackies move generation and iteration logic to compare the speed.
  The jacky move ordering logic is excluded here to have a more realistic comparison (move ordering costs quite a lot speed).

  In the comparison, jackies code is about 50-70% slower. To me its unclear about the reason.
*/

import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.search.SearchThreadContext;
import org.mattlang.jc.engine.search.SearchThreadContexts;
import org.mattlang.jc.movegenerator.BBCheckCheckerImpl;
import org.mattlang.jc.movegenerator.GenMode;
import org.mattlang.jc.movegenerator.MoveGeneration;
import org.mattlang.jc.moves.MoveBoardIterator;
import org.mattlang.jc.moves.MoveImpl;
import org.mattlang.jc.moves.StagedMoveIterationPreparer;

class QbbPerft2 {

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

    /* Check the correctness of the move generator with the Perft function */
    static long Perft(int depth) {

        long tot = 0;
        MoveList moveList = moveLists[depth];
        Color siteToMove = board.getSiteToMove();
        moveList.reset(siteToMove);

        MoveGeneration.generateAttacks(board, siteToMove, moveList);
        MoveGeneration.generateQuiets(board, siteToMove, moveList);

//         do testwise sorting to test how much time this takes...
//        OrderCalculator oc = SearchThreadContexts.CONTEXTS.getContext(1).getOrderCalculator();
//        oc.prepareOrder(siteToMove, 0, 0, depth, board, 0);
//        moveList.scoreMoves(oc);

        MoveImpl wrapper = new MoveImpl("a1a1");
        for (int i = 0; i < moveList.size(); i++) {
            wrapper.fromLongEncoded(moveList.get(i));
            board.domove(wrapper);
            if (!checkChecker.isInChess(board, siteToMove)) {
                if (depth > 1) {
                    tot += Perft(depth - 1);
                } else
                    tot++;
            }
            board.undo(wrapper);
        }

        return tot;
    }

    static long PerftOld(int depth) {

        long tot = 0;

        SearchThreadContext context = SearchThreadContexts.CONTEXTS.getContext(1);
        StagedMoveIterationPreparer mip =
                context.getMoveIterationPreparer(depth);
        mip.prepare(context, GenMode.NORMAL, board, board.getSiteToMove(), depth, 0, 0);
        MoveBoardIterator iterator = mip.iterateMoves();
        while (iterator.doNextValidMove()) {
            if (depth > 1) {
                tot += PerftOld(depth - 1);
            } else
                tot++;
        }

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
        PerftSettings[] Test = {
                new PerftSettings("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", 6, 119060324),
                new PerftSettings("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1", 5, 193690690),
                new PerftSettings("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1", 7, 178633661),
                new PerftSettings("r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1", 6, 706045033),
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