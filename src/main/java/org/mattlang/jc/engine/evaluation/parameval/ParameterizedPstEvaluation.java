package org.mattlang.jc.engine.evaluation.parameval;

import static org.mattlang.jc.engine.evaluation.evaltables.Pattern.loadFromFullPath;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.FigureConstants;
import org.mattlang.jc.engine.evaluation.evaltables.Pattern;

/**
 * A Tapered, parameterized PST Evaluation where the PST Tables are loaded from resource files.
 */
public final class ParameterizedPstEvaluation implements EvalComponent {

    private Pattern pawnMG;
    private Pattern knightMG;
    private Pattern bishopMG;
    private Pattern rookMG;
    private Pattern queenMG;
    private Pattern kingMG;
    private Pattern pawnEG;
    private Pattern knightEG;
    private Pattern bishopEG;
    private Pattern rookEG;
    private Pattern queenEG;
    private Pattern kingEG;

    private Pattern[] mgPatterns = new Pattern[7];
    private Pattern[] egPatterns = new Pattern[7];

    public ParameterizedPstEvaluation(String subPath) {
        pawnMG = loadFromFullPath(subPath + "pawnMG.csv");
        knightMG = loadFromFullPath(subPath + "knightMG.csv");
        bishopMG = loadFromFullPath(subPath + "bishopMG.csv");
        rookMG = loadFromFullPath(subPath + "rookMG.csv");
        queenMG = loadFromFullPath(subPath + "queenMG.csv");
        kingMG = loadFromFullPath(subPath + "kingMG.csv");
        pawnEG = loadFromFullPath(subPath + "pawnEG.csv");
        knightEG = loadFromFullPath(subPath + "knightEG.csv");
        bishopEG = loadFromFullPath(subPath + "bishopEG.csv");
        rookEG = loadFromFullPath(subPath + "rookEG.csv");
        queenEG = loadFromFullPath(subPath + "queenEG.csv");
        kingEG = loadFromFullPath(subPath + "kingEG.csv");

        mgPatterns[FigureConstants.FT_PAWN] = pawnMG;
        mgPatterns[FigureConstants.FT_BISHOP] = bishopMG;
        mgPatterns[FigureConstants.FT_KNIGHT] = knightMG;
        mgPatterns[FigureConstants.FT_ROOK] = rookMG;
        mgPatterns[FigureConstants.FT_QUEEN] = queenMG;
        mgPatterns[FigureConstants.FT_KING] = kingMG;

        egPatterns[FigureConstants.FT_PAWN] = pawnEG;
        egPatterns[FigureConstants.FT_BISHOP] = bishopEG;
        egPatterns[FigureConstants.FT_KNIGHT] = knightEG;
        egPatterns[FigureConstants.FT_ROOK] = rookEG;
        egPatterns[FigureConstants.FT_QUEEN] = queenEG;
        egPatterns[FigureConstants.FT_KING] = kingEG;
    }

    @Override
    public void eval(EvalResult result, BoardRepresentation bitBoard) {
//        BitChessBoard bb = bitBoard.getBoard();
//
//        result.midGame += pawnMG.calcScore(bb.getPawns(nWhite), bb.getPawns(nBlack));
//        result.endGame += pawnEG.calcScore(bb.getPawns(nWhite), bb.getPawns(nBlack));
    }

    public Pattern getPatternMG(byte figureType) {
        return mgPatterns[figureType];
    }

    public Pattern getPatternEG(byte figureType) {
        return egPatterns[figureType];
    }

    public void evalSingleFigure(EvalResult result, int pos, byte figType, Color color) {
        int factor = color == Color.WHITE ? 1 : -1;
        result.midGame += getPatternMG(figType).getVal(pos, color) * factor;
        result.endGame += getPatternEG(figType).getVal(pos, color) * factor;
    }
}
