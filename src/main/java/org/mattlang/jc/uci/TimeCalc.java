package org.mattlang.jc.uci;

import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.engine.evaluation.PhaseCalculator;

public class TimeCalc {

    public static final int DELAY_BUFFER_MS = 100;
    public static final int ONE_SECOND_MS = 1000;

    public static long determineCalculationTime(GameState gameState, GoParameter goParams) {
        // if we have special "go" parameters, then override thinktime:
        if (goParams.movetime > 0) {
            return goParams.movetime;
        } else {
            long restTime;
            long incTime;
            long opponentRestTime;
            long opponentIncTime;
            long restMoves;
            if (gameState.getWho2Move() == Color.WHITE) {
                restTime = goParams.wtime;
                incTime = goParams.winc;
                opponentRestTime = goParams.btime;
                opponentIncTime = goParams.binc;
            } else {
                restTime = goParams.btime;
                incTime = goParams.binc;
                opponentRestTime = goParams.wtime;
                opponentIncTime = goParams.winc;
            }
            restMoves = goParams.movestogo;
            double phase = PhaseCalculator.calcPhaseFactor(gameState.getBoard());

            return determineTime(restTime, opponentRestTime, incTime, opponentIncTime, restMoves, phase);
        }
    }

    public static long determineTime(long restTime, long opponentRestTime, long incTime,
            long opponentIncTime,
            long restMoves,
            double phase) {

        // preset with halve the inc time (if we have one):
        long time = incTime / 2;

        // be a bit more generours, if we have more time than the opponent; in that case add a port of the diff
        // if we are behind subtract a bit:
        time += calcBonus(restTime, opponentRestTime);

        // if we have restmoves given, scale by the rest moves:
        time += splitRestTime(restMoves, restTime, phase);

        // double check and ajust chosen time if we have approximatly only time left for 5 or 10 moves:
        if (time != 0) {
            int approxamatelyFutureMoves = (int) (restTime / time);
            if (approxamatelyFutureMoves < 5) {
                time -= restTime / 10;
            } else if (approxamatelyFutureMoves < 10) {
                time -= restTime / 20;
            }
        }

        time = doubleCheckLimits(time, restTime, incTime);

        return time;
    }

    private static int AVERAGE_MOVES_PER_GAME = 40;
    private static int AVERAGE_MOVES_IN_ENDGAME = 20;

    /**
     * Silly forecase of future moves depending on game phase.
     *
     * @param phase game phase
     * @return
     */
    private static int forecastFutureMoveCount(double phase) {

        // lets say we have 40 moves in general for opening/middle and 20 for end game:
        // weight by phase where we are; endgame weights always full:
        return (int) (AVERAGE_MOVES_PER_GAME * phase + AVERAGE_MOVES_IN_ENDGAME);

        // todo maybe include current score to scale better here?
    }

    /**
     * Calculate a bonus time based if we have more time than the opponent.
     *
     * @param restTime
     * @param opponentRestTime
     * @return
     */
    private static long calcBonus(long restTime, long opponentRestTime) {
        long diff = restTime - opponentRestTime;
        if (diff > 0) {
            return diff / 3;
        } else {
            return 0;
        }
    }

    /**
     * Split the rest of our time by the given rest moves or estimated moves to add to our calculation time.
     *
     * @param restMoves
     * @param restTime
     * @param phase
     * @return
     */
    private static long splitRestTime(long restMoves, long restTime, double phase) {
        // if we have restmoves given, scale by the rest moves:
        if (restMoves != 0 && restTime > 0) {
            long averageTimeForThisMove = restTime / restMoves;
            return averageTimeForThisMove;
        } else {
            // otherwise try a silly forecase of future move count based on the board material:
            int estimatedMoves = forecastFutureMoveCount(phase);
            long averageTime = restTime / estimatedMoves;
            return averageTime;
        }
    }

    /**
     * Double check the calculated time, that we do not exceed our rest time, especially when we have nearly no time
     * left.
     * Choose a value based on inctime if available as fallback.
     *
     * @param time
     * @param restTime
     * @param incTime
     * @return
     */
    private static long doubleCheckLimits(long time, long restTime, long incTime) {
        // subtract a bit to not overceed the time by engine stop delays:
        if (time > ONE_SECOND_MS) {
            time -= DELAY_BUFFER_MS;
        }

        // fallback:
        if (time <= 0) {
            time = ONE_SECOND_MS;
        }

        // but we should spend at least halve the inc time if we have inc time available
        if (incTime > 0 && time < incTime / 2) {
            time = incTime / 2;

        }

        // if we estime still more than the resttime, go back to half the inc time if possible.
        if (time > restTime) {
            if (incTime > 0) {
                time = incTime / 2;
            } else {
                // we are nearly out of time... no rescue: at least only use half of the time:
                time = restTime / 2;
            }
            if (time >= ONE_SECOND_MS) {
                time -= DELAY_BUFFER_MS;
            }
        }
        return time;
    }
}
