package org.mattlang.jc.movegenerator;

import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.FigureConstants.*;
import static org.mattlang.jc.board.bitboard.BB.getKingAttacs;
import static org.mattlang.jc.board.bitboard.BB.getKnightAttacs;
import static org.mattlang.jc.board.bitboard.MagicBitboards.genBishopAttacs;
import static org.mattlang.jc.board.bitboard.MagicBitboards.genRookAttacs;
import static org.mattlang.jc.movegenerator.CastlingDef.*;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BB;
import org.mattlang.jc.board.bitboard.BitChessBoard;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.moves.MoveListImpl;

/**
 * see https://www.chessprogramming.org/10x12_Board
 * TSCP Implementation of move generator with some own modifications.
 *
 * Mixture of mailbox & bitboard move generation to slighlty refactor using bitboards.
 * Works only with the BitBoard implementation.
 */
public class BBMoveGeneratorImpl2 implements MoveGenerator {

    /**
     * Generation types.
     */
    public enum GenTypes {
        CAPTURES,
        QUIET,
        ALL
    }

    /**
     * @param board current board
     * @param side  the side to move
     */
    public MoveList generate(BoardRepresentation board, Color side) {
        MoveList moves = Factory.getDefaults().moveList.create();
        generate(board, side, moves);
        return moves;
    }

    /**
     * @param board current board
     * @param side  the side to move
     */
    public void generate(BoardRepresentation board, Color side, MoveList collector) {
        generate(board, side, collector, GenTypes.ALL);
    }

    public void generate(BoardRepresentation board, Color side, MoveCollector collector, GenTypes types) {

        BitChessBoard bb = board.getBoard();

        Color xside = side.invert();  /* the side not to move */

        long ownFigsMask = bb.getColorMask(xside.invert());
        long opponentFigsMask = bb.getColorMask(xside);
        long empty = ~ownFigsMask & ~opponentFigsMask;

        if (types == GenTypes.CAPTURES || types == GenTypes.ALL) {
            genPawnCaptureMoves(board, collector, side);
        }
        if (types == GenTypes.QUIET || types == GenTypes.ALL) {
            genPawnMoves(bb, collector, side, false);
        }

        long bishopBB = bb.getPieceSet(FT_BISHOP, side);
        while (bishopBB != 0) {
            final int bishop = Long.numberOfTrailingZeros(bishopBB);
            genBishopMoves(bb, bishop, collector, ownFigsMask, opponentFigsMask, empty, types);
            bishopBB &= bishopBB - 1;
        }

        long knightBB = bb.getPieceSet(FT_KNIGHT, side);
        while (knightBB != 0) {
            final int knight = Long.numberOfTrailingZeros(knightBB);
            genKnightMoves(bb, knight, collector, ownFigsMask, opponentFigsMask, types);
            knightBB &= knightBB - 1;
        }

        long rookBB = bb.getPieceSet(FT_ROOK, side);
        while (rookBB != 0) {
            final int rook = Long.numberOfTrailingZeros(rookBB);
            genRookMoves(bb, rook, collector, ownFigsMask, opponentFigsMask, empty, types);
            rookBB &= rookBB - 1;
        }

        long queenBB = bb.getPieceSet(FT_QUEEN, side);
        while (queenBB != 0) {
            final int queen = Long.numberOfTrailingZeros(queenBB);
            genQueenMoves(bb, queen, collector, ownFigsMask, opponentFigsMask, empty, types);
            queenBB &= queenBB - 1;
        }

        long kingBB = bb.getPieceSet(FT_KING, side);
        final int king = Long.numberOfTrailingZeros(kingBB);
        genKingMoves(bb, king, collector, ownFigsMask, opponentFigsMask, types);

        if (types == GenTypes.QUIET || types == GenTypes.ALL) {
            generateRochade(board, side, collector);
        }
    }

    private void genKingMoves(BitChessBoard bb, int kingPos, MoveCollector collector, long ownFigsMask,
            long opponentFigsMask, GenTypes types) {
        long kingAttack = getKingAttacs(kingPos);

        long moves = kingAttack & ~ownFigsMask;
        long attacks = moves & opponentFigsMask;
        long quietMoves = moves & ~attacks;

        if (types == GenTypes.CAPTURES || types == GenTypes.ALL) {
            while (attacks != 0) {
                final int toIndex = Long.numberOfTrailingZeros(attacks);
                collector.genMove(FT_KING, kingPos, toIndex, bb.get(toIndex));
                attacks &= attacks - 1;
            }
        }

        if (types == GenTypes.QUIET || types == GenTypes.ALL) {
            while (quietMoves != 0) {
                final int toIndex = Long.numberOfTrailingZeros(quietMoves);
                collector.genMove(FT_KING, kingPos, toIndex, (byte) 0);
                quietMoves &= quietMoves - 1;
            }
        }

    }

