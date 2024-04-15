package org.mattlang.jc.board.bitboard;

import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.RochadeType.LONG;
import static org.mattlang.jc.board.RochadeType.SHORT;
import static org.mattlang.jc.moves.CastlingMove.createCastlingMove;
import static org.mattlang.jc.moves.MoveImpl.*;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.CastlingType;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.moves.CastlingMove;
import org.mattlang.jc.moves.MoveImpl;

/**
 * Contains the move-relevant classes for castlings.
 * They are initialized within a board to have it adjusted for frc-chess to have the proper castlings.
 */
public final class BoardCastlings {

    /**
     * The castling moves for regular chess are statically predefined, since they are used most often.
     * (This saves memory especially during tuning, to reuse the instances for all tuning positions).
     */
    public static final CastlingMove CASTLING_MOVE_WHITE_LONG = createCastlingMove(WHITE, LONG,
            MoveImpl.CASTLING_WHITE_LONG, 4, 2, 0, 3);
    public static final CastlingMove CASTLING_MOVE_WHITE_SHORT = createCastlingMove(WHITE, SHORT,
            CASTLING_WHITE_SHORT, 4, 6, 7, 5);
    public static final CastlingMove CASTLING_MOVE_BLACK_SHORT = createCastlingMove(BLACK, SHORT,
            CASTLING_BLACK_SHORT, 60, 62, 63, 61);
    public static final CastlingMove CASTLING_MOVE_BLACK_LONG = createCastlingMove(BLACK, LONG,
            MoveImpl.CASTLING_BLACK_LONG, 60, 58, 56, 59);
    private final BoardRepresentation board;

    public BoardCastlings(BoardRepresentation board) {
        this.board = board;
    }


    private CastlingMove castlingWhiteLong = CASTLING_MOVE_WHITE_LONG;

    private CastlingMove castlingWhiteShort = CASTLING_MOVE_WHITE_SHORT;

    private CastlingMove castlingBlackShort = CASTLING_MOVE_BLACK_SHORT;

    private CastlingMove castlingBlackLong = CASTLING_MOVE_BLACK_LONG;



    public void generateCastlingMoves(Color side, MoveList collector) {
        switch (side) {
        case WHITE:
            if (castlingWhiteLong.getDef().check(board)) {
                collector.addCastlingMove(castlingWhiteLong);
            }
            if (castlingWhiteShort.getDef().check(board)) {
                collector.addCastlingMove(castlingWhiteShort);
            }
            break;
        case BLACK:
            if (castlingBlackShort.getDef().check(board)) {
                collector.addCastlingMove(castlingBlackShort);
            }
            if (castlingBlackLong.getDef().check(board)) {
                collector.addCastlingMove(castlingBlackLong);
            }
            break;
        }
    }

    public CastlingMove getCastlingMove(byte type) {
        switch (type) {
        case CASTLING_WHITE_LONG:
            return castlingWhiteLong;
        case CASTLING_WHITE_SHORT:
            return castlingWhiteShort;
        case CASTLING_BLACK_SHORT:
            return castlingBlackShort;
        case CASTLING_BLACK_LONG:
            return castlingBlackLong;
        }
        throw new IllegalStateException("no castling move!");
    }

    public CastlingMove getCastlingWhiteLong() {
        return castlingWhiteLong;
    }

    public CastlingMove getCastlingWhiteShort() {
        return castlingWhiteShort;
    }

    public CastlingMove getCastlingBlackShort() {
        return castlingBlackShort;
    }

    public CastlingMove getCastlingBlackLong() {
        return castlingBlackLong;
    }

    public void setCastlingMove(CastlingType castlingType, CastlingMove castlingMove) {
        switch (castlingType) {
        case WHITE_SHORT:
            castlingWhiteShort = castlingMove;
            break;
        case WHITE_LONG:
            castlingWhiteLong = castlingMove;
            break;
        case BLACK_SHORT:
            castlingBlackShort = castlingMove;
            break;
        case BLACK_LONG:
            castlingBlackLong = castlingMove;
            break;
        }
    }

    public CastlingMove getCastlingMove(CastlingType castlingType) {
        switch (castlingType) {
        case WHITE_SHORT:
            return castlingWhiteShort;
        case WHITE_LONG:
            return castlingWhiteLong;
        case BLACK_SHORT:
            return castlingBlackShort;
        case BLACK_LONG:
            return castlingBlackLong;
        }
        throw new IllegalArgumentException("unknown castling type!");
    }

    public void initFrom(BoardCastlings boardCastlings) {
        this.castlingWhiteShort = boardCastlings.getCastlingWhiteShort();
        this.castlingWhiteLong = boardCastlings.getCastlingWhiteLong();
        this.castlingBlackShort = boardCastlings.getCastlingBlackShort();
        this.castlingBlackLong = boardCastlings.getCastlingBlackLong();
    }
}
