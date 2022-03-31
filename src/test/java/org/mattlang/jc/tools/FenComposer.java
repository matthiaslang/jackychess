package org.mattlang.jc.tools;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Figure;
import org.mattlang.jc.board.RochadeType;

/**
 * Class to build a fen string from a board position.
 */
public class FenComposer {

    // position fen 8/8/8/3qk3/8/4K3/8/8 w - - 0 1 moves e3e2

    private static final String fen = "position startpos";
    private static final String fenfen = "position fen";

    private String preStr = fen;

    private ArrayList<String> moves = new ArrayList<>();

    public void clear() {
        moves.clear();
        preStr = fen;
    }

    public void add(String move) {
        moves.add(move);
    }

    public String createFenStr() {
        String fenCmd = preStr;
        if (moves.size() > 0) {
            fenCmd += " moves " + moves.stream().collect(Collectors.joining(" "));
        }
        return fenCmd;
    }

    public void parseMoves(String fen) {
        clear();
        if (fen.contains("moves")) {
            int index = fen.indexOf("moves");
            String fenMoves = fen.substring(index + 6);
            for (String m : fenMoves.split(" ")) {
                add(m.trim());
            }
            preStr = fen.substring(0, index).trim();
        } else {
            preStr = fen.trim();
        }

    }

    public void createFenFromBoard(BoardRepresentation board,
            Color who2Move, int noHalfMoves, int nextMoveNum) {
        clear();
        preStr = fenfen + " " + buildFenPosition(board) + " " + colorFen(who2Move) + " " + rochade(board) + " "
                + enpassant(board) + " " + noHalfMoves + " " + nextMoveNum;
    }

    private String enpassant(BoardRepresentation board) {
        int i = board.getEnPassantMoveTargetPos();
        if (i == -1) {
            return "-";
        } else {
            return Integer.toString(i);
        }
    }

    private String rochade(BoardRepresentation board) {
        StringBuilder b = new StringBuilder();
        if (board.isCastlingAllowed(Color.WHITE, RochadeType.SHORT)) {
            b.append("K");
        }
        if (board.isCastlingAllowed(Color.WHITE, RochadeType.LONG)) {
            b.append("Q");
        }
        if (board.isCastlingAllowed(Color.BLACK, RochadeType.SHORT)) {
            b.append("k");
        }
        if (board.isCastlingAllowed(Color.BLACK, RochadeType.LONG)) {
            b.append("q");
        }
        if (b.length() == 0) {
            b.append("-");
        }
        return b.toString();
    }

    private String colorFen(Color who2Move) {
        return who2Move == Color.WHITE ? "w" : "b";
    }

    public void createFenFromBoard(BoardRepresentation board) {
        createFenFromBoard(board, Color.WHITE, 0, 0);
    }

    public static String buildFenPosition(BoardRepresentation board) {
        StringBuilder fen = new StringBuilder();
        for (int i = 0; i < 8; ++i) {
            StringBuilder row = new StringBuilder();
            int emptyCounter = 0;
            for (int j = 0; j < 8; ++j) {
                Figure figure = board.getFigurePos(i, j);
                if (figure == Figure.EMPTY) {
                    emptyCounter++;
                } else {
                    if (emptyCounter > 0) {
                        row.append(emptyCounter);
                        emptyCounter = 0;
                    }
                    row.append(figure.figureChar);
                }

            }
            if (emptyCounter > 0) {
                row.append(emptyCounter);
            }
            if (fen.length() > 0) {
                fen.append("/");
            }
            fen.append(row);
        }
        return fen.toString();
    }

}
