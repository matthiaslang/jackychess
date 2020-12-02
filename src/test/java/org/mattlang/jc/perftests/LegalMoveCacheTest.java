package org.mattlang.jc.perftests;

import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;

import java.util.HashMap;

import org.junit.Ignore;
import org.junit.Test;
import org.mattlang.jc.Factory;
import org.mattlang.jc.board.Board;
import org.mattlang.jc.engine.BasicMoveList;
import org.mattlang.jc.movegenerator.MoveGenerator;
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

    @Test
    public void compareHashCache() {
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
        System.out.println("always generate list: " + stopwatch.toString());

        stopwatch.start();

        HashMap<Board, MoveList> whitemap = new HashMap<>();
        HashMap<Board, MoveList> blackmap = new HashMap<>();
        MoveGenerator generator = new MoveGenerator();
        MoveList whiteMoves = generator.generate(board, WHITE);
        whitemap.put(board, whiteMoves);
        MoveGenerator generator2 = new MoveGenerator();
        MoveList blackMoves = generator2.generate(board, BLACK);
        blackmap.put(board, blackMoves);


        for (int i = 0; i < num; i++) {
             whiteMoves = whitemap.get(board);
             whiteMoves.size();
             blackMoves = blackmap.get(board);
             blackMoves.size();
        }
        stopwatch.stop();
        System.out.println("use hashmap: " + stopwatch.toString());

    }

}