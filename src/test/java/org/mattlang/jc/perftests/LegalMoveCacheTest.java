package org.mattlang.jc.perftests;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mattlang.jc.Factory;
import org.mattlang.jc.StopWatch;
import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.BasicMoveList;
import org.mattlang.jc.engine.Engine;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.evaluation.SimpleNegaMaxEval;
import org.mattlang.jc.engine.search.NegaMax;
import org.mattlang.jc.movegenerator.CachingLegalMoveGenerator;
import org.mattlang.jc.movegenerator.LegalMoveGeneratorImpl3;
import org.mattlang.jc.movegenerator.MoveGenerator;
import org.mattlang.jc.movegenerator.MoveGeneratorImpl2;

import java.util.HashMap;

import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;

//@Ignore
public class LegalMoveCacheTest  {


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

        Factory.getDefaults().moveList.set(() ->  new BasicMoveList());

        stopwatch.start();
        int num = 10000000;
        for (int i = 0; i < num; i++) {
            MoveGenerator generator = new MoveGeneratorImpl2();
            MoveList whiteMoves = generator.generate(board, WHITE);

            MoveGenerator generator2 = new MoveGeneratorImpl2();
            MoveList blackMoves = generator2.generate(board, BLACK);
        }
        stopwatch.stop();
        System.out.println("always generate list: " + stopwatch.toString());

        stopwatch.start();

        HashMap<Board, MoveList> whitemap = new HashMap<>();
        HashMap<Board, MoveList> blackmap = new HashMap<>();
        MoveGenerator generator = new MoveGeneratorImpl2();
        MoveList whiteMoves = generator.generate(board, WHITE);
        whitemap.put(board, whiteMoves);
        MoveGenerator generator2 = new MoveGeneratorImpl2();
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

        Factory.getDefaults().moveList.set(() ->  new BasicMoveList());

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

    @Test
    @Ignore // caching does not make it faster with current implementation...
    public void compareMoveListCaching() {

        int depth = 5;
        StopWatch stopWatch = new StopWatch();

        Factory.getDefaults().moveGenerator.set(() -> new MoveGeneratorImpl2());

        Factory.getDefaults().legalMoveGenerator.set(() -> new LegalMoveGeneratorImpl3());
        // now starting engine:
        Engine engine = new Engine(new NegaMax(new SimpleNegaMaxEval()), depth);
        engine.getBoard().setFenPosition("position startpos moves e2e4 a7a6 f2f4 a6a5 a2a4");
        System.out.println(engine.getBoard().toUniCodeStr());
        stopWatch.start();
        Move move = engine.go();
        stopWatch.stop();
        long durationWithoutCaching = stopWatch.getDuration();

        System.out.println("time without caching: " + stopWatch.toString() + " found move " + move.toStr());

        Factory.getDefaults().legalMoveGenerator.set(() -> new CachingLegalMoveGenerator(new LegalMoveGeneratorImpl3()));
        engine = new Engine(new NegaMax(new SimpleNegaMaxEval()), depth);
        engine.getBoard().setFenPosition("position startpos moves e2e4 a7a6 f2f4 a6a5 a2a4");

        stopWatch.start();
        move = engine.go();
        stopWatch.stop();
        long durationWithCaching = stopWatch.getDuration();
        System.out.println("time with caching: " + stopWatch.toString() + " found move " + move.toStr());

        // should be around %30 faster
        Assert.assertTrue(durationWithCaching < durationWithoutCaching);
    }
}