package org.mattlang.jc.engine.evaluation.parameval;

import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.FigureConstants.*;
import static org.mattlang.jc.board.bitboard.BB.*;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BB;
import org.mattlang.jc.board.bitboard.BitChessBoard;
import org.mattlang.jc.board.bitboard.MagicBitboards;
import org.mattlang.jc.engine.evaluation.Tools;
import org.mattlang.jc.engine.evaluation.parameval.mobility.MobFigParams;
import org.mattlang.jc.engine.evaluation.parameval.mobility.MobilityEvalResult;

/**
 * Paremeterized Mobility Evaluation.
 */
public class ParameterizedMobilityEvaluation implements EvalComponent {

    private static int[] SAFETYTABLE = {
            0, 0, 1, 2, 3, 5, 7, 9, 12, 15,
            18, 22, 26, 30, 35, 39, 44, 50, 56, 62,
            68, 75, 82, 85, 89, 97, 105, 113, 122, 131,
            140, 150, 169, 180, 191, 202, 213, 225, 237, 248,
            260, 272, 283, 295, 307, 319, 330, 342, 354, 366,
            377, 389, 401, 412, 424, 436, 448, 459, 471, 483,
            494, 500, 500, 500, 500, 500, 500, 500, 500, 500,
            500, 500, 500, 500, 500, 500, 500, 500, 500, 500,
            500, 500, 500, 500, 500, 500, 500, 500, 500, 500,
            500, 500, 500, 500, 500, 500, 500, 500, 500, 500
    };

    /**
     * splitted results by color, and type of evaluation, e.g. mobility, captures, connectivity).
     * They are splitted mainly for debugging purpose.
     */
    private MobilityEvalResult wResult;
    private MobilityEvalResult bResult;

    private MobFigParams paramsKnight;
    private MobFigParams paramsBishop;
    private MobFigParams paramsRook;
    private MobFigParams paramsQueen;
    private MobFigParams paramsKing;

    public ParameterizedMobilityEvaluation(EvalConfig config) {

        paramsKnight = new MobFigParams(config, "knight");
        paramsBishop = new MobFigParams(config, "bishop");
        paramsRook = new MobFigParams(config, "rook");
        paramsQueen = new MobFigParams(config, "queen");
        paramsKing = new MobFigParams(config, "king");

        wResult = new MobilityEvalResult(config);
        bResult = new MobilityEvalResult(config);

    }

    private void eval(BoardRepresentation bitBoard) {

        clear();

        // calc mobility of each piece.
        // we do not count sqares which are attached by enemy pawns.
        BitChessBoard bb = bitBoard.getBoard();

        eval(bb, Color.WHITE);
        eval(bb, Color.BLACK);

    }

    private void clear() {
        wResult.clear();
        bResult.clear();
    }

