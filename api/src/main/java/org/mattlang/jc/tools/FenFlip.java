package org.mattlang.jc.tools;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static org.mattlang.jc.board.Color.WHITE;

import org.mattlang.jc.board.Color;

/**
 * Inverts a FEN Position for testing purpose.
 */
public class FenFlip {

    /**
     * Flips the Board.
     * Rotates it by the centre and inverts the colors.
     * Note that this is different to a horizontal mirrored board.
     * @param positionStr
     * @return
     */
    public String flipFen(String positionStr) {

        if (!positionStr.startsWith("position")) {
            throw new IllegalStateException(
                    "Error Parsing fen position string: Not starting with 'position':" + positionStr);
        }

        Color who2Move = WHITE;
        String[] splitted = positionStr.split(" ");
        String fen = splitted[1];
        int movesSection = 2;

        StringBuilder flipped = new StringBuilder();

        if ("startpos".equals(fen)) {
            throw new IllegalArgumentException("no need to flip startpos fens!");

        } else if ("fen".equals(fen)) {
            // rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1
            // position fen rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 2 moves f1d3 a7a6 g1f3
            String figures = splitted[2];
            String zug = splitted[3];
            String rochade = splitted[4];
            String enpassant = splitted[5];
            String noHalfMoves = splitted[6];
            String nextMoveNum = splitted[7];

            flipped.append("position fen ");

            flipped.append(flipFigures(figures));

            flipped.append(" ");
            flipped.append("w".equals(zug) ? "b" : "w");
            flipped.append(" ");

            flipped.append(flipRochade(rochade));
            flipped.append(" ");
            flipped.append(invertEnPassant(enpassant));

            flipped.append(" ");
            flipped.append(noHalfMoves);
            flipped.append(" ");
            flipped.append(nextMoveNum);

            movesSection = 8;
        } else {
            throw new IllegalArgumentException("fen position wrong: no 'position startpos' nor 'position fen' found!");
        }

        if (splitted.length > movesSection) {
            if ("moves".equals(splitted[movesSection])) {
                throw new IllegalArgumentException("cant flip fens with 'moves' section!");
            }
        }
        return flipped.toString();
    }

    public String mirrorHorizontalFen(String positionStr) {

        if (!positionStr.startsWith("position")) {
            throw new IllegalStateException(
                    "Error Parsing fen position string: Not starting with 'position':" + positionStr);
        }

        Color who2Move = WHITE;
        String[] splitted = positionStr.split(" ");
        String fen = splitted[1];
        int movesSection = 2;

        StringBuilder flipped = new StringBuilder();

        if ("startpos".equals(fen)) {
            throw new IllegalArgumentException("no need to flip startpos fens!");

        } else if ("fen".equals(fen)) {
            // rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1
            // position fen rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 2 moves f1d3 a7a6 g1f3
            String figures = splitted[2];
            String zug = splitted[3];
            String rochade = splitted[4];
            String enpassant = splitted[5];
            String noHalfMoves = splitted[6];
            String nextMoveNum = splitted[7];

            flipped.append("position fen ");

            flipped.append(mirrorFiguresHorizontal(figures));

            flipped.append(" ");
            flipped.append("w".equals(zug) ? "b" : "w");
            flipped.append(" ");

            flipped.append(flipRochade(rochade));
            flipped.append(" ");
            flipped.append(invertEnPassant(enpassant));

            flipped.append(" ");
            flipped.append(noHalfMoves);
            flipped.append(" ");
            flipped.append(nextMoveNum);

            movesSection = 8;
        } else {
            throw new IllegalArgumentException("fen position wrong: no 'position startpos' nor 'position fen' found!");
        }

        if (splitted.length > movesSection) {
            if ("moves".equals(splitted[movesSection])) {
                throw new IllegalArgumentException("cant flip fens with 'moves' section!");
            }
        }
        return flipped.toString();
    }


    private String invertEnPassant(String enpassant) {
        if ("-".equals(enpassant)) {
            return enpassant;
        }
        // 6 3 flip?

        return enpassant;
    }

    private String flipRochade(String rochade) {
        if ("-".equals(rochade)) {
            return rochade;
        }
        return invertCase(rochade);
    }

    private String mirrorFiguresHorizontal(String figures) {
        String[] rsltRows = new String[8];
        String[] figRows = figures.split("/");
        for (int i = 0; i < figRows.length; i++) {
            rsltRows[7-i] = invertCase(figRows[i]);
        }
        String flipped = new StringBuilder(stream(rsltRows).collect(joining("/"))).toString();
        return flipped;
    }

    private String flipFigures(String figures) {
        String[] figRows = figures.split("/");
        for (int i = 0; i < figRows.length; i++) {
            figRows[i] = invertCase(figRows[i]);
        }
        String flipped = new StringBuilder(stream(figRows).collect(joining("/"))).reverse().toString();
        return flipped;
    }

    private String invertCase(String figRow) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < figRow.length(); i++) {
            char ch = figRow.charAt(i);

            if (ch == Character.toUpperCase(ch)) {
                b.append(Character.toLowerCase(ch));
            } else {
                b.append(Character.toUpperCase(ch));
            }
        }
        return b.toString();
    }

}
