package org.mattlang.jc.engine.evaluation.parameval;

import static java.lang.Long.bitCount;
import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.FigureConstants.*;
import static org.mattlang.jc.engine.evaluation.parameval.EvalScore.SCORE;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BB;
import org.mattlang.jc.board.bitboard.BitChessBoard;
import org.mattlang.jc.board.bitboard.MagicBitboards;

public class ParameterizedThreatsEvaluation implements EvalComponent {

    // ThreatByMinor/ByRook[attacked PieceType] contains bonuses according to
    // which piece type attacks which one. Attacks on lesser pieces which are
    // pawn-defended are not considered.

    private static final EvalScore[] ThreatByMinor = {
            SCORE(0, 0), SCORE(5, 32), SCORE(55, 41), SCORE(77, 56), SCORE(89, 119), SCORE(79, 162)
    };

    private static final EvalScore[] ThreatByRook = {
            SCORE(0, 0), SCORE(3, 44), SCORE(37, 68), SCORE(42, 60), SCORE(0, 39), SCORE(58, 43)
    };
    private static final EvalScore ThreatByKing = SCORE(24, 89);

    private static final EvalScore Hanging = SCORE(69, 36);

    private static final EvalScore WeakQueenProtection = SCORE(14, 0);

    private static final EvalScore RestrictedPiece = SCORE(7, 7);

    private static final EvalScore ThreatByPawnPush = SCORE(48, 39);
    private static final EvalScore ThreatBySafePawn = SCORE(173, 94);

    private static final EvalScore SliderOnQueen = SCORE(60, 18);

    private static final EvalScore KnightOnQueen = SCORE(16, 11);
    /*


    constexpr Score ThreatByMinor[PIECE_TYPE_NB] = {
        S(0, 0), S(5, 32), S(55, 41), S(77, 56), S(89, 119), S(79, 162)
    };

    constexpr Score ThreatByRook[PIECE_TYPE_NB] = {
        S(0, 0), S(3, 44), S(37, 68), S(42, 60), S(0, 39), S(58, 43)
    };

      constexpr Score ThreatByKing        = S( 24, 89);

       constexpr Score Hanging             = S( 69, 36);

        constexpr Score WeakQueenProtection = S( 14,  0);

        constexpr Score RestrictedPiece     = S(  7,  7);

          constexpr Score ThreatByPawnPush    = S( 48, 39);
  constexpr Score ThreatBySafePawn    = S(173, 94);

    constexpr Score SliderOnQueen       = S( 60, 18);

      constexpr Score KnightOnQueen       = S( 16, 11);
    */

    private EvalScore whiteThreats = new EvalScore();

    private EvalScore blackThreats = new EvalScore();

    @Override
    public void eval(EvalResult result, BoardRepresentation bitBoard) {

        evalThreads(result, whiteThreats, bitBoard.getBoard(), WHITE);
        evalThreads(result, blackThreats, bitBoard.getBoard(), Color.BLACK);
        result.plus(whiteThreats).minus(blackThreats);
    }

    private void evalThreads(EvalResult result, EvalScore score, BitChessBoard bb, Color us) {
        Color them = us.invert();

        long TRank3BB = (us == WHITE ? BB.rank3 : BB.rank6);

        score.reset();

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
                score.plus(ThreatByMinor[bb.getFigType(Long.numberOfTrailingZeros(b))]);
                b &= b - 1;
            }

            b = weak & result.getAttacks(us, FT_ROOK);
            while (b != 0L) {
                score.plus(ThreatByRook[bb.getFigType(Long.numberOfTrailingZeros(b))]);
                b &= b - 1;
            }
            if ((weak & result.getAttacks(us, FT_KING)) != 0L)
                score.plus(ThreatByKing);

            b = ~result.getAttacks(them, FT_ALL)
                    | (nonPawnEnemies & result.getDoubleAttacks(us));
            score.plusMult(Hanging, bitCount(weak & b));

