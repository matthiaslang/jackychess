package org.mattlang.jc.engine.evaluation.parameval.endgame;

import static org.mattlang.jc.board.bitboard.BB.Square.SQ_A1;
import static org.mattlang.jc.engine.evaluation.Tools.*;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BitChessBoard;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedMaterialEvaluation;

/**
 * Mate with KBN vs K. This is similar to KX vs K, but we have to drive the
 * defending king towards a corner square that our bishop attacks.
 */
public class KBNvsKEndgameFunction implements EndgameFunction {

    private static final int VALUE_KNOWN_WIN = 10000;

    public int evaluate(BoardRepresentation board, Color stronger, Color weaker,
            ParameterizedMaterialEvaluation matEvaluation) {

        BitChessBoard bb = board.getBoard();

        // Stalemate detection with lone king
        //        if (pos.side_to_move() == weakSide && !MoveList<LEGAL>(pos).size())
        //            return VALUE_DRAW;

        int strongKing = Long.numberOfTrailingZeros(bb.getKings(stronger));
        int strongBishop = Long.numberOfTrailingZeros(bb.getBishops(stronger));
        int weakKing = Long.numberOfTrailingZeros(bb.getKings(weaker));

        // If our bishop does not attack A1/H8, we flip the enemy king square
        // to drive to opposite corners (A8/H1).

        int result = (VALUE_KNOWN_WIN + 3520)
                + push_close(strongKing, weakKing)
                + 420 * push_to_corner(opposite_colors(strongBishop, SQ_A1.ordinal()) ? flip_file(weakKing) : weakKing);

        return stronger == board.getSiteToMove() ? result : -result;
    }
}
