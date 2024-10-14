package org.mattlang.jc.engine.evaluation;

import static org.mattlang.jc.board.BB.IN_BETWEEN;
import static org.mattlang.jc.board.FigureConstants.*;
import static org.mattlang.jc.board.bitboard.MagicBitboards.genBishopAttacs;
import static org.mattlang.jc.board.bitboard.MagicBitboards.genRookAttacs;

import org.mattlang.jc.board.bitboard.BitChessBoard;

import lombok.Getter;

@Getter
public class PinnedCalc {

    /**
     * all pinned pieces (both black and white).
     * (They can´t be moved without giving check to its site).
     */

    private long pinnedPieces = 0;

    /**
     * all discovered pieces (both black and white).
     * (They give check to the other site by moving out of line).
     */

    private long discoveredPieces = 0;


    /**
     * Checking pieces for white and black.
     */
    //    private long[] checkingPieces = new long[2];

    public void calcPinnedDiscoveredAndChecking(BitChessBoard board) {

        pinnedPieces = 0;
        discoveredPieces = 0;

        //        int kingPosWhite = Long.numberOfTrailingZeros(getKings(nWhite));
        //        int kingPosBlack = Long.numberOfTrailingZeros(getKings(nBlack));

        // init checking pieces with king captures by knights, pawns and Kings:
        // the "King" part is only relevant to reuse the checkingPieces Field for illeal moves.
        // and therefore only for the current "opponent" side. Maybe we should therefore only do it for the "opponent" side
        //        checkingPieces[nWhite] = getKnights(nBlack) & getKnightAttacs(kingPosWhite)
        //                | genPawnAttacs(this, kingPosWhite, BLACK)
        //                | getKings(nBlack) & getKingAttacs(kingPosWhite);
        //
        //        checkingPieces[nBlack] = getKnights(nWhite) & getKnightAttacs(kingPosBlack)
        //                | genPawnAttacs(this, kingPosBlack, WHITE)
        //                | getKings(nWhite) & getKingAttacs(kingPosBlack);

        long allPieces = board.getPieces();

        long bishopAndQueen = board.getPieceSet(FT_BISHOP) | board.getPieceSet(FT_QUEEN);
        long rookAndQueen = board.getPieceSet(FT_ROOK) | board.getPieceSet(FT_QUEEN);

        if (bishopAndQueen == 0 && rookAndQueen == 0) {
            return;
        }

        for (int kingColor = 0; kingColor <= 1; kingColor++) {

            int enemyColor = kingColor == 0 ? 1 : 0;
            long enemyPieces = board.getColorMask(enemyColor);

            long enemyBishopAndQueen = bishopAndQueen & enemyPieces;
            long enemyRookAndQueen = rookAndQueen & enemyPieces;
            if (enemyBishopAndQueen == 0 && enemyRookAndQueen == 0) {
                continue;
            }

            int kingColorPos = Long.numberOfTrailingZeros(board.getKings(kingColor));

            long enemyPiece =
                    genBishopAttacs(kingColorPos, enemyBishopAndQueen) & enemyBishopAndQueen
                            | genRookAttacs(kingColorPos, enemyRookAndQueen) & enemyRookAndQueen;

            long ourPieces = board.getColorMask(kingColor);
            while (enemyPiece != 0) {
                final long checkedPiece = IN_BETWEEN[kingColorPos][Long.numberOfTrailingZeros(enemyPiece)] & allPieces;
                //                if (checkedPiece == 0) {
                //                    checkingPieces[kingColor] |= Long.lowestOneBit(enemyPiece);
                //                } else
                if (Long.bitCount(checkedPiece) == 1) {
                    pinnedPieces |= checkedPiece & ourPieces;
                    discoveredPieces |= checkedPiece & enemyPieces;
                }
                enemyPiece &= enemyPiece - 1;
            }
        }
    }

    public boolean isDiscoveredMove(final int fromIndex) {
        if (discoveredPieces == 0) {
            return false;
        }
        return (discoveredPieces & (1L << fromIndex)) != 0;
    }
}
