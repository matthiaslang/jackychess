package org.mattlang.jc.perftests;

import org.assertj.core.api.SoftAssertions;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.CheckChecker;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.movegenerator.BBCheckCheckerImpl;
import org.mattlang.jc.movegenerator.MoveGenerator;
import org.mattlang.jc.moves.MoveBoardIterator;
import org.mattlang.jc.moves.MoveListImpl;

/**
 * PerfTests Methods
 */
public class Perft {

    private static boolean debug = false;

    static int nodes = 0;
    static int captures = 0;
    static int ep = 0;
    static int castles = 0;
    static int promotion = 0;

    static long start;

    public static void perftReset() {
        nodes = 0;
        captures = 0;
        ep = 0;
        castles = 0;
        promotion = 0;
        start = System.currentTimeMillis();
    }

    public static void assertPerft(MoveGenerator generator, BoardRepresentation board, Color color, int depth,
            int expectedNodes, int expectedCaptures, int expectedEP, int expectedCastles, int expectedPromotions) {
        assertPerft(generator, board, color, depth, expectedNodes, expectedCaptures, expectedEP, expectedCastles, expectedPromotions,
                (visitedBoard, color1, depth1, cursor) -> {
        });
    }

    public static void assertPerft(MoveGenerator generator, BoardRepresentation board, Color color, int depth,
            int expectedNodes, int expectedCaptures, int expectedEP, int expectedCastles, int expectedPromotions,  PerftConsumer visitor) {
        perftReset();
        perft(generator, board, color, depth, visitor);
        long stop = System.currentTimeMillis();
        long duration = stop - start;
        if (duration != 0) {
            long nodesPerSecond = (long) nodes * 1000 / duration;
            System.out.println("Nodes/s = " + nodesPerSecond);
        }
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(nodes).as("Leave Nodes").isEqualTo(expectedNodes);
        softly.assertThat(captures).as("Captures").isEqualTo(expectedCaptures);
        softly.assertThat(ep).as("En Passant").isEqualTo(expectedEP);
        softly.assertThat(castles).as("Castles (Rochade)").isEqualTo(expectedCastles);
        softly.assertThat(promotion).as("Promotions").isEqualTo(expectedPromotions);
        softly.assertAll();
    }

    public static void perft(MoveGenerator generator,
            BoardRepresentation board,
            Color color,
            int depth,
            PerftConsumer nodeConsumer) {

        CheckChecker checkChecker = new BBCheckCheckerImpl();
        perft(generator, board, checkChecker, color, depth, nodeConsumer);
    }

    public static void perft(MoveGenerator generator,
            BoardRepresentation board,
            CheckChecker checkChecker,
            Color color,
            int depth,
            PerftConsumer nodeConsumer) {

        if (depth == 0) {
            nodes++;
            if (debug == true) {
                System.out.println(board.toUniCodeStr());
            }
            return;
        }

        MoveList moves = new MoveListImpl();
        generator.generate(board, color, moves);

        try (MoveBoardIterator iterator = moves.iterateMoves(board, checkChecker)) {


            while (iterator.doNextMove()) {

                if (depth == 1) {
                    if (iterator.isCapture()) {
                        captures++;
                    }
                    if (iterator.isEnPassant()) {
                        ep++;
                    }
                    if (iterator.isCastling()) {
                        castles++;
                    }
                    if (iterator.isPawnPromotion()) {
                        promotion++;
                    }
                }

                nodeConsumer.accept(board, color, depth, iterator);

                perft(generator, board, checkChecker, color.invert(), depth - 1, nodeConsumer);

            }
        }

    }

}
