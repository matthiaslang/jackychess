package org.mattlang.jc.uci;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;

import org.junit.Test;

public class UciStringParserTest {

    @Test
    public void testMatching() {
        UciStringParser parser = new UciStringParser("this   is a test of parsing  uci strings   ");

        assertThat(parser.hasNext()).isTrue();

        assertThat(parser.match("this")).isTrue();
        assertThat(parser.match("is")).isTrue();
        assertThat(parser.match("a")).isTrue();
        assertThat(parser.match("test")).isTrue();
        assertThat(parser.match("of")).isTrue();
        assertThat(parser.match("parsing")).isTrue();
        assertThat(parser.match("uci")).isTrue();
        assertThat(parser.match("strings")).isTrue();

        assertThat(parser.hasNext()).isFalse();
    }

    @Test
    public void testFenstring() {
        UciStringParser parser = new UciStringParser(
                "position fen bb1qr1kr/p3pppp/1n1p2n1/1pp5/4PP2/4N3/PPPP3P/BBNQRK1R b - - moves e7e6");

        assertThat(parser.hasNext()).isTrue();

        assertThat(parser.match("position")).isTrue();
        assertThat(parser.match("fen")).isTrue();

        String pos = parser.match();
        assertThat(pos).isEqualTo("bb1qr1kr/p3pppp/1n1p2n1/1pp5/4PP2/4N3/PPPP3P/BBNQRK1R");
        String color = parser.match();
        assertThat(color).isEqualTo("b");
        assertThat(parser.match("moves")).isFalse();

        String num1 = parser.match();
        assertThat(num1).isEqualTo("-");
        String num2 = parser.match();
        assertThat(num2).isEqualTo("-");
        assertThat(parser.match("moves")).isTrue();
        assertThat(parser.match()).isEqualTo("e7e6");
    }

    @Test
    public void testHasNext() {
        UciStringParser parser = new UciStringParser("a b c");

        ArrayList<String> result=new ArrayList<>();

        while(parser.hasNext()){
            result.add(parser.match());
        }

        assertThat(result.size()).isEqualTo(3);

    }
}