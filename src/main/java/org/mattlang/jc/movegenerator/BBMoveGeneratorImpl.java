package org.mattlang.jc.movegenerator;

import static org.mattlang.jc.board.FigureConstants.*;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BB;
import org.mattlang.jc.board.bitboard.BitChessBoard;
import org.mattlang.jc.board.bitboard.MagicBitboards;
import org.mattlang.jc.engine.MoveList;

/**
 * see https://www.chessprogramming.org/10x12_Board
 * TSCP Implementation of move generator with some own modifications.
 *
 * Mixture of mailbox & bitboard move generation to slighlty refactor using bitboards.
 * Works only with the BitBoard implementation.
 */
public class BBMoveGeneratorImpl {

    /**
     * @param board current board
     * @param side  the side to move
     */
    public void generate(BoardRepresentation board, Color side, MoveList collector) {

        BitChessBoard bb = board.getBoard();

        Color xside = side.invert();  /* the side not to move */

        long ownFigsMask = bb.getColorMask(xside.invert());
        long opponentFigsMask = bb.getColorMask(xside);
        long empty = ~ownFigsMask & ~opponentFigsMask;

        MoveGeneration.genPawnCaptureMoves(board, collector, side);
        MoveGeneration.genPawnMoves(bb, empty, collector, side);

        long bishopBB = bb.getPieceSet(FT_BISHOP, side);
        while (bishopBB != 0) {
            final int bishop = Long.numberOfTrailingZeros(bishopBB);
            genBishopMoves(bb, bishop, collector, ownFigsMask, opponentFigsMask, empty);
            bishopBB &= bishopBB - 1;
        }

        long knightBB = bb.getPieceSet(FT_KNIGHT, side);
        while (knightBB != 0) {
            final int knight = Long.numberOfTrailingZeros(knightBB);
            genKnightMoves(bb, knight, collector, ownFigsMask, opponentFigsMask);
            knightBB &= knightBB - 1;
        }

        long rookBB = bb.getPieceSet(FT_ROOK, side);
        while (rookBB != 0) {
            final int rook = Long.numberOfTrailingZeros(rookBB);
            genRookMoves(bb, rook, collector, ownFigsMask, opponentFigsMask, empty);
            rookBB &= rookBB - 1;
        }

        long queenBB = bb.getPieceSet(FT_QUEEN, side);
        while (queenBB != 0) {
            final int queen = Long.numberOfTrailingZeros(queenBB);
            genQueenMoves(bb, queen, collector, ownFigsMask, opponentFigsMask, empty);
            queenBB &= queenBB - 1;
        }

        long kingBB = bb.getPieceSet(FT_KING, side);
        final int king = Long.numberOfTrailingZeros(kingBB);
        genKingMoves(bb, king, collector, ownFigsMask, opponentFigsMask);

        board.getBoardCastlings().generateCastlingMoves(side, collector);
    }

    private void genKingMoves(BitChessBoard bb, int kingPos, MoveCollector collector, long ownFigsMask,
            long opponentFigsMask) {
        long kingAttack = BB.getKingAttacs(kingPos);

        long moves = kingAttack & ~ownFigsMask;
        long attacks = moves & opponentFigsMask;
        long quietMoves = moves & ~attacks;

        while (attacks != 0) {
            final int toIndex = Long.numberOfTrailingZeros(attacks);
            collector.genMove(FT_KING, kingPos, toIndex, bb.get(toIndex));
            attacks &= attacks - 1;
        }

        while (quietMoves != 0) {
            final int toIndex = Long.numberOfTrailingZeros(quietMoves);
            collector.genMove(FT_KING, kingPos, toIndex, (byte) 0);
            quietMoves &= quietMoves - 1;
        }

    }

    private void genKnightMoves(BitChessBoard bb, int knight, MoveCollector collector, long ownFigsMask,
            long opponentFigsMask) {
        long knightAttack = BB.getKnightAttacs(knight);

        long moves = knightAttack & ~ownFigsMask;
        long attacks = moves & opponentFigsMask;
        long quietMoves = moves & ~attacks;

        while (attacks != 0) {
            final int toIndex = Long.numberOfTrailingZeros(attacks);
            collector.genMove(FT_KNIGHT, knight, toIndex, bb.get(toIndex));
            attacks &= attacks - 1;
        }

        while (quietMoves != 0) {
            final int toIndex = Long.numberOfTrailingZeros(quietMoves);
            collector.genMove(FT_KNIGHT, knight, toIndex, (byte) 0);
            quietMoves &= quietMoves - 1;
        }

    }

    private void genQueenMoves(BitChessBoard bb, int queen,
            MoveCollector collector, long ownFigsMask,
            long opponentFigsMask, long empty) {
        long occupancy = ownFigsMask | opponentFigsMask;
        long attacksRook = MagicBitboards.genRookAttacs(queen, occupancy);
        long attacksBishop = MagicBitboards.genBishopAttacs(queen, occupancy);
        long attacks = attacksRook | attacksBishop;

        generateBBMoves(attacks, bb, queen, FT_QUEEN, collector, opponentFigsMask, empty);

    }

    private void genRookMoves(BitChessBoard bb, int rook,
            MoveCollector collector, long ownFigsMask,
            long opponentFigsMask, long empty) {
        long occupancy = ownFigsMask | opponentFigsMask;
        long attacks = MagicBitboards.genRookAttacs(rook, occupancy);

        generateBBMoves(attacks, bb, rook, FT_ROOK, collector, opponentFigsMask, empty);
    }

    private void genBishopMoves(BitChessBoard bb, int bishop,
            MoveCollector collector, long ownFigsMask,
            long opponentFigsMask, long empty) {
        long occupancy = ownFigsMask | opponentFigsMask;

        long attacks = MagicBitboards.genBishopAttacs(bishop, occupancy);

        generateBBMoves(attacks, bb, bishop, FT_BISHOP, collector, opponentFigsMask, empty);
    }

    private void generateBBMoves(long attacks, BitChessBoard bb, int figPos, byte figType,
            MoveCollector collector,
            long opponentFigsMask, long empty) {

        long quiet = attacks & empty;
        long captures = attacks & opponentFigsMask;

        while (captures != 0) {
            final int toIndex = Long.numberOfTrailingZeros(captures);
            collector.genMove(figType, figPos, toIndex, bb.get(toIndex));
            captures &= captures - 1;
        }

        while (quiet != 0) {
            final int toIndex = Long.numberOfTrailingZeros(quiet);
            collector.genMove(figType, figPos, toIndex, (byte) 0);
            quiet &= quiet - 1;
        }

    }

}
