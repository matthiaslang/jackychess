package org.mattlang.jc.chessTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mattlang.jc.chessTests.EigenmannRapidEngineChessIT.CHESS_SUITE_TEST_TIMEOUT;

import org.mattlang.jc.SearchParameter;
import org.mattlang.jc.TestPosition;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.Engine;
import org.mattlang.jc.engine.search.TimeoutException;
import org.mattlang.jc.uci.GameContext;

/**
 * Simple epd parsing (only relevant parts for our tests...)
 */

public class ChessTestRun {

    public static void testPosition(Engine engine, TestPosition testPosition) {
        System.out.println(testPosition.fen + " expected moves: " + testPosition.getExpectedBestMoves());

        GameState gameState = engine.getBoard().setFenPosition(testPosition.getFenPosition());
        System.out.println(engine.getBoard().toUniCodeStr());

        GameContext gameContext = new GameContext();
        int[] counter = new int[1];
        // listener: as soon as we have found the "right" move, we can stop the engine to safe time:
        engine.registerListener(bestMove -> {
            if (testPosition.getExpectedBestMoves().contains(bestMove)) {
                counter[0]++;
                throw new TimeoutException();
            }
        });
        SearchParameter searparams = new SearchParameter(CHESS_SUITE_TEST_TIMEOUT);

        Move move = engine.go(searparams, gameState, gameContext);
        gameContext.logStatistics();

        // if we havent already stopped because we have the expected move found, validate the result:
        if (counter[0] == 0) {
            System.out.println(move.toStr());
            assertThat(testPosition.getExpectedBestMoves()).contains(move);
        }

    }

}
