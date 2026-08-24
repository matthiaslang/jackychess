package org.mattlang.jc.command.commands;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mattlang.jc.SearchParameter;
import org.mattlang.jc.board.BoardPrinter;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.engine.Engine;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;
import org.mattlang.jc.engine.search.IterativeSearchResult;
import org.mattlang.jc.engine.search.SearchThreadContexts;
import org.mattlang.jc.engine.tt.Caching;
import org.mattlang.jc.tools.FenFlip;
import org.mattlang.jc.uci.GameContext;
import org.mattlang.jc.uci.UCI;
import org.mattlang.jc.util.FenComposer;
import org.mattlang.jc.util.Logging;
import org.mattlang.tuning.data.builder.BoardAndMoves;
import org.mattlang.tuning.data.pgnparser.PgnGame;
import org.mattlang.tuning.data.pgnparser.PgnMove;
import org.mattlang.tuning.data.pgnparser.PgnParser;

@Disabled
public class AnalyzeTest {

    @Test
    public void testFen() {
        String fen = "position fen 8/6p1/1K6/P3N3/5pp1/7k/8/8 w - - 2 17";

        ParameterizedEvaluation evaluation = new ParameterizedEvaluation();

        BitBoard board = new BitBoard();
        board.setFenPosition(fen);
        int eval;
//        int eval = evaluation.eval(board, board.getSiteToMove().ordinal());
//        System.out.println(fen + " eval = " + eval);


        board.setFenPosition("position fen 8/6p1/PK6/4N3/6p1/5p1k/8/8 w - - 2 17");
        System.out.println(board.toUniCodeStr());
         eval = evaluation.eval(board, board.getSiteToMove().ordinal());
        System.out.println(fen + " eval = " + eval);
        eval = evaluation.eval(board, board.getSiteToMove().invert().ordinal());
        System.out.println(fen + " eval black = " + eval);

        System.out.println("inverted test:");
        board=createInverted("position fen 8/6p1/PK6/4N3/6p1/5p1k/8/8 w - - 2 17");
        System.out.println(board.toUniCodeStr());
        eval = evaluation.eval(board, board.getSiteToMove().ordinal());
        System.out.println( " eval = " + eval);
        eval = evaluation.eval(board, board.getSiteToMove().invert().ordinal());
        System.out.println( " eval black = " + eval);
    }

    private BitBoard createInverted(String fen) {
        FenFlip fenflip = new FenFlip();
        String flippedFen = fenflip.mirrorHorizontalFen(fen);
        BitBoard board = new BitBoard();

        board.setFenPosition(flippedFen);

        return board;
    }


    @Test
    public void analyze() throws IOException {
        PgnParser parser = new PgnParser();
        File file = new File("/Users/MLang/privat/jc/turniere/tournament20260711/jackychess-tournament.pgn");
        List<PgnGame> games = parser.parse(file);

        System.setProperty("jacky.logging.activate", "true");
        Logging.initLogging();
        Caching.CACHING.getTtCache().reset();
        SearchThreadContexts.CONTEXTS.reset();
        OutputStream fullOutput=new ByteArrayOutputStream();
        UCI.instance.attachStreams(System.in, fullOutput);

        analyze(games.get(3));

    }

    private void analyze(PgnGame game) {
        BoardAndMoves bam = new BoardAndMoves();
        bam.prepareGame(game);
        for (PgnMove pgnMove : game.getMoves()) {

            analyzePosition(bam.getBoard());
            System.out.println(pgnMove.getWhite().getMoveText().getText());
            bam.doMove(pgnMove.getWhite());
            if (pgnMove.getBlack() != null) {
                System.out.println(pgnMove.getBlack().getMoveText().getText());
                bam.doMove(pgnMove.getBlack());
            }

        }

    }

    private void analyzePosition(BoardRepresentation board) {

      BoardPrinter.printSmallBoard(board);

        ParameterizedEvaluation evaluation = new ParameterizedEvaluation();

        Engine engine = new Engine();
        SearchParameter params=new SearchParameter(1000, 63);
        IterativeSearchResult result = engine.goIterative(params, new GameState(board), new GameContext());

        int eval = evaluation.eval(board, board.getSiteToMove().ordinal());
        boolean isEGEval=evaluation.isUsingEndgameFunction(board);

        System.out.printf("%s \t: %s %s %s: %s\n", FenComposer.buildFenPosition(board), isEGEval, eval, result.getSavedMove() , result.getRslt().toPvLogStr());

        // todo game abspielen, bei jedem zu: engine laufen lassen mit kurzer laufzeit und zug bzw. pv ausgeben lassen
        // ggf. welche eval function benutzt wurde usw...
        // e2e4: fen: pv, eval: normal, end game? eval wert

    }
}
