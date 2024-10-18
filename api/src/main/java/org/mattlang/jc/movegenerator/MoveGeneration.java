
package org.mattlang.jc.movegenerator;

import static org.mattlang.jc.board.BB.getKingAttacs;
import static org.mattlang.jc.board.BB.getKnightAttacs;
import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.FigureConstants.*;
import static org.mattlang.jc.board.bitboard.MagicBitboards.genBishopAttacs;
import static org.mattlang.jc.board.bitboard.MagicBitboards.genRookAttacs;

import org.mattlang.jc.board.BB;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BitChessBoard;
import org.mattlang.jc.board.bitboard.BoardCastlings;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.moves.CastlingMove;

/**
 * Bitboard move generation used in staged move generation.
 */
public final class MoveGeneration {

    public static void generateQuiets(BoardRepresentation board, Color side, MoveList collector) {

        BitChessBoard bb = board.getBoard();

        Color xside = side.invert();  /* the side not to move */

        long ownFigsMask = bb.getColorMask(xside.invert());
        long opponentFigsMask = bb.getColorMask(xside);
        long empty = ~ownFigsMask & ~opponentFigsMask;

        genPawnMoves(bb, empty, collector, side);

        long occupancy = ownFigsMask | opponentFigsMask;

        long bishopBB = bb.getPieceSet(FT_BISHOP, side);
        while (bishopBB != 0) {
            final int bishop = Long.numberOfTrailingZeros(bishopBB);

            long quiet = genBishopAttacs(bishop, occupancy) & empty;
            collector.genQuietMoves(FT_BISHOP, bishop, quiet);

            bishopBB &= bishopBB - 1;
        }

        long knightBB = bb.getPieceSet(FT_KNIGHT, side);
        while (knightBB != 0) {
            final int knight = Long.numberOfTrailingZeros(knightBB);

            long quietMoves = getKnightAttacs(knight) & empty;
            collector.genQuietMoves(FT_KNIGHT, knight, quietMoves);

            knightBB &= knightBB - 1;
        }

        long rookBB = bb.getPieceSet(FT_ROOK, side);
        while (rookBB != 0) {
            final int rook = Long.numberOfTrailingZeros(rookBB);

            long quiet = genRookAttacs(rook, occupancy) & empty;
            collector.genQuietMoves(FT_ROOK, rook, quiet);

            rookBB &= rookBB - 1;
        }

        long queenBB = bb.getPieceSet(FT_QUEEN, side);
        while (queenBB != 0) {
            final int queen = Long.numberOfTrailingZeros(queenBB);

            long quiet = (genRookAttacs(queen, occupancy) | genBishopAttacs(queen, occupancy)) & empty;
            collector.genQuietMoves(FT_QUEEN, queen, quiet);

            queenBB &= queenBB - 1;
        }

        long kingBB = bb.getPieceSet(FT_KING, side);
        final int king = Long.numberOfTrailingZeros(kingBB);

        long quietMoves = getKingAttacs(king) & empty;
        collector.genQuietMoves(FT_KING, king, quietMoves);

        generateCastlingMoves(board, board.getBoardCastlings(), side, collector);
    }


    private static void generateCastlingMoves(BoardRepresentation board, BoardCastlings boardCastlings, Color side, MoveList collector) {
        switch (side) {
        case WHITE:
            addCheckCastling(board, boardCastlings.getCastlingWhiteLong(), collector);
            addCheckCastling(board, boardCastlings.getCastlingWhiteShort(), collector);

            break;
        case BLACK:
            addCheckCastling(board, boardCastlings.getCastlingBlackShort(), collector);
            addCheckCastling(board, boardCastlings.getCastlingBlackLong(), collector);
            break;
        }
    }

    private static void addCheckCastling(BoardRepresentation board,CastlingMove castlingMove,MoveList collector) {
        if (castlingMove.getDef().check(board)) {
            collector.addCastlingMove(castlingMove);
        }
    }


