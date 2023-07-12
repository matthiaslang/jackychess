package org.mattlang.jc.engine.evaluation.parameval;

import static org.mattlang.jc.board.FigureConstants.*;
import static org.mattlang.jc.board.bitboard.BitChessBoard.nBlack;
import static org.mattlang.jc.board.bitboard.BitChessBoard.nWhite;
import static org.mattlang.jc.engine.evaluation.evaltables.Pattern.loadFromFullPath;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.board.bitboard.BitChessBoard;
import org.mattlang.jc.engine.evaluation.evaltables.Pattern;

import lombok.Getter;

/**
 * A Tapered, parameterized PST Evaluation where the PST Tables are loaded from resource files.
 */
@Getter
public class ParameterizedPstEvaluation implements EvalComponent {

    public static final String PAWN_MG_CSV = "pawnMG.csv";
    public static final String KNIGHT_MG_CSV = "knightMG.csv";
    public static final String BISHOP_MG_CSV = "bishopMG.csv";
    public static final String ROOK_MG_CSV = "rookMG.csv";
    public static final String QUEEN_MG_CSV = "queenMG.csv";
    public static final String KING_MG_CSV = "kingMG.csv";
    public static final String PAWN_EG_CSV = "pawnEG.csv";
    public static final String KNIGHT_EG_CSV = "knightEG.csv";
    public static final String BISHOP_EG_CSV = "bishopEG.csv";
    public static final String ROOK_EG_CSV = "rookEG.csv";
    public static final String QUEEN_EG_CSV = "queenEG.csv";
    public static final String KING_EG_CSV = "kingEG.csv";

    private final Pattern pawnMGEG;
    private final Pattern knightMGEG;
    private final Pattern bishopMGEG;
    private final Pattern rookMGEG;
    private final Pattern queenMGEG;
    private final Pattern kingMGEG;

    private final Pattern pawnDeltaScoring;
    private final Pattern knightDeltaScoring;
    private final Pattern bishopDeltaScoring;
    private final Pattern rookDeltaScoring;
    private final Pattern queenDeltaScoring;
    private final Pattern kingDeltaScoring;

    public ParameterizedPstEvaluation(String subPath) {
        pawnMGEG = loadCombinedPattern(subPath, PAWN_MG_CSV, PAWN_EG_CSV);
        knightMGEG = loadCombinedPattern(subPath, KNIGHT_MG_CSV, KNIGHT_EG_CSV);
        bishopMGEG = loadCombinedPattern(subPath, BISHOP_MG_CSV, BISHOP_EG_CSV);
        rookMGEG = loadCombinedPattern(subPath, ROOK_MG_CSV, ROOK_EG_CSV);
        queenMGEG = loadCombinedPattern(subPath, QUEEN_MG_CSV, QUEEN_EG_CSV);
        kingMGEG = loadCombinedPattern(subPath, KING_MG_CSV, KING_EG_CSV);

        pawnDeltaScoring = pawnMGEG.extractMg();
        knightDeltaScoring = knightMGEG.extractMg();
        bishopDeltaScoring = bishopMGEG.extractMg();
        rookDeltaScoring = rookMGEG.extractMg();
        queenDeltaScoring = queenMGEG.extractMg();
        kingDeltaScoring = kingMGEG.extractMg();

    }

    private Pattern loadCombinedPattern(String subPath, String mgFile, String egFile) {
        Pattern patternMg = loadFromFullPath(subPath + mgFile);
        Pattern patternEg = loadFromFullPath(subPath + egFile);
        return Pattern.combine(patternMg, patternEg);
    }

    @Override
    public void eval(EvalResult result, BoardRepresentation bitBoard) {
        BitChessBoard bb = bitBoard.getBoard();

        result.getMgEgScore().add(pawnMGEG.calcScore(bb.getPawns(nWhite), bb.getPawns(nBlack)) +
                knightMGEG.calcScore(bb.getKnights(nWhite), bb.getKnights(nBlack)) +
                bishopMGEG.calcScore(bb.getBishops(nWhite), bb.getBishops(nBlack)) +
                rookMGEG.calcScore(bb.getRooks(nWhite), bb.getRooks(nBlack)) +
                queenMGEG.calcScore(bb.getQueens(nWhite), bb.getQueens(nBlack)) +
                kingMGEG.calcScore(bb.getKings(nWhite), bb.getKings(nBlack)));
    }

    public int calcPstDelta(Color color, Move m) {
        Pattern pattern = getPstForFigure(m.getFigureType());
        return pattern.calcPstDelta(m.getToIndex(), m.getFromIndex(), color);
    }

    public Pattern getPstForFigure(byte figureType) {
        switch (figureType) {
        case FT_PAWN:
            return pawnDeltaScoring;
        case FT_KNIGHT:
            return knightDeltaScoring;
        case FT_BISHOP:
            return bishopDeltaScoring;
        case FT_ROOK:
            return rookDeltaScoring;
        case FT_QUEEN:
            return queenDeltaScoring;
        case FT_KING:
            return kingDeltaScoring;
        }
        throw new IllegalArgumentException("illegal figure type " + figureType);
    }

}
