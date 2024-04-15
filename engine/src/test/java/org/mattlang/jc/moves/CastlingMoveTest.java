package org.mattlang.jc.moves;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.Figure.*;
import static org.mattlang.jc.board.RochadeType.LONG;
import static org.mattlang.jc.board.RochadeType.SHORT;
import static org.mattlang.jc.moves.CastlingMove.createCastlingMove;
import static org.mattlang.jc.moves.MoveImpl.*;

import org.junit.Test;
import org.mattlang.jc.board.Figure;
import org.mattlang.jc.board.RochadeType;
import org.mattlang.jc.movegenerator.CastlingDef;

public class CastlingMoveTest {

    // the standard castling definitions
    private CastlingDef castlingLWhite = new CastlingDef(
            WHITE,
            LONG,
            new int[] { 0, 1, 2, 3, 4 },
            new Figure[] { W_Rook, EMPTY, EMPTY, EMPTY, W_King },
            new int[] { 2, 3, 4 }, 1L << 0, 1L << 4, 1L << 1 | 1L << 2 | 1L << 3);

    private CastlingDef castlingSWhite = new CastlingDef(
            WHITE,
            RochadeType.SHORT,
            new int[] { 4, 5, 6, 7 },
            new Figure[] { W_King, EMPTY, EMPTY, W_Rook },
            new int[] { 4, 5, 6 }, 1L << 7, 1L << 4, 1L << 5 | 1L << 7);

    private CastlingDef castlingSBlack = new CastlingDef(
            BLACK,
            RochadeType.SHORT,
            new int[] { 60, 61, 62, 63 },
            new Figure[] { B_King, EMPTY, EMPTY, B_Rook },
            new int[] { 60, 61, 62 }, 1L << 63, 1L << 60, 1L << 61 | 1L << 62);

    private CastlingDef castlingLBlack = new CastlingDef(
            BLACK,
            LONG,
            new int[] { 56, 57, 58, 59, 60 },
            new Figure[] { B_Rook, EMPTY, EMPTY, EMPTY, B_King },
            new int[] { 58, 59, 60 }, 1L << 56, 1L << 60, 1L << 57 | 1L << 58 | 1L << 59);

    private final CastlingMove castlingWhiteLong = new CastlingMove(castlingLWhite,
            CASTLING_WHITE_LONG, 4, 2, 0, 3);


    private final CastlingMove castlingWhiteShort = new CastlingMove(castlingSWhite,
            CASTLING_WHITE_SHORT, 4, 6, 7, 5);

    private final CastlingMove castlingBlackShort = new CastlingMove(castlingSBlack,
            CASTLING_BLACK_SHORT, 60, 62, 63, 61);

    private final CastlingMove castlingBlackLong = new CastlingMove(castlingLBlack,
            MoveImpl.CASTLING_BLACK_LONG, 60, 58, 56, 59);

    /**
     * Check that the creation method creates identical castling definitions
     */
    @Test
    public void testDefsCreation() {
        assertThat(createCastlingMove(WHITE, LONG, CASTLING_WHITE_LONG, 4, 2, 0, 3))
                .isEqualTo(castlingWhiteLong);

        assertThat(createCastlingMove(WHITE, SHORT, CASTLING_WHITE_SHORT, 4, 6, 7, 5))
                .isEqualTo(castlingWhiteShort);

        assertThat(createCastlingMove(BLACK, LONG, CASTLING_BLACK_LONG, 60, 58, 56, 59))
                .isEqualTo(castlingBlackLong);

        assertThat(createCastlingMove(BLACK, SHORT, CASTLING_BLACK_SHORT, 60, 62, 63, 61))
                .isEqualTo(castlingBlackShort);
    }

    @Test
    public void testTargets() {
        assertThat(CastlingMove.gWKingTargetPos).isEqualTo(6);
        assertThat(CastlingMove.gWRookTargetPos).isEqualTo(5);

        assertThat(CastlingMove.cWKingTargetPos).isEqualTo(2);
        assertThat(CastlingMove.cWRookTargetPos).isEqualTo(3);


        assertThat(CastlingMove.gBKingTargetPos).isEqualTo(62);
        assertThat(CastlingMove.gBRookTargetPos).isEqualTo(61);

        assertThat(CastlingMove.cBKingTargetPos).isEqualTo(58);
        assertThat(CastlingMove.cBRookTargetPos).isEqualTo(59);


        assertThat(createCastlingMove(BLACK, LONG, CASTLING_BLACK_LONG, 60, 58, 56, 59))
                .isEqualTo(castlingBlackLong);


    }
}