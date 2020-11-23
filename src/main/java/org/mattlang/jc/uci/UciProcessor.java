package org.mattlang.jc.uci;

import java.util.Optional;

import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.Engine;

public class UciProcessor {

    private Engine engine = new Engine();

    private boolean finished = false;

    public void start() {
        while (!finished) {
            Optional<String> optCmd = UCI.instance.readCommand();
            if (optCmd.isPresent()) {
                processCmd(optCmd.get());
            }
        }
    }

    private void processCmd(String cmdStr) {
        cmdStr = cmdStr.trim();
        if ("uci".equals(cmdStr)) {
            identifyYourself();
        } else if ("isready".equals(cmdStr)) {
            UCI.instance.putCommand("readyok");
        } else if ("ucinewgame".equals(cmdStr)) {
            // dont need to response anything
        } else if (cmdStr.startsWith("position")) {
            setPosition(cmdStr);
        } else if (cmdStr.startsWith("go ")) {
            engine.go();
        } else if ("stop".equals(cmdStr)) {
            engine.stop();
        }

    }

    private void setPosition(String positionStr) {
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

            setPosition(figures, zug, rochade, enpassant, noHalfMoves, nextMoveNum);

            movesSection = 7;
        }
        if (splitted.length > movesSection) {
            if ("moves".equals(splitted[movesSection])) {
                for (int moveIndex = movesSection + 1; moveIndex < splitted.length - 1; moveIndex++) {
                    String moveStr = splitted[moveIndex];
                    Move move = new Move(moveStr);
                    engine.move(move);
                }
            }
        }
    }

    private void setPosition(String figures, String zug, String rochade, String enpassant, String noHalfMoves,
            String nextMoveNum) {
        // rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1
        engine.clearPosition();
        String[] rows = figures.split("/");
        for (int i = 0; i < 8; i++) {
            String expandedRow = expandRow(rows[i]);
            for (int j =0; j<8; j++) {
                engine.setPos(8 - i, j, expandedRow.charAt(j));
            }
        }
        // todo parse and set rest of fen string...

    }

    private String expandRow(String row) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < row.length(); i++) {
            char ch = row.charAt(i);
            if (Character.isDigit(ch)) {
                int empties = Integer.parseInt(String.valueOf(ch));
                for (int e = 1; e <= empties; e++) {
                    b.append(" ");
                }
            } else {
                b.append(ch);
            }
        }
        return b.toString();
    }

    private void identifyYourself() {
        UCI.instance.putCommand("id name JackChess 1.0");
        UCI.instance.putCommand("id author Matthias Lang");
        //        UCI.instance.putCommand("option ");
        UCI.instance.putCommand("uciok");
    }

    private void sendBestMove() {
        // todo...
        // bestmove g1f3 ponder d8f6
    }
}
