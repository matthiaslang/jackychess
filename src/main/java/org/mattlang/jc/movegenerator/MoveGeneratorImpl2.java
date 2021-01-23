package org.mattlang.jc.movegenerator;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.*;
import org.mattlang.jc.engine.MoveList;

import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.Figure.*;
import static org.mattlang.jc.board.FigureConstants.*;

/**
 * see https://www.chessprogramming.org/10x12_Board
 * TSCP Implementation of move generator with some own modifications.
 *
 * Optimization, working only with Board2.
 */
public class MoveGeneratorImpl2 implements MoveGenerator {

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

        Color xside = side.invert();  /* the side not to move */

        for (int i = 0; i < 64; ++i) { /* loop over all squares (no piece list) */
            byte figure = board.getFigureCode(i);
            if (figure != FigureConstants.FT_EMPTY && Figure.getColor(figure) == side) { /* looking for own pieces and pawns to move */
                boolean isPawn = figure == FigureConstants.W_PAWN || figure == FigureConstants.B_PAWN;


                if (!isPawn) { /* piece or pawn */
                    byte figureCode = (byte) (figure & MASK_OUT_COLOR);
                    int[] figOffsets = offset[figureCode];
                    genPieceMoves(board, i, moves, xside, figureCode, figOffsets, slide[figureCode]);
                } else {
                    /* pawn moves */
                    genPawnMoves(board, moves, i, side);

                }
            }
        }
        generateRochade(board, side, moves);
        return moves;
    }

    private static void genPieceMoves(BoardRepresentation board, int i, MoveList moves, Color xside,
                               byte figureType,
                                      int[] figOffsets, boolean slide) {

        for (int j = 0; j < figOffsets.length; ++j) { /* for all knight or ray directions */
            for (int n = i;;) { /* starting with from square */
                n = mailbox[mailbox64[n] + figOffsets[j]]; /* next square along the ray j */
                if (n == -1) break; /* outside board */
                byte targetN = board.getFigureCode(n);
                if (targetN != FigureConstants.FT_EMPTY) {
                    if (Figure.getColor(targetN) == xside)
                        moves.genMove(figureType, i, n, targetN); /* capture from i to n */
                    break;
                }
                moves.genMove(figureType, i, n, (byte)0); /* quiet move from i to n */
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

    static class CastlingDef{
        Color side;
        RochadeType rochadeType;

        int[] fieldPos;
        Figure[] fieldPosFigures;
        int[] fieldCheckTst;

        public CastlingDef(Color side, RochadeType rochadeType, int[] fieldPos, Figure[] fieldPosFigures, int[] fieldCheckTst) {
            this.side = side;
            this.rochadeType = rochadeType;
            this.fieldPos = fieldPos;
            this.fieldPosFigures = fieldPosFigures;
            this.fieldCheckTst = fieldCheckTst;
        }

        public boolean check(BoardRepresentation board) {
            // check if rochade is still allowed:
            if (board.isCastlingAllowed(side, rochadeType)){
                // check that the relevant figures and empty fields are as needs to be for castling:
                if (checkPos(board)) {
                    // check that king pos and moves are not in check:
                    for (int pos : fieldCheckTst) {
                        if (MoveGeneratorImpl2.canFigureCaptured(board, pos, side)) {
                            return false;
                        }
                    }
                    return true;
                }
            }
            return false;
        }

        private boolean checkPos(BoardRepresentation board) {
            for (int i = 0; i < fieldPos.length; i++) {
                Figure figure = board.getPos(fieldPos[i]);
                if (fieldPosFigures[i] != figure) {
                    return false;
                }
            }
            return true;
        }
    }

    public static final CastlingDef ROCHADE_L_WHITE = new CastlingDef(
            WHITE,
            RochadeType.LONG,
            new int[]{0, 1, 2, 3, 4},
            new Figure[]{W_Rook, EMPTY, EMPTY, EMPTY, W_King},
            new int[]{2, 3, 4});

    public static final CastlingDef ROCHADE_S_WHITE = new CastlingDef(
            WHITE,
            RochadeType.SHORT,
            new int[]{4, 5, 6, 7},
            new Figure[]{W_King, EMPTY, EMPTY, W_Rook},
            new int[]{4, 5, 6});

    public static final CastlingDef ROCHADE_S_BLACK = new CastlingDef(
            BLACK,
            RochadeType.SHORT,
            new int[]{60, 61, 62, 63},
            new Figure[]{B_King, EMPTY, EMPTY, B_Rook},
            new int[]{60, 61, 62});

    public static final CastlingDef ROCHADE_L_BLACK = new CastlingDef(
            BLACK,
            RochadeType.LONG,
            new int[]{56, 57, 58, 59, 60},
            new Figure[]{B_Rook, EMPTY, EMPTY, EMPTY, B_King},
            new int[]{58, 59, 60});


    private void generateRochade(BoardRepresentation board, Color side, MoveList moves) {
        switch (side) {
            case WHITE:
                if (ROCHADE_L_WHITE.check(board)) {
                    moves.addRochadeLongWhite();
                }
                if (ROCHADE_S_WHITE.check(board)) {
                    moves.addRochadeShortWhite();
                }
                break;
            case BLACK:
                if (ROCHADE_S_BLACK.check(board)) {
                    moves.addRochadeShortBlack();
                }
                if (ROCHADE_L_BLACK.check(board)) {
                    moves.addRochadeLongBlack();
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

    private static void genPawnCaptureMoves(BoardRepresentation board, MoveList moves, int i, Color side) {
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
                } else if (target == FigureConstants.FT_EMPTY) {
                    moves.hypotheticalPawnCapture(i, n);
                }
            }
        }
    }


}
