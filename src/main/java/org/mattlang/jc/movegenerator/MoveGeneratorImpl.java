package org.mattlang.jc.movegenerator;

import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.Figure.*;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Figure;
import org.mattlang.jc.board.FigureType;
import org.mattlang.jc.engine.MoveList;

/**
 * see https://www.chessprogramming.org/10x12_Board
 * TSCP Implementation of move generator with some own modifications.
 */
public class MoveGeneratorImpl implements MoveGenerator {

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
    private static final int[] offsets = { 0, 8, 4, 4, 8, 8 }; /* knight or ray directions */
    private static final int[][] offset = {
            { 0, 0, 0, 0, 0, 0, 0, 0 },
            { -21, -19, -12, -8, 8, 12, 19, 21 }, /* KNIGHT */
            { -11, -9, 9, 11, 0, 0, 0, 0 }, /* BISHOP */
            { -10, -1, 1, 10, 0, 0, 0, 0 }, /* ROOK */
            { -11, -10, -9, -1, 1, 9, 10, 11 }, /* QUEEN */
            { -11, -10, -9, -1, 1, 9, 10, 11 }  /* KING */
    };

    private static final int[] pawnCaptureOffset = { 11, 9 };

    /**
     * @param board current board
     * @param side  the side to move
     */
    public MoveList generate(Board board, Color side) {
        MoveList moves = Factory.createMoveList();
        return generate(board, side, moves);
    }

    /**
     * @param board current board
     * @param side  the side to move
     */
    public MoveList generate(Board board, Color side, MoveList moves) {


        Color xside = side == WHITE ? BLACK : WHITE;  /* the side not to move */

        for (int i = 0; i < 64; ++i) { /* loop over all squares (no piece list) */
            Figure figure = board.getFigure(i);
            if (figure != EMPTY && figure.color == side) { /* looking for own pieces and pawns to move */
                FigureType p = figure.figureType;
                if (p != FigureType.Pawn) { /* piece or pawn */
                    for (int j = 0; j < offsets[p.figureCode]; ++j) { /* for all knight or ray directions */
                        for (int n = i;;) { /* starting with from square */
                            n = mailbox[mailbox64[n] + offset[p.figureCode][j]]; /* next square along the ray j */
                            if (n == -1) break; /* outside board */
                            Figure targetN = board.getFigure(n);
                            if (targetN != EMPTY) {
                                if (targetN.color == xside)
                                    moves.genMove(i, n, targetN); /* capture from i to n */
                                break;
                            }
                            moves.genMove(i, n, null); /* quiet move from i to n */
                            if (!slide[p.figureCode]) break; /* next direction */
                        }
                    }
                } else {
                    /* pawn moves */
                    genPawnMoves(board, moves, i, figure);

                }
            }
        }
        generateRochade(board, side, moves);
        // todo en passant is missing...
        return moves;
    }

    private boolean checkPos(Board board, int[] pos, Figure... figures) {
        for (int i = 0; i < pos.length; i++) {
            Figure figure = board.getPos(pos[i]);
            if (figures[i] != figure) {
                return false;
            }
        }
        return true;
    }

    private void generateRochade(Board board, Color side, MoveList moves) {
        switch (side) {
        case WHITE:
            if (checkPos(board, ROCHADE_L_WHITE, W_Rook, EMPTY, EMPTY, EMPTY, W_King)) {
                moves.addRochadeLongWhite();
            }
            if (checkPos(board, ROCHADE_S_WHITE, W_King, EMPTY, EMPTY, W_Rook)) {
                moves.addRochadeShortWhite();
            }
            break;
        case BLACK:
            if (checkPos(board, ROCHADE_S_BLACK, B_King, EMPTY, EMPTY, B_Rook)) {
                moves.addRochadeShortBlack();
            }
            if (checkPos(board, ROCHADE_L_BLACK, B_Rook, EMPTY, EMPTY, EMPTY, B_King)) {
                moves.addRochadeLongBlack();
            }
            break;
        }
    }

    private void genPawnMoves(Board board, MoveList moves, int i, Figure figure) {
        boolean isOnBaseLine = false;
        if (figure.color == WHITE) {
            isOnBaseLine = i >= 8 && i <= 15;
        } else {
            isOnBaseLine = i >= 48 && i <= 55;
        }
        int pawnOffset = figure.color == WHITE ? 10 : -10;
        // check single and double move:
        // get single move:
        int n = mailbox[mailbox64[i] + pawnOffset];
        if (n != -1) {
            Figure target = board.getFigure(n);
            if (target == EMPTY) {
                // get double move from baseline:
                moves.genPawnMove(i, n, figure, null);
                if (isOnBaseLine) {
                    n = mailbox[mailbox64[i] + 2 * pawnOffset];
                    target = board.getFigure(n);
                    if (target == EMPTY) {
                        moves.genPawnMove(i, n, figure, null);
                    }
                }
            }
        }
        // check capture:
        int m = figure.color == WHITE ? 1 : -1;
        for (int offset : pawnCaptureOffset) {
            n = mailbox[mailbox64[i] + offset * m];
            if (n != -1) {
                Figure target = board.getFigure(n);
                if (target != EMPTY) {
                    moves.genPawnMove(i, n, figure, target);
                }
            }
        }
    }


}
