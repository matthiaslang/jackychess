package org.mattlang.jc.movegenerator;

import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.FigureConstants.*;
import static org.mattlang.jc.movegenerator.CastlingDef.*;
import static org.mattlang.jc.movegenerator.MoveGeneratorImpl3.*;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.*;
import org.mattlang.jc.board.bitboard.BB;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.board.bitboard.BitChessBoard;
import org.mattlang.jc.board.bitboard.MagicBitboards;
import org.mattlang.jc.engine.MoveList;

/**
 * see https://www.chessprogramming.org/10x12_Board
 * TSCP Implementation of move generator with some own modifications.
 *
 * Mixture of mailbox & bitboard move generation to slighlty refactor using bitboards.
 * Works only with the BitBoard implementation.
 */
public class BBMoveGeneratorImpl implements MoveGenerator {



    private static final long[] kingAttacks = new long[64];
    private static final long[] knightAttacks = new long[64];

    static {
        // precalculate attacks:
        long sqBB = 1;
        for (int sq = 0; sq < 64; sq++, sqBB <<= 1) {
            kingAttacks[sq] = BB.kingAttacks(sqBB);
            knightAttacks[sq] = BB.knightAttacks(sqBB);
        }
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
    public void generate(BoardRepresentation board, Color side, MoveCollector collector) {

        BitBoard bitBoard = (BitBoard) board;

        Color xside = side.invert();  /* the side not to move */

        PieceList pieces = side == WHITE ? board.getWhitePieces() : board.getBlackPieces();

        long ownFigsMask = bitBoard.getBoard().getColorMask(xside.invert());
        long opponentFigsMask = bitBoard.getBoard().getColorMask(xside);
        long empty = ~ownFigsMask & ~opponentFigsMask;

        genPawnMoves(bitBoard, collector, side);
        genPawnCaptureMoves(bitBoard, collector, side);

        for (int bishop : pieces.getBishops().getArr()) {
            genBishopMoves(bitBoard, bishop, collector, ownFigsMask, opponentFigsMask, empty);
        }

        for (int knight : pieces.getKnights().getArr()) {
            genKnightMoves(bitBoard, knight, collector, ownFigsMask, opponentFigsMask);
        }

        for (int rook : pieces.getRooks().getArr()) {
            genRookMoves(bitBoard, rook, collector, ownFigsMask, opponentFigsMask, empty);
        }

        for (int queen : pieces.getQueens().getArr()) {
            genQueenMoves(bitBoard, queen, collector, ownFigsMask, opponentFigsMask, empty);
        }

        genKingMoves(bitBoard, pieces.getKing(), collector, ownFigsMask, opponentFigsMask);

        generateRochade(board, side, collector);
    }

    private void genKingMoves(BitBoard board, int kingPos, MoveCollector collector, long ownFigsMask, long opponentFigsMask) {
        long kingAttack = kingAttacks[kingPos];

        long moves = kingAttack & ~ownFigsMask;
        long attacks = moves & opponentFigsMask;
        long quietMoves = moves & ~attacks;

        while (attacks != 0) {
            final int toIndex = Long.numberOfTrailingZeros(attacks);
            collector.genMove(FT_KING, kingPos, toIndex, board.getFigureCode(toIndex));
            attacks &= attacks - 1;
        }

        while (quietMoves != 0) {
            final int toIndex = Long.numberOfTrailingZeros(quietMoves);
            collector.genMove(FT_KING, kingPos, toIndex, (byte) 0);
            quietMoves &= quietMoves - 1;
        }

    }

    private void genKnightMoves(BitBoard board, int knight, MoveCollector collector,long ownFigsMask, long opponentFigsMask) {
        long knightAttack = knightAttacks[knight];

        long moves = knightAttack & ~ownFigsMask;
        long attacks = moves & opponentFigsMask;
        long quietMoves = moves & ~attacks;

        while (attacks != 0) {
            final int toIndex = Long.numberOfTrailingZeros(attacks);
            collector.genMove(FT_KNIGHT, knight, toIndex, board.getFigureCode(toIndex));
            attacks &= attacks - 1;
        }

        while (quietMoves != 0) {
            final int toIndex = Long.numberOfTrailingZeros(quietMoves);
            collector.genMove(FT_KNIGHT, knight, toIndex, (byte) 0);
            quietMoves &= quietMoves - 1;
        }

    }

    private void genQueenMoves(BitBoard bitBoard, int queen,
            MoveCollector collector, long ownFigsMask,
            long opponentFigsMask, long empty) {
        long occupancy = ownFigsMask | opponentFigsMask;
        long attacksRook = MagicBitboards.genRookAttacs(queen, occupancy);
        long attacksBishop = MagicBitboards.genBishopAttacs(queen, occupancy);
        long attacks = attacksRook | attacksBishop;

        generateBBMoves(attacks, bitBoard, queen, FT_QUEEN, collector, opponentFigsMask, empty);

    }

    private void genRookMoves(BitBoard bitBoard, int rook,
            MoveCollector collector, long ownFigsMask,
            long opponentFigsMask, long empty) {
        long occupancy = ownFigsMask | opponentFigsMask;
        long attacks = MagicBitboards.genRookAttacs(rook, occupancy);

        generateBBMoves(attacks, bitBoard, rook, FT_ROOK, collector, opponentFigsMask, empty);
    }

    private void genBishopMoves(BitBoard bitBoard, int bishop,
            MoveCollector collector, long ownFigsMask,
            long opponentFigsMask, long empty) {
        long occupancy = ownFigsMask | opponentFigsMask;

        long attacks = MagicBitboards.genBishopAttacs(bishop, occupancy);

        generateBBMoves(attacks, bitBoard, bishop, FT_BISHOP, collector, opponentFigsMask, empty);
    }

    private void generateBBMoves(long attacks, BitBoard bitBoard, int figPos, byte figType,
            MoveCollector collector,
            long opponentFigsMask, long empty) {

        long quiet = attacks & empty;
        long captures = attacks & opponentFigsMask;

        while (quiet != 0) {
            final int toIndex = Long.numberOfTrailingZeros(quiet);
            collector.genMove(figType, figPos, toIndex, (byte) 0);
            quiet &= quiet - 1;
        }

        while (captures != 0) {
            final int toIndex = Long.numberOfTrailingZeros(captures);
            collector.genMove(figType, figPos, toIndex, bitBoard.getFigureCode(toIndex));
            captures &= captures - 1;
        }
    }

    /**
     * Returns true, if the figure on position i can be captured by the other side.
     * This could be used to test check situations during legal move generation, etc.
     *
     * It tests captures by using "inverse" logic, going from the position in question to the opponents positions.
     *
     * @param board
     * @param i
     * @return
     */
    public static boolean canFigureCaptured(BoardRepresentation board, int i) {
        byte figure = board.getFigureCode(i);
        Color side = Figure.getColor(figure);
        return canFigureCaptured(board, i, side);
    }

    /**
     * Returns true, if the fiel on position i can be captured/controlled by the other side.
     * This could be used to test check situations during legal move generation, etc.
     *
     * It tests captures by using "inverse" logic, going from the position in question to the opponents positions.
     *
     * @param board
     * @param i
     * @return
     */
    public static boolean canFigureCaptured(BoardRepresentation board, int i, Color side) {
        Color xside = side.invert();

        BitBoard bitBoard = (BitBoard) board;

        BitChessBoard bb = bitBoard.getBoard();
        long ownFigsMask = bb.getColorMask(xside.invert());
        long opponentFigsMask = bb.getColorMask(xside);

        // 1. test bishop and queen diagonal captures
        long occupancy = ownFigsMask | opponentFigsMask;

        long attacks = MagicBitboards.genBishopAttacs(i, occupancy);
        if ((attacks & bb.getPieceSet(FT_BISHOP, xside)) != 0) {
            return true;
        }
        long otherQueens = bb.getPieceSet(FT_QUEEN, xside);
        if ((attacks & otherQueens) != 0) {
            return true;
        }

        // 2. test rook and queen vertical/horizontal captures:
        attacks = MagicBitboards.genRookAttacs(i, occupancy);
        if ((attacks & bb.getPieceSet(FT_ROOK, xside)) != 0) {
            return true;
        }
        if ((attacks & otherQueens) != 0) {
            return true;
        }

        // 3. test knight
        attacks = knightAttacks[i];
        if ((attacks & bb.getPieceSet(FT_KNIGHT, xside)) != 0) {
            return true;

        }

        // 4. pawns:
        long otherPawns = side == WHITE ? bb.getPieceSet(FT_PAWN, BLACK) : bb.getPieceSet(FT_PAWN, WHITE);
        long figMask = 1L << i;

        if (side == WHITE) {
            long capturesEast = BB.bPawnWestAttacks(otherPawns);
            if ((figMask & capturesEast) != 0) {
                return true;
            }

            long capturesWest = BB.bPawnEastAttacks(otherPawns);
            if ((figMask & capturesWest) != 0) {
                return true;
            }

        } else {
            long capturesEast = BB.wPawnWestAttacks(otherPawns);
            if ((figMask & capturesEast) != 0) {
                return true;
            }

            long capturesWest = BB.wPawnEastAttacks(otherPawns);
            if ((figMask & capturesWest) != 0) {
                return true;
            }
        }

        // 5. test king
        attacks = kingAttacks[i];
        if ((attacks & bb.getPieceSet(FT_KING, xside)) != 0) {
            return true;
        }

        return false;
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

    private void genPawnCaptureMoves(BitBoard bitBoard, MoveCollector collector, Color side) {
        BitChessBoard bb = bitBoard.getBoard();
        long pawns = bb.getPieceSet(FT_PAWN, side);

        long otherPieces = side == WHITE ? bb.getColorMask(BLACK) : bb.getColorMask(WHITE);

        if (side == WHITE) {
            long capturesEast = pawns & BB.bPawnWestAttacks(otherPieces);

            while (capturesEast != 0) {
                final int fromIndex = Long.numberOfTrailingZeros(capturesEast);
                collector.genPawnMove(fromIndex, fromIndex + 9, side, bitBoard.getFigureCode(fromIndex + 9));
                capturesEast &= capturesEast - 1;
            }

            long capturesWest = pawns & BB.bPawnEastAttacks(otherPieces);

            while (capturesWest != 0) {
                final int fromIndex = Long.numberOfTrailingZeros(capturesWest);
                collector.genPawnMove(fromIndex, fromIndex + 7, side, bitBoard.getFigureCode(fromIndex + 7));
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
                collector.genPawnMove(fromIndex, fromIndex - 7, side, bitBoard.getFigureCode(fromIndex - 7));
                capturesEast &= capturesEast - 1;
            }

            long capturesWest = pawns & BB.wPawnEastAttacks(otherPieces);
            while (capturesWest != 0) {
                final int fromIndex = Long.numberOfTrailingZeros(capturesWest);
                collector.genPawnMove(fromIndex, fromIndex - 9, side, bitBoard.getFigureCode(fromIndex - 9));
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

    private void genPawnMoves(BitBoard bitBoard, MoveCollector collector, Color side) {
        BitChessBoard bb = bitBoard.getBoard();
        long pawns = bb.getPieceSet(FT_PAWN, side);
        long empty = ~(bb.getColorMask(WHITE) | bb.getColorMask(BLACK));

        if (side == WHITE) {
            long singlePushTargets = BB.wSinglePushTargets(pawns, empty);

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

        } else {
            long singlePushTargets = BB.bSinglePushTargets(pawns, empty);

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

    private static void genPawnCaptureMoves(BoardRepresentation board, MoveCollector collector, int i, Color side) {
        int n;
        int m = side == WHITE ? 1 : -1;
        Color xside = side.invert();
        for (int offset : pawnCaptureOffset) {
            n = mailbox[mailbox64[i] + offset * m];
            if (n != -1) {
                byte target = board.getFigureCode(n);
                if (target != FigureConstants.FT_EMPTY && Figure.getColor(target) == xside) {
                    collector.genPawnMove(i, n, side, target);
                } else if (board.isEnPassantCapturePossible(n)) {
                    collector.genEnPassant(i, n, side, board.getEnPassantCapturePos());
                } else if (target == FigureConstants.FT_EMPTY) {
                    collector.hypotheticalPawnCapture(i, n);
                }
            }
        }
    }

}
