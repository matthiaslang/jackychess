package org.mattlang.jc.engine.evaluation.taperedEval;

import static org.mattlang.jc.engine.evaluation.Weights.DOUBLED_PAWN_PENALTY;
import static org.mattlang.jc.engine.evaluation.Weights.PASSED_PAWN_WEIGHT;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.evaluation.BoardStats;

/**
 * Analyzes pawn structure.
 */
public class PawnStructureEval {

    public int eval(BoardRepresentation currBoard, BoardStats wstats, BoardStats bstats, Color who2Move) {

        // passing pawns:
        int who2mov = who2Move == Color.WHITE ? 1 : -1;

        int[] rowWChecker = new int[8];
        int[] rowBChecker = new int[8];
        for (int i = 0; i < 8; i++) {
            rowWChecker[i] = -1;
            rowBChecker[i] = -1;
        }

        int wDouble = 0;
        int bDouble = 0;
        for (int pawnPos : currBoard.getWhitePieces().getPawns().getArr()) {
            int x = pawnPos % 8;
            int y = pawnPos / 8;

            if (rowWChecker[x] >= 0) {
                wDouble++;
                rowWChecker[x] = y;
            } else {
                rowWChecker[x] = y;
            }
        }

        for (int pawnPos : currBoard.getBlackPieces().getPawns().getArr()) {
            int x = pawnPos % 8;
            int y = pawnPos / 8;
            rowBChecker[x] = y;
        }

        int wPassed = 0;
        int bPassed = 0;

        for (int i = 0; i < 8; i++) {
            if (rowWChecker[i] >= 0) {
                if (rowBChecker[i] < 0) {
                    // we have a passed one:
                    wPassed++;
                }
            } else if (rowBChecker[i] >= 0) {
                if (rowWChecker[i] < 0) {
                    bPassed++;
                }
            }
        }

        return PASSED_PAWN_WEIGHT * (wPassed - bPassed) * who2mov
                + DOUBLED_PAWN_PENALTY * (wDouble - bDouble) * who2mov;
    }
}
