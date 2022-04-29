package org.mattlang.jc.engine.evaluation.parameval.endgame;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedMaterialEvaluation;
import org.mattlang.jc.material.Material;
import org.mattlang.jc.util.FenComposer;

public interface EndgameFunction {

    static final Logger logger = Logger.getLogger(EndgameFunction.class.getName());

    int evaluate(BoardRepresentation board, Color stronger, Color weaker,
            ParameterizedMaterialEvaluation matEvaluation);

    public static void assertMat(BoardRepresentation board, Material expectedMat) {
        int expMat = expectedMat.getMaterial();
        int expMatInverse = expectedMat.switchSidesOnMaterial();
        int mat = board.getMaterial().getMaterial();
        if (mat == expMat || mat == expMatInverse) {
            return;
        }
        if (logger.isLoggable(Level.SEVERE)) {

            logger.severe("Not Expected Material on Board\n" + board.toUniCodeStr());
            logger.severe("Position: " + FenComposer.buildFenPosition(board));
        }
        // throw new AssertionError("Not expected Material for end game evaluation function! " );

    }

    public static void assertMat(BoardRepresentation board, Color color, int expectedMat) {
        int mat = color == Color.WHITE ? board.getMaterial().getWhiteMat() : board.getMaterial().getBlackAsWhitePart();
        if (mat == expectedMat) {
            return;
        }
        if (logger.isLoggable(Level.SEVERE)) {

            logger.severe("Not Expected Material on Board\n" + board.toUniCodeStr());
            logger.severe("Position: " + FenComposer.buildFenPosition(board));
        }

        throw new AssertionError("Not expected Material for end game evaluation function!");

    }
}
