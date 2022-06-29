package org.mattlang.tuning.data.pgnparser;

import static org.mattlang.tuning.data.pgnparser.AlgebraicNotation.determineFigureType;
import static org.mattlang.tuning.data.pgnparser.AlgebraicNotation.toIndex;
import static org.mattlang.tuning.data.pgnparser.PgnMoveDescrType.*;

import org.mattlang.jc.board.FigureType;

import lombok.Getter;

@Getter
/**
 * Represents an algebraic notated Move (usually within a pgn file).
 */
public class MoveText implements Symbol {

    /**
     * the original text description of the move.
     */
    private final String str;

    /**
     * the move type.
     */
    private PgnMoveDescrType type;

    /**
     * the moving figure of a regular move.
     */
    private FigureType figure;

    /**
     * to Index of a regular move.
     */
    private int toIdx;

    /**
     * optional from spec of a regular move.
     */
    private String fromSpec;
    /**
     * is it a promotion?
     */
    private boolean promotion;

    /**
     * the promoted figure. default ist Queen
     */
    private FigureType promotedFigureType = FigureType.Queen;
    private boolean checkHint;
    private boolean captureHint;
    private boolean checkMateHint;

    public MoveText(String str) {
        this.str = str.trim();

        parseMoveText(str);
    }

    private void parseMoveText(String str) {
        // special Moves:
        switch (str) {
        case "0-0":
        case "O-O":
            type = CASTLING_SHORT;
            break;
        case "0-0-0":
        case "O-O-O":
            type = CASTLING_LONG;
            break;
        case "1/2-1/2":
            type = DRAW;
            break;
        case "1-0":
            type = MATE_WHITE;
            break;
        case "0-1":
            type = MATE_BLACK;
            break;
        default:
            type = NORMAL;
        }

        if (type == NORMAL) {
            parseRegularMove();
        }
    }

    private void parseRegularMove() {
        String s = str;

        if (s.contains("+")) {
            checkHint = true;
        }
        if (s.contains("x")) {
            captureHint = true;
        }
        if (s.contains("#")) {
            checkMateHint = true;
        }

        if (s.contains("=")) {
            int idx = s.indexOf("=");
            char promFigChar = s.charAt(idx + 1);
            promotedFigureType = determineFigureType(promFigChar);
            // remove the "=" part from the string
            s = s.replace("=" + promFigChar, "");
        }

        String movespec = s.replace("+", ""); // replace check hint
        movespec = movespec.replace("#", ""); // replace check hint

        figure = AlgebraicNotation.determineFigureType(movespec);
        String to = movespec.substring(movespec.length() - 2);

        fromSpec = movespec.substring(0, movespec.length() - 2);
        if (fromSpec.length() > 0 && Character.isUpperCase(fromSpec.charAt(0))) {
            fromSpec = fromSpec.substring(1);
        }
        fromSpec = fromSpec.replace("x", "");
        toIdx = toIndex(to);

        // todo maybe use MoveImpl.isOnLastLine
        if (figure == FigureType.Pawn && (toIdx <= 7 || toIdx >= 56)) {
            // mark as Promotion:
            promotion = true;

        }

    }
}