    private void genKnightMoves(BitChessBoard bb, int knight, MoveCollector collector, long ownFigsMask,
            long opponentFigsMask, GenTypes types) {
        long knightAttack = getKnightAttacs(knight);

        long moves = knightAttack & ~ownFigsMask;
        long attacks = moves & opponentFigsMask;
        long quietMoves = moves & ~attacks;

        if (types == GenTypes.CAPTURES || types == GenTypes.ALL) {
            while (attacks != 0) {
                final int toIndex = Long.numberOfTrailingZeros(attacks);
                collector.genMove(FT_KNIGHT, knight, toIndex, bb.get(toIndex));
                attacks &= attacks - 1;
            }
        }

        if (types == GenTypes.QUIET || types == GenTypes.ALL) {
            while (quietMoves != 0) {
                final int toIndex = Long.numberOfTrailingZeros(quietMoves);
                collector.genMove(FT_KNIGHT, knight, toIndex, (byte) 0);
                quietMoves &= quietMoves - 1;
            }
        }

    }

    private void genQueenMoves(BitChessBoard bb, int queen,
            MoveCollector collector, long ownFigsMask,
            long opponentFigsMask, long empty, GenTypes types) {
        long occupancy = ownFigsMask | opponentFigsMask;
        long attacksRook = genRookAttacs(queen, occupancy);
        long attacksBishop = genBishopAttacs(queen, occupancy);
        long attacks = attacksRook | attacksBishop;

        generateBBMoves(attacks, bb, queen, FT_QUEEN, collector, opponentFigsMask, empty, types);

    }

    private void genRookMoves(BitChessBoard bb, int rook,
            MoveCollector collector, long ownFigsMask,
            long opponentFigsMask, long empty, GenTypes types) {
        long occupancy = ownFigsMask | opponentFigsMask;
        long attacks = genRookAttacs(rook, occupancy);

        generateBBMoves(attacks, bb, rook, FT_ROOK, collector, opponentFigsMask, empty, types);
    }

    private void genBishopMoves(BitChessBoard bb, int bishop,
            MoveCollector collector, long ownFigsMask,
            long opponentFigsMask, long empty, GenTypes types) {
        long occupancy = ownFigsMask | opponentFigsMask;

        long attacks = genBishopAttacs(bishop, occupancy);

        generateBBMoves(attacks, bb, bishop, FT_BISHOP, collector, opponentFigsMask, empty, types);
    }

    private void generateBBMoves(long attacks, BitChessBoard bb, int figPos, byte figType,
            MoveCollector collector,
            long opponentFigsMask, long empty, GenTypes types) {


        if (types == GenTypes.CAPTURES || types == GenTypes.ALL) {
            long captures = attacks & opponentFigsMask;
            while (captures != 0) {
                final int toIndex = Long.numberOfTrailingZeros(captures);
                collector.genMove(figType, figPos, toIndex, bb.get(toIndex));
                captures &= captures - 1;
            }
        }

        if (types == GenTypes.QUIET || types == GenTypes.ALL) {
            long quiet = attacks & empty;
            while (quiet != 0) {
                final int toIndex = Long.numberOfTrailingZeros(quiet);
                collector.genMove(figType, figPos, toIndex, (byte) 0);
                quiet &= quiet - 1;
            }
        }

    }

    private void generateRochade(BoardRepresentation board, Color side, MoveCollector collector) {
        switch (side) {
        case WHITE:
            if (ROCHADE_L_WHITE.check(board)) {
                collector.addRochadeLongWhite();
            }
            if (ROCHADE_S_WHITE.check(board)) {
                collector.addRochadeShortWhite();
            }
            break;
        case BLACK:
            if (ROCHADE_S_BLACK.check(board)) {
                collector.addRochadeShortBlack();
            }
            if (ROCHADE_L_BLACK.check(board)) {
                collector.addRochadeLongBlack();
            }
            break;
        }
    }

    private void genPawnCaptureMoves(BoardRepresentation bitBoard, MoveCollector collector, Color side) {
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

    public void genPawnMoves(BitChessBoard bb, MoveCollector collector, Color side, boolean onlyPromotions) {
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

    /**
     * Generates pawn attacs used in see algorithm for a specific square. We need only to take care about regular pawn attacks,
     * not en passant, as en passant can only happen on a pawn-non-capture-move before.
     *
     * @param bitBoard
     * @param s
     * @param side
     * @return
     */
    private long genPawnAttacs(BoardRepresentation bitBoard, int s, Color side) {

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

    public long attackersTo(int s, long occupied, BoardRepresentation bitBoard) {

        BitChessBoard bb = bitBoard.getBoard();
        return (genPawnAttacs(bitBoard, s, WHITE))
                | (genPawnAttacs(bitBoard, s, BLACK))
                | (getKnightAttacs(s) & bb.getPieceSet(FT_KNIGHT))
                | (genRookAttacs(s, occupied) & (bb.getPieceSet(FT_ROOK) | bb.getPieceSet(FT_QUEEN)))
                | (genBishopAttacs(s, occupied) & (bb.getPieceSet(FT_BISHOP) | bb.getPieceSet(FT_QUEEN)))
                | (getKingAttacs(s) & bb.getPieceSet(FT_KING));
    }
}
