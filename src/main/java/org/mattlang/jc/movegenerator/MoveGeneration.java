
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
import org.mattlang.jc.moves.MoveListImpl;

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

        genPawnMoves(bb, collector, side, false);

        long bishopBB = bb.getPieceSet(FT_BISHOP, side);
        while (bishopBB != 0) {
            final int bishop = Long.numberOfTrailingZeros(bishopBB);
            genBishopMoveQuiets(bishop, collector, ownFigsMask, opponentFigsMask, empty);
            bishopBB &= bishopBB - 1;
        }

        long knightBB = bb.getPieceSet(FT_KNIGHT, side);
        while (knightBB != 0) {
            final int knight = Long.numberOfTrailingZeros(knightBB);
            genKnightMoveQuiets(knight, collector, ownFigsMask, opponentFigsMask);
            knightBB &= knightBB - 1;
        }

        long rookBB = bb.getPieceSet(FT_ROOK, side);
        while (rookBB != 0) {
            final int rook = Long.numberOfTrailingZeros(rookBB);
            genRookMoveQuiets(rook, collector, ownFigsMask, opponentFigsMask, empty);
            rookBB &= rookBB - 1;
        }

        long queenBB = bb.getPieceSet(FT_QUEEN, side);
        while (queenBB != 0) {
            final int queen = Long.numberOfTrailingZeros(queenBB);
            genQueenMoveQuiets(queen, collector, ownFigsMask, opponentFigsMask, empty);
            queenBB &= queenBB - 1;
        }

        long kingBB = bb.getPieceSet(FT_KING, side);
        final int king = Long.numberOfTrailingZeros(kingBB);
        genKingMoveQuiets(king, collector, ownFigsMask, opponentFigsMask);

        board.getBoardCastlings().generateCastlingMoves(side, collector);
    }

    public static void generateAttacks(BoardRepresentation board, Color side, MoveCollector collector) {

        BitChessBoard bb = board.getBoard();

        Color xside = side.invert();  /* the side not to move */

        long ownFigsMask = bb.getColorMask(xside.invert());
        long opponentFigsMask = bb.getColorMask(xside);

        genPawnCaptureMoves(board, collector, side);

        long bishopBB = bb.getPieceSet(FT_BISHOP, side);
        while (bishopBB != 0) {
            final int bishop = Long.numberOfTrailingZeros(bishopBB);
            genBishopAttacks(bb, bishop, collector, ownFigsMask, opponentFigsMask);
            bishopBB &= bishopBB - 1;
        }

        long knightBB = bb.getPieceSet(FT_KNIGHT, side);
        while (knightBB != 0) {
            final int knight = Long.numberOfTrailingZeros(knightBB);
            genKnightAttacks(bb, knight, collector, ownFigsMask, opponentFigsMask);
            knightBB &= knightBB - 1;
        }

        long rookBB = bb.getPieceSet(FT_ROOK, side);
        while (rookBB != 0) {
            final int rook = Long.numberOfTrailingZeros(rookBB);
            genRookAttacks(bb, rook, collector, ownFigsMask, opponentFigsMask);
            rookBB &= rookBB - 1;
        }

        long queenBB = bb.getPieceSet(FT_QUEEN, side);
        while (queenBB != 0) {
            final int queen = Long.numberOfTrailingZeros(queenBB);
            genQueenAttacks(bb, queen, collector, ownFigsMask, opponentFigsMask);
            queenBB &= queenBB - 1;
        }

        long kingBB = bb.getPieceSet(FT_KING, side);
        final int king = Long.numberOfTrailingZeros(kingBB);
        genKingMoveAttacks(bb, king, collector, ownFigsMask, opponentFigsMask);

    }

    private static void genKingMoveQuiets(int kingPos, MoveCollector collector, long ownFigsMask,
            long opponentFigsMask) {
        long kingAttack = getKingAttacs(kingPos);

        long moves = kingAttack & ~ownFigsMask;
        long attacks = moves & opponentFigsMask;
        long quietMoves = moves & ~attacks;

        while (quietMoves != 0) {
            final int toIndex = Long.numberOfTrailingZeros(quietMoves);
            collector.genMove(FT_KING, kingPos, toIndex, (byte) 0);
            quietMoves &= quietMoves - 1;
        }
    }

    private static void genKingMoveAttacks(BitChessBoard bb, int kingPos, MoveCollector collector, long ownFigsMask,
            long opponentFigsMask) {
        long kingAttack = getKingAttacs(kingPos);

        long moves = kingAttack & ~ownFigsMask;
        long attacks = moves & opponentFigsMask;

        while (attacks != 0) {
            final int toIndex = Long.numberOfTrailingZeros(attacks);
            collector.genMove(FT_KING, kingPos, toIndex, bb.get(toIndex));
            attacks &= attacks - 1;
        }
    }

    private static void genKnightMoveQuiets(int knight, MoveCollector collector, long ownFigsMask,
            long opponentFigsMask) {
        long knightAttack = getKnightAttacs(knight);

        long moves = knightAttack & ~ownFigsMask;
        long attacks = moves & opponentFigsMask;
        long quietMoves = moves & ~attacks;

        while (quietMoves != 0) {
            final int toIndex = Long.numberOfTrailingZeros(quietMoves);
            collector.genMove(FT_KNIGHT, knight, toIndex, (byte) 0);
            quietMoves &= quietMoves - 1;
        }
    }

    private static void genKnightAttacks(BitChessBoard bb, int knight, MoveCollector collector, long ownFigsMask,
            long opponentFigsMask) {
        long knightAttack = getKnightAttacs(knight);

        long moves = knightAttack & ~ownFigsMask;
        long attacks = moves & opponentFigsMask;

        while (attacks != 0) {
            final int toIndex = Long.numberOfTrailingZeros(attacks);
            collector.genMove(FT_KNIGHT, knight, toIndex, bb.get(toIndex));
            attacks &= attacks - 1;
        }

    }

    private static void genQueenMoveQuiets(int queen,
            MoveCollector collector, long ownFigsMask,
            long opponentFigsMask, long empty) {
        long occupancy = ownFigsMask | opponentFigsMask;
        long attacksRook = genRookAttacs(queen, occupancy);
        long attacksBishop = genBishopAttacs(queen, occupancy);
        long attacks = attacksRook | attacksBishop;

        generateBBMoveQuiets(attacks, queen, FT_QUEEN, collector, empty);

    }

    private static void genQueenAttacks(BitChessBoard bb, int queen,
            MoveCollector collector, long ownFigsMask,
            long opponentFigsMask) {
        long occupancy = ownFigsMask | opponentFigsMask;
        long attacksRook = genRookAttacs(queen, occupancy);
        long attacksBishop = genBishopAttacs(queen, occupancy);
        long attacks = attacksRook | attacksBishop;

        generateBBMoveAttacks(attacks, bb, queen, FT_QUEEN, collector, opponentFigsMask);

    }

    private static void genRookMoveQuiets(int rook,
            MoveCollector collector, long ownFigsMask,
            long opponentFigsMask, long empty) {
        long occupancy = ownFigsMask | opponentFigsMask;
        long attacks = genRookAttacs(rook, occupancy);

        generateBBMoveQuiets(attacks, rook, FT_ROOK, collector, empty);
    }

    private static void genRookAttacks(BitChessBoard bb, int rook,
            MoveCollector collector, long ownFigsMask,
            long opponentFigsMask) {
        long occupancy = ownFigsMask | opponentFigsMask;
        long attacks = genRookAttacs(rook, occupancy);

        generateBBMoveAttacks(attacks, bb, rook, FT_ROOK, collector, opponentFigsMask);
    }

    private static void genBishopMoveQuiets(int bishop,
            MoveCollector collector, long ownFigsMask,
            long opponentFigsMask, long empty) {
        long occupancy = ownFigsMask | opponentFigsMask;
        long attacks = genBishopAttacs(bishop, occupancy);

        generateBBMoveQuiets(attacks, bishop, FT_BISHOP, collector, empty);
    }

    private static void genBishopAttacks(BitChessBoard bb, int bishop,
            MoveCollector collector, long ownFigsMask,
            long opponentFigsMask) {
        long occupancy = ownFigsMask | opponentFigsMask;
        long attacks = genBishopAttacs(bishop, occupancy);

        generateBBMoveAttacks(attacks, bb, bishop, FT_BISHOP, collector, opponentFigsMask);
    }

    private static void generateBBMoveQuiets(long attacks, int figPos, byte figType,
            MoveCollector collector, long empty) {
        long quiet = attacks & empty;
        while (quiet != 0) {
            final int toIndex = Long.numberOfTrailingZeros(quiet);
            collector.genMove(figType, figPos, toIndex, (byte) 0);
            quiet &= quiet - 1;
        }
    }

    private static void generateBBMoveAttacks(long attacks, BitChessBoard bb, int figPos, byte figType,
            MoveCollector collector, long opponentFigsMask) {

        long captures = attacks & opponentFigsMask;
        while (captures != 0) {
            final int toIndex = Long.numberOfTrailingZeros(captures);
            collector.genMove(figType, figPos, toIndex, bb.get(toIndex));
            captures &= captures - 1;
        }
    }

    public static void genPawnCaptureMoves(BoardRepresentation bitBoard, MoveCollector collector, Color side) {
        BitChessBoard bb = bitBoard.getBoard();
        long pawns = bb.getPieceSet(FT_PAWN, side);

        long otherPieces = side == WHITE ? bb.getColorMask(BLACK) : bb.getColorMask(WHITE);

        if (side == WHITE) {
            long capturesEast = pawns & BB.bPawnWestAttacks(otherPieces);

            while (capturesEast != 0) {
                final int fromIndex = Long.numberOfTrailingZeros(capturesEast);
                collector.genPawnMove(fromIndex, fromIndex + 9, side, bb.get(fromIndex + 9));
                capturesEast &= capturesEast - 1;
            }

            long capturesWest = pawns & BB.bPawnEastAttacks(otherPieces);

            while (capturesWest != 0) {
                final int fromIndex = Long.numberOfTrailingZeros(capturesWest);
                collector.genPawnMove(fromIndex, fromIndex + 7, side, bb.get(fromIndex + 7));
                capturesWest &= capturesWest - 1;
            }

            // en passant:
            if (bitBoard.getEnPassantMoveTargetPos() >= 0) {
                long epMask = 1L << bitBoard.getEnPassantMoveTargetPos();

                capturesEast = pawns & BB.bPawnWestAttacks(epMask);

                while (capturesEast != 0) {
                    final int fromIndex = Long.numberOfTrailingZeros(capturesEast);
                    collector.genEnPassant(fromIndex, fromIndex + 9, side, bitBoard.getEnPassantCapturePos());
                    capturesEast &= capturesEast - 1;
                }

                capturesWest = pawns & BB.bPawnEastAttacks(epMask);

                while (capturesWest != 0) {
                    final int fromIndex = Long.numberOfTrailingZeros(capturesWest);
                    collector.genEnPassant(fromIndex, fromIndex + 7, side, bitBoard.getEnPassantCapturePos());
                    capturesWest &= capturesWest - 1;
                }

            }

        } else {
            long capturesEast = pawns & BB.wPawnWestAttacks(otherPieces);

            while (capturesEast != 0) {
                final int fromIndex = Long.numberOfTrailingZeros(capturesEast);
                collector.genPawnMove(fromIndex, fromIndex - 7, side, bb.get(fromIndex - 7));
                capturesEast &= capturesEast - 1;
            }

            long capturesWest = pawns & BB.wPawnEastAttacks(otherPieces);
            while (capturesWest != 0) {
                final int fromIndex = Long.numberOfTrailingZeros(capturesWest);
                collector.genPawnMove(fromIndex, fromIndex - 9, side, bb.get(fromIndex - 9));
                capturesWest &= capturesWest - 1;
            }

            // en passant:
            if (bitBoard.getEnPassantMoveTargetPos() >= 0) {
                long epMask = 1L << bitBoard.getEnPassantMoveTargetPos();

                capturesEast = pawns & BB.wPawnWestAttacks(epMask);

                while (capturesEast != 0) {
                    final int fromIndex = Long.numberOfTrailingZeros(capturesEast);
                    collector.genEnPassant(fromIndex, fromIndex - 7, side, bitBoard.getEnPassantCapturePos());
                    capturesEast &= capturesEast - 1;
                }

                capturesWest = pawns & BB.wPawnEastAttacks(epMask);

                while (capturesWest != 0) {
                    final int fromIndex = Long.numberOfTrailingZeros(capturesWest);
                    collector.genEnPassant(fromIndex, fromIndex - 9, side, bitBoard.getEnPassantCapturePos());
                    capturesWest &= capturesWest - 1;
                }
            }
        }
    }

    public static void genPawnMoves(BitChessBoard bb, MoveCollector collector, Color side, boolean onlyPromotions) {
        long pawns = bb.getPieceSet(FT_PAWN, side);
        long empty = ~(bb.getColorMask(WHITE) | bb.getColorMask(BLACK));

        if (side == WHITE) {
            long singlePushTargets = BB.wSinglePushTargets(pawns, empty);

            if (onlyPromotions) {
                while (singlePushTargets != 0) {
                    final int toIndex = Long.numberOfTrailingZeros(singlePushTargets);
                    if (MoveListImpl.isOnLastLine(side, toIndex)) {
                        collector.genPawnMove(toIndex - 8, toIndex, side, (byte) 0);
                    }
                    singlePushTargets &= singlePushTargets - 1;
                }
            } else {
                while (singlePushTargets != 0) {
                    final int toIndex = Long.numberOfTrailingZeros(singlePushTargets);
                    collector.genPawnMove(toIndex - 8, toIndex, side, (byte) 0);
                    singlePushTargets &= singlePushTargets - 1;
                }

                long doublePushTargets = BB.wDblPushTargets(pawns, empty);
                while (doublePushTargets != 0) {
                    final int toIndex = Long.numberOfTrailingZeros(doublePushTargets);
                    collector.genPawnMove(toIndex - 16, toIndex, side, (byte) 0);
                    doublePushTargets &= doublePushTargets - 1;
                }
            }

        } else {
            long singlePushTargets = BB.bSinglePushTargets(pawns, empty);

            if (onlyPromotions) {
                while (singlePushTargets != 0) {
                    final int toIndex = Long.numberOfTrailingZeros(singlePushTargets);
                    if (MoveListImpl.isOnLastLine(side, toIndex)) {
                        collector.genPawnMove(toIndex + 8, toIndex, side, (byte) 0);
                    }
                    singlePushTargets &= singlePushTargets - 1;
                }
            } else {
                while (singlePushTargets != 0) {
                    final int toIndex = Long.numberOfTrailingZeros(singlePushTargets);
                    collector.genPawnMove(toIndex + 8, toIndex, side, (byte) 0);
                    singlePushTargets &= singlePushTargets - 1;
                }

                long doublePushTargets = BB.bDoublePushTargets(pawns, empty);
                while (doublePushTargets != 0) {
                    final int toIndex = Long.numberOfTrailingZeros(doublePushTargets);
                    collector.genPawnMove(toIndex + 16, toIndex, side, (byte) 0);
                    doublePushTargets &= doublePushTargets - 1;
                }
            }

        }
    }

}
