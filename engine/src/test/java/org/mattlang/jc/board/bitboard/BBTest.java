package org.mattlang.jc.board.bitboard;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mattlang.jc.board.Color.nBlack;
import static org.mattlang.jc.board.Color.nWhite;
import static org.mattlang.jc.board.bitboard.BB.*;
import static org.mattlang.jc.engine.evaluation.parameval.mobility.MobilityEvalResult.BLACK_KNIGHT_STARTPOS;

import org.junit.Test;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.evaluation.Tools;
import org.mattlang.jc.engine.evaluation.parameval.KingZoneMasks;

public class BBTest {

    @Test
    public void testFileFill() {
        long rookMask = 1L << 63;

        long fileFilled = BB.fileFill(rookMask);

        System.out.println("Pattern \n" + BitChessBoard.toStr(fileFilled));
    }

    @Test
    public void testKingZone() {
        long kingMask = 1L << 63;

        System.out.println("Pattern \n" + BitChessBoard.toStr(kingMask));
        long fileFilled = KingZoneMasks.createKingZoneMask(63, kingMask, Color.WHITE);

        System.out.println("Pattern \n" + BitChessBoard.toStr(fileFilled));

        fileFilled = KingZoneMasks.createKingZoneMask(63, kingMask, Color.BLACK);

        System.out.println("Pattern \n" + BitChessBoard.toStr(fileFilled));

    }

    @Test
    public void testKingZone2() {
        long kingMask = 1L << 1;

        System.out.println("Pattern \n" + BitChessBoard.toStr(kingMask));
        long fileFilled = KingZoneMasks.createKingZoneMask(1,kingMask, Color.WHITE);

        System.out.println("Pattern \n" + BitChessBoard.toStr(fileFilled));

        fileFilled = KingZoneMasks.createKingZoneMask(1,kingMask, Color.BLACK);

        System.out.println("Pattern \n" + BitChessBoard.toStr(fileFilled));

    }

    @Test
    public void testPatterns() {
        long A = 0x0101010101010101L;
        long B = 0x0202020202020202L;
        long C = 0x0404040404040404L;
        long D = 0x0808080808080808L;
        long E = 0x1010101010101010L;
        long F = 0x2020202020202020L;
        long G = 0x4040404040404040L;
        long H = 0x8080808080808080L;

        System.out.println("Pattern \n" + BitChessBoard.toStr(BLACK_KNIGHT_STARTPOS));
    }

    @Test
    public void testPatterns2() {
        long l = 0b0000000000000000L;
        System.out.println("Pattern \n" + BitChessBoard.toStr(BB.FGH_File));
    }

    @Test
    public void testKingAttacks() {

        for (int i = 0; i < 64; i++) {
            System.out.println("King pos \n" + BitChessBoard.toStr(1L << i));
            System.out.println("Attacks: \n" + BitChessBoard.toStr(BB.kingAttacks(1L << i)));
        }

    }

    @Test
    public void southOne() {
        for (int i = 0; i < 64; i++) {
            System.out.println("pos \n" + BitChessBoard.toStr(1L << i));
            System.out.println("South one: \n" + BitChessBoard.toStr(BB.soutOne(1L << i)));
        }
    }

    @Test
    public void testSouth() {
        long pos = 1L << 62;

        System.out.println("pos \n" + BitChessBoard.toStr(pos));
        System.out.println("South one: \n" + BitChessBoard.toStr(BB.noWeOne(pos)));
    }

    @Test
    public void fieldsBitBoardRepresentation() {

        BitBoard board = new BitBoard();
        board.setFenPosition("position fen K7/8/p7/8/8/7P/8/r6k w - - 1 56 ");
        board.println();

        long wpawns = board.getBoard().getPawns(nWhite);
        long brooks = board.getBoard().getRooks(nBlack);
        System.out.println(BB.toStrBoard(wpawns));

        long wpawnsByFields = BB.H3;
        System.out.println(BB.toStrBoard(wpawnsByFields));
        assertThat(wpawns).isEqualTo(wpawnsByFields);

        assertThat(brooks).isEqualTo(A1);

    }

    @Test
    public void fieldsConsistencyCheck() {
        // some sample field tests:
        assertThat(BB.rank1 & BB.A).isEqualTo(A1);
        assertThat(BB.rank3 & BB.C).isEqualTo(C3);
        assertThat(BB.rank7 & BB.F).isEqualTo(F7);

        for (File file : File.values()) {
            for (Rank rank : Rank.values()) {
                int square = Tools.makeSquare(file, rank);

                assertThat(file.fileMask & rank.rankMask).isEqualTo(1L << square);
                Fields field = Fields.find(file, rank);
                assertThat(file.fileMask & rank.rankMask).isEqualTo(1L << field.square);

                assertThat(Tools.file_bb_ofSquare(field.square)).isEqualTo(file.fileMask);
            }
        }

    }
}