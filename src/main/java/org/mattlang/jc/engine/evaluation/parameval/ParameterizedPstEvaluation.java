package org.mattlang.jc.engine.evaluation.parameval;

import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.FigureConstants.*;
import static org.mattlang.jc.board.bitboard.BitChessBoard.nBlack;
import static org.mattlang.jc.board.bitboard.BitChessBoard.nWhite;
import static org.mattlang.jc.engine.evaluation.evaltables.Pattern.loadFromFullPath;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.FigureConstants;
import org.mattlang.jc.board.bitboard.BitChessBoard;
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

    /**
     * redundant patterns derived from the Pattern classes. These are used for incremental pst updates and
     * redundant copied into these array to speed up lookup.
     * The black values are negated so that a looked up value needs only to be summed up with the current incremental
     * value.
     *
     * Dimensions:
     * [color][eg/mg][figuretype][pos]
     */
    private int[][][][] incPatterns = new int[2][2][7][64];

    public static final int MG = 0;
    public static final int EG = 1;

    public ParameterizedPstEvaluation(ParameterizedMaterialEvaluation materialEvaluation, String subPath) {
        pawnMG = loadFromFullPath(subPath + "pawnMG.csv").applyOffset(materialEvaluation.getPawnMG());
        knightMG = loadFromFullPath(subPath + "knightMG.csv").applyOffset(materialEvaluation.getKnightMG());
        bishopMG = loadFromFullPath(subPath + "bishopMG.csv").applyOffset(materialEvaluation.getBishopMG());
        rookMG = loadFromFullPath(subPath + "rookMG.csv").applyOffset(materialEvaluation.getRookMG());
        queenMG = loadFromFullPath(subPath + "queenMG.csv").applyOffset(materialEvaluation.getQueenMG());
        kingMG = loadFromFullPath(subPath + "kingMG.csv");
        pawnEG = loadFromFullPath(subPath + "pawnEG.csv").applyOffset(materialEvaluation.getPawnEG());
        knightEG = loadFromFullPath(subPath + "knightEG.csv").applyOffset(materialEvaluation.getKnightEG());
        bishopEG = loadFromFullPath(subPath + "bishopEG.csv").applyOffset(materialEvaluation.getBishopEG());
        rookEG = loadFromFullPath(subPath + "rookEG.csv").applyOffset(materialEvaluation.getRookEG());
        queenEG = loadFromFullPath(subPath + "queenEG.csv").applyOffset(materialEvaluation.getQueenEG());
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

        for (int color = nWhite; color <= nBlack; color++) {
            for (int stage = MG; stage <= EG; stage++) {
                Pattern[] patterns = stage == MG ? mgPatterns : egPatterns;
                for (int fig = FT_PAWN; fig <= FT_KING; fig++) {
                    for (int pos = 0; pos < 64; pos++) {
                        incPatterns[color][stage][fig][pos] =
                                patterns[fig].getVal(pos, color == nWhite ? WHITE : BLACK) * (color == nBlack ? -1 : 1);
                    }
                }
            }
        }

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
        int color = ((figCode & BLACK.code) == BLACK.code) ? nBlack : nWhite;

        incrementalResult.midGame -= incPatterns[color][MG][figureType][pos];
        incrementalResult.endGame -= incPatterns[color][EG][figureType][pos];

    }

    public void incrementalAddFigure(EvalResult incrementalResult, int pos, byte figCode) {
        byte figureType = (byte) (figCode & MASK_OUT_COLOR);
        int color = ((figCode & BLACK.code) == BLACK.code) ? nBlack : nWhite;

        incrementalResult.midGame += incPatterns[color][MG][figureType][pos];
        incrementalResult.endGame += incPatterns[color][EG][figureType][pos];
    }

    public void incrementalMoveFigure(EvalResult incrementalResult, int from, int to, byte figCode) {
        byte figureType = (byte) (figCode & MASK_OUT_COLOR);
        int color = ((figCode & BLACK.code) == BLACK.code) ? nBlack : nWhite;

        incrementalResult.midGame += incPatterns[color][MG][figureType][to] - incPatterns[color][MG][figureType][from];
        incrementalResult.endGame += incPatterns[color][EG][figureType][to] - incPatterns[color][EG][figureType][from];
    }
}
