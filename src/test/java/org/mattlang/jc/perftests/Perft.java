package org.mattlang.jc.perftests;

import org.assertj.core.api.SoftAssertions;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.movegenerator.PositionBasedGenerator;

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


    public static void assertPerft(PositionBasedGenerator<MoveList> generator, BoardRepresentation board, Color color, int depth,
                            int expectedNodes, int expectedCaptures, int expectedEP, int expectedCastles, int expectedPromotions) {
        perftReset();
        perft(generator, board, color, depth, (visitedBoard, color1, depth1) -> {
        });
        long stop = System.currentTimeMillis();
        long duration = stop-start;
        if (duration!=0) {
            long nodesPerSecond = (long)nodes * 1000 / duration;
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

    public static void perft(PositionBasedGenerator<MoveList> generator,
                      BoardRepresentation board,
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

        MoveList moves = generator.generate(board, color);
        MoveCursor moveCursor = moves.iterate();
        while (moveCursor.hasNext()) {
            moveCursor.next();

            if (depth == 1) {
                if (moveCursor.isCapture()) {
                    captures++;
                    if (debug == true) {
                        //System.out.println("color: " + color + ", capture " + captures + ": " + moveCursor.getMove().toStr() + " on board: ");
                        //System.out.println(board.toUniCodeStr());
                    }

                }
                if (moveCursor.isEnPassant()) {
                    ep++;
                }
                if (moveCursor.isCastling()) {
                    castles++;
                }
                if (moveCursor.isPawnPromotion()) {
                    promotion++;
                }
            }
            moveCursor.move(board);

            nodeConsumer.accept(board, color, depth);

            perft(generator, board, color.invert(), depth - 1, nodeConsumer);
            moveCursor.undoMove(board);
        }
//        if (moves instanceof MoveListImpl) {
//            MoveListPool.instance.dispose((MoveListImpl) moves);
//        }
    }
}
