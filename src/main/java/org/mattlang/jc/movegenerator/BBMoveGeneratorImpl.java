package org.mattlang.jc.movegenerator;

import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.FigureConstants.*;
import static org.mattlang.jc.movegenerator.CastlingDef.*;
import static org.mattlang.jc.movegenerator.MoveGeneratorImpl3.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.*;
import org.mattlang.jc.board.bitboard.BB;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.moves.MoveImpl;

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

        for (int pawn : pieces.getPawns().getArr()) {
            genPawnMoves(board, collector, pawn, side);
        }

        for (int bishop : pieces.getBishops().getArr()) {
            byte figureCode = FigureType.Bishop.figureCode;
            int[] figOffsets = offset[figureCode];
            genPieceMoves(board, bishop, collector, xside, figureCode, figOffsets, slide[figureCode]);
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

    private List<MoveImpl> extractList(MoveList moveList){
        ArrayList<MoveImpl>  l1 = new ArrayList<>();
        for (MoveCursor moveCursor : moveList) {
            l1.add(new MoveImpl(moveCursor.getMoveInt()));
        }
        l1.sort(Comparator.comparingInt(MoveImpl::toInt));
        return l1;
    }


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

    private void genPawnMoves(BoardRepresentation board, MoveCollector collector, int i, Color side) {
        boolean isOnBaseLine = false;
        if (side == WHITE) {
            isOnBaseLine = i >= 8 && i <= 15;
        } else {
            isOnBaseLine = i >= 48 && i <= 55;
        }
        int pawnOffset = side == WHITE ? 10 : -10;
        // check single and double move:
        // get single move:
        int n = mailbox[mailbox64[i] + pawnOffset];
        if (n != -1) {
            byte target = board.getFigureCode(n);
            if (target == FigureConstants.FT_EMPTY) {

                collector.genPawnMove(i, n, side, (byte) 0);

                if (isOnBaseLine) {
                    // get double move from baseline:
                    n = mailbox[mailbox64[i] + 2 * pawnOffset];
                    target = board.getFigureCode(n);
                    if (target == FigureConstants.FT_EMPTY) {
                        collector.genPawnMove(i, n, side, (byte) 0);
                    }
                }
            }
        }
        // check capture:
        genPawnCaptureMoves(board, collector, i, side);
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
