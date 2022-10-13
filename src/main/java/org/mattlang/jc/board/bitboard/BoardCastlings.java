package org.mattlang.jc.board.bitboard;

import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.RochadeType.LONG;
import static org.mattlang.jc.board.RochadeType.SHORT;
import static org.mattlang.jc.moves.CastlingMove.createCastlingMove;
import static org.mattlang.jc.moves.MoveImpl.*;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.movegenerator.MoveCollector;
import org.mattlang.jc.moves.CastlingMove;
import org.mattlang.jc.moves.MoveImpl;

/**
 * Contains the move-relevant classes for castlings.
 * They are initialized within a board to have it adjusted for frc-chess to have the proper castlings.
 */
public final class BoardCastlings {

    private BoardRepresentation board;

    public BoardCastlings(BoardRepresentation board) {
        this.board = board;
    }

    private final CastlingMove castlingWhiteLong = createCastlingMove(WHITE, LONG,
            CASTLING_WHITE_LONG, 4, 2, 0, 3);

    private final CastlingMove castlingWhiteShort = createCastlingMove(WHITE, SHORT,
            CASTLING_WHITE_SHORT, 4, 6, 7, 5);

    private final CastlingMove castlingBlackShort = createCastlingMove(BLACK, SHORT,
            CASTLING_BLACK_SHORT, 60, 62, 63, 61);

    private final CastlingMove castlingBlackLong = createCastlingMove(BLACK, LONG,
            MoveImpl.CASTLING_BLACK_LONG, 60, 58, 56, 59);



    public void generateCastlingMoves(Color side, MoveCollector collector) {
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

}
