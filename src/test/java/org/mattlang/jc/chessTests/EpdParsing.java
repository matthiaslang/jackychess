package org.mattlang.jc.chessTests;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.mattlang.jc.TestPosition;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.Engine;
import org.mattlang.jc.uci.GameContext;
import org.mattlang.tuning.data.pgnparser.AlgebraicNotation;
import org.mattlang.tuning.data.pgnparser.MoveText;
import org.mattlang.tuning.data.pgnparser.TextPosition;

/**
 * Simple epd parsing (only relevant parts for our tests...)
 */

public class EpdParsing {

    public static List<String[]> getEPDTests(String rawEpds) {
        List<String> epds = Arrays.asList(rawEpds.split("\n"));
        return epds.stream().map(epd -> splitEpd(epd)).collect(Collectors.toList());
    }

    public static List<TestPosition> convertTests(String rawEpds) {
        return getEPDTests(rawEpds).stream()
                .map(arr -> new TestPosition(arr[0], arr[1], arr[2]))
                .collect(Collectors.toList());
    }

    private static String[] splitEpd(String epd) {
        String[] split = epd.split("bm ");
        if (split.length != 2) {
            split = epd.split("am ");
        }
        if (split.length != 2) {
            System.out.println(epd);
        }
        String position = split[0];
        String cmdPart = split[1];
        String[] cmds = cmdPart.split(";");
        String expectedBestMove = cmds[0];
        String testName = cmds[1];
        return new String[]{position, expectedBestMove, testName};
    }







    public static void testPosition(Engine engine, String position, String expectedBestMove) {
        System.out.println(position + " " + expectedBestMove);



        // set fen position out of the epd description:
        // todo we are not able to parse epd correctly right now, but with that workaround we get it to work
        // as FEN:
        GameState gameState = engine.getBoard().setFenPosition("position fen " + position + " 0 0");
        System.out.println(engine.getBoard().toUniCodeStr());
        Move move = engine.go(gameState, new GameContext());

        System.out.println(move.toStr());

        MoveText moveText=new MoveText(expectedBestMove, emptyPos());
        Move algExpectedMove=AlgebraicNotation.moveFromAN(gameState.getBoard(), gameState.getWho2Move(), moveText);

        assertThat(move).isEqualTo(algExpectedMove);

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