            // Additional bonus if weak piece is only protected by a queen
            score.plusMult(WeakQueenProtection, bitCount(weak & result.getAttacks(them, FT_QUEEN)));
        }

        // Bonus for restricting their piece moves
        long b = result.getAttacks(them, FT_ALL)
                & ~stronglyProtected
                & result.getAttacks(us, FT_ALL);
        score.plusMult(RestrictedPiece, bitCount(b));

        // Protected or unattacked squares
        long safe = ~result.getAttacks(them, FT_ALL) | result.getAttacks(us, FT_ALL);

        // Bonus for attacking enemy pieces with our relatively safe pawns
        b = bb.getPawns(us) & safe;
        b = BB.pawnAttacks(us, b) & nonPawnEnemies;
        score.plusMult(ThreatBySafePawn, bitCount(b));

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
        score.plusMult(ThreatByPawnPush, bitCount(b));

        // Bonus for threats on the next moves against enemy queen
        if (bb.getQueensCount(them) == 1) {
            int queenImbalance = bb.getQueensCount() == 1 ? 1 : 0;

            int s = Long.numberOfTrailingZeros(bb.getQueens(them.ordinal()));
            safe = getMobilityArea(bb, us)
                    & ~bb.getPawns(us)
                    & ~stronglyProtected;

            b = result.getAttacks(us, FT_KNIGHT) & BB.getKnightAttacs(s);

            score.plusMult(KnightOnQueen, bitCount(b & safe) * (1 + queenImbalance));

            b = (result.getAttacks(us, FT_BISHOP) & MagicBitboards.genBishopAttacs(s, bb.getPieces()))
                    | (result.getAttacks(us, FT_ROOK) & MagicBitboards.genRookAttacs(s, bb.getPieces()));

            score.plusMult(SliderOnQueen, bitCount(b & safe & result.getDoubleAttacks(us)) * (1 + queenImbalance));
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

    // Evaluation::threats() assigns bonuses according to the types of the
    // attacking and the attacked pieces.
    /*
    template<Tracing T> template<Color Us>
    Score Evaluation<T>::threats() const {

        constexpr Color     Them     = ~Us;
        constexpr Direction Up       = pawn_push(Us);
        constexpr Bitboard  TRank3BB = (Us == WHITE ? Rank3BB : Rank6BB);

        Bitboard b, weak, defended, nonPawnEnemies, stronglyProtected, safe;
        Score score = SCORE_ZERO;

        // Non-pawn enemies
        nonPawnEnemies = pos.pieces(Them) & ~pos.pieces(PAWN);

        // Squares strongly protected by the enemy, either because they defend the
        // square with a pawn, or because they defend the square twice and we don't.
        stronglyProtected =  attackedBy[Them][PAWN]
                | (attackedBy2[Them] & ~attackedBy2[Us]);

        // Non-pawn enemies, strongly protected
        defended = nonPawnEnemies & stronglyProtected;

        // Enemies not strongly protected and under our attack
        weak = pos.pieces(Them) & ~stronglyProtected & attackedBy[Us][ALL_PIECES];

        // Bonus according to the kind of attacking pieces
        if (defended | weak)
        {
            b = (defended | weak) & (attackedBy[Us][KNIGHT] | attackedBy[Us][BISHOP]);
            while (b)
                score += ThreatByMinor[type_of(pos.piece_on(pop_lsb(b)))];

            b = weak & attackedBy[Us][ROOK];
            while (b)
                score += ThreatByRook[type_of(pos.piece_on(pop_lsb(b)))];

            if (weak & attackedBy[Us][KING])
                score += ThreatByKing;

            b =  ~attackedBy[Them][ALL_PIECES]
                    | (nonPawnEnemies & attackedBy2[Us]);
            score += Hanging * popcount(weak & b);

            // Additional bonus if weak piece is only protected by a queen
            score += WeakQueenProtection * popcount(weak & attackedBy[Them][QUEEN]);
        }

        // Bonus for restricting their piece moves
        b =   attackedBy[Them][ALL_PIECES]
                & ~stronglyProtected
                &  attackedBy[Us][ALL_PIECES];
        score += RestrictedPiece * popcount(b);

        // Protected or unattacked squares
        safe = ~attackedBy[Them][ALL_PIECES] | attackedBy[Us][ALL_PIECES];

        // Bonus for attacking enemy pieces with our relatively safe pawns
        b = pos.pieces(Us, PAWN) & safe;
        b = pawn_attacks_bb<Us>(b) & nonPawnEnemies;
        score += ThreatBySafePawn * popcount(b);

        // Find squares where our pawns can push on the next move
        b  = shift<Up>(pos.pieces(Us, PAWN)) & ~pos.pieces();
        b |= shift<Up>(b & TRank3BB) & ~pos.pieces();

        // Keep only the squares which are relatively safe
        b &= ~attackedBy[Them][PAWN] & safe;

        // Bonus for safe pawn threats on the next move
        b = pawn_attacks_bb<Us>(b) & nonPawnEnemies;
        score += ThreatByPawnPush * popcount(b);

        // Bonus for threats on the next moves against enemy queen
        if (pos.count<QUEEN>(Them) == 1)
        {
            bool queenImbalance = pos.count<QUEEN>() == 1;

            Square s = pos.square<QUEEN>(Them);
            safe =   mobilityArea[Us]
                    & ~pos.pieces(Us, PAWN)
                    & ~stronglyProtected;

            b = attackedBy[Us][KNIGHT] & attacks_bb<KNIGHT>(s);

            score += KnightOnQueen * popcount(b & safe) * (1 + queenImbalance);

            b =  (attackedBy[Us][BISHOP] & attacks_bb<BISHOP>(s, pos.pieces()))
           | (attackedBy[Us][ROOK  ] & attacks_bb<ROOK  >(s, pos.pieces()));

            score += SliderOnQueen * popcount(b & safe & attackedBy2[Us]) * (1 + queenImbalance);
        }

        if constexpr (T)
        Trace::add(THREAT, Us, score);

        return score;
    }
    */

    public static final int[] DOUBLE_ATTACKED = { 0, 16, 34, 72, 4, -14, 0 };

    public static final int[] THREATS_MG = { 38, 66, 90, 16, 66, 38, 12, 16, -6 };
    public static final int[] THREATS_EG = { 34, 20, -64, 16, 10, -48, 28, 4, 14 };
    public static final int[] THREATS = new int[THREATS_MG.length];

    public static final int IX_PAWN_PUSH_THREAT 			= 3;
    public static final int IX_PAWN_ATTACKS 				= 1;
    public static final int IX_MULTIPLE_PAWN_ATTACKS 		= 0;
    public static final int IX_MAJOR_ATTACKED				= 6;
    public static final int IX_PAWN_ATTACKED 				= 8;
    public static final int IX_QUEEN_ATTACKED 				= 2;
    public static final int IX_ROOK_ATTACKED 				= 4;
    public static final int IX_QUEEN_ATTACKED_MINOR			= 5;

    public static int calculateThreats(EvalResult result, EvalScore evalScore, BitChessBoard bb) {
        int score = 0;
        final long whites = bb.getPieceSet(BitChessBoard.nWhite);
        final long whitePawns = bb.getPawns(BitChessBoard.nWhite);
        final long blacks = bb.getPieceSet(BitChessBoard.nBlack);
        final long blackPawns = bb.getPawns(BitChessBoard.nBlack);
        final long whiteAttacks = result.getAttacks(WHITE, FT_ALL);
        final long whitePawnAttacks = result.getAttacks(WHITE, FT_PAWN);
        final long whiteMinorAttacks = result.getAttacks(WHITE, FT_KNIGHT) | result.getAttacks(WHITE, FT_BISHOP);
        final long blackAttacks = result.getAttacks(BLACK, FT_ALL);
        final long blackPawnAttacks = result.getAttacks(BLACK, FT_PAWN);
        final long blackMinorAttacks = result.getAttacks(BLACK, FT_KNIGHT) | result.getAttacks(BLACK, FT_BISHOP);

        final long emptySpaces = ~whites & ~blacks;

        // double attacked pieces
        long piece = result.getDoubleAttacks(WHITE) & blacks;
        while (piece != 0) {
            score += DOUBLE_ATTACKED[bb.getFigType(Long.numberOfTrailingZeros(piece))];
            piece &= piece - 1;
        }
        piece = result.getDoubleAttacks(BLACK) & whites;
        while (piece != 0) {
            score -= DOUBLE_ATTACKED[bb.getFigType(Long.numberOfTrailingZeros(piece))];
            piece &= piece - 1;
        }

        // if we have pawns:
        if ((whitePawns | blackPawns) != 0L) {

            // unused outposts
//            score += Long.bitCount(cb.passedPawnsAndOutposts & emptySpaces & whiteMinorAttacks & whitePawnAttacks)
//                    * EvalConstants.THREATS[EvalConstants.IX_UNUSED_OUTPOST];
//            score -= Long.bitCount(cb.passedPawnsAndOutposts & emptySpaces & blackMinorAttacks & blackPawnAttacks)
//                    * EvalConstants.THREATS[EvalConstants.IX_UNUSED_OUTPOST];

            // pawn push threat
            piece = (whitePawns << 8) & emptySpaces & ~blackAttacks;
            score += Long.bitCount(BB.pawnAttacks(WHITE, piece) & blacks)
                    * THREATS[IX_PAWN_PUSH_THREAT];
            piece = (blackPawns >>> 8) & emptySpaces & ~whiteAttacks;
            score -= Long.bitCount(BB.pawnAttacks(BLACK, piece) & whites)
                    * THREATS[IX_PAWN_PUSH_THREAT];

            // piece attacked by pawn
            score += Long.bitCount(whitePawnAttacks & blacks & ~blackPawns)
                    * THREATS[IX_PAWN_ATTACKS];
            score -= Long.bitCount(blackPawnAttacks & whites & ~whitePawns)
                    * THREATS[IX_PAWN_ATTACKS];

            // multiple pawn attacks possible
            if (Long.bitCount(whitePawnAttacks & blacks) > 1) {
                score += THREATS[IX_MULTIPLE_PAWN_ATTACKS];
            }
            if (Long.bitCount(blackPawnAttacks & whites) > 1) {
                score -= THREATS[IX_MULTIPLE_PAWN_ATTACKS];
            }

            // pawn attacked
            score += Long.bitCount(whiteAttacks & blackPawns) * THREATS[IX_PAWN_ATTACKED];
            score -= Long.bitCount(blackAttacks & whitePawns) * THREATS[IX_PAWN_ATTACKED];

        }

        // minors attacked and not defended by a pawn
        score += Long.bitCount(whiteAttacks & (bb.getKnights(BitChessBoard.nBlack) | bb.getBishops(BitChessBoard.nBlack) & ~blackAttacks))
                * THREATS[IX_MAJOR_ATTACKED];
        score -= Long.bitCount(blackAttacks & (bb.getKnights(BitChessBoard.nWhite) | bb.getBishops(BitChessBoard.nWhite) & ~whiteAttacks))
                * THREATS[IX_MAJOR_ATTACKED];

        if (bb.getQueens(BitChessBoard.nBlack) != 0) {
            // queen attacked by rook
            score += Long.bitCount(bb.getRooks(BitChessBoard.nWhite) & bb.getQueens(BitChessBoard.nBlack) )
                    * THREATS[IX_QUEEN_ATTACKED];
            // queen attacked by minors
            score += Long.bitCount(whiteMinorAttacks & bb.getQueens(BitChessBoard.nBlack) )
                    * THREATS[IX_QUEEN_ATTACKED_MINOR];
        }

        if (bb.getQueens(BitChessBoard.nWhite) != 0) {
            // queen attacked by rook
            score -= Long.bitCount(bb.getRooks(BitChessBoard.nBlack) & bb.getQueens(BitChessBoard.nWhite))
                    * THREATS[IX_QUEEN_ATTACKED];
            // queen attacked by minors
            score -= Long.bitCount(blackMinorAttacks & bb.getQueens(BitChessBoard.nWhite))
                    * THREATS[IX_QUEEN_ATTACKED_MINOR];
        }

        // rook attacked by minors
        score += Long.bitCount(whiteMinorAttacks & bb.getRooks(BitChessBoard.nBlack))
                * THREATS[IX_ROOK_ATTACKED];
        score -= Long.bitCount(blackMinorAttacks & bb.getRooks(BitChessBoard.nWhite))
                * THREATS[IX_ROOK_ATTACKED];

        return score;
    }
}
