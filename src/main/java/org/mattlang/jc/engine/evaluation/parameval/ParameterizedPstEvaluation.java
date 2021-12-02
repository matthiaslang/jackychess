package org.mattlang.jc.engine.evaluation.parameval;

import static org.mattlang.jc.board.bitboard.BitChessBoard.nBlack;
import static org.mattlang.jc.board.bitboard.BitChessBoard.nWhite;
import static org.mattlang.jc.engine.evaluation.evaltables.Pattern.loadFromFullPath;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.PieceList;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.board.bitboard.BitChessBoard;
import org.mattlang.jc.engine.EvaluateFunction;
import org.mattlang.jc.engine.evaluation.PhaseCalculator;
import org.mattlang.jc.engine.evaluation.evaltables.Pattern;

/**
 * A Tapered, parameterized PST Evaluation where the PST Tables are loaded from resource files.
 */
public class ParameterizedPstEvaluation implements EvaluateFunction {

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

    }

    @Override
    public int eval(BoardRepresentation currBoard, Color who2Move) {

        if (currBoard instanceof BitBoard) {
            return evalBitboard((BitBoard) currBoard, who2Move);
        } else {
            int who2mov = who2Move == Color.WHITE ? 1 : -1;
            PieceList wp = currBoard.getWhitePieces();
            PieceList bp = currBoard.getBlackPieces();

            int midGame = pawnMG.calcScore(wp.getPawns(), bp.getPawns(), who2mov) +
                    knightMG.calcScore(wp.getKnights(), bp.getKnights(), who2mov) +
                    bishopMG.calcScore(wp.getBishops(), bp.getBishops(), who2mov) +
                    rookMG.calcScore(wp.getRooks(), bp.getRooks(), who2mov) +
                    queenMG.calcScore(wp.getQueens(), bp.getQueens(), who2mov) +
                    kingMG.calcScore(wp.getKing(), bp.getKing(), who2mov);
            int endGame = pawnEG.calcScore(wp.getPawns(), bp.getPawns(), who2mov) +
                    knightEG.calcScore(wp.getKnights(), bp.getKnights(), who2mov) +
                    bishopEG.calcScore(wp.getBishops(), bp.getBishops(), who2mov) +
                    rookEG.calcScore(wp.getRooks(), bp.getRooks(), who2mov) +
                    queenEG.calcScore(wp.getQueens(), bp.getQueens(), who2mov) +
                    kingEG.calcScore(wp.getKing(), bp.getKing(), who2mov);

            double score = PhaseCalculator.scaleByPhase(wp, bp, midGame, endGame);
            return (int) score;
        }
    }

    private int evalBitboard(BitBoard currBoard, Color who2Move) {
        int who2mov = who2Move == Color.WHITE ? 1 : -1;
        BitChessBoard bb = currBoard.getBoard();

        int midGame = pawnMG.calcScore(bb.getPawns(nWhite), bb.getPawns(nBlack), who2mov) +
                knightMG.calcScore(bb.getKnights(nWhite), bb.getKnights(nBlack), who2mov) +
                bishopMG.calcScore(bb.getBishops(nWhite), bb.getBishops(nBlack), who2mov) +
                rookMG.calcScore(bb.getRooks(nWhite), bb.getRooks(nBlack), who2mov) +
                queenMG.calcScore(bb.getQueens(nWhite), bb.getQueens(nBlack), who2mov) +
                kingMG.calcScore(bb.getKings(nWhite), bb.getKings(nBlack), who2mov);
        int endGame = pawnEG.calcScore(bb.getPawns(nWhite), bb.getPawns(nBlack), who2mov) +
                knightEG.calcScore(bb.getKnights(nWhite), bb.getKnights(nBlack), who2mov) +
                bishopEG.calcScore(bb.getBishops(nWhite), bb.getBishops(nBlack), who2mov) +
                rookEG.calcScore(bb.getRooks(nWhite), bb.getRooks(nBlack), who2mov) +
                queenEG.calcScore(bb.getQueens(nWhite), bb.getQueens(nBlack), who2mov) +
                kingEG.calcScore(bb.getKings(nWhite), bb.getKings(nBlack), who2mov);

        double score = PhaseCalculator.scaleByPhase(bb, midGame, endGame);
        return (int) score;
    }

}
