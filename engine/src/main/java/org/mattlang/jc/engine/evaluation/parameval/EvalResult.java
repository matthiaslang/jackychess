package org.mattlang.jc.engine.evaluation.parameval;

import static org.mattlang.jc.board.Color.*;
import static org.mattlang.jc.board.FigureConstants.FT_ALL;
import static org.mattlang.jc.board.FigureConstants.FT_PAWN;
import static org.mattlang.jc.engine.evaluation.PhaseCalculator.scaleByPhase;

import java.util.Arrays;

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

    public static final int MAX_ATT_INDEX = FT_ALL + 1;
    /**
     * non pawn material, sum of both sides.
     */
    public int nonPawnMat;

    // values cached in pawn/king hash cache:
    public int pawnEval;
    public long whitePassers;
    public long blackPassers;

    public int[] pksafety = new int[2];
    public int pkeval;

    /**
     * summed up mid game/ end game score.
     */
    private MgEgScore mgEgScore = new MgEgScore();

    /**
     * summed up score which is not tapered.
     */
    public int result;

    /**
     * all attacke per figure type; (index 0 unused, + "all" attacks on index FT_ALL).
     */
    private final long[][] attacks = new long[2][MAX_ATT_INDEX];
    private final long[] doubleAttacks = new long[2];

    /**
     * found pawn cache entry of the position to be evaluated.
     */
    private PawnCacheEntry pawnEntry;

    public void clear() {
        mgEgScore.clear();
        result = 0;

        for (int i = 0; i < 2; i++) {
            doubleAttacks[i] = 0;
            for (int j = FT_PAWN; j < MAX_ATT_INDEX; j++) {
                attacks[i][j] = 0L;
            }
        }
        pawnEval = 0;
        whitePassers = 0L;
        blackPassers = 0L;
        pkeval = 0;
        Arrays.fill(pksafety, 0);
        pawnEntry = null;

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
        updateAttacks(capturesEast | capturesWest, FT_PAWN, nWhite);

        long bPawns = bb.getPieceSet(FT_PAWN, BLACK);
        long wPieces = bb.getColorMask(WHITE);

        capturesEast = bPawns & BB.wPawnWestAttacks(wPieces);
        capturesWest = bPawns & BB.wPawnEastAttacks(wPieces);

        updateAttacks(capturesEast | capturesWest, FT_PAWN, nBlack);

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

    public long getDoubleAttacks(int color) {
        return doubleAttacks[color];
    }

    public EvalResult add(MgEgScore score) {
        mgEgScore.add(score);
        return this;
    }

    public EvalResult minus(MgEgScore score) {
        mgEgScore.subtract(score);
        return this;
    }

    public void setPawnEntry(PawnCacheEntry pawnEntry) {
        this.pawnEntry = pawnEntry;
    }

    public PawnCacheEntry getPawnEntry() {
        return pawnEntry;
    }
}
