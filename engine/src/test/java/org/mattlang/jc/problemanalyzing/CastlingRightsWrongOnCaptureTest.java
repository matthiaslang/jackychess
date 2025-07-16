package org.mattlang.jc.problemanalyzing;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.mattlang.jc.board.*;
import org.mattlang.jc.engine.Engine;
import org.mattlang.jc.uci.UCI;
import org.mattlang.jc.util.Logging;
import org.mattlang.tuning.data.pgnparser.AlgebraicNotation;
import org.mattlang.tuning.data.pgnparser.MoveText;
import org.mattlang.tuning.data.pgnparser.TextPosition;

public class CastlingRightsWrongOnCaptureTest {

    @Test
    public void analyzeCastlingRightsWrongWhenCapturingARook() throws IOException {

        Logging.initLogging();
        UCI.instance.attachStreams();

        // now starting engine:
        Engine engine = new Engine();
        GameState gameState = engine.getBoard()
                .setFenPosition("position fen rnbqrbk1/2pn1ppp/1p2p3/2PpP3/1P1P4/2N1B3/4NPPP/R2QKB1R b KQ - - -");
        System.out.println(engine.getBoard().toUniCodeStr());
        BoardRepresentation board = gameState.getBoard();

        Move move = AlgebraicNotation.moveFromAN(board, Color.BLACK, new MoveText("Rxa1", emptyPos()));
        board.domove(move);

        assertThat(board.isCastlingAllowed(CastlingType.WHITE_LONG)).isFalse();
        assertThat(board.isCastlingAllowed(CastlingType.WHITE_SHORT)).isTrue();

    }

    public static TextPosition emptyPos() {
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
