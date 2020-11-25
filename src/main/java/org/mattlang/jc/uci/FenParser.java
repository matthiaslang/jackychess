package org.mattlang.jc.uci;

import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.Engine;

public class FenParser {

    public void setPosition(String positionStr, Engine engine) {
        String[] splitted = positionStr.split(" ");
        String fen = splitted[1];
        int movesSection = 2;
        if ("startpos".equals(fen)) {
            engine.setStartPosition();

        } else if ("fen".equals(fen)) {
            // rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1
            String figures = splitted[2];
            String zug = splitted[3];
            String rochade = splitted[3];
            String enpassant = splitted[4];
            String noHalfMoves = splitted[5];
            String nextMoveNum = splitted[6];

            setPosition(engine, figures, zug, rochade, enpassant, noHalfMoves, nextMoveNum);

            movesSection = 7;
        }
        if (splitted.length > movesSection) {
            if ("moves".equals(splitted[movesSection])) {
                for (int moveIndex = movesSection + 1; moveIndex < splitted.length; moveIndex++) {
                    String moveStr = splitted[moveIndex];
                    Move move = new Move(moveStr);
                    engine.move(move);
                }
            }
        }
    }

    private void setPosition(Engine engine, String figures, String zug, String rochade, String enpassant,
            String noHalfMoves,
            String nextMoveNum) {
        // rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1
        engine.clearPosition();
        String[] rows = figures.split("/");
        engine.setPosition(rows);
        // todo parse and set rest of fen string...

    }
}
