package org.mattlang.jc.engine.evaluation.minimalpst;

import static org.mattlang.jc.board.bitboard.BitChessBoard.nBlack;
import static org.mattlang.jc.board.bitboard.BitChessBoard.nWhite;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BitChessBoard;
import org.mattlang.jc.engine.EvaluateFunction;
import org.mattlang.jc.engine.evaluation.PhaseCalculator;
import org.mattlang.jc.engine.evaluation.evaltables.Pattern;

/**
 * Tapered Evaluation function with only pst tables. Adapted from MinimalChess
 * https://github.com/lithander/MinimalChessEngine/blob/master/MinimalChess/Evaluation.cs
 * as experimental evaluation function.
 *
 */
public class MinimalPstEvaluation implements EvaluateFunction {

    public final static Pattern PAWN_MG = new Pattern(new int[]{
            100,   100,   100,   100,   100,   100,   100,   100,
            173,   227,   144,   189,   182,   228,   124,    75,
            82,    94,   111,   115,   152,   150,   109,    68,
            65,    95,    87,   102,   104,    92,    98,    59,
            52,    80,    77,    92,    96,    86,    90,    55,
            55,    79,    77,    70,    83,    82,   114,    69,
            45,    80,    61,    56,    64,   102,   116,    59,
            100,   100,   100,   100,   100,   100,   100,   100,
    });

    public final static Pattern KNIGHT_MG = new Pattern(new int[]{
            106,   248,   280,   272,   326,   196,   291,   178,
            224,   255,   369,   322,   319,   360,   307,   289,
            254,   365,   342,   368,   387,   423,   380,   344,
            296,   322,   322,   357,   343,   378,   324,   325,
            292,   313,   320,   317,   333,   326,   326,   299,
            283,   298,   317,   317,   326,   323,   329,   290,
            277,   254,   294,   303,   304,   325,   295,   290,
            214,   284,   251,   273,   289,   281,   288,   278,
    });


    public final static Pattern BISHOP_MG = new Pattern(new int[]{
            275,   319,   255,   290,   292,   289,   316,   307,
            301,   344,   312,   314,   354,   382,   347,   287,
            316,   357,   377,   366,   355,   374,   364,   334,
            330,   337,   350,   378,   370,   373,   339,   330,
            327,   349,   345,   357,   365,   344,   341,   336,
            333,   349,   345,   347,   344,   359,   347,   340,
            339,   346,   348,   331,   340,   352,   363,   335,
            304,   331,   318,   315,   321,   321,   301,   308,
    });

    public final static Pattern ROOK_MG = new Pattern(new int[]{
            504,   520,   493,   517,   519,   493,   489,   500,
            501,   506,   538,   535,   545,   538,   491,   507,
            477,   503,   506,   506,   495,   523,   529,   488,
            452,   473,   485,   509,   501,   512,   475,   460,
            445,   456,   472,   477,   483,   472,   490,   457,
            437,   461,   465,   460,   479,   473,   475,   446,
            435,   467,   461,   468,   476,   487,   475,   409,
            460,   466,   480,   488,   489,   476,   447,   454,
    });

    public final static Pattern QUEEN_MG = new Pattern(new int[]{
            854,   910,   921,   913,   955,   939,   928,   926,
            881,   867,   910,   921,   898,   961,   936,   948,
            894,   896,   916,   921,   939,   969,   954,   960,
            879,   880,   898,   894,   907,   925,   904,   904,
            897,   881,   897,   896,   904,   904,   910,   905,
            890,   908,   898,   903,   901,   908,   917,   911,
            872,   893,   917,   906,   912,   919,   903,   908,
            903,   888,   897,   915,   891,   879,   875,   858,
    });

    public final static Pattern KING_MG = new Pattern(new int[]{
            -6,    46,    33,    22,   -26,   -14,    14,    12,
            23,    11,    22,    20,    11,     5,   -10,   -25,
            23,    22,    30,     6,    10,    29,    28,    -4,
            -1,    -4,     5,   -12,   -11,   -11,    -5,   -38,
            -15,     8,   -18,   -40,   -53,   -36,   -27,   -46,
            6,     1,   -37,   -73,   -80,   -54,   -20,   -21,
            12,    11,   -10,   -74,   -52,   -20,    16,    18,
            -12,    44,    19,   -66,     6,   -29,    32,    24,
    });


    public final static Pattern PAWN_EG = new Pattern(new int[]{
            100,   100,   100,   100,   100,   100,   100,   100,
            269,   260,   245,   222,   230,   218,   253,   274,
            183,   190,   174,   158,   145,   141,   172,   173,
            124,   117,   106,    98,    92,    97,   110,   108,
            106,   102,    89,    86,    87,    85,    96,    91,
            96,   100,    85,    94,    92,    89,    92,    84,
            106,   102,    99,   101,   103,    93,    96,    86,
            100,   100,   100,   100,   100,   100,   100,   100,
    });

