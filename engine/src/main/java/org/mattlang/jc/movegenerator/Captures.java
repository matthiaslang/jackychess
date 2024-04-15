package org.mattlang.jc.movegenerator;

import static org.mattlang.jc.board.FigureConstants.*;
import static org.mattlang.jc.board.bitboard.BitChessBoard.nWhite;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Figure;
import org.mattlang.jc.board.bitboard.BB;
import org.mattlang.jc.board.bitboard.BitChessBoard;
import org.mattlang.jc.board.bitboard.MagicBitboards;

/**
 * Methods to prove if a figure/field can be captured.
 */
public class Captures {

    /**
     * Returns true, if the figure on position i can be captured by the other side.
     * This could be used to test check situations during legal move generation, etc.
     *
     * It tests captures by using "inverse" logic, going from the position in question to the opponents positions.
     *
     * @param board
     * @param i
     * @return
     */
    public static boolean canFigureCaptured(BoardRepresentation board, int i) {
        byte figure = board.getFigureCode(i);
        Color side = Figure.getColor(figure);
        return canFigureCaptured(board, i, side);
    }

    public static boolean canKingCaptured(BoardRepresentation bitBoard, Color side) {
        long kingBB = bitBoard.getBoard().getPieceSet(FT_KING, side);
        int kingPos = Long.numberOfTrailingZeros(kingBB);
        return canFigureCaptured(bitBoard, kingPos, side);
    }

    /**
     * Returns true, if the fiel on position i can be captured/controlled by the other side.
     * This could be used to test check situations during legal move generation, etc.
     *
     * It tests captures by using "inverse" logic, going from the position in question to the opponents positions.
     *
     * @param board
     * @param i
     * @return
     */
    public static boolean canFigureCaptured(BoardRepresentation board, int i, Color cside) {

        int side = cside.ordinal();
        int xside = cside.invert().ordinal();

        BitChessBoard bb = board.getBoard();
        long ownFigsMask = bb.getColorMask(side);
        long opponentFigsMask = bb.getColorMask(xside);

        // 1. test bishop and queen diagonal captures
        long occupancy = ownFigsMask | opponentFigsMask;

        long attacks = MagicBitboards.genBishopAttacs(i, occupancy);
        if ((attacks & bb.getPieceSet(FT_BISHOP, xside)) != 0) {
            return true;
        }
        long otherQueens = bb.getPieceSet(FT_QUEEN, xside);
        if ((attacks & otherQueens) != 0) {
            return true;
        }

        // 2. test rook and queen vertical/horizontal captures:
        attacks = MagicBitboards.genRookAttacs(i, occupancy);
        if ((attacks & otherQueens) != 0) {
            return true;
        }
        if ((attacks & bb.getPieceSet(FT_ROOK, xside)) != 0) {
            return true;
        }

        // 3. test knight
        attacks = BB.getKnightAttacs(i);
        if ((attacks & bb.getPieceSet(FT_KNIGHT, xside)) != 0) {
            return true;

        }

        // 4. pawns:
        long otherPawns = bb.getPieceSet(FT_PAWN, xside);
        long figMask = 1L << i;

        if (side == nWhite) {
            long capturesEast = BB.bPawnWestAttacks(otherPawns);
            if ((figMask & capturesEast) != 0) {
                return true;
            }

            long capturesWest = BB.bPawnEastAttacks(otherPawns);
            if ((figMask & capturesWest) != 0) {
                return true;
            }

        } else {
            long capturesEast = BB.wPawnWestAttacks(otherPawns);
            if ((figMask & capturesEast) != 0) {
                return true;
            }

            long capturesWest = BB.wPawnEastAttacks(otherPawns);
            if ((figMask & capturesWest) != 0) {
                return true;
            }
        }

        // 5. test king
        attacks = BB.getKingAttacs(i);
        if ((attacks & bb.getPieceSet(FT_KING, xside)) != 0) {
            return true;
        }

        return false;
    }

}
