
package org.mattlang.jc.movegenerator;

import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.FigureConstants.*;
import static org.mattlang.jc.board.bitboard.BB.getKingAttacs;
import static org.mattlang.jc.board.bitboard.BB.getKnightAttacs;
import static org.mattlang.jc.board.bitboard.MagicBitboards.genBishopAttacs;
import static org.mattlang.jc.board.bitboard.MagicBitboards.genRookAttacs;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BB;
import org.mattlang.jc.board.bitboard.BitChessBoard;

/**
 * Bitboard move generation used in staged move generation.
 */
public final class MoveGeneration {

    public static void generateQuiets(BoardRepresentation board, Color side, MoveCollector collector) {

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

        board.getBoardCastlings().generateCastlingMoves(side, collector);
    }

    public static void generateAttacks(BoardRepresentation board, Color side, MoveCollector collector) {

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

    public static void genPawnCaptureMoves(BoardRepresentation bitBoard, MoveCollector collector, Color side) {
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
                    collector.genEnPassant(fromIndex, fromIndex + 9, bitBoard.getEnPassantCapturePos());
                    capturesEast &= capturesEast - 1;
                }

                capturesWest = pawns & BB.bPawnEastAttacks(epMask);

                while (capturesWest != 0) {
                    final int fromIndex = Long.numberOfTrailingZeros(capturesWest);
                    collector.genEnPassant(fromIndex, fromIndex + 7, bitBoard.getEnPassantCapturePos());
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
                    collector.genEnPassant(fromIndex, fromIndex - 7, bitBoard.getEnPassantCapturePos());
                    capturesEast &= capturesEast - 1;
                }

                capturesWest = pawns & BB.wPawnEastAttacks(epMask);

                while (capturesWest != 0) {
                    final int fromIndex = Long.numberOfTrailingZeros(capturesWest);
                    collector.genEnPassant(fromIndex, fromIndex - 9, bitBoard.getEnPassantCapturePos());
                    capturesWest &= capturesWest - 1;
                }
            }
        }
    }

    public static void genPawnMoves(BitChessBoard bb, long empty, MoveCollector collector, Color side) {
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

    private static void genPawnCaptures(MoveCollector collector, BitChessBoard bb, long targets, int offset) {
        while (targets != 0) {
            final int fromIndex = Long.numberOfTrailingZeros(targets);
            collector.genPawnMove(fromIndex, fromIndex + offset, bb.get(fromIndex + offset));
            targets &= targets - 1;
        }
    }

    private static void genAttacks(MoveCollector collector, byte figType, int figPos, long captures, BitChessBoard bb) {
        while (captures != 0) {
            final int toIndex = Long.numberOfTrailingZeros(captures);
            collector.genMove(figType, figPos, toIndex, bb.get(toIndex));
            captures &= captures - 1;
        }
    }
}