    public void eval(BitChessBoard bb, Color side) {

        MobilityEvalResult result = side == WHITE ? wResult : bResult;

        Color xside = side.invert();

        long ownFigsMask = bb.getColorMask(side);
        long opponentFigsMask = bb.getColorMask(xside);
        long empty = ~ownFigsMask & ~opponentFigsMask;
        long occupancy = ownFigsMask | opponentFigsMask;

        long oppKingMask = bb.getPieceSet(FT_KING, xside);
        long oppKingZone = createKingZoneMask(oppKingMask, xside);
        int oppKingPos = Long.numberOfTrailingZeros(oppKingMask);

        // opponents pawn attacs. we exclude fields under opponents pawn attack from our mobility
        long oppPawnAttacs = createOpponentPawnAttacs(bb, side);
        long noOppPawnAttacs = ~oppPawnAttacs;

        long bishopBB = bb.getPieceSet(FT_BISHOP, side);
        while (bishopBB != 0) {
            final int bishop = Long.numberOfTrailingZeros(bishopBB);

            long attacks = MagicBitboards.genBishopAttacs(bishop, occupancy);
            long mobility = attacks & empty & noOppPawnAttacs;
            long captures = attacks & opponentFigsMask;
            long kingZoneAttacs = attacks & oppKingZone;
            long connectivity = attacks & ownFigsMask;
            int tropism = getTropism(bishop, oppKingPos);

            result.countFigureVals(paramsBishop, mobility, captures, connectivity, kingZoneAttacs, tropism);

            bishopBB &= bishopBB - 1;
        }

        long knightBB = bb.getPieceSet(FT_KNIGHT, side);
        while (knightBB != 0) {
            final int knight = Long.numberOfTrailingZeros(knightBB);

            long knightAttack = BB.getKnightAttacs(knight);

            long moves = knightAttack & ~ownFigsMask;
            long captures = moves & opponentFigsMask;
            long mobility = moves & ~captures & noOppPawnAttacs;
            long kingZoneAttacs = knightAttack & oppKingZone;
            long connectivity = knightAttack & ownFigsMask;
            int tropism = getTropism(knight, oppKingPos);

            result.countFigureVals(paramsKnight, mobility, captures, connectivity, kingZoneAttacs, tropism);

            knightBB &= knightBB - 1;
        }

        long ownPawns = bb.getPieceSet(FT_PAWN, side);
        long oppPawns = bb.getPieceSet(FT_PAWN, xside);

        long rookBB = bb.getPieceSet(FT_ROOK, side);
        while (rookBB != 0) {
            final int rook = Long.numberOfTrailingZeros(rookBB);

            long attacks = MagicBitboards.genRookAttacs(rook, occupancy);
            long mobility = attacks & empty & noOppPawnAttacs;
            long captures = attacks & opponentFigsMask;
            long connectivity = attacks & ownFigsMask;
            long kingZoneAttacs = attacks & oppKingZone;
            int tropism = getTropism(rook, oppKingPos);

            result.countFigureVals(paramsRook, mobility, captures, connectivity, kingZoneAttacs, tropism);

            result.rookOpenFiles(rook, oppKingPos, ownPawns, oppPawns);

            rookBB &= rookBB - 1;
        }

        long queenBB = bb.getPieceSet(FT_QUEEN, side);
        while (queenBB != 0) {
            final int queen = Long.numberOfTrailingZeros(queenBB);

            long attacksRook = MagicBitboards.genRookAttacs(queen, occupancy);
            long attacksBishop = MagicBitboards.genBishopAttacs(queen, occupancy);
            long attacks = attacksRook | attacksBishop;
            long mobility = attacks & empty & noOppPawnAttacs;
            long captures = attacks & opponentFigsMask;
            long kingZoneAttacs = attacks & oppKingZone;
            long connectivity = attacks & ownFigsMask;
            int tropism = getTropism(queen, oppKingPos);

            result.countFigureVals(paramsQueen, mobility, captures, connectivity, kingZoneAttacs, tropism);

            result.evalEarlyDevelopedQueen(queenBB, bishopBB, knightBB, side);

            queenBB &= queenBB - 1;
        }

        long kingBB = bb.getPieceSet(FT_KING, side);
        final int king = Long.numberOfTrailingZeros(kingBB);

        long kingAttack = BB.getKingAttacs(king);

        long moves = kingAttack & ~ownFigsMask;
        long captures = moves & opponentFigsMask;
        long mobility = moves & ~captures & noOppPawnAttacs;
        long kingZoneAttacs = kingAttack & oppKingZone;
        long connectivity = kingAttack & ownFigsMask;
        int tropism = getTropism(king, oppKingPos);

        result.countFigureVals(paramsKing, mobility, captures, connectivity, kingZoneAttacs, tropism);

        result.blockedPieces(bb, side);
    }

    public static long createKingZoneMask(long oppKingMask, Color xside) {
        long frontMask = xside == WHITE ? kingAttacks(nortOne(oppKingMask)) : kingAttacks(soutOne(oppKingMask));
        return kingAttacks(oppKingMask) | frontMask;
    }

    /**
     * Creates opponents pawn attacs.
     *
     * @param bb
     * @param side
     * @return
     */
    public static long createOpponentPawnAttacs(BitChessBoard bb, Color side) {
        long otherPawns = side == WHITE ? bb.getPieceSet(FT_PAWN, BLACK) : bb.getPieceSet(FT_PAWN, WHITE);

        if (side == WHITE) {
            long capturesEast = BB.bPawnWestAttacks(otherPawns);
            long capturesWest = BB.bPawnEastAttacks(otherPawns);
            return capturesEast | capturesWest;
        } else {
            long capturesEast = BB.wPawnWestAttacks(otherPawns);
            long capturesWest = BB.wPawnEastAttacks(otherPawns);
            return capturesEast | capturesWest;
        }
    }

    /**
     * Weights the distance to the king from -7 to +6.
     * +6 == nearest to the king. -7 fartest from king.
     *
     * @param sq1
     * @param sq2
     * @return
     */
    private int getTropism(int sq1, int sq2) {
        return 7 - Tools.manhattanDistance(sq1, sq2);
    }

    @Override
    public void eval(EvalResult result, BoardRepresentation bitBoard) {
        eval(bitBoard);

        result.midGame += (wResult.mobilityMG - bResult.mobilityMG);
        result.endGame += (wResult.mobilityEG - bResult.mobilityEG);

        result.midGame += (wResult.tropismMG - bResult.tropismMG);
        result.endGame += (wResult.tropismEG - bResult.tropismEG);

        /**************************************************************************
         *  Merge king attack score. We don't apply this value if there are less   *
         *  than two attackers or if the attacker has no queen.                    *
         **************************************************************************/

        if (wResult.kingAttCount < 2 || bitBoard.getBoard().getQueensCount(BitChessBoard.nWhite) == 0)
            wResult.kingAttWeight = 0;
        if (bResult.kingAttCount < 2 || bitBoard.getBoard().getQueensCount(BitChessBoard.nBlack) == 0)
            bResult.kingAttWeight = 0;

        result.result += (SAFETYTABLE[wResult.kingAttWeight] - SAFETYTABLE[bResult.kingAttWeight]);

        result.result += (wResult.positionalThemes - bResult.positionalThemes);

        result.result += (wResult.blockages - bResult.blockages);
    }
}
