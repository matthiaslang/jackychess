package org.mattlang.jc.board;

import static org.mattlang.jc.board.CastlingFields.*;
import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.RochadeType.LONG;
import static org.mattlang.jc.board.RochadeType.SHORT;
import static org.mattlang.jc.moves.MoveImpl.*;

import lombok.Getter;

@Getter
public enum CastlingType {

    WHITE_LONG(WHITE, LONG, CASTLING_WHITE_LONG_TYPE, cWKingTargetPos, cWRookTargetPos),
    WHITE_SHORT(WHITE, SHORT, CASTLING_WHITE_SHORT_TYPE, gWKingTargetPos, gWRookTargetPos),

    BLACK_LONG(BLACK, LONG, CASTLING_BLACK_LONG_TYPE, cBKingTargetPos, cBRookTargetPos),
    BLACK_SHORT(BLACK, SHORT, CASTLING_BLACK_SHORT_TYPE, gBKingTargetPos, gBRookTargetPos);

    private final int color;

    private final RochadeType rochadeType;

    private final byte castlingMoveType;

    private final int kingTargetPos;

    private final int rookTargetPos;

    private final byte castlingBitMask;

    CastlingType(Color color, RochadeType rochadeType, byte castlingMoveType, int kingTargetPos, int rookTargetPos) {
        this.color = color.ordinal();
        this.rochadeType = rochadeType;
        this.castlingMoveType = castlingMoveType;
        this.kingTargetPos = kingTargetPos;
        this.rookTargetPos = rookTargetPos;
        this.castlingBitMask = createMask(color, rochadeType);
    }

    public static CastlingType of(int color, RochadeType rochadeType) {
        for (CastlingType castlingType : CastlingType.values()) {
            if (castlingType.getColor() == color && castlingType.getRochadeType() == rochadeType) {
                return castlingType;
            }
        }
        throw new IllegalStateException("no matching castling type!");
    }

    private static byte createMask(Color color, RochadeType type) {
        int idx = type == SHORT ? 0 : 1;
        if (color == Color.BLACK) {
            idx += 2;
        }
        byte allowedMask = (byte) (1 << idx);
        return allowedMask;
    }
}
