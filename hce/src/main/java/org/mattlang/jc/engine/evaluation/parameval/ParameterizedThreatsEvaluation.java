package org.mattlang.jc.engine.evaluation.parameval;

import static java.lang.Long.bitCount;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.FigureConstants.*;

import org.mattlang.jc.board.BB;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BitChessBoard;
import org.mattlang.jc.board.bitboard.MagicBitboards;
import org.mattlang.jc.engine.evaluation.annotation.EvalConfigParam;
import org.mattlang.jc.engine.evaluation.annotation.EvalConfigurable;
import org.mattlang.jc.engine.evaluation.annotation.EvalValueInterval;
import org.mattlang.jc.engine.evaluation.parameval.functions.MgEgArrayFunction;

import lombok.Getter;

@Getter
@EvalConfigurable(prefix = "threats")
@EvalValueInterval(min = 0, max = 200)
public class ParameterizedThreatsEvaluation implements EvalComponent {

    @EvalConfigParam(name = "ThreatByMinor")
    private MgEgArrayFunction threatByMinorMgEg;

    @EvalConfigParam(name = "ThreatByRook")
    private MgEgArrayFunction threatByRookMgEg;

    @EvalConfigParam(name = "ThreatByKing", mgEgCombined = true)
    private int threatByKingMgEg;

    @EvalConfigParam(name = "Hanging", mgEgCombined = true)
    private int hangingMgEg;

    @EvalConfigParam(name = "WeakQueenProtection", mgEgCombined = true)
    private int weakQueenProtectionMgEg;

    @EvalConfigParam(name = "RestrictedPiece", mgEgCombined = true)
    private int restrictedPieceMgEg;

    @EvalConfigParam(name = "ThreatByPawnPush", mgEgCombined = true)
    private int threatByPawnPushMgEg;

    @EvalConfigParam(name = "ThreatBySafePawn", mgEgCombined = true)
    private int threatBySafePawnMgEg;

    @EvalConfigParam(name = "SliderOnQueen", mgEgCombined = true)
    private int sliderOnQueenMgEg;

    @EvalConfigParam(name = "KnightOnQueen", mgEgCombined = true)
    private int knightOnQueenMgEg;

    public ParameterizedThreatsEvaluation() {
    }

    @Override
    public void eval(EvalResult result, BoardRepresentation bitBoard) {
        int whiteThreats = evalThreads(result, bitBoard.getBoard(), WHITE);
        int blackThreats = evalThreads(result, bitBoard.getBoard(), Color.BLACK);
        result.add(whiteThreats - blackThreats);
    }

