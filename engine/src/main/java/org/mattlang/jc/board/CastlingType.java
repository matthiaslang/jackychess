package org.mattlang.jc.board;

import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.RochadeType.LONG;
import static org.mattlang.jc.board.RochadeType.SHORT;
import static org.mattlang.jc.moves.CastlingMove.*;
import static org.mattlang.jc.moves.MoveImpl.*;

import lombok.Getter;

@Getter
public enum CastlingType {

    WHITE_LONG(WHITE, LONG, CASTLING_WHITE_LONG, cWKingTargetPos, cWRookTargetPos),
    WHITE_SHORT(WHITE, SHORT, CASTLING_WHITE_SHORT, gWKingTargetPos, gWRookTargetPos),

    BLACK_LONG(BLACK, LONG, CASTLING_BLACK_LONG, cBKingTargetPos, cBRookTargetPos),
    BLACK_SHORT(BLACK, SHORT, CASTLING_BLACK_SHORT, gBKingTargetPos, gBRookTargetPos);

    private final Color color;

    private final RochadeType rochadeType;

    private final byte castlingMoveType;

    private final int kingTargetPos;

    private final int rookTargetPos;

    private final byte castlingBitMask;

    CastlingType(Color color, RochadeType rochadeType, byte castlingMoveType, int kingTargetPos, int rookTargetPos) {
        this.color = color;
        this.rochadeType = rochadeType;
        this.castlingMoveType = castlingMoveType;
        this.kingTargetPos = kingTargetPos;
        this.rookTargetPos = rookTargetPos;
        this.castlingBitMask = createMask(color, rochadeType);
    }

    public static CastlingType of(Color color, RochadeType rochadeType) {
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
