package org.mattlang.jc.tools;

import static org.mattlang.jc.engine.evaluation.Tools.fileOf;
import static org.mattlang.jc.engine.evaluation.Tools.rankOf;

import java.util.List;
import java.util.stream.Collectors;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.FigureType;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.moves.MoveImpl;
import org.mattlang.jc.uci.FenParser;

/**
 * Untested so far. Should be used for eigenmann & bratkopec tests to properly compare the expected move which
 * does currently not work very well...
 */
public class AlgebraicNotation {

    /**
     * Converts a move in algebraic notation into a long algebraic notation which is used in UCI.
     *
     * @param algNotMove
     * @return
     */
    public String longANFromShortAN(BoardRepresentation board, Color color, String algNotMove) {
        algNotMove = algNotMove.trim();
        algNotMove = algNotMove.replace("+", ""); // replace check hint

        FigureType figureType = determineFigureType(algNotMove);

        String to = algNotMove.substring(algNotMove.length() - 2);

        String fromSpec = algNotMove.substring(0, algNotMove.length() - 2);
        if (Character.isUpperCase(fromSpec.charAt(0))) {
            fromSpec = fromSpec.substring(1);
        }
        fromSpec = fromSpec.replace("x", "");

        int toidx = toIndex(to);

        String moveStr = determineMoveStr(board, color, figureType, toidx, fromSpec);

        return moveStr;
    }

    private String determineMoveStr(BoardRepresentation board, Color color, FigureType figureType, int toidx,
            String fromSpec) {

        List<MoveImpl> allMoves =
                LegalMoves.generateLegalMoves(board, color).extractList();

        List<MoveImpl> matching = allMoves.stream()
                .filter(m -> m.getFigureType() == figureType.figureCode && m.getToIndex() == toidx)
                .collect(Collectors.toList());
        switch (matching.size()) {
        case 0:
            throw new IllegalStateException("somethings wrong, no legal move seems to match!");
        case 1:
            return matching.get(0).toStr();
        default:
            // its not unique, seems multiple same figure types can move to this target place,
            // we need to distinguish by the fromSpec:

            for (MoveImpl move : matching) {
                if (matchesFromSpec(move, fromSpec)) {
                    return move.toStr();
                }
            }
            throw new IllegalStateException("cant identify move from ambiguous matching ones..");
        }

    }

    private static boolean matchesFromSpec(MoveImpl move, String fromSpec) {
        return rankOf(move.getFromIndex()) == rankFromSpec(fromSpec)
                || fileOf(move.getFromIndex()) == fileFromSpec(fromSpec)
                || move.getFromIndex() == posFromSpec(fromSpec);
    }

    private static int posFromSpec(String fromSpec) {
        if (fromSpec.length() == 2) {
            return toIndex(fromSpec);
        }
        return -1000;
    }

    private static int fileFromSpec(String fromSpec) {
        if (fromSpec.length() == 1) {
            if (Character.isAlphabetic(fromSpec.charAt(0))) {
                return fileFromChar(fromSpec.charAt(0));
            }
        }
        return -1000;
    }

    private static int rankFromSpec(String fromSpec) {
        if (fromSpec.length() == 1) {
            if (Character.isDigit(fromSpec.charAt(0))) {
                return rankFromChar(fromSpec.charAt(0));
            }
        }
        return -1000;
    }

    private static int rankFromChar(char rank) {
        return Integer.parseInt("" + rank) - 1;
    }

    private static int fileFromChar(char file) {
        return file - 'a';
    }

    private static int toIndex(String to) {
        int x = fileFromChar(to.charAt(0));
        int y = rankFromChar(to.charAt(1));
        return (7 - y) * 8 + x;
    }

    /**
     * Converts a move in algebraic notation into a Move Object.
     *
     * @param algNotMove
     * @return
     */
    public Move moveFromAN(BoardRepresentation board, Color color, String algNotMove) {
        String longAn = longANFromShortAN(board, color, algNotMove);
        FenParser fenParser = new FenParser();
        return fenParser.parseMove(board, longAn);
    }

    private static FigureType determineFigureType(String algNotMove) {
        char fig = algNotMove.charAt(0);
        if (Character.isUpperCase(fig)) {
            for (FigureType figureType : FigureType.values()) {
                if (figureType.figureChar == fig) {
                    return figureType;
                }
            }
            throw new IllegalStateException("unable to determine Figure Type of " + algNotMove);
        } else {
            return FigureType.Pawn;
        }
    }
}
