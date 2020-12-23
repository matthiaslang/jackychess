package org.mattlang.jc.movegenerator;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.*;
import org.mattlang.jc.engine.MoveList;

import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.Figure.*;
import static org.mattlang.jc.board.FigureConstants.MASK_OUT_COLOR;

/**
 * see https://www.chessprogramming.org/10x12_Board
 * TSCP Implementation of move generator with some own modifications.
 *
 * Optimization, working only with Board2.
 */
public class MoveGeneratorImpl2 implements MoveGenerator {

    public static final int[] ROCHADE_L_WHITE = { 0, 1, 2, 3, 4 };
    public static final int[] ROCHADE_S_WHITE = { 4, 5, 6, 7 };
    public static final int[] ROCHADE_S_BLACK = { 60, 61, 62, 63 };
    public static final int[] ROCHADE_L_BLACK = { 56, 57, 58, 59, 60 };

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
            -1,  0,  1,  2,  3,  4,  5,  6,  7, -1,
            -1,  8,  9, 10, 11, 12, 13, 14, 15, -1,
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
            { -10, -1, 1, 10}, /* ROOK */
            { -11, -10, -9, -1, 1, 9, 10, 11 }, /* QUEEN */
            { -11, -10, -9, -1, 1, 9, 10, 11 }  /* KING */
    };

    private static final int[] pawnCaptureOffset = { 11, 9 };

    /**
     * @param board current board
     * @param side  the side to move
     */
    public MoveList generate(BoardRepresentation board, Color side) {
        MoveList moves = Factory.getDefaults().moveList.create();
        return generate(board, side, moves);
    }

    /**
     * @param board current board
     * @param side  the side to move
     */
    public MoveList generate(BoardRepresentation board, Color side, MoveList moves) {

        Board2 board2 = (Board2) board;

        Color xside = side.invert();  /* the side not to move */

        for (int i = 0; i < 64; ++i) { /* loop over all squares (no piece list) */
            byte figure = board.getFigureCode(i);
            if (figure != FigureConstants.FT_EMPTY && Figure.getColor(figure) == side) { /* looking for own pieces and pawns to move */
                boolean isPawn = figure == FigureConstants.W_PAWN || figure == FigureConstants.B_PAWN;


                if (!isPawn) { /* piece or pawn */
                    byte figureCode = (byte) (figure & MASK_OUT_COLOR);
                    int[] figOffsets = offset[figureCode];
                    genPieceMoves(board, i, moves, xside, figOffsets, slide[figureCode]);
                } else {
                    /* pawn moves */
                    genPawnMoves(board, moves, i, side);

                }
            }
        }
        generateRochade(board, side, moves);
        // todo en passant is missing...
        return moves;
    }

    private void genPieceMoves(BoardRepresentation board, int i, MoveList moves, Color xside,
                               int[] figOffsets, boolean slide) {

        for (int j = 0; j < figOffsets.length; ++j) { /* for all knight or ray directions */
            for (int n = i;;) { /* starting with from square */
                n = mailbox[mailbox64[n] + figOffsets[j]]; /* next square along the ray j */
                if (n == -1) break; /* outside board */
                byte targetN = board.getFigureCode(n);
                if (targetN != FigureConstants.FT_EMPTY) {
                    if (Figure.getColor(targetN) == xside)
                        moves.genMove(i, n, targetN); /* capture from i to n */
                    break;
                }
                moves.genMove(i, n, (byte)0); /* quiet move from i to n */
                if (!slide) break; /* next direction */
            }
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
    public boolean canFigureCaptured(BoardRepresentation board, int i) {
        byte figure = board.getFigureCode(i);
        Color side = Figure.getColor(figure);
        Color xside = side.invert();

        CaptureChecker captureChecker = new CaptureChecker();
        // 1. test rook && queen vertical horizontal captures:
        genPieceMoves(board, i, captureChecker, xside, offset[FigureConstants.FT_ROOK], slide[FigureConstants.FT_ROOK]);
        if (captureChecker.hasCapturesBy(FigureConstants.FT_ROOK)) {
            return true;
        }
        if (captureChecker.hasCapturesBy(FigureConstants.FT_QUEEN)) {
            return true;
        }
        // 2. test bishop and queen diagonal captures:
        captureChecker.reset();
        genPieceMoves(board, i, captureChecker, xside, offset[FigureConstants.FT_BISHOP], slide[FigureConstants.FT_BISHOP]);
        if (captureChecker.hasCapturesBy(FigureConstants.FT_BISHOP)) {
            return true;
        }
        if (captureChecker.hasCapturesBy(FigureConstants.FT_QUEEN)) {
            return true;
        }

        // 3. test knight
        captureChecker.reset();
        genPieceMoves(board, i, captureChecker, xside, offset[FigureConstants.FT_KNIGHT], slide[FigureConstants.FT_KNIGHT]);
        if (captureChecker.hasCapturesBy(FigureConstants.FT_KNIGHT)) {
            return true;
        }

        // 4. pawns:
        captureChecker.reset();
        genPawnMoves(board, captureChecker, i, side);
        if (captureChecker.hasCapturesBy(FigureConstants.FT_PAWN)) {
            return true;
        }

        // 5. test king
        captureChecker.reset();
        genPieceMoves(board, i, captureChecker, xside, offset[FigureConstants.FT_KING], slide[FigureConstants.FT_KING]);
        if (captureChecker.hasCapturesBy(FigureConstants.FT_KING)) {
            return true;
        }

        return false;
    }


    private boolean checkPos(BoardRepresentation board, int[] pos, Figure... figures) {
        for (int i = 0; i < pos.length; i++) {
            Figure figure = board.getPos(pos[i]);
            if (figures[i] != figure) {
                return false;
            }
        }
        return true;
    }

    private void generateRochade(BoardRepresentation board, Color side, MoveList moves) {
        switch (side) {
        case WHITE:
            if (checkPos(board, ROCHADE_L_WHITE, W_Rook, EMPTY, EMPTY, EMPTY, W_King)) {
                if (board.getWhiteRochade().isaAllowed()) {
                    moves.addRochadeLongWhite();
                }
            }
            if (checkPos(board, ROCHADE_S_WHITE, W_King, EMPTY, EMPTY, W_Rook)) {
                if (board.getWhiteRochade().ishAllowed()) {
                    moves.addRochadeShortWhite();
                }
            }
            break;
        case BLACK:
            if (checkPos(board, ROCHADE_S_BLACK, B_King, EMPTY, EMPTY, B_Rook)) {
                if (board.getBlackRochace().ishAllowed()) {
                    moves.addRochadeShortBlack();
                }
            }
            if (checkPos(board, ROCHADE_L_BLACK, B_Rook, EMPTY, EMPTY, EMPTY, B_King)) {
                if (board.getBlackRochace().isaAllowed()) {
                    moves.addRochadeLongBlack();
                }
            }
            break;
        }
    }

    private void genPawnMoves(BoardRepresentation board, MoveList moves, int i, Color side) {
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
            if (target ==  FigureConstants.FT_EMPTY) {

                moves.genPawnMove(i, n, side, (byte)0, -1);
                int singlemove=n;
                if (isOnBaseLine) {
                    // get double move from baseline:
                    n = mailbox[mailbox64[i] + 2 * pawnOffset];
                    target = board.getFigureCode(n);
                    if (target ==  FigureConstants.FT_EMPTY) {
                        moves.genPawnMove(i, n, side, (byte)0, singlemove);
                    }
                }
            }
        }
        // check capture:
        genPawnCaptureMoves(board, moves, i, side);
    }

    private void genPawnCaptureMoves(BoardRepresentation board, MoveList moves, int i, Color side) {
        int n;
        int m = side == WHITE ? 1 : -1;
        Color xside = side.invert();
        for (int offset : pawnCaptureOffset) {
            n = mailbox[mailbox64[i] + offset * m];
            if (n != -1) {
                byte target = board.getFigureCode(n);
                if (target != FigureConstants.FT_EMPTY && Figure.getColor(target) == xside) {
                    moves.genPawnMove(i, n, side, target, -1);
                } else if (board.isEnPassantCapturePossible(n)) {
                    moves.genEnPassant(i, n, side, board.getEnPassantCapturePos());
                }
            }
        }
    }


}
