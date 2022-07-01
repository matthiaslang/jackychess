package org.mattlang.jc.engine.evaluation.parameval;

import static org.mattlang.jc.board.bitboard.BitChessBoard.nBlack;
import static org.mattlang.jc.board.bitboard.BitChessBoard.nWhite;
import static org.mattlang.jc.engine.evaluation.evaltables.Pattern.loadFromFullPath;

import org.mattlang.jc.board.BoardRepresentation;
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
        pawnMG = loadFromFullPath(subPath + PAWN_MG_CSV);
        knightMG = loadFromFullPath(subPath + KNIGHT_MG_CSV);
        bishopMG = loadFromFullPath(subPath + BISHOP_MG_CSV);
        rookMG = loadFromFullPath(subPath + ROOK_MG_CSV);
        queenMG = loadFromFullPath(subPath + QUEEN_MG_CSV);
        kingMG = loadFromFullPath(subPath + KING_MG_CSV);
        pawnEG = loadFromFullPath(subPath + PAWN_EG_CSV);
        knightEG = loadFromFullPath(subPath + KNIGHT_EG_CSV);
        bishopEG = loadFromFullPath(subPath + BISHOP_EG_CSV);
        rookEG = loadFromFullPath(subPath + ROOK_EG_CSV);
        queenEG = loadFromFullPath(subPath + QUEEN_EG_CSV);
        kingEG = loadFromFullPath(subPath + KING_EG_CSV);

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
}
