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

    private Color color;

    private RochadeType rochadeType;

    private byte castlingMoveType;

    private int kingTargetPos;

    private int rookTargetPos;

    CastlingType(Color color, RochadeType rochadeType, byte castlingMoveType, int kingTargetPos, int rookTargetPos) {
        this.color = color;
        this.rochadeType = rochadeType;
        this.castlingMoveType = castlingMoveType;
        this.kingTargetPos = kingTargetPos;
        this.rookTargetPos = rookTargetPos;
    }
}
