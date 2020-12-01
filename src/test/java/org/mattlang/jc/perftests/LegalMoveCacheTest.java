package org.mattlang.jc.perftests;

import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mattlang.jc.Factory;
import org.mattlang.jc.board.Board;
import org.mattlang.jc.engine.BasicMoveList;
import org.mattlang.jc.engine.MoveGenerator;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.compactmovelist.CompactMoveList;

@Ignore
public class LegalMoveCacheTest  {

    @Test
    public void compareit() {
        Board board = new Board();

        board.setStartPosition();

        System.out.println(board.toUniCodeStr());

        StopWatch stopwatch = new StopWatch();

        Factory.setMoveListSupplier(() -> new BasicMoveList());

        stopwatch.start();
        int num = 10000000;
        for (int i = 0; i < num; i++) {
            MoveGenerator generator = new MoveGenerator();
            MoveList whiteMoves = generator.generate(board, WHITE);

            MoveGenerator generator2 = new MoveGenerator();
            MoveList blackMoves = generator2.generate(board, BLACK);
        }
        stopwatch.stop();
        System.out.println("basic list: " + stopwatch.toString());

        Factory.setMoveListSupplier(() -> new CompactMoveList());

        stopwatch.start();
        for (int i = 0; i < num; i++) {
            MoveGenerator generator = new MoveGenerator();
            MoveList whiteMoves = generator.generate(board, WHITE);

            MoveGenerator generator2 = new MoveGenerator();
            MoveList blackMoves = generator2.generate(board, BLACK);
        }
        stopwatch.stop();
        System.out.println("compact list: " + stopwatch.toString());

    }

}