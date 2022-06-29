package org.mattlang.tuning.data.pgnparser;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.Test;

public class PgnParserTest {

    @Test
    public void testParsing() throws IOException {
        PgnParser parser = new PgnParser();

        InputStream in = PgnParserTest.class.getResourceAsStream("/testpgn/example.pgn");
        List<PgnGame> games = parser.parse(in);

        assertThat(games).hasSize(400);
    }
}