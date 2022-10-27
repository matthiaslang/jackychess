package org.mattlang.jc.board;

import static org.mattlang.jc.board.CastlingRights.*;
import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.RochadeType.LONG;
import static org.mattlang.jc.board.RochadeType.SHORT;
import static org.mattlang.jc.moves.CastlingMove.*;
import static org.mattlang.jc.moves.MoveImpl.*;

import lombok.Getter;

@Getter
public enum CastlingType {

    WHITE_LONG(WHITE, LONG, CASTLING_WHITE_LONG, cWKingTargetPos, cWRookTargetPos, WHITE_LONG_MASK),
    WHITE_SHORT(WHITE, SHORT, CASTLING_WHITE_SHORT, gWKingTargetPos, gWRookTargetPos, WHITE_SHORT_MASK),

    BLACK_LONG(BLACK, LONG, CASTLING_BLACK_LONG, cBKingTargetPos, cBRookTargetPos, BLACK_LONG_MASK),
    BLACK_SHORT(BLACK, SHORT, CASTLING_BLACK_SHORT, gBKingTargetPos, gBRookTargetPos, BLACK_SHORT_MASK);

    private final Color color;

    private final RochadeType rochadeType;

    private final byte castlingMoveType;

    private final int kingTargetPos;

    private final int rookTargetPos;

    private final byte rightsMask;

    CastlingType(Color color, RochadeType rochadeType, byte castlingMoveType, int kingTargetPos, int rookTargetPos,
            byte rightsMask) {
        this.color = color;
        this.rochadeType = rochadeType;
        this.castlingMoveType = castlingMoveType;
        this.kingTargetPos = kingTargetPos;
        this.rookTargetPos = rookTargetPos;
        this.rightsMask = rightsMask;
    }

    public static CastlingType of(Color color, RochadeType rochadeType) {
        for (CastlingType castlingType : CastlingType.values()) {
            if (castlingType.getColor() == color && castlingType.getRochadeType() == rochadeType) {
                return castlingType;
            }
        }
        throw new IllegalStateException("no matching castling type!");
    }
}
