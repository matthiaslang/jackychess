package org.mattlang.jc.uci;

import static org.mattlang.jc.engine.BasicMoveList.*;

import org.mattlang.jc.board.BasicMove;
import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.board.PawnPromotionMove;

public class FenParser {

    public void setPosition(String positionStr, Board board) {
        String[] splitted = positionStr.split(" ");
        String fen = splitted[1];
        int movesSection = 2;
        if ("startpos".equals(fen)) {
            board.setStartPosition();

        } else if ("fen".equals(fen)) {
            // rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1
            // position fen rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 2 moves f1d3 a7a6 g1f3
            String figures = splitted[2];
            String zug = splitted[3];
            String rochade = splitted[4];
            String enpassant = splitted[5];
            String noHalfMoves = splitted[6];
            String nextMoveNum = splitted[7];

            setPosition(board, figures, zug, rochade, enpassant, noHalfMoves, nextMoveNum);

            movesSection = 8;
        }
        if (splitted.length > movesSection) {
            if ("moves".equals(splitted[movesSection])) {
                for (int moveIndex = movesSection + 1; moveIndex < splitted.length; moveIndex++) {
                    String moveStr = splitted[moveIndex];
                    Move move = parseMove(moveStr);
                    board.move(move);
                }
            }
        }
    }

    private BasicMove parseMove(String moveStr) {
        // todo we need to distinguish between different move implementations via factory somehow
        if ("e1g1".equals(moveStr)) {
            return ROCHADE_MOVE_SW;
        } else if ("e1c1".equals(moveStr)) {
            return ROCHADE_MOVE_LW;
        } else if ("e8g8".equals(moveStr)) {
            return ROCHADE_MOVE_SB;
        } else if ("e8c8".equals(moveStr)) {
            return ROCHADE_MOVE_LB;
        }

        // simple pawn queen promotion
        // todo support other promotions, too
        if (moveStr.endsWith("q")) {
            BasicMove parsed = new BasicMove(moveStr);
           return new PawnPromotionMove(parsed.getFromIndex(), parsed.getToIndex(), (byte)0);
        }

        return new BasicMove(moveStr);
    }

    private void setPosition(Board board, String figures, String zug, String rochade, String enpassant,
            String noHalfMoves,
            String nextMoveNum) {
        // rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1
        board.clearPosition();
        String[] rows = figures.split("/");
        board.setPosition(rows);
        // todo parse and set rest of fen string...

    }
}
