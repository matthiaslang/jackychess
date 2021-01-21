package org.mattlang.jc.engine.evaluation;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.PieceList;

/**
 * Tapered Eval, see https://www.chessprogramming.org/Tapered_Eval.
 */
public class TaperedEval {

    public static final int PAWN_PHASE = 0;
    public static final int KNIGHT_PHASE = 1;
    public static final int BISHOP_PHASE = 1;
    public static final int ROOK_PHASE = 2;
    public static final int QUEEN_PHASE = 4;
    public static final int TOTAL_PHASE =
            PAWN_PHASE * 16 + KNIGHT_PHASE * 4 + BISHOP_PHASE * 4 + ROOK_PHASE * 4 + QUEEN_PHASE * 2;

    public int calcPhase(BoardRepresentation board) {
        int phase = TOTAL_PHASE;

        PieceList white = board.getWhitePieces();
        PieceList black = board.getBlackPieces();

        phase -= white.getPawns().size() * PAWN_PHASE; // Where wp is the number of white pawns currently on the board
        phase -= white.getKnights().size() * KNIGHT_PHASE;    // White knights
        phase -= white.getBishops().size() * BISHOP_PHASE;
        phase -= white.getRooks().size() * ROOK_PHASE;
        phase -= white.getQueens().size() * QUEEN_PHASE;

        phase -= black.getPawns().size() * PAWN_PHASE;
        phase -= black.getKnights().size() * KNIGHT_PHASE;
        phase -= black.getBishops().size() * BISHOP_PHASE;
        phase -= black.getRooks().size() * ROOK_PHASE;
        phase -= black.getQueens().size() * QUEEN_PHASE;

        phase = (phase * 256 + (TOTAL_PHASE / 2)) / TOTAL_PHASE;

        return phase;
    }

    public int eval(BoardRepresentation board, int opening, int endgame) {
        int phase = calcPhase(board);
        int eval = ((opening * (256 - phase)) + (endgame * phase)) / 256;
        return eval;
    }
}
