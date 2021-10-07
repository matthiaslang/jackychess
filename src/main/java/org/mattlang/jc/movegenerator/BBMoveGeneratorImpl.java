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
            byte figureCode = FigureType.Rook.figureCode;
            int[] figOffsets = offset[figureCode];
            genPieceMoves(board, rook, collector, xside, figureCode, figOffsets, slide[figureCode]);
        }

        for (int queen : pieces.getQueens().getArr()) {
            byte figureCode = FigureType.Queen.figureCode;
            int[] figOffsets = offset[figureCode];
            genPieceMoves(board, queen, collector, xside, figureCode, figOffsets, slide[figureCode]);
        }

        genKingMoves(bitBoard, pieces.getKing(), collector, ownFigsMask, opponentFigsMask);

        generateRochade(board, side, collector);
    }

    private boolean debugmode =false;


    private static void genPieceMoves(BoardRepresentation board, int i, MoveCollector collector, Color xside,
            byte figureType,
            int[] figOffsets, boolean slide) {

        for (int j = 0; j < figOffsets.length; ++j) { /* for all knight or ray directions */
            for (int n = i; ; ) { /* starting with from square */
                n = mailbox[mailbox64[n] + figOffsets[j]]; /* next square along the ray j */
                if (n == -1)
                    break; /* outside board */
                byte targetN = board.getFigureCode(n);
                if (targetN != FigureConstants.FT_EMPTY) {
                    if (Figure.getColor(targetN) == xside)
                        collector.genMove(figureType, i, n, targetN); /* capture from i to n */
                    break;
                }
                collector.genMove(figureType, i, n, (byte) 0); /* quiet move from i to n */
                if (!slide)
                    break; /* next direction */
            }
        }
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

    private void genBishopMoves(BitBoard bitBoard, int bishop, MoveCollector collector, long ownFigsMask,
            long opponentFigsMask, long empty) {
        long occupancy = ownFigsMask | opponentFigsMask & ~(1 << bishop);

        long attacks = MagicBitboards.genBishopAttacs(bishop, occupancy);

        long quiet = attacks & empty;
        long captures = attacks & opponentFigsMask;

        while (quiet != 0) {
            final int toIndex = Long.numberOfTrailingZeros(quiet);
            collector.genMove(FT_BISHOP, bishop, toIndex, (byte) 0);
            quiet &= quiet - 1;
        }

        while (captures != 0) {
            final int toIndex = Long.numberOfTrailingZeros(captures);
            collector.genMove(FT_BISHOP, bishop, toIndex, bitBoard.getFigureCode(toIndex));
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

        CaptureChecker captureChecker = new CaptureChecker();
        // 1. test rook && queen vertical & horizontal captures:
        genPieceMoves(board, i, captureChecker, xside, FT_ROOK, offset[FT_ROOK], slide[FT_ROOK]);
        if (captureChecker.hasCapturesBy(FT_ROOK)) {
            return true;
        }
        if (captureChecker.hasCapturesBy(FigureConstants.FT_QUEEN)) {
            return true;
        }
        // 2. test bishop and queen diagonal captures:
        captureChecker.reset();
        genPieceMoves(board, i, captureChecker, xside, FT_BISHOP, offset[FT_BISHOP], slide[FT_BISHOP]);
        if (captureChecker.hasCapturesBy(FT_BISHOP)) {
            return true;
        }
        if (captureChecker.hasCapturesBy(FigureConstants.FT_QUEEN)) {
            return true;
        }

        // 3. test knight
        captureChecker.reset();
        genPieceMoves(board, i, captureChecker, xside, FT_KNIGHT, offset[FT_KNIGHT], slide[FT_KNIGHT]);
        if (captureChecker.hasCapturesBy(FT_KNIGHT)) {
            return true;
        }

        // 4. pawns:
        captureChecker.reset();
        genPawnCaptureMoves(board, captureChecker, i, side);
        if (captureChecker.hasCapturesBy(FigureConstants.FT_PAWN)) {
            return true;
        }

        // 5. test king
        captureChecker.reset();
        genPieceMoves(board, i, captureChecker, xside, FT_KING, offset[FT_KING], slide[FT_KING]);
        if (captureChecker.hasCapturesBy(FT_KING)) {
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