    public static void generateAttacks(BoardRepresentation board, Color side, MoveList collector) {

        BitChessBoard bb = board.getBoard();

        Color xside = side.invert();  /* the side not to move */

        long ownFigsMask = bb.getColorMask(xside.invert());
        long opponentFigsMask = bb.getColorMask(xside);
        long occupancy = ownFigsMask | opponentFigsMask;

        genPawnCaptureMoves(board, collector, side);

        long bishopBB = bb.getPieceSet(FT_BISHOP, side);
        while (bishopBB != 0) {
            final int bishop = Long.numberOfTrailingZeros(bishopBB);

            long attacks = genBishopAttacs(bishop, occupancy);
            genAttacks(collector, FT_BISHOP, bishop, attacks & opponentFigsMask, bb);

            bishopBB &= bishopBB - 1;
        }

        long knightBB = bb.getPieceSet(FT_KNIGHT, side);
        while (knightBB != 0) {
            final int knight = Long.numberOfTrailingZeros(knightBB);

            long attacks = getKnightAttacs(knight) & opponentFigsMask;
            genAttacks(collector, FT_KNIGHT, knight, attacks, bb);

            knightBB &= knightBB - 1;
        }

        long rookBB = bb.getPieceSet(FT_ROOK, side);
        while (rookBB != 0) {
            final int rook = Long.numberOfTrailingZeros(rookBB);

            long attacks = genRookAttacs(rook, occupancy);
            genAttacks(collector, FT_ROOK, rook, attacks & opponentFigsMask, bb);

            rookBB &= rookBB - 1;
        }

        long queenBB = bb.getPieceSet(FT_QUEEN, side);
        while (queenBB != 0) {
            final int queen = Long.numberOfTrailingZeros(queenBB);

            long attacks = genRookAttacs(queen, occupancy) | genBishopAttacs(queen, occupancy);
            genAttacks(collector, FT_QUEEN, queen, attacks & opponentFigsMask, bb);

            queenBB &= queenBB - 1;
        }

        long kingBB = bb.getPieceSet(FT_KING, side);
        final int king = Long.numberOfTrailingZeros(kingBB);

        long attacks = getKingAttacs(king) & opponentFigsMask;
        genAttacks(collector, FT_KING, king, attacks, bb);

    }

    public static void genPawnCaptureMoves(BoardRepresentation bitBoard, MoveList collector, Color side) {
        BitChessBoard bb = bitBoard.getBoard();
        long pawns = bb.getPieceSet(FT_PAWN, side);

        long otherPieces = side == WHITE ? bb.getColorMask(BLACK) : bb.getColorMask(WHITE);

        if (side == WHITE) {
            long capturesEast = pawns & BB.bPawnWestAttacks(otherPieces);

            genPawnCaptures(collector, bb, capturesEast, 9);

            long capturesWest = pawns & BB.bPawnEastAttacks(otherPieces);

            genPawnCaptures(collector, bb, capturesWest, 7);

            // en passant:
            if (bitBoard.getEnPassantMoveTargetPos() >= 0) {
                long epMask = 1L << bitBoard.getEnPassantMoveTargetPos();

                capturesEast = pawns & BB.bPawnWestAttacks(epMask);

                while (capturesEast != 0) {
                    final int fromIndex = Long.numberOfTrailingZeros(capturesEast);
                    collector.genEnPassant(fromIndex, fromIndex + 9);
                    capturesEast &= capturesEast - 1;
                }

                capturesWest = pawns & BB.bPawnEastAttacks(epMask);

                while (capturesWest != 0) {
                    final int fromIndex = Long.numberOfTrailingZeros(capturesWest);
                    collector.genEnPassant(fromIndex, fromIndex + 7);
                    capturesWest &= capturesWest - 1;
                }

            }

        } else {
            long capturesEast = pawns & BB.wPawnWestAttacks(otherPieces);

            genPawnCaptures(collector, bb, capturesEast, -7);

            long capturesWest = pawns & BB.wPawnEastAttacks(otherPieces);
            genPawnCaptures(collector, bb, capturesWest, -9);

            // en passant:
            if (bitBoard.getEnPassantMoveTargetPos() >= 0) {
                long epMask = 1L << bitBoard.getEnPassantMoveTargetPos();

                capturesEast = pawns & BB.wPawnWestAttacks(epMask);

                while (capturesEast != 0) {
                    final int fromIndex = Long.numberOfTrailingZeros(capturesEast);
                    collector.genEnPassant(fromIndex, fromIndex - 7);
                    capturesEast &= capturesEast - 1;
                }

                capturesWest = pawns & BB.wPawnEastAttacks(epMask);

                while (capturesWest != 0) {
                    final int fromIndex = Long.numberOfTrailingZeros(capturesWest);
                    collector.genEnPassant(fromIndex, fromIndex - 9);
                    capturesWest &= capturesWest - 1;
                }
            }
        }
    }

