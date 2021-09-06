package org.mattlang.jc.engine.sorting;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.mattlang.jc.board.Figure;
import org.mattlang.jc.moves.MoveImpl;

public class MvvLvaTest {

    @Test
    public void testMvvLvaCalc() {
        assertThat(MvvLva.calcMMVLVA(Figure.W_Pawn.figureCode, (byte) 0)).isEqualTo(-100);
        assertThat(MvvLva.calcMMVLVA(Figure.W_Pawn.figureCode, Figure.B_Queen.figureCode)).isEqualTo(800);

        int movePawnQuiet= MoveImpl.createNormalMove(Figure.W_Pawn.figureCode, 7, 12, (byte) 0);
        assertThat(MvvLva.calcMMVLVA(movePawnQuiet)).isEqualTo(-100);
        assertThat(MvvLva.calcMMVLVA(new MoveImpl(movePawnQuiet))).isEqualTo(-100);

        int movePawnXQueen= MoveImpl.createNormalMove(Figure.W_Pawn.figureCode, 7, 12, Figure.B_Queen.figureCode);
        assertThat(MvvLva.calcMMVLVA(movePawnXQueen)).isEqualTo(800);
        assertThat(MvvLva.calcMMVLVA(new MoveImpl(movePawnXQueen))).isEqualTo(800);

    }
}