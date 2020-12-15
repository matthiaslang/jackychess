package org.mattlang.jc.poc;

import org.junit.Test;
import org.mattlang.jc.StopWatch;
import org.mattlang.jc.board.Color;

import static org.mattlang.jc.board.FigureType.*;

public class MaterialTablePocTest {

    int mattbl[][][][][][][][][][][][] = new int[5][5][5][5][5][5][5][5][5][5][5][5];

    @Test
    public void testpoc() {

        StopWatch watch = new StopWatch();
        watch.start();
        for (int i = 0; i < 1000000; i++) {
            docalc(Color.WHITE);
        }
        watch.stop();
        System.out.println("time normal calc: " + watch.toString());

        watch.start();
        for (int i = 0; i < 1000000; i++) {
            docallindex(Color.WHITE);
        }
        watch.stop();
        System.out.println("time index usage: " + watch.toString());


    }

    private void docallindex(Color white) {
        int val = mattbl[3][3][3][3][3][3][3][3][3][3][3][3];
    }
    int counts[][] = {{0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}};


    private void docalc(Color who2Move) {

        int who2mov = who2Move == Color.WHITE ? 1 : -1;
        int score = 200000000 * (counts[0][King.figureCode] - counts[1][King.figureCode]) * who2mov +
                900 * (counts[0][Queen.figureCode] - counts[1][Queen.figureCode]) * who2mov +
                500 * (counts[0][Rook.figureCode] - counts[1][Rook.figureCode]) * who2mov +
                330 * (counts[0][Bishop.figureCode] - counts[1][Bishop.figureCode]) * who2mov +
                320 * (counts[0][Knight.figureCode] - counts[1][Knight.figureCode]) * who2mov +
                100 * (counts[0][Pawn.figureCode] - counts[1][Pawn.figureCode]) * who2mov +
                // two bishop bonus:
                50 * (counts[0][Bishop.figureCode] == 2 ? 1 : 0 - counts[1][Bishop.figureCode] == 2 ? 1 : 0) * who2mov +
                // penalty for two knights:
                -50 * (counts[0][Knight.figureCode] == 2 ? 1 : 0 - counts[1][Knight.figureCode] == 2 ? 1 : 0) * who2mov
                +
                // penalty for having no pawns (especially in endgame)
                -500 * (counts[0][Pawn.figureCode] == 0 ? 1 : 0 - counts[1][Pawn.figureCode] == 0 ? 1 : 0) * who2mov;

    }
}