    public static void genPawnMoves(BitChessBoard bb, long empty, MoveList collector, Color side) {
        long pawns = bb.getPieceSet(FT_PAWN, side);

        if (side == WHITE) {
            long singlePushTargets = BB.wSinglePushTargets(pawns, empty);

            collector.genPawnQuiets(singlePushTargets, -8);

            long doublePushTargets = BB.wDblPushTargets(pawns, empty);
            collector.genPawnQuiets(doublePushTargets, -16);

        } else {
            long singlePushTargets = BB.bSinglePushTargets(pawns, empty);

            collector.genPawnQuiets(singlePushTargets, 8);

            long doublePushTargets = BB.bDoublePushTargets(pawns, empty);
            collector.genPawnQuiets(doublePushTargets, 16);
        }
    }

    private static void genPawnCaptures(MoveList collector, BitChessBoard bb, long targets, int offset) {
        while (targets != 0) {
            final int fromIndex = Long.numberOfTrailingZeros(targets);
            collector.genPawnMove(fromIndex, fromIndex + offset, bb.get(fromIndex + offset));
            targets &= targets - 1;
        }
    }

    private static void genAttacks(MoveList collector, byte figType, int figPos, long captures, BitChessBoard bb) {
        while (captures != 0) {
            final int toIndex = Long.numberOfTrailingZeros(captures);
            collector.genMove(figType, figPos, toIndex, bb.get(toIndex));
            captures &= captures - 1;
        }
    }


    public static void genPawnQuietPromotions(BitChessBoard bb, MoveList collector, Color side) {
        long pawns = bb.getPieceSet(FT_PAWN, side);
        long empty = ~(bb.getColorMask(WHITE) | bb.getColorMask(BLACK));

        if (side == WHITE) {
            long singlePushTargets = BB.wSinglePushTargets(pawns & BB.rank7, empty);
            while (singlePushTargets != 0) {
                final int toIndex = Long.numberOfTrailingZeros(singlePushTargets);
                collector.genPawnMove(toIndex - 8, toIndex, (byte) 0);
                singlePushTargets &= singlePushTargets - 1;
            }
        } else {
            long singlePushTargets = BB.bSinglePushTargets(pawns & BB.rank2, empty);
            while (singlePushTargets != 0) {
                final int toIndex = Long.numberOfTrailingZeros(singlePushTargets);
                collector.genPawnMove(toIndex + 8, toIndex, (byte) 0);
                singlePushTargets &= singlePushTargets - 1;
            }
        }
    }

    /**
     * Generates pawn attacs used in see algorithm for a specific square. We need only to take care about regular pawn
     * attacks,
     * not en passant, as en passant can only happen on a pawn-non-capture-move before.
     *
     * @param bitBoard
     * @param s
     * @param side
     * @return
     */
    private static long genPawnAttacs(BoardRepresentation bitBoard, int s, Color side) {

        long sMask = 1L << s;
        BitChessBoard bb = bitBoard.getBoard();
        long pawns = bb.getPieceSet(FT_PAWN, side);

        long otherPieces = sMask;

        long allAttacks = 0L;

        if (side == WHITE) {
            long capturesEast = pawns & BB.bPawnWestAttacks(otherPieces);
            long capturesWest = pawns & BB.bPawnEastAttacks(otherPieces);
            allAttacks = capturesEast | capturesWest;

        } else {
            long capturesEast = pawns & BB.wPawnWestAttacks(otherPieces);
            long capturesWest = pawns & BB.wPawnEastAttacks(otherPieces);
            allAttacks = capturesEast | capturesWest;
        }

        return allAttacks;
    }

    /// Position::attackers_to() computes a bitboard of all pieces which attack a
    /// given square. Slider attacks use the occupied bitboard to indicate occupancy.

    public static long attackersTo(int s, long occupied, BoardRepresentation bitBoard) {

        BitChessBoard bb = bitBoard.getBoard();
        return (genPawnAttacs(bitBoard, s, WHITE))
                | (genPawnAttacs(bitBoard, s, BLACK))
                | (getKnightAttacs(s) & bb.getPieceSet(FT_KNIGHT))
                | (genRookAttacs(s, occupied) & (bb.getPieceSet(FT_ROOK) | bb.getPieceSet(FT_QUEEN)))
                | (genBishopAttacs(s, occupied) & (bb.getPieceSet(FT_BISHOP) | bb.getPieceSet(FT_QUEEN)))
                | (getKingAttacs(s) & bb.getPieceSet(FT_KING));
    }
}
