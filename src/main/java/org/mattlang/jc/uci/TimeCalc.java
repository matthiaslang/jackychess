package org.mattlang.jc.uci;

import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.engine.evaluation.PhaseCalculator;

public class TimeCalc {

    public static final int DELAY_BUFFER_MS = 200;
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
            return determineTime(gameState, restTime, opponentRestTime, incTime, opponentIncTime, restMoves);
        }
    }

    private static long determineTime(GameState gameState, long restTime, long opponentRestTime, long incTime,
            long opponentIncTime,
            long restMoves) {

        long time = incTime / 2;

        long diff = restTime - opponentRestTime;
        if (diff > 0) {
            time += diff / 3;
        } else {
            time -= diff / 3;
        }

        if (restMoves != 0 && restTime > 0) {
            long averageTimeForThisMove = restTime / restMoves;

            time += averageTimeForThisMove;
        } else {
            // determine by gamephase
            double phase = PhaseCalculator.calcPhaseFactor(gameState.getBoard());
            boolean isOpening = phase > 0.6;
            if (isOpening) {
                // estimate moves by phase:
                // lets say a game has 60 moves in average:
                int averageMovesPerGame = 60;
                // weight by phase where we are
                averageMovesPerGame = (int) (averageMovesPerGame * phase + 5);

                long averageTime = restTime / averageMovesPerGame;

                time += averageTime;
            } else {
                // average end game moves..
                int averageMovesEndGame = 20;
                long averageTime = restTime / averageMovesEndGame;
                time += averageTime;
            }
        }

        // subtract a bit to not overceed the time by engine stop delays:
        if (time > ONE_SECOND_MS) {
            time -= DELAY_BUFFER_MS;
        }

        // fallback:
        if (time <= 0) {
            time = ONE_SECOND_MS;
        }

        // double check chosen time:
        int approxamatelyFutureMoves = (int) (restTime / time);
        if (approxamatelyFutureMoves < 5) {
            time = restTime / 10;
        } else if (approxamatelyFutureMoves < 10) {
            time /= 2;
        }

        if (time > restTime) {
            if (incTime > 0) {
                time = incTime;
            }
            if (time > restTime) {
                time = restTime;
            }
            if (time >= ONE_SECOND_MS) {
                time -= DELAY_BUFFER_MS;
            }
        }
        return time;
    }
}
