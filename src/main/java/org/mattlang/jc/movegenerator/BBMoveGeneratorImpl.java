package org.mattlang.jc.movegenerator;

import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.FigureConstants.*;
import static org.mattlang.jc.movegenerator.CastlingDef.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.*;
import org.mattlang.jc.board.bitboard.BB;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.board.bitboard.BitChessBoard;
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

    /* Now we have the mailbox array, so called because it looks like a
   mailbox, at least according to Bob Hyatt. This is useful when we
   need to figure out what pieces can go where. Let's say we have a
   rook on square a4 (32) and we want to know if it can move one
   square to the left. We subtract 1, and we get 31 (h5). The rook
   obviously can't move to h5, but we don't know that without doing
   a lot of annoying work. Sooooo, what we do is figure out a4's
   mailbox number, which is 61. Then we subtract 1 from 61 (60) and
   see what mailbox[60] is. In this case, it's -1, so it's out of
   bounds and we can forget it. You can see how mailbox[] is used
   in attack() in board.c. */

    private static final int[] mailbox = {
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, 0, 1, 2, 3, 4, 5, 6, 7, -1,
            -1, 8, 9, 10, 11, 12, 13, 14, 15, -1,
            -1, 16, 17, 18, 19, 20, 21, 22, 23, -1,
            -1, 24, 25, 26, 27, 28, 29, 30, 31, -1,
            -1, 32, 33, 34, 35, 36, 37, 38, 39, -1,
            -1, 40, 41, 42, 43, 44, 45, 46, 47, -1,
            -1, 48, 49, 50, 51, 52, 53, 54, 55, -1,
            -1, 56, 57, 58, 59, 60, 61, 62, 63, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1
    };

    private static final int[] mailbox64 = {
            21, 22, 23, 24, 25, 26, 27, 28,
            31, 32, 33, 34, 35, 36, 37, 38,
            41, 42, 43, 44, 45, 46, 47, 48,
            51, 52, 53, 54, 55, 56, 57, 58,
            61, 62, 63, 64, 65, 66, 67, 68,
            71, 72, 73, 74, 75, 76, 77, 78,
            81, 82, 83, 84, 85, 86, 87, 88,
            91, 92, 93, 94, 95, 96, 97, 98
    };

    /* should the figures silde? (bishop, rook & queen)  */
    private static final boolean[] slide = { false, false, true, true, true, false };

    private static final int[][] offset = {
            { 0, 0, 0, 0, 0, 0, 0, 0 }, /* Pawn, not used */
            { -21, -19, -12, -8, 8, 12, 19, 21 }, /* KNIGHT */
            { -11, -9, 9, 11 }, /* BISHOP */
            { -10, -1, 1, 10 }, /* ROOK */
            { -11, -10, -9, -1, 1, 9, 10, 11 }, /* QUEEN */
            { -11, -10, -9, -1, 1, 9, 10, 11 }  /* KING */
    };

    private static final int[] pawnCaptureOffset = { 11, 9 };

    private static final long[] kingAttacks = new long[64];

    static {
        // precalculate attacks:
        long sqBB = 1;
        for (int sq = 0; sq < 64; sq++, sqBB <<= 1) {
            kingAttacks[sq] = BB.kingAttacks(sqBB);
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

        for (int pawn : pieces.getPawns().getArr()) {
            genPawnMoves(board, collector, pawn, side);
        }

        for (int bishop : pieces.getBishops().getArr()) {
            byte figureCode = FigureType.Bishop.figureCode;
            int[] figOffsets = offset[figureCode];
            genPieceMoves(board, bishop, collector, xside, figureCode, figOffsets, slide[figureCode]);
        }

        for (int knight : pieces.getKnights().getArr()) {
            byte figureCode = FigureType.Knight.figureCode;
            int[] figOffsets = offset[figureCode];
            genPieceMoves(board, knight, collector, xside, figureCode, figOffsets, slide[figureCode]);
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

        genKingMoves(bitBoard, pieces.getKing(), collector, xside);

        // todo test debug code:
//
//        MoveList lnew=new MoveListImpl();
//        MoveList lold=new MoveListImpl();
//
//        genKingMoves(bitBoard, pieces.getKing(), lnew, xside);
//
//        byte figureCode = FigureType.King.figureCode;
//        int[] figOffsets = offset[figureCode];
//        genPieceMoves(board, pieces.getKing(), lold, xside, figureCode, figOffsets, slide[figureCode]);
//
//        List<MoveImpl> llnew=extractList(lnew);
//        List<MoveImpl> llold =extractList(lold);
//        if (!llnew.equals(llold)) {
//            System.out.println(board.toUniCodeStr());
//             debugmode=true;
//            // debug recomputing with new way:
//            genKingMoves(bitBoard, pieces.getKing(), lnew, xside);
//
//            throw new IllegalStateException("different move lists");
//        }



        // end test code

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

    private void genKingMoves(BitBoard board, int kingPos, MoveCollector collector, Color xside) {
        long kingAttack = kingAttacks[kingPos];

        long ownFigsMask = board.getBoard().getColorMask(xside.invert());
        long opponentFigsMask = board.getBoard().getColorMask(xside);

        long moves = kingAttack & ~ownFigsMask;
        long attacks = moves & opponentFigsMask;
        long quietMoves = moves & ~attacks;

        if (debugmode){
            System.out.println("kingattack: \n" + BitChessBoard.toStr(kingAttack));
            System.out.println("ownfigs: \n" + BitChessBoard.toStr(ownFigsMask));
            System.out.println("oppenentfigs: \n" + BitChessBoard.toStr(opponentFigsMask));

            // debug intersting situation where we have different quiet and attack moves
            System.out.println("moves: \n" + BitChessBoard.toStr(moves));
            System.out.println("attacks: \n" + BitChessBoard.toStr(attacks));
            System.out.println("quietmoves: \n" + BitChessBoard.toStr(quietMoves));

            System.out.println("debug");
        }

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
