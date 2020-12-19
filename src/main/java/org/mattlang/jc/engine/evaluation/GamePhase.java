package org.mattlang.jc.engine.evaluation;

import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.FigureType;

/**
 * Gamephase: Open, Middle or Endgame.
 */
public enum GamePhase {

    Opening,
    Middle,
    End;


    public static GamePhase determineGamePhase(BoardRepresentation board) {

        Board startBoard = new Board();
        startBoard.setStartPosition();
        int diffs = 0;
        int figuresOnBoard = 0;

        for (int i = 0; i < 64; i++) {
            byte figureCode = board.getFigureCode(i);
            if (figureCode != startBoard.getFigureCode(i)) {
                diffs++;
            }
            if (figureCode != FigureType.EMPTY.figureCode) {
                figuresOnBoard++;
            }
        }
        if (figuresOnBoard < 14) {
            return End;
        }

        if (diffs < 14) {
            return Opening;
        }

        return Middle;
    }
}
