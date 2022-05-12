package org.mattlang.jc.engine.evaluation.parameval;

import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.FigureConstants.MASK_OUT_COLOR;
import static org.mattlang.jc.board.bitboard.BitChessBoard.nBlack;
import static org.mattlang.jc.board.bitboard.BitChessBoard.nWhite;
import static org.mattlang.jc.engine.evaluation.evaltables.Pattern.loadFromFullPath;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.FigureConstants;
import org.mattlang.jc.board.bitboard.BitChessBoard;
import org.mattlang.jc.engine.evaluation.evaltables.Pattern;

/**
 * A Tapered, parameterized PST Evaluation where the PST Tables are loaded from resource files.
 */
public class ParameterizedPstEvaluation implements EvalComponent {

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
        BitChessBoard bb = bitBoard.getBoard();

        result.midGame += pawnMG.calcScore(bb.getPawns(nWhite), bb.getPawns(nBlack)) +
                knightMG.calcScore(bb.getKnights(nWhite), bb.getKnights(nBlack)) +
                bishopMG.calcScore(bb.getBishops(nWhite), bb.getBishops(nBlack)) +
                rookMG.calcScore(bb.getRooks(nWhite), bb.getRooks(nBlack)) +
                queenMG.calcScore(bb.getQueens(nWhite), bb.getQueens(nBlack)) +
                kingMG.calcScore(bb.getKings(nWhite), bb.getKings(nBlack));
        result.endGame += pawnEG.calcScore(bb.getPawns(nWhite), bb.getPawns(nBlack)) +
                knightEG.calcScore(bb.getKnights(nWhite), bb.getKnights(nBlack)) +
                bishopEG.calcScore(bb.getBishops(nWhite), bb.getBishops(nBlack)) +
                rookEG.calcScore(bb.getRooks(nWhite), bb.getRooks(nBlack)) +
                queenEG.calcScore(bb.getQueens(nWhite), bb.getQueens(nBlack)) +
                kingEG.calcScore(bb.getKings(nWhite), bb.getKings(nBlack));
    }

    public void incrementalRemoveFigure(EvalResult incrementalResult, int pos, byte figCode) {
        byte figureType = (byte) (figCode & MASK_OUT_COLOR);
        Color color = ((figCode & BLACK.code) == BLACK.code) ? BLACK : WHITE;

        int factor = color == WHITE ? 1 : -1;
        incrementalResult.midGame -= mgPatterns[figureType].getVal(pos, color) * factor;
        incrementalResult.endGame -= egPatterns[figureType].getVal(pos, color) * factor;

    }

    public void incrementalAddFigure(EvalResult incrementalResult, int pos, byte figCode) {
        byte figureType = (byte) (figCode & MASK_OUT_COLOR);
        Color color = ((figCode & BLACK.code) == BLACK.code) ? BLACK : WHITE;

        int factor = color == WHITE ? 1 : -1;
        incrementalResult.midGame += mgPatterns[figureType].getVal(pos, color) * factor;
        incrementalResult.endGame += egPatterns[figureType].getVal(pos, color) * factor;
    }

    public void incrementalMoveFigure(EvalResult incrementalResult, int from, int to, byte figCode) {
        byte figureType = (byte) (figCode & MASK_OUT_COLOR);
        Color color = ((figCode & BLACK.code) == BLACK.code) ? BLACK : WHITE;

        int factor = color == WHITE ? 1 : -1;
        incrementalResult.midGame -= mgPatterns[figureType].getVal(from, color) * factor;
        incrementalResult.endGame -= egPatterns[figureType].getVal(from, color) * factor;

        incrementalResult.midGame += mgPatterns[figureType].getVal(to, color) * factor;
        incrementalResult.endGame += egPatterns[figureType].getVal(to, color) * factor;
    }
}
