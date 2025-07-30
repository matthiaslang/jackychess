package org.mattlang.tuning.data.pgnparser;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.mattlang.jc.board.FigureType;
import org.mattlang.tuning.data.builder.EmptyPos;

class ParsedMoveTextTest {

    @Test
    public void testparsingd2d4() {
        ParsedMoveText pmt = new ParsedMoveText(new MoveText("d2d4", EmptyPos.EMPTYPOS));
        assertThat(pmt.getToIdx()).isEqualTo(27);
    }

    @Test
    public void testparsingd2_d4() {
        ParsedMoveText pmt = new ParsedMoveText(new MoveText("d2-d4", EmptyPos.EMPTYPOS));
        assertThat(pmt.getToIdx()).isEqualTo(27);
    }

    @Test
    public void testparsingd7_d8q() {
        ParsedMoveText pmt = new ParsedMoveText(new MoveText("d7-d8=R", EmptyPos.EMPTYPOS));
        assertThat(pmt.getToIdx()).isEqualTo(59);
        assertThat(pmt.isPromotion()).isTrue();
        assertThat(pmt.getPromotedFigureType()).isEqualTo(FigureType.Rook);
    }

}