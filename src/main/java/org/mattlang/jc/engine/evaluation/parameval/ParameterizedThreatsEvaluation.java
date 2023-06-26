package org.mattlang.jc.engine.evaluation.parameval;

import static java.lang.Long.bitCount;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.FigureConstants.*;

import java.util.function.Consumer;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BB;
import org.mattlang.jc.board.bitboard.BitChessBoard;
import org.mattlang.jc.board.bitboard.MagicBitboards;
import org.mattlang.jc.engine.evaluation.parameval.functions.ArrayFunction;

import lombok.Getter;

@Getter
public class ParameterizedThreatsEvaluation implements EvalComponent {

    public static final String THREAT_BY_MINOR_MG = "threads.ThreatByMinorMg";
    public static final String THREAT_BY_MINOR_EG = "threads.ThreatByMinorEg";
    public static final String THREAT_BY_ROOK_MG = "threads.ThreatByRookMg";
    public static final String THREAT_BY_ROOK_EG = "threads.ThreatByRookEg";
    public static final String THREAT_BY_KING_MG = "threads.ThreatByKingMg";
    public static final String THREAT_BY_KING_EG = "threads.ThreatByKingEg";
    public static final String HANGING_MG = "threads.HangingMg";
    public static final String HANGING_EG = "threads.HangingEg";
    public static final String WEAK_QUEEN_PROTECTION_MG = "threads.WeakQueenProtectionMg";
    public static final String WEAK_QUEEN_PROTECTION_EG = "threads.WeakQueenProtectionEg";
    public static final String RESTRICTED_PIECE_MG = "threads.RestrictedPieceMg";
    public static final String RESTRICTED_PIECE_EG = "threads.RestrictedPieceEg";
    public static final String THREAT_BY_PAWN_PUSH_MG = "threads.ThreatByPawnPushMg";
    public static final String THREAT_BY_PAWN_PUSH_EG = "threads.ThreatByPawnPushEg";
    public static final String THREAT_BY_SAFE_PAWN_MG = "threads.ThreatBySafePawnMg";
    public static final String THREAT_BY_SAFE_PAWN_EG = "threads.ThreatBySafePawnEg";
    public static final String SLIDER_ON_QUEEN_MG = "threads.SliderOnQueenMg";
    public static final String SLIDER_ON_QUEEN_EG = "threads.SliderOnQueenEg";
    public static final String KNIGHT_ON_QUEEN_MG = "threads.KnightOnQueenMg";
    public static final String KNIGHT_ON_QUEEN_EG = "threads.KnightOnQueenEg";

    private final ArrayFunction threatByMinorMg;
    private final ArrayFunction threatByMinorEg;
    private ArrayFunction threatByMinorMgEg;

    private final ArrayFunction threatByRookMg;
    private final ArrayFunction threatByRookEg;
    private ArrayFunction threatByRookMgEg;

    private final boolean active;

    private int threatByKingMgEg;
    private ChangeableMgEgScore threatByKingScore;

    private int hangingMgEg;
    private ChangeableMgEgScore hangingScore;

    private int weakQueenProtectionMgEg;
    private ChangeableMgEgScore weakQueenProtectionScore;

    private int restrictedPieceMgEg;
    private ChangeableMgEgScore restrictedPieceScore;

    private int threatByPawnPushMgEg;
    private ChangeableMgEgScore threatByPawnPushScore;

    private int threatBySafePawnMgEg;
    private ChangeableMgEgScore threatBySafePawnScore;

    private int sliderOnQueenMgEg;
    private ChangeableMgEgScore sliderOnQueenScore;

    private int knightOnQueenMgEg;
    private ChangeableMgEgScore knightOnQueenScore;

    private MgEgScore whiteThreats = new MgEgScore();

    private MgEgScore blackThreats = new MgEgScore();

    public ParameterizedThreatsEvaluation(boolean forTuning, EvalConfig config) {
        active = forTuning || config.getBoolProp("threads.active");

        threatByMinorMg = config.parseArray(THREAT_BY_MINOR_MG);
        threatByMinorEg = config.parseArray(THREAT_BY_MINOR_EG);

        threatByRookMg = config.parseArray(THREAT_BY_ROOK_MG);
        threatByRookEg = config.parseArray(THREAT_BY_ROOK_EG);

        updateCombinedArrays();

        threatByKingScore =
                readCombinedConfigVal(config, THREAT_BY_KING_MG, THREAT_BY_KING_EG, val -> threatByKingMgEg = val);

        hangingScore = readCombinedConfigVal(config, HANGING_MG, HANGING_EG, val -> hangingMgEg = val);

        weakQueenProtectionScore = readCombinedConfigVal(config, WEAK_QUEEN_PROTECTION_MG, WEAK_QUEEN_PROTECTION_EG,
                val -> weakQueenProtectionMgEg = val);

        restrictedPieceScore = readCombinedConfigVal(config, RESTRICTED_PIECE_MG, RESTRICTED_PIECE_EG,
                val -> restrictedPieceMgEg = val);

        threatByPawnPushScore = readCombinedConfigVal(config, THREAT_BY_PAWN_PUSH_MG, THREAT_BY_PAWN_PUSH_EG,
                val -> threatByPawnPushMgEg = val);

        threatBySafePawnScore = readCombinedConfigVal(config, THREAT_BY_SAFE_PAWN_MG, THREAT_BY_SAFE_PAWN_EG,
                val -> threatBySafePawnMgEg = val);

        sliderOnQueenScore =
                readCombinedConfigVal(config, SLIDER_ON_QUEEN_MG, SLIDER_ON_QUEEN_EG, val -> sliderOnQueenMgEg = val);

        knightOnQueenScore =
                readCombinedConfigVal(config, KNIGHT_ON_QUEEN_MG, KNIGHT_ON_QUEEN_EG, val -> knightOnQueenMgEg = val);

    }

    private ChangeableMgEgScore readCombinedConfigVal(EvalConfig config, String mgPropKey, String egPropKey,
            Consumer<Integer> changeListener) {
        int mgVal = config.getPosIntProp(mgPropKey);
        int egVal = config.getPosIntProp(egPropKey);
        int score = MgEgScore.createMgEgScore(mgVal, egVal);
        ChangeableMgEgScore changeableMgEgScore = new ChangeableMgEgScore(changeListener, score);
        // call changelistener after initialisation to update value:
        changeListener.accept(score);
        return changeableMgEgScore;
    }

    @Override
    public void eval(EvalResult result, BoardRepresentation bitBoard) {
        if (!active) {
            return;
        }

        evalThreads(result, whiteThreats, bitBoard.getBoard(), WHITE);
        evalThreads(result, blackThreats, bitBoard.getBoard(), Color.BLACK);
        result.add(whiteThreats).minus(blackThreats);
    }

    private void evalThreads(EvalResult result, MgEgScore score, BitChessBoard bb, Color us) {
        Color them = us.invert();

        long TRank3BB = (us == WHITE ? BB.rank3 : BB.rank6);

        score.clear();

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
                score.add(threatByMinorMgEg.calc(figType));
                b &= b - 1;
            }

            b = weak & result.getAttacks(us, FT_ROOK);
            while (b != 0L) {
                byte figType = bb.getFigType(Long.numberOfTrailingZeros(b));
                score.add(threatByRookMgEg.calc(figType));
                b &= b - 1;
            }
            if ((weak & result.getAttacks(us, FT_KING)) != 0L) {
                score.add(threatByKingMgEg);
            }

            b = ~result.getAttacks(them, FT_ALL)
                    | (nonPawnEnemies & result.getDoubleAttacks(us));
            int bitcount = bitCount(weak & b);
            score.add(hangingMgEg * bitcount);

            // Additional bonus if weak piece is only protected by a queen
            bitcount = bitCount(weak & result.getAttacks(them, FT_QUEEN));
            score.add(weakQueenProtectionMgEg * bitcount);
        }

        // Bonus for restricting their piece moves
        long b = result.getAttacks(them, FT_ALL)
                & ~stronglyProtected
                & result.getAttacks(us, FT_ALL);
        int bitcount = bitCount(b);
        score.add(restrictedPieceMgEg * bitcount);

        // Protected or unattacked squares
        long safe = ~result.getAttacks(them, FT_ALL) | result.getAttacks(us, FT_ALL);

        // Bonus for attacking enemy pieces with our relatively safe pawns
        b = bb.getPawns(us) & safe;
        b = BB.pawnAttacks(us, b) & nonPawnEnemies;
        bitcount = bitCount(b);
        score.add(threatBySafePawnMgEg * bitcount);

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
        score.add(threatByPawnPushMgEg * bitcount);

        // Bonus for threats on the next moves against enemy queen
        if (bb.getQueensCount(them) == 1) {
            int queenImbalance = bb.getQueensCount() == 1 ? 1 : 0;

            int s = Long.numberOfTrailingZeros(bb.getQueens(them.ordinal()));
            safe = getMobilityArea(bb, us)
                    & ~bb.getPawns(us)
                    & ~stronglyProtected;

            b = result.getAttacks(us, FT_KNIGHT) & BB.getKnightAttacs(s);
            int factor = bitCount(b & safe) * (1 + queenImbalance);
            score.add(knightOnQueenMgEg * factor);

            b = (result.getAttacks(us, FT_BISHOP) & MagicBitboards.genBishopAttacs(s, bb.getPieces()))
                    | (result.getAttacks(us, FT_ROOK) & MagicBitboards.genRookAttacs(s, bb.getPieces()));
            factor = bitCount(b & safe & result.getDoubleAttacks(us)) * (1 + queenImbalance);
            score.add(sliderOnQueenMgEg * factor);
        }
    }

    private long getMobilityArea(BitChessBoard bb, Color us) {
        long LowRanks = (us == WHITE ? BB.rank2 | BB.rank3 : BB.rank7 | BB.rank6);

        // Find our pawns that are blocked or on the first two ranks
        long shifted = us == WHITE ? BB.soutOne(bb.getPieces()) : BB.nortOne(bb.getPieces());
        long b = bb.getPawns(us) & (shifted | LowRanks);

        // Squares occupied by those pawns, by our king or queen, by blockers to attacks on our king
        // or controlled by enemy pawns are excluded from the mobility area.

        // todo poor mans impl without taking blockers and pawn attacks into account
        return ~(b | bb.getKings(us) | bb.getQueens(
                us.ordinal()) /*| pos.blockers_for_king(Us) | pe->pawn_attacks(Them)*/);
    }

    public void updateCombinedArrays() {
        threatByMinorMgEg = ArrayFunction.combine(threatByMinorMg, threatByMinorEg);
        threatByRookMgEg = ArrayFunction.combine(threatByRookMg, threatByRookEg);
    }
}
