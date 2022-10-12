package org.mattlang.jc.board.bitboard;

import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.Figure.*;
import static org.mattlang.jc.moves.MoveImpl.*;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Figure;
import org.mattlang.jc.board.RochadeType;
import org.mattlang.jc.movegenerator.CastlingDef;
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

    private CastlingDef castlingLWhite = new CastlingDef(
            WHITE,
            RochadeType.LONG,
            new int[] { 0, 1, 2, 3, 4 },
            new Figure[] { W_Rook, EMPTY, EMPTY, EMPTY, W_King },
            new int[] { 2, 3, 4 });

    private final CastlingMove castlingWhiteLong = new CastlingMove(castlingLWhite,
            CASTLING_WHITE_LONG, 4, 2, 0, 3);

    private CastlingDef castlingSWhite = new CastlingDef(
            WHITE,
            RochadeType.SHORT,
            new int[] { 4, 5, 6, 7 },
            new Figure[] { W_King, EMPTY, EMPTY, W_Rook },
            new int[] { 4, 5, 6 });

    private CastlingDef castlingSBlack = new CastlingDef(
            BLACK,
            RochadeType.SHORT,
            new int[] { 60, 61, 62, 63 },
            new Figure[] { B_King, EMPTY, EMPTY, B_Rook },
            new int[] { 60, 61, 62 });

    private CastlingDef castlingLBlack = new CastlingDef(
            BLACK,
            RochadeType.LONG,
            new int[] { 56, 57, 58, 59, 60 },
            new Figure[] { B_Rook, EMPTY, EMPTY, EMPTY, B_King },
            new int[] { 58, 59, 60 });



    private final CastlingMove castlingWhiteShort = new CastlingMove(castlingSWhite,
            CASTLING_WHITE_SHORT, 4, 6, 7, 5);

    private final CastlingMove castlingBlackShort = new CastlingMove(castlingSBlack,
            CASTLING_BLACK_SHORT, 60, 62, 63, 61);

    private final CastlingMove castlingBlackLong = new CastlingMove(castlingLBlack,
            MoveImpl.CASTLING_BLACK_LONG, 60, 58, 56, 59);



    public void generateCastlingMoves(Color side, MoveCollector collector) {
        switch (side) {
        case WHITE:
            if (castlingLWhite.check(board)) {
                collector.addCastlingMove(castlingWhiteLong);
            }
            if (castlingSWhite.check(board)) {
                collector.addCastlingMove(castlingWhiteShort);
            }
            break;
        case BLACK:
            if (castlingSBlack.check(board)) {
                collector.addCastlingMove(castlingBlackShort);
            }
            if (castlingLBlack.check(board)) {
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
