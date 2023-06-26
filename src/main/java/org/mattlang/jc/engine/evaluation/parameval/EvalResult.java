package org.mattlang.jc.engine.evaluation.parameval;

import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.FigureConstants.FT_ALL;
import static org.mattlang.jc.board.FigureConstants.FT_PAWN;
import static org.mattlang.jc.engine.evaluation.PhaseCalculator.scaleByPhase;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BB;
import org.mattlang.jc.board.bitboard.BitChessBoard;

import lombok.Getter;

/**
 * Contains the intermediate results of the evaluation.
 */
@Getter
public final class EvalResult {

    /**
     * non pawn material, sum of both sides.
     */
    public int nonPawnMat;

    /**
     * summed up mid game/ end game score.
     */
    private MgEgScore mgEgScore = new MgEgScore();

    /**
     * summed up score which is not tapered.
     */
    public int result;

    private final long[][] attacks = new long[2][7];
    private final long[] doubleAttacks = new long[2];

    public void clear() {
        mgEgScore.clear();
        result = 0;

        for (int i = 0; i < 2; i++) {
            doubleAttacks[i] = 0;
            for (int j = 0; j < 7; j++) {
                attacks[i][j] = 0L;
            }
        }
    }

    /**
     * Calculates the complete score by tapereing the mid and end game score and adding the non-tapered score.
     *
     * @param bitBoard
     * @return
     */
    public int calcCompleteScore(BoardRepresentation bitBoard) {
        int score = (int) scaleByPhase(bitBoard.getBoard(), mgEgScore.getMgScore(), mgEgScore.getEgScore()) + result;
        return score;
    }

    public void updateAttacks(final long attacs, final byte figureType, final int color) {
        doubleAttacks[color] |= attacks[color][FT_ALL] & attacs;
        attacks[color][FT_ALL] |= attacs;
        attacks[color][figureType] |= attacs;
    }

    public void updatePawnAttacs(BitChessBoard bb) {

        long wPawns = bb.getPieceSet(FT_PAWN, WHITE);
        long bPieces = bb.getColorMask(BLACK);

        long capturesEast = wPawns & BB.bPawnWestAttacks(bPieces);
        long capturesWest = wPawns & BB.bPawnEastAttacks(bPieces);
        updateAttacks(capturesEast | capturesWest, FT_PAWN, BitChessBoard.nWhite);

        long bPawns = bb.getPieceSet(FT_PAWN, BLACK);
        long wPieces = bb.getColorMask(WHITE);

        capturesEast = bPawns & BB.wPawnWestAttacks(wPieces);
        capturesWest = bPawns & BB.wPawnEastAttacks(wPieces);

        updateAttacks(capturesEast | capturesWest, FT_PAWN, BitChessBoard.nBlack);

    }

    public long getAttacks(int color, byte figureType) {
        return attacks[color][figureType];
    }

    public long getAttacks(Color color, byte figureType) {
        return attacks[color.ordinal()][figureType];
    }

    public long getDoubleAttacks(Color color) {
        return doubleAttacks[color.ordinal()];
    }

    public EvalResult add(MgEgScore score) {
        mgEgScore.add(score);
        return this;
    }

    public EvalResult minus(MgEgScore score) {
        mgEgScore.subtract(score);
        return this;
    }
}
