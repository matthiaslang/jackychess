package org.mattlang.jc.moves;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.mattlang.jc.board.Figure;
import org.mattlang.jc.board.bitboard.BitBoard;

public class MoveImplTest {

    @Test
    public void testLongConversion() {
        MoveImpl m = new MoveImpl(Figure.W_Pawn.figureCode, 10, 20, Figure.B_Bishop.figureCode);

        int l = m.toLongEncoded();

        MoveImpl m2 = new MoveImpl(l);
        assertThat(m2).isEqualTo(m);

        m = MoveImpl.createEnPassant(50, 60, Figure.B_Bishop.figureCode);
        l = m.toLongEncoded();

        m2 = new MoveImpl(l);
        assertThat(m2).isEqualTo(m);

        m = MoveImpl.createPromotion(0, 63, Figure.B_Queen.figureCode, Figure.W_Queen);
        l = m.toLongEncoded();

        m2 = new MoveImpl(l);
        assertThat(m2).isEqualTo(m);

        BitBoard board = new BitBoard();

        m = MoveImpl.createCastling(board.getBoardCastlings().getCastlingBlackLong());
        l = m.toLongEncoded();

        m2 = new MoveImpl(l);
        assertThat(m2).isEqualTo(m);
    }

    @Test
    public void tests() {
        byte b = -1;
        long l = ((long) b) << 32;
        long ll = (long) b << 32;

        System.out.println(l);
        System.out.println(ll);
    }

    @Test
    public void testBitshiftling(){
        testLongEncoding(500, 234);
        testLongEncoding(500, -234);
        testLongEncoding(-500, 234);
        testLongEncoding(-500, -234);
    }

    public void testLongEncoding(int x, int y){
        long l = (((long)x) << 32) | (y & 0xffffffffL);
        int xx = (int)(l >> 32);
        int yy = (int)l;
        assertThat(xx).isEqualTo(x);
        assertThat(yy).isEqualTo(y);
    }
}