package org.mattlang.jc.movegenerator;

import org.mattlang.jc.board.FigureType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FigureParams {

    PAWNPARAMS(FigureType.Pawn.figureCode, false, new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 }),
    KNIGHTPARAMS(FigureType.Knight.figureCode, false, new byte[] { -21, -19, -12, -8, 8, 12, 19, 21 }),
    BISHOPPARAMS(FigureType.Bishop.figureCode, true, new byte[] { -11, -9, 9, 11 }),
    ROOKPARAMS(FigureType.Rook.figureCode, true, new byte[] { -10, -1, 1, 10 }),
    QUEENPARAMS(FigureType.Queen.figureCode, true, new byte[] { -11, -10, -9, -1, 1, 9, 10, 11 }),
    KINGPARAMS(FigureType.King.figureCode, false, new byte[] { -11, -10, -9, -1, 1, 9, 10, 11 });

    /**
     * figure code of piece.
     */
    byte figureCode;
    /**
     * sliding figure or not.
     */
    boolean slide;
    /**
     * ray offsets.
     */
    byte[] offset;

}