    public final static Pattern KNIGHT_EG = new Pattern(new int[]{
            225,   237,   268,   249,   258,   250,   220,   190,
            254,   273,   265,   283,   276,   261,   261,   234,
            257,   265,   293,   292,   281,   279,   264,   246,
            265,   284,   303,   302,   302,   293,   286,   266,
            265,   274,   296,   305,   297,   297,   286,   263,
            261,   277,   281,   294,   290,   280,   265,   260,
            245,   260,   271,   276,   278,   262,   258,   239,
            246,   236,   258,   264,   261,   262,   238,   225,
    });

    public final static Pattern BISHOP_EG = new Pattern(new int[]{
            287,   278,   286,   287,   292,   287,   286,   277,
            290,   295,   304,   286,   296,   288,   294,   284,
            298,   292,   297,   298,   300,   304,   299,   297,
            295,   307,   308,   308,   309,   306,   298,   298,
            291,   298,   309,   314,   304,   307,   295,   289,
            285,   296,   305,   307,   310,   300,   294,   285,
            283,   285,   292,   298,   302,   289,   288,   271,
            275,   290,   279,   291,   289,   283,   290,   284,
    });

    public final static Pattern ROOK_EG = new Pattern(new int[]{
            510,   506,   516,   514,   512,   507,   506,   505,
            508,   510,   507,   509,   497,   501,   506,   504,
            502,   503,   504,   504,   500,   493,   494,   495,
            501,   498,   509,   497,   499,   497,   494,   499,
            498,   501,   503,   500,   494,   491,   488,   486,
            492,   495,   491,   496,   489,   486,   488,   482,
            491,   489,   495,   498,   490,   488,   485,   493,
            491,   499,   500,   500,   497,   491,   497,   480,
    });

    public final static Pattern QUEEN_EG = new Pattern(new int[]{
            919,   932,   941,   942,   940,   933,   924,   941,
            895,   935,   936,   947,   971,   932,   932,   907,
            895,   913,   917,   958,   957,   935,   927,   909,
            920,   935,   927,   955,   971,   950,   970,   943,
            892,   945,   934,   963,   948,   944,   948,   931,
            906,   887,   924,   916,   922,   926,   923,   916,
            899,   899,   881,   898,   900,   888,   880,   878,
            885,   890,   885,   869,   910,   886,   900,   878,
    });

    public final static Pattern KING_EG = new Pattern(new int[]{
            -71,   -40,   -23,   -25,   -12,    12,     2,   -14,
            -13,    13,     6,    10,    11,    29,    19,    11,
            3,    14,    16,    10,    13,    35,    37,     8,
            -13,    16,    19,    22,    19,    27,    22,     1,
            -24,    -7,    16,    21,    24,    19,     6,   -14,
            -24,    -7,    10,    20,    24,    17,     7,   -11,
            -31,   -13,     3,    11,    12,     3,    -7,   -21,
            -56,   -39,   -25,   -11,   -28,   -14,   -29,   -51,
    });

    @Override
    public int eval(BoardRepresentation currBoard, Color who2Move) {
        int who2mov = who2Move == Color.WHITE ? 1 : -1;
        BitChessBoard bb = currBoard.getBoard();

        int midGame = PAWN_MG.calcScore(bb.getPawns(nWhite), bb.getPawns(nBlack), who2mov) +
                KNIGHT_MG.calcScore(bb.getKnights(nWhite), bb.getKnights(nBlack), who2mov) +
                BISHOP_MG.calcScore(bb.getBishops(nWhite), bb.getBishops(nBlack), who2mov) +
                ROOK_MG.calcScore(bb.getRooks(nWhite), bb.getRooks(nBlack), who2mov) +
                QUEEN_MG.calcScore(bb.getQueens(nWhite), bb.getQueens(nBlack), who2mov) +
                KING_MG.calcScore(bb.getKings(nWhite), bb.getKings(nBlack), who2mov);
        int endGame = PAWN_EG.calcScore(bb.getPawns(nWhite), bb.getPawns(nBlack), who2mov) +
                KNIGHT_EG.calcScore(bb.getKnights(nWhite), bb.getKnights(nBlack), who2mov) +
                BISHOP_EG.calcScore(bb.getBishops(nWhite), bb.getBishops(nBlack), who2mov) +
                ROOK_EG.calcScore(bb.getRooks(nWhite), bb.getRooks(nBlack), who2mov) +
                QUEEN_EG.calcScore(bb.getQueens(nWhite), bb.getQueens(nBlack), who2mov) +
                KING_EG.calcScore(bb.getKings(nWhite), bb.getKings(nBlack), who2mov);

        double score = PhaseCalculator.scaleByPhase(bb, midGame, endGame);
        return (int) score;
    }

}
