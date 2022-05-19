package org.mattlang.jc.engine.evaluation.parameval;

import static org.mattlang.jc.board.bitboard.BitChessBoard.nBlack;
import static org.mattlang.jc.board.bitboard.BitChessBoard.nWhite;
import static org.mattlang.jc.engine.evaluation.evaltables.Pattern.loadFromFullPath;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.bitboard.BitChessBoard;
import org.mattlang.jc.engine.evaluation.evaltables.Pattern;

/**
 * A Tapered, parameterized PST Evaluation where the PST Tables are loaded from resource files.
 * The value get combined with the pure material values to evaluate them both together.
 */
public class ParameterizedPstEvaluation implements EvalComponent {

    private final boolean pawnsPhaseDiffer;
    private final boolean knightsPhaseDiffer;
    private final boolean bispopsPhaseDiffer;
    private final boolean rooksPhaseDiffer;
    private final boolean queensPhaseDiffer;
    private final boolean kingsPhaseDiffer;
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

    public ParameterizedPstEvaluation(ParameterizedMaterialEvaluation materialEvaluation, String subPath) {
        // load psts and add the material values as offset:
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

        // compare EG/MG pattern to decide if they differ and we need to calculate them twice:
        pawnsPhaseDiffer = !pawnMG.equals(pawnEG);
        knightsPhaseDiffer = !knightMG.equals(knightEG);
        bispopsPhaseDiffer = !bishopMG.equals(bishopEG);
        rooksPhaseDiffer = !rookMG.equals(rookEG);
        queensPhaseDiffer = !queenMG.equals(queenEG);
        kingsPhaseDiffer = !kingMG.equals(kingEG);
    }

    @Override
    public void eval(EvalResult result, BoardRepresentation bitBoard) {
        BitChessBoard bb = bitBoard.getBoard();

        int pawnEval = pawnMG.calcScore(bb.getPawns(nWhite), bb.getPawns(nBlack));
        int knightEval = knightMG.calcScore(bb.getKnights(nWhite), bb.getKnights(nBlack));
        int bishopEval = bishopMG.calcScore(bb.getBishops(nWhite), bb.getBishops(nBlack));
        int rookEval = rookMG.calcScore(bb.getRooks(nWhite), bb.getRooks(nBlack));
        int queenEval = queenMG.calcScore(bb.getQueens(nWhite), bb.getQueens(nBlack));
        int kingEval = kingMG.calcScore(bb.getKings(nWhite), bb.getKings(nBlack));

        result.midGame += pawnEval + knightEval + bishopEval + rookEval + queenEval + kingEval;

        if (pawnsPhaseDiffer) {
            pawnEval = pawnEG.calcScore(bb.getPawns(nWhite), bb.getPawns(nBlack));
        }
        if (knightsPhaseDiffer) {
            knightEval = knightEG.calcScore(bb.getKnights(nWhite), bb.getKnights(nBlack));
        }
        if (bispopsPhaseDiffer) {
            bishopEval = bishopEG.calcScore(bb.getBishops(nWhite), bb.getBishops(nBlack));
        }
        if (rooksPhaseDiffer) {
            rookEval = rookEG.calcScore(bb.getRooks(nWhite), bb.getRooks(nBlack));
        }
        if (queensPhaseDiffer) {
            queenEval = queenEG.calcScore(bb.getQueens(nWhite), bb.getQueens(nBlack));
        }
        if (kingsPhaseDiffer) {
            kingEval = kingEG.calcScore(bb.getKings(nWhite), bb.getKings(nBlack));
        }

        result.endGame += pawnEval + knightEval + bishopEval + rookEval + queenEval + kingEval;
    }
}