    private int evalThreads(EvalResult result, BitChessBoard bb, Color us) {
        Color them = us.invert();

        long TRank3BB = (us == WHITE ? BB.rank3 : BB.rank6);

        int eval = 0;

        // Non-pawn enemies
        long nonPawnEnemies = bb.getColorMask(them) & ~bb.getPieceSet(FT_PAWN);

        // Squares strongly protected by the enemy, either because they defend the
        // square with a pawn, or because they defend the square twice and we don't.
        long stronglyProtected = result.getAttacks(them, FT_PAWN)
                | (result.getDoubleAttacks(them) & ~result.getDoubleAttacks(us));

        // Non-pawn enemies, strongly protected
        long defended = nonPawnEnemies & stronglyProtected;

        // Enemies not strongly protected and under our attack
        long weak = bb.getColorMask(them) & ~stronglyProtected & result.getAttacks(us, FT_ALL);

        // Bonus according to the kind of attacking pieces
        if ((defended | weak) != 0L) {
            long b = (defended | weak) & (result.getAttacks(us, FT_KNIGHT) | result.getAttacks(us, FT_BISHOP));
            while (b != 0L) {
                byte figType = bb.getFigType(Long.numberOfTrailingZeros(b));
                eval += (threatByMinorMgEg.calc(figType));
                b &= b - 1;
            }

            b = weak & result.getAttacks(us, FT_ROOK);
            while (b != 0L) {
                byte figType = bb.getFigType(Long.numberOfTrailingZeros(b));
                eval += (threatByRookMgEg.calc(figType));
                b &= b - 1;
            }
            if ((weak & result.getAttacks(us, FT_KING)) != 0L) {
                eval += (threatByKingMgEg);
            }

            b = ~result.getAttacks(them, FT_ALL)
                    | (nonPawnEnemies & result.getDoubleAttacks(us));
            int bitcount = bitCount(weak & b);
            eval += (hangingMgEg * bitcount);

            // Additional bonus if weak piece is only protected by a queen
            bitcount = bitCount(weak & result.getAttacks(them, FT_QUEEN));
            eval += (weakQueenProtectionMgEg * bitcount);
        }

        // Bonus for restricting their piece moves
        long b = result.getAttacks(them, FT_ALL)
                & ~stronglyProtected
                & result.getAttacks(us, FT_ALL);
        int bitcount = bitCount(b);
        eval += (restrictedPieceMgEg * bitcount);

        // Protected or unattacked squares
        long safe = ~result.getAttacks(them, FT_ALL) | result.getAttacks(us, FT_ALL);

        // Bonus for attacking enemy pieces with our relatively safe pawns
        b = bb.getPawns(us) & safe;
        b = BB.pawnAttacks(us, b) & nonPawnEnemies;
        bitcount = bitCount(b);
        eval += (threatBySafePawnMgEg * bitcount);

        // Find squares where our pawns can push on the next move
        switch (us) {
        case WHITE:
            b = BB.nortOne(bb.getPawns(us)) & ~bb.getPieces();
            b |= BB.nortOne(b & TRank3BB) & ~bb.getPieces();
            break;
        case BLACK:
            b = BB.soutOne(bb.getPawns(us)) & ~bb.getPieces();
            b |= BB.soutOne(b & TRank3BB) & ~bb.getPieces();
            break;
        }

        // Keep only the squares which are relatively safe
        b &= ~result.getAttacks(them, FT_PAWN) & safe;

        // Bonus for safe pawn threats on the next move
        b = BB.pawnAttacks(us, b) & nonPawnEnemies;
        bitcount = bitCount(b);
        eval += (threatByPawnPushMgEg * bitcount);

        // Bonus for threats on the next moves against enemy queen
        if (bb.getQueensCount(them) == 1) {
            int queenImbalance = bb.getQueensCount() == 1 ? 1 : 0;

            int s = Long.numberOfTrailingZeros(bb.getQueens(them.ordinal()));
            safe = getMobilityArea(result, bb, us)
                    & ~bb.getPawns(us)
                    & ~stronglyProtected;

            b = result.getAttacks(us, FT_KNIGHT) & BB.getKnightAttacs(s);
            int factor = bitCount(b & safe) * (1 + queenImbalance);
            eval += (knightOnQueenMgEg * factor);

            b = (result.getAttacks(us, FT_BISHOP) & MagicBitboards.genBishopAttacs(s, bb.getPieces()))
                    | (result.getAttacks(us, FT_ROOK) & MagicBitboards.genRookAttacs(s, bb.getPieces()));
            factor = bitCount(b & safe & result.getDoubleAttacks(us)) * (1 + queenImbalance);
            eval += (sliderOnQueenMgEg * factor);
        }

        return eval;
    }

    private long getMobilityArea(EvalResult result, BitChessBoard bb, Color us) {
        long LowRanks = (us == WHITE ? BB.rank2 | BB.rank3 : BB.rank7 | BB.rank6);

        // Find our pawns that are blocked or on the first two ranks
        long shifted = us == WHITE ? BB.soutOne(bb.getPieces()) : BB.nortOne(bb.getPieces());
        long b = bb.getPawns(us) & (shifted | LowRanks);

        // Squares occupied by those pawns, by our king or queen, by blockers to attacks on our king
        // or controlled by enemy pawns are excluded from the mobility area.

        // todo poor mans impl without taking blockers and pawn attacks into account
        return ~(b | bb.getKings(us) | bb.getQueens(
                us.ordinal()) | result.getAttacks(us.invert(),
                FT_PAWN)/*| pos.blockers_for_king(Us) | pe->pawn_attacks(Them)*/);
    }

}
