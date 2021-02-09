package org.mattlang.jc.engine.evaluation.taperedEval;

import static org.mattlang.jc.engine.evaluation.Weights.DOUBLED_PAWN_PENALTY;
import static org.mattlang.jc.engine.evaluation.Weights.PASSED_PAWN_WEIGHT;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.PieceList;
import org.mattlang.jc.engine.evaluation.BoardStats;

/**
 * Analyzes pawn structure.
 */
public class PawnStructureEval {

    static class PawnLineChecker {

        int[] rowChecker = new int[8];

        int[] doubles = new int[8];

        int passed = 0;

        int doubleCount = 0;

        public PawnLineChecker() {
            for (int i = 0; i < 8; i++) {
                rowChecker[i] = -1;
            }
        }

        public void fill(PieceList.Array pawns) {
            for (int pawnPos : pawns.getArr()) {
                int x = pawnPos % 8;
                int y = pawnPos / 8;

                if (rowChecker[x] >= 0) {
                    doubles[x]++;
                    doubleCount++;
                }
                rowChecker[x] = y;
            }
        }
    }

    public void analyzePassed(PawnLineChecker wline, PawnLineChecker bline) {
        for (int i = 0; i < 8; i++) {
            if (wline.rowChecker[i] >= 0) {
                if (bline.rowChecker[i] < 0) {
                    // we have a passed one, if we do not have a double one:
                    if (wline.doubles[i] == 0) {
                        wline.passed++;
                    }
                }
            }
            if (bline.rowChecker[i] >= 0) {
                if (wline.rowChecker[i] < 0) {
                    if (bline.doubles[i] == 0) {
                        bline.passed++;
                    }
                }
            }
        }
    }

    public int eval(BoardRepresentation currBoard, BoardStats wstats, BoardStats bstats, Color who2Move) {

        // passing pawns:
        int who2mov = who2Move == Color.WHITE ? 1 : -1;

        PawnLineChecker wChecker = new PawnLineChecker();
        PawnLineChecker bChecker = new PawnLineChecker();

        wChecker.fill(currBoard.getWhitePieces().getPawns());
        bChecker.fill(currBoard.getBlackPieces().getPawns());

        analyzePassed(wChecker, bChecker);

        return PASSED_PAWN_WEIGHT * (wChecker.passed - bChecker.passed) * who2mov
                + DOUBLED_PAWN_PENALTY * (wChecker.doubleCount - bChecker.doubleCount) * who2mov;
    }
}
