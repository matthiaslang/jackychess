package org.mattlang.jc.moves;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mattlang.jc.board.CastlingType.*;
import static org.mattlang.jc.moves.CastlingMove.createCastlingMove;

import org.junit.Test;
import org.mattlang.jc.movegenerator.CastlingDef;

public class CastlingMoveTest {

    // the standard castling definitions
    private CastlingDef castlingLWhite = new CastlingDef(
            WHITE_LONG,
            new int[] { 2, 3, 4 }, 1L << 0, 1L << 4, 1L << 1 | 1L << 2 | 1L << 3);

    private CastlingDef castlingSWhite = new CastlingDef(
            WHITE_SHORT,
            new int[] { 4, 5, 6 }, 1L << 7, 1L << 4, 1L << 5 | 1L << 6);

    private CastlingDef castlingSBlack = new CastlingDef(
            BLACK_SHORT,
            new int[] { 60, 61, 62 }, 1L << 63, 1L << 60, 1L << 61 | 1L << 62);

    private CastlingDef castlingLBlack = new CastlingDef(
            BLACK_LONG,
            new int[] { 58, 59, 60 }, 1L << 56, 1L << 60, 1L << 57 | 1L << 58 | 1L << 59);

    private final CastlingMove castlingWhiteLong = new CastlingMove(castlingLWhite,
            4, 2, 0, 3);

    private final CastlingMove castlingWhiteShort = new CastlingMove(castlingSWhite,
            4, 6, 7, 5);

    private final CastlingMove castlingBlackShort = new CastlingMove(castlingSBlack,
            60, 62, 63, 61);

    private final CastlingMove castlingBlackLong = new CastlingMove(castlingLBlack,
            60, 58, 56, 59);

    /**
     * Check that the creation method creates identical castling definitions
     */
    @Test
    public void testDefsCreation() {
        assertThat(createCastlingMove(WHITE_LONG, 4, 2, 0, 3))
                .isEqualTo(castlingWhiteLong);

        assertThat(createCastlingMove(WHITE_SHORT, 4, 6, 7, 5))
                .isEqualTo(castlingWhiteShort);

        assertThat(createCastlingMove(BLACK_LONG, 60, 58, 56, 59))
                .isEqualTo(castlingBlackLong);

        assertThat(createCastlingMove(BLACK_SHORT, 60, 62, 63, 61))
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

        assertThat(createCastlingMove(BLACK_LONG, 60, 58, 56, 59))
                .isEqualTo(castlingBlackLong);

    }
}