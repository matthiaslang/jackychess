package org.mattlang.tuning.data.pgnparser;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mattlang.tuning.data.builder.BoardAndMoves;
import org.mattlang.tuning.data.builder.PgnGameBuilder;
import org.mattlang.tuning.data.pgnwriter.PgnWriter;

public class PgnParserTest {

    @Test
    public void testParsing() throws IOException {
        PgnParser parser = new PgnParser();

        InputStream in = PgnParserTest.class.getResourceAsStream("/testpgn/example.pgn");
        List<PgnGame> games = parser.parse(in);

        assertThat(games).hasSize(400);

        // try the pgn builder and write functionality:
        List<PgnGame> buildedGames = new ArrayList<>();
        for (PgnGame game : games) {
            BoardAndMoves boardAndMoves = new BoardAndMoves(game);
            PgnGame buildedGame = PgnGameBuilder.from(boardAndMoves).toGame();
            buildedGames.add(buildedGame);
        }

        PgnWriter pgnWriter = new PgnWriter();
        pgnWriter.writeGames(new File("outputpgn.pgn"), buildedGames);

    }

    @Test
    public void testWrite() {
//        PgnWriter writer = new PgnWriter();
//        PgnGame game;
//        String str = writer.gameToString(game);
    }
}