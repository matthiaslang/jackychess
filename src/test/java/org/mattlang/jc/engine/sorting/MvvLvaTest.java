package org.mattlang.jc.engine.sorting;

import static org.mattlang.jc.engine.sorting.MvvLva.calcMMVLVA;
import static org.mattlang.jc.moves.MoveImpl.createNormalMove;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.mattlang.jc.board.Figure;
import org.mattlang.jc.board.FigureType;
import org.mattlang.jc.moves.MoveImpl;

public class MvvLvaTest {

    @Test
    public void testMvvLvaCalc() {
        SoftAssertions softly = new SoftAssertions();

        int movePawnQuiet = createNormalMove(FigureType.Pawn.figureCode, 7, 12, (byte) 0);

        softly.assertThat(calcMMVLVA(new MoveImpl(movePawnQuiet))).isEqualTo(-1);

        int movePawnXQueen = createNormalMove(Figure.W_Pawn.figureCode, 7, 12, Figure.B_Queen.figureCode);

        softly.assertThat(calcMMVLVA(new MoveImpl(movePawnXQueen))).isEqualTo(29);

        softly.assertAll();
    }
}