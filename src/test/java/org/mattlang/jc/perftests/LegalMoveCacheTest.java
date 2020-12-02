package org.mattlang.jc.perftests;

import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;

import java.util.HashMap;

import org.junit.Ignore;
import org.junit.Test;
import org.mattlang.jc.Factory;
import org.mattlang.jc.board.Board;
import org.mattlang.jc.engine.BasicMoveList;
import org.mattlang.jc.engine.evaluation.SimpleNegaMaxEval;
import org.mattlang.jc.movegenerator.MoveGenerator;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.compactmovelist.CompactMoveList;

@Ignore
public class LegalMoveCacheTest  {

    /**
     * REsult: currently the compactmovelist ist slower than the basicmovelist (thats really surprising...)
     */
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

    /**
     * Caching movelists with board as key: this is much faster than recreating, therefore very beneficial
     * for the engine search algorithms.
     */
    @Test
    public void compareMoveListHashCache() {
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

    /**
     * caching eval function result: in classical mini/max caching does not help, since  the eval function
     * is only used on the leafs and therefore not multiple times.
     * In incremental analysis however it could be beneficial but we need also there testing to ensure it really.
     */
    @Test
    public void compareEvalFunctionHashCache() {
        Board board = new Board();

        board.setStartPosition();

        System.out.println(board.toUniCodeStr());

        StopWatch stopwatch = new StopWatch();

        Factory.setMoveListSupplier(() -> new BasicMoveList());

        SimpleNegaMaxEval eval = new SimpleNegaMaxEval();

        stopwatch.start();
        int num = 100_000_000;
        for (int i = 0; i < num; i++) {
            eval.eval(board, WHITE);
            eval.eval(board, BLACK);
        }
        stopwatch.stop();
        System.out.println("always evaluate: " + stopwatch.toString());

        stopwatch.start();

        HashMap<Board, Integer> whitemap = new HashMap<>();
        HashMap<Board, Integer> blackmap = new HashMap<>();

        whitemap.put(board,  eval.eval(board, WHITE));
        blackmap.put(board, eval.eval(board, BLACK));


        for (int i = 0; i < num; i++) {
            whitemap.get(board);
            blackmap.get(board);

        }
        stopwatch.stop();
        System.out.println("use hashmap: " + stopwatch.toString());

    }


}