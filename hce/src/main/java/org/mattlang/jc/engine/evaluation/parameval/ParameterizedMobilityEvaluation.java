package org.mattlang.jc.engine.evaluation.parameval;

import static org.mattlang.jc.board.Color.*;
import static org.mattlang.jc.board.FigureConstants.*;
import static org.mattlang.jc.board.bitboard.MagicBitboards.genBishopAttacs;
import static org.mattlang.jc.board.bitboard.MagicBitboards.genRookAttacs;
import static org.mattlang.jc.engine.evaluation.parameval.MgEgScore.getEgScore;
import static org.mattlang.jc.engine.evaluation.parameval.MgEgScore.getMgScore;

import org.mattlang.jc.board.BB;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BitChessBoard;
import org.mattlang.jc.engine.evaluation.annotation.EvalConfigPrefix;
import org.mattlang.jc.engine.evaluation.annotation.EvalConfigurable;
import org.mattlang.jc.engine.evaluation.parameval.mobility.MobFigParams;
import org.mattlang.jc.engine.evaluation.parameval.mobility.MobilityEvalResult;

import lombok.Getter;

/**
 * Paremeterized Mobility Evaluation.
 */
@Getter
@EvalConfigurable(prefix = "mob")
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
    private final MobilityEvalResult wResult;
    private final MobilityEvalResult bResult;

    @EvalConfigPrefix(prefix = "knight")
    private final MobFigParams paramsKnight;

    @EvalConfigPrefix(prefix = "bishop")
    private final MobFigParams paramsBishop;

    @EvalConfigPrefix(prefix = "rook")
    private final MobFigParams paramsRook;

    @EvalConfigPrefix(prefix = "queen")
    private final MobFigParams paramsQueen;

    @EvalConfigPrefix(prefix = "king")
    private final MobFigParams paramsKing;

    public ParameterizedMobilityEvaluation() {

        paramsKnight = new MobFigParams();
        paramsBishop = new MobFigParams();
        paramsRook = new MobFigParams();
        paramsQueen = new MobFigParams();
        paramsKing = new MobFigParams();

        wResult = new MobilityEvalResult();
        bResult = new MobilityEvalResult();

    }

    private void evalMobility(EvalResult result, BoardRepresentation bitBoard) {

        clear();

        BitChessBoard bb = bitBoard.getBoard();

        // update pawn attacks
        result.updatePawnAttacs(bb);
        // calc mobility of each piece.
        // we do not count sqares which are attached by enemy pawns.

        long occupancy = bb.getColorMask(nWhite) | bb.getColorMask(nBlack);

        // this update also the attacks information of each piece type
        evalMobilityAndAttacks(result, bb, Color.WHITE, occupancy);
        evalMobilityAndAttacks(result, bb, Color.BLACK, occupancy);

    }

    private void clear() {
        wResult.clear();
        bResult.clear();
    }

    private void evalMobilityAndAttacks(EvalResult evalResult, BitChessBoard bb, Color side, long occupancy) {

        MobilityEvalResult result = side == WHITE ? wResult : bResult;
        result.init(side, bb);

        final long ourBishopBB = bb.getPieceSet(FT_BISHOP, side);
        long bishopBB = ourBishopBB;
        while (bishopBB != 0) {
            final int bishop = Long.numberOfTrailingZeros(bishopBB);

            long attacks = genBishopAttacs(bishop, occupancy);

            evalResult.updateAttacks(attacks, FT_BISHOP, side.ordinal());
            result.countFigureMobilityVals(paramsBishop, bishop, attacks);

            bishopBB &= bishopBB - 1;
        }

        final long ourKnightBB = bb.getPieceSet(FT_KNIGHT, side);
        long knightBB = ourKnightBB;
        while (knightBB != 0) {
            final int knight = Long.numberOfTrailingZeros(knightBB);
            long knightAttack = BB.getKnightAttacs(knight);

            evalResult.updateAttacks(knightAttack, FT_KNIGHT, side.ordinal());
            result.countFigureMobilityVals(paramsKnight, knight, knightAttack);

            knightBB &= knightBB - 1;
        }

        long rookBB = bb.getPieceSet(FT_ROOK, side);
        while (rookBB != 0) {
            final int rook = Long.numberOfTrailingZeros(rookBB);

            long attacks = genRookAttacs(rook, occupancy);

            evalResult.updateAttacks(attacks, FT_ROOK, side.ordinal());
            result.countFigureMobilityVals(paramsRook, rook, attacks);

            result.rookOpenFiles(rook);

            rookBB &= rookBB - 1;
        }

        long queenBB = bb.getPieceSet(FT_QUEEN, side);
        result.evalEarlyDevelopedQueen(queenBB, ourBishopBB, ourKnightBB, side);
        while (queenBB != 0) {
            final int queen = Long.numberOfTrailingZeros(queenBB);

            long attacks = genRookAttacs(queen, occupancy) | genBishopAttacs(queen, occupancy);

            evalResult.updateAttacks(attacks, FT_QUEEN, side.ordinal());
            result.countFigureMobilityVals(paramsQueen, queen, attacks);

            queenBB &= queenBB - 1;
        }

        long kingBB = bb.getPieceSet(FT_KING, side);
        final int king = Long.numberOfTrailingZeros(kingBB);

        long kingAttack = BB.getKingAttacs(king);

        evalResult.updateAttacks(kingAttack, FT_KING, side.ordinal());
        result.countFigureMobilityVals(paramsKing, king, kingAttack);

    }


    @Override
    public void eval(EvalResult result, BoardRepresentation bitBoard) {
        evalMobility(result, bitBoard);

        result.getMgEgScore().add(wResult.eval - bResult.eval);

        /**************************************************************************
         *  Merge king attack score. We don't apply this value if there are less   *
         *  than two attackers or if the attacker has no queen.                    *
         **************************************************************************/

        if (wResult.kingAttCount < 2 || bitBoard.getBoard().getQueensCount(nWhite) == 0)
            wResult.kingAttWeightMgEg = 0;
        if (bResult.kingAttCount < 2 || bitBoard.getBoard().getQueensCount(nBlack) == 0)
            bResult.kingAttWeightMgEg = 0;

        result.getMgEgScore()
                .addMg(getSafetyValue(getMgScore(wResult.kingAttWeightMgEg)) - getSafetyValue(
                        getMgScore(bResult.kingAttWeightMgEg)));
        result.getMgEgScore()
                .addEg(getSafetyValue(getEgScore(wResult.kingAttWeightMgEg)) - getSafetyValue(
                        getEgScore(bResult.kingAttWeightMgEg)));

        result.add(MgEgScore.createMgEgScore(wResult.positionalThemes - bResult.positionalThemes, 0));
    }

    public static int getSafetyValue(int kingAtt) {
        return kingAtt < SAFETYTABLE.length ? SAFETYTABLE[kingAtt] : 500;
    }

}
