package org.mattlang.jc.chessTests;

import static org.assertj.core.api.Assertions.assertThat;

import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.Engine;
import org.mattlang.jc.engine.search.TimeoutException;
import org.mattlang.jc.uci.GameContext;
import org.mattlang.tuning.data.pgnparser.AlgebraicNotation;
import org.mattlang.tuning.data.pgnparser.MoveText;
import org.mattlang.tuning.data.pgnparser.TextPosition;

/**
 * Simple epd parsing (only relevant parts for our tests...)
 */

public class EpdParsing {

    public static void testPosition(Engine engine, String position, String expectedBestMove) {
        System.out.println(position + " " + expectedBestMove);

        // set fen position out of the epd description:
        // todo we are not able to parse epd correctly right now, but with that workaround we get it to work
        // as FEN:
        GameState gameState = engine.getBoard().setFenPosition("position fen " + position + " 0 0");
        System.out.println(engine.getBoard().toUniCodeStr());

        MoveText moveText = new MoveText(expectedBestMove, emptyPos());
        Move algExpectedMove = AlgebraicNotation.moveFromAN(gameState.getBoard(), gameState.getWho2Move(), moveText);

        GameContext gameContext = new GameContext();
        int[] counter = new int[1];
        // listener: as soon as we have found the "right" move, we can stop the engine to safe time:
        engine.registerListener(bestMove -> {
            if (bestMove.equals(algExpectedMove)) {
                counter[0]++;
                throw new TimeoutException();
            }
        });
        Move move = engine.go(gameState, gameContext);
        gameContext.logStatistics();

        // if we havent already stopped because we have the expected move found, validate the result:
        if (counter[0] == 0) {
            System.out.println(move.toStr());
            assertThat(move).isEqualTo(algExpectedMove);
        }

    }

    private static TextPosition emptyPos() {
        return new TextPosition() {

            @Override
            public int getLineNo() {
                return 0;
            }

            @Override
            public int getColNo() {
                return 0;
            }
        };
    }
}
