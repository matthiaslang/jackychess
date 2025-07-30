package org.mattlang.tuning.data.pgnparser;

import static org.mattlang.tuning.data.pgnparser.AlgebraicNotation.determineFigureType;
import static org.mattlang.tuning.data.pgnparser.PgnMoveDescrType.*;

import org.mattlang.jc.board.FigureType;
import org.mattlang.jc.board.IndexConversion;
import org.mattlang.jc.uci.FenConstants;

import lombok.Getter;

/**
 * Parsed Move Text.
 */
@Getter
public class ParsedMoveText {

    private final MoveText moveText;
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

    /* parse helper fields */
    private int currPos = -1;

    private char matchedChar;
    private String matchedString = "";

    public ParsedMoveText(MoveText moveText) {
        this.moveText = moveText;
        parseMoveText(moveText.getText());
    }

    private void parseMoveText(String str) {
        if (str.contains("+")) {
            checkHint = true;
        }
        if (str.contains("x")) {
            captureHint = true;
        }
        if (str.contains("#")) {
            checkMateHint = true;
        }
        if (str.startsWith(FenConstants.CASTLING_LONG1) || str.startsWith(FenConstants.CASTLING_LONG2)) {
            type = CASTLING_LONG;
        } else if (str.startsWith(FenConstants.CASTLING_SHORT1) || str.startsWith(FenConstants.CASTLING_SHORT2)) {
            type = CASTLING_SHORT;
        } else {
            type = NORMAL;
            parseRegularMove();
        }
    }

    private void overreadHintsAndDecorators() {
        char curr = getCurrChar();
        while (curr == ' ' || curr == '+' || curr == '#' || curr == 'x' || curr == '-') {
            currPos++;
            if (currPos == moveText.getText().length()) {
                break;
            }
            curr = getCurrChar();
        }
    }

    private char getNextChar() {
        char ch = getCurrChar();
        currPos++;
        return ch;
    }

    private char getCurrChar() {
        if (currPos >= moveText.getText().length()) {
            return '\n';
        } else {
            return moveText.getText().charAt(currPos);
        }
    }

    private boolean checkMatch(boolean matchCondition) {
        if (matchCondition) {
            matchedChar = getNextChar();
            matchedString += matchedChar;
            return true;
        }
        return false;
    }

    private boolean match(char ch) {
        overreadHintsAndDecorators();
        return checkMatch(ch == getCurrChar());
    }

    private boolean matchDigit() {
        overreadHintsAndDecorators();
        return checkMatch(Character.isDigit(getCurrChar()));
    }

    private boolean matchUpperLetter() {
        overreadHintsAndDecorators();
        return checkMatch(Character.isUpperCase(getCurrChar()));
    }

    private boolean matchLowerLetter() {
        overreadHintsAndDecorators();
        return checkMatch(Character.isLowerCase(getCurrChar()));
    }

    private String consumeMatchedStr() {
        String str = matchedString;
        matchedChar = '\n';
        matchedString = "";
        return str;
    }

    private char consumeMatchedChar() {
        char ch = matchedChar;
        matchedChar = '\n';
        matchedString = "";
        return ch;
    }

    private void parseRegularMove() {

        currPos = 0;

        // parse optional figure letter:
        parseFigureLetter();

        // parse optional disambiguous symbols or the position:
        int indexPos1 = parseDisambigousOrPos();
        // parse an optional second position:
        int indexPos2 = parseOptionalSecondPos();

        // interpret now the one or two positions we got:
        if (indexPos1 >= 0 && indexPos2 >= 0) {
            fromSpec = IndexConversion.convert(indexPos1);
            toIdx = indexPos2;
        } else if (indexPos1 >= 0) {
            toIdx = indexPos1;
        } else if (indexPos2 >= 0) {
            toIdx = indexPos2;
        } else {
            throw new PgnParserException("error parsing/ordering move indexes", moveText);
        }

        // set promotion marker:
        if (figure == FigureType.Pawn && (toIdx <= 7 || toIdx >= 56)) {
            // mark as Promotion:
            promotion = true;
        }

        // now there could be a promotion info coming at last:
        if (promotion) {
            parsePromotionFigure();
        }

    }

    private void parsePromotionFigure() {
        if (match('=') && matchUpperLetter()) {
            promotedFigureType = determineFigureType(consumeMatchedChar());
        } else if (match('/') && matchUpperLetter()) {
            promotedFigureType = determineFigureType(consumeMatchedChar());
        } else if (match('(') && matchUpperLetter()) {
            promotedFigureType = determineFigureType(consumeMatchedChar());
        } else if (matchUpperLetter()) {
            promotedFigureType = determineFigureType(consumeMatchedChar());
        } else {
            throw new PgnParserException("error parsing promotion part", moveText);
        }
    }

    private int parseOptionalSecondPos() {
        // now there should be the "to" pos or nothing if we have already a first pos:
        if (matchLowerLetter()) {
            if (matchDigit()) {
                return IndexConversion.parsePos(consumeMatchedStr());
            } else {
                throw new PgnParserException("error parsing to move coordinate", moveText);
            }
        }
        return -1;
    }

    private int parseDisambigousOrPos() {
        if (matchLowerLetter()) {
            // its either a disambiguous hint or a coordinate part:
            if (matchDigit()) {
                return IndexConversion.parsePos(consumeMatchedStr());
            } else {
                fromSpec = consumeMatchedStr();
            }

        } else if (matchDigit()) {
            // its a "from" disambiguous hint:
            fromSpec = consumeMatchedStr();
        }
        return -1;
    }

    private void parseFigureLetter() {
        if (matchUpperLetter()) {
            figure = AlgebraicNotation.determineFigureType(consumeMatchedChar());
        } else {
            figure = FigureType.Pawn;
        }
    }
}
