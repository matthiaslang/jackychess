package org.mattlang.jc.engine.evaluation.parameval;

import static org.mattlang.jc.engine.evaluation.PhaseCalculator.scaleByPhase;

import org.mattlang.jc.board.bitboard.BitBoard;

import lombok.Getter;

@Getter
public class EvalResult {

    /**
     * summed up mid game score.
     */
    public int midGame;

    /**
     * summed up end game score.
     */
    public int endGame;

    /**
     * summed up score which is not tapered.
     */
    public int result;


    private DetailedEvalResult details = new DetailedEvalResult();

    public void clear() {
        midGame = 0;
        endGame = 0;
        result = 0;
    }

    /**
     * Calculates the complete score by tapereing the mid and end game score and adding the non-tapered score.
     *
     * @param bitBoard
     * @return
     */
    public int calcCompleteScore(BitBoard bitBoard) {
        int score = (int) scaleByPhase(bitBoard, midGame, endGame) + result;
        return score;
    }
}
