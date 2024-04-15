package org.mattlang.jc.material;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.bitboard.BitBoard;

public class MaterialTest {

    @Test
    public void matParsing() {
        Material m1 = new Material("B");
        Material m2 = new Material("KB");

        assertThat(m1.getMaterial()).isEqualTo(m2.getMaterial());

        BoardRepresentation board = new BitBoard();
        //        board.setStartPosition();
        board.setFenPosition("position fen kb6/8/8/8/4K3/8/8/B7 w - - 2 17 ");

        Material correspondingMat = new Material("kbKB");

        assertThat(correspondingMat.getMaterial()).isEqualTo(board.getMaterial().getMaterial());

    }

    @Test
    public void testConversions() {
        BoardRepresentation board = new BitBoard();
        //        board.setStartPosition();
        board.setFenPosition("position fen k7/4q3/8/8/4K3/8/8/B7 w - - 2 17 ");

        Material m = new Material();
        m.init(board);

        int wm = m.getWhiteMat();

        int bm = m.getBlackMat();

        assertThat(wm | bm).isEqualTo(m.getMaterial());

        int switched = m.switchSidesOnMaterial();

        Material switchedMat = new Material(switched);

        int switchedTwoTimes = switchedMat.switchSidesOnMaterial();

        assertThat(switchedTwoTimes).isEqualTo(m.getMaterial());

        int pieceMat = m.getPieceMat();

        int pawnMat = m.getPawnsMat();

        assertThat(pieceMat | pawnMat).isEqualTo(m.getMaterial());
    }

}