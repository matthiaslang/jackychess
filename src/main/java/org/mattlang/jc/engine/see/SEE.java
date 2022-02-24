package org.mattlang.jc.engine.see;

import static org.mattlang.jc.board.FigureConstants.*;
import static org.mattlang.jc.board.bitboard.BB.least_significant_square_bb;
import static org.mattlang.jc.board.bitboard.MagicBitboards.genBishopAttacs;
import static org.mattlang.jc.board.bitboard.MagicBitboards.genRookAttacs;

import org.mattlang.jc.board.*;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.board.bitboard.BitChessBoard;
import org.mattlang.jc.movegenerator.BBMoveGeneratorImpl2;

public class SEE {

    BBMoveGeneratorImpl2 moveGen = new BBMoveGeneratorImpl2();

    private static int[] pieceValue = new int[128];

    // todo take weights from evaluation instead of hard coding here...
    static {
        pieceValue[FigureType.Pawn.figureCode] = 100;
        pieceValue[Figure.B_Pawn.figureCode] = 100;
        pieceValue[Figure.W_Pawn.figureCode] = 100;

        pieceValue[FigureType.Knight.figureCode] = 325;
        pieceValue[Figure.B_Knight.figureCode] = 325;
        pieceValue[Figure.W_Knight.figureCode] = 325;

        pieceValue[FigureType.Bishop.figureCode] = 335;
        pieceValue[Figure.B_Bishop.figureCode] = 335;
        pieceValue[Figure.W_Bishop.figureCode] = 335;

        pieceValue[FigureType.Rook.figureCode] = 500;
        pieceValue[Figure.B_Rook.figureCode] = 500;
        pieceValue[Figure.W_Rook.figureCode] = 500;

        pieceValue[FigureType.Queen.figureCode] = 975;
        pieceValue[Figure.B_Queen.figureCode] = 975;
        pieceValue[Figure.W_Queen.figureCode] = 975;

        pieceValue[FigureType.King.figureCode] = 32000;
        pieceValue[Figure.B_King.figureCode] = 32000;
        pieceValue[Figure.W_King.figureCode] = 32000;

    }

    public static int pieceVal(byte fig) {
        return pieceValue[fig];
    }

    /// Position::see_ge (Static Exchange Evaluation Greater or Equal) tests if the
    /// SEE value of move is greater or equal to the given threshold. We'll use an
    /// algorithm similar to alpha-beta pruning with a null window.

    /**
     * see algorithm adapted from stockfish. Not fully implemented, and not well tested, yet.
     * @param bitBoard
     * @param m
     * @param threshold
     * @return
     */
    public boolean see_ge(BoardRepresentation bitBoard, Move m, int threshold) {

        //        assert(is_ok(m));

        // Only deal with normal moves, assume others pass a simple SEE
        if (m.isCastling()) {
            return 0 >= threshold;
        }
        //        if (type_of(m) != NORMAL)
        //            return VALUE_ZERO >= threshold;

        int posfrom = m.getFromIndex();
        int posto = m.getToIndex();

        int swap = pieceValue[m.getCapturedFigure()] - threshold;
        if (swap < 0)
            return false;

        swap = pieceValue[m.getFigureType()] - swap;
        if (swap <= 0)
            return true;

        long from = 1L << posfrom;
        long to = 1L << posto;
        //        assert(color_of(piece_on(from)) == sideToMove);
        BitChessBoard b = bitBoard.getBoard();
        long occupied = b.getPieces() ^ from ^ to;
        Color stm = bitBoard.getSiteToMove();
        long attackers = moveGen.attackersTo(posto, occupied, bitBoard);
        long stmAttackers, bb;
        int res = 1;

        while (true) {
            stm = stm.invert();
            attackers &= occupied;

            // If stm has no more attackers then give up: stm loses
            if ((stmAttackers = attackers & b.getColorMask(stm)) == 0L)
                break;

            // Don't allow pinned pieces to attack as long as there are
            // pinners on their original square.

            // todo we can not identify pinners currently...
            //            if (pinners(~stm) & occupied)
            //                stmAttackers &= ~blockers_for_king(stm);

            if (stmAttackers == 0L)
                break;

            res ^= 1;

            // Locate and remove the next least valuable attacker, and add to
            // the bitboard 'attackers' any X-ray attackers behind it.
            if ((bb = stmAttackers & b.getPieceSet(FT_PAWN)) != 0) {
                if ((swap = pieceValue[FT_PAWN] - swap) < res)
                    break;

                occupied ^= least_significant_square_bb(bb);
                attackers |= genBishopAttacs(posto, occupied) & (b.getPieceSet(FT_BISHOP) | b.getPieceSet(FT_QUEEN));
            } else if ((bb = stmAttackers & b.getPieceSet(FT_KNIGHT)) != 0) {
                if ((swap = pieceValue[FT_KNIGHT] - swap) < res)
                    break;

                occupied ^= least_significant_square_bb(bb);
            } else if ((bb = stmAttackers & b.getPieceSet(FT_BISHOP)) != 0) {
                if ((swap = pieceValue[FT_BISHOP] - swap) < res)
                    break;

                occupied ^= least_significant_square_bb(bb);
                attackers |= genBishopAttacs(posto, occupied) & (b.getPieceSet(FT_BISHOP) | b.getPieceSet(FT_QUEEN));
            } else if ((bb = stmAttackers & b.getPieceSet(FT_ROOK)) != 0) {
                if ((swap = pieceValue[FT_ROOK] - swap) < res)
                    break;

                occupied ^= least_significant_square_bb(bb);
                attackers |= genRookAttacs(posto, occupied) & (b.getPieceSet(FT_ROOK) | b.getPieceSet(FT_QUEEN));
            } else if ((bb = stmAttackers & b.getPieceSet(FT_QUEEN)) != 0) {
                if ((swap = pieceValue[FT_QUEEN] - swap) < res)
                    break;

                occupied ^= least_significant_square_bb(bb);
                attackers |= genBishopAttacs(posto, occupied) & (b.getPieceSet(FT_BISHOP) | b.getPieceSet(FT_QUEEN))
                        | genRookAttacs(posto, occupied) & (b.getPieceSet(FT_ROOK) | b.getPieceSet(FT_QUEEN));
            } else // KING
                // If we "capture" with the king but opponent still has attackers,
                // reverse the result.
                return (attackers & ~b.getColorMask(stm)) != 0 ? (res ^ 1) != 0 : res != 0;
        }

        return res != 0;
    }

    /**
     * Same as see_ge, only that the move has already been executed on the board!
     *
     * todo we should of course refactor this. there is only one difference in the begining occupied field
     * that the "from" is unset.
     * But we need more unit tests to correctly refactor this...
     *
     * @param bitBoard
     * @param m
     * @param threshold
     * @return
     */
    public boolean see_ge2(BitBoard bitBoard, Move m, int threshold) {

        //        assert(is_ok(m));

        // Only deal with normal moves, assume others pass a simple SEE
        if (m.isCastling()) {
            return 0 >= threshold;
        }
        //        if (type_of(m) != NORMAL)
        //            return VALUE_ZERO >= threshold;

        int posfrom = m.getFromIndex();
        int posto = m.getToIndex();

        int swap = pieceValue[m.getCapturedFigure()] - threshold;
        if (swap < 0)
            return false;

        swap = pieceValue[m.getFigureType()] - swap;
        if (swap <= 0)
            return true;

        long from = 1L << posfrom;
        long to = 1L << posto;
        //        assert(color_of(piece_on(from)) == sideToMove);
        BitChessBoard b = bitBoard.getBoard();
        long occupied = b.getPieces() ^ to;
        Color stm = bitBoard.getSiteToMove();
        long attackers = moveGen.attackersTo(posto, occupied, bitBoard);
        long stmAttackers, bb;
        int res = 1;

        while (true) {
            stm = stm.invert();
            attackers &= occupied;

            // If stm has no more attackers then give up: stm loses
            if ((stmAttackers = attackers & b.getColorMask(stm)) == 0L)
                break;

            // Don't allow pinned pieces to attack as long as there are
            // pinners on their original square.

            // todo we can not identify pinners currently...
            //            if (pinners(~stm) & occupied)
            //                stmAttackers &= ~blockers_for_king(stm);

            if (stmAttackers == 0L)
                break;

            res ^= 1;

            // Locate and remove the next least valuable attacker, and add to
            // the bitboard 'attackers' any X-ray attackers behind it.
            if ((bb = stmAttackers & b.getPieceSet(FT_PAWN)) != 0) {
                if ((swap = pieceValue[FT_PAWN] - swap) < res)
                    break;

                occupied ^= least_significant_square_bb(bb);
                attackers |= genBishopAttacs(posto, occupied) & (b.getPieceSet(FT_BISHOP) | b.getPieceSet(FT_QUEEN));
            } else if ((bb = stmAttackers & b.getPieceSet(FT_KNIGHT)) != 0) {
                if ((swap = pieceValue[FT_KNIGHT] - swap) < res)
                    break;

                occupied ^= least_significant_square_bb(bb);
            } else if ((bb = stmAttackers & b.getPieceSet(FT_BISHOP)) != 0) {
                if ((swap = pieceValue[FT_BISHOP] - swap) < res)
                    break;

                occupied ^= least_significant_square_bb(bb);
                attackers |= genBishopAttacs(posto, occupied) & (b.getPieceSet(FT_BISHOP) | b.getPieceSet(FT_QUEEN));
            } else if ((bb = stmAttackers & b.getPieceSet(FT_ROOK)) != 0) {
                if ((swap = pieceValue[FT_ROOK] - swap) < res)
                    break;

                occupied ^= least_significant_square_bb(bb);
                attackers |= genRookAttacs(posto, occupied) & (b.getPieceSet(FT_ROOK) | b.getPieceSet(FT_QUEEN));
            } else if ((bb = stmAttackers & b.getPieceSet(FT_QUEEN)) != 0) {
                if ((swap = pieceValue[FT_QUEEN] - swap) < res)
                    break;

                occupied ^= least_significant_square_bb(bb);
                attackers |= genBishopAttacs(posto, occupied) & (b.getPieceSet(FT_BISHOP) | b.getPieceSet(FT_QUEEN))
                        | genRookAttacs(posto, occupied) & (b.getPieceSet(FT_ROOK) | b.getPieceSet(FT_QUEEN));
            } else // KING
                // If we "capture" with the king but opponent still has attackers,
                // reverse the result.
                return (attackers & ~b.getColorMask(stm)) != 0 ? (res ^ 1) != 0 : res != 0;
        }

        return res != 0;
    }

    public boolean see_ge(BitBoard bitBoard, Move m) {
        return see_ge(bitBoard, m, 0);
    }
}
