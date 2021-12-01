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

    private Pattern PAWN_MG;
    private Pattern KNIGHT_MG;
    private Pattern BISHOP_MG;
    private Pattern ROOK_MG;
    private Pattern QUEEN_MG;
    private Pattern KING_MG;
    private Pattern PAWN_EG;
    private Pattern KNIGHT_EG;
    private Pattern BISHOP_EG;
    private Pattern ROOK_EG;
    private Pattern QUEEN_EG;
    private Pattern KING_EG;

    public ParameterizedPstEvaluation(String subPath) {
        PAWN_MG = loadFromFullPath(subPath + "pawnMG.csv");
        KNIGHT_MG = loadFromFullPath(subPath + "knightMG.csv");
        BISHOP_MG = loadFromFullPath(subPath + "bishopMG.csv");
        ROOK_MG = loadFromFullPath(subPath + "rookMG.csv");
        QUEEN_MG = loadFromFullPath(subPath + "queenMG.csv");
        KING_MG = loadFromFullPath(subPath + "kingMG.csv");
        PAWN_EG = loadFromFullPath(subPath + "pawnEG.csv");
        KNIGHT_EG = loadFromFullPath(subPath + "knightEG.csv");
        BISHOP_EG = loadFromFullPath(subPath + "bishopEG.csv");
        ROOK_EG = loadFromFullPath(subPath + "rookEG.csv");
        QUEEN_EG = loadFromFullPath(subPath + "queenEG.csv");
        KING_EG = loadFromFullPath(subPath + "kingEG.csv");

    }

    @Override
    public int eval(BoardRepresentation currBoard, Color who2Move) {

        if (currBoard instanceof BitBoard) {
            return evalBitboard((BitBoard) currBoard, who2Move);
        } else {
            int who2mov = who2Move == Color.WHITE ? 1 : -1;
            PieceList wp = currBoard.getWhitePieces();
            PieceList bp = currBoard.getBlackPieces();

            int midGame = PAWN_MG.calcScore(wp.getPawns(), bp.getPawns(), who2mov) +
                    KNIGHT_MG.calcScore(wp.getKnights(), bp.getKnights(), who2mov) +
                    BISHOP_MG.calcScore(wp.getBishops(), bp.getBishops(), who2mov) +
                    ROOK_MG.calcScore(wp.getRooks(), bp.getRooks(), who2mov) +
                    QUEEN_MG.calcScore(wp.getQueens(), bp.getQueens(), who2mov) +
                    KING_MG.calcScore(wp.getKing(), bp.getKing(), who2mov);
            int endGame = PAWN_EG.calcScore(wp.getPawns(), bp.getPawns(), who2mov) +
                    KNIGHT_EG.calcScore(wp.getKnights(), bp.getKnights(), who2mov) +
                    BISHOP_EG.calcScore(wp.getBishops(), bp.getBishops(), who2mov) +
                    ROOK_EG.calcScore(wp.getRooks(), bp.getRooks(), who2mov) +
                    QUEEN_EG.calcScore(wp.getQueens(), bp.getQueens(), who2mov) +
                    KING_EG.calcScore(wp.getKing(), bp.getKing(), who2mov);

            double score = PhaseCalculator.scaleByPhase(wp, bp, midGame, endGame);
            return (int) score;
        }
    }

    private int evalBitboard(BitBoard currBoard, Color who2Move) {
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
