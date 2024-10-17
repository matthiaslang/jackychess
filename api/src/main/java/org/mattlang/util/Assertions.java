package org.mattlang.util;

import static org.mattlang.jc.board.Figure.*;
import static org.mattlang.jc.board.FigureConstants.*;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.mattlang.jc.BuildConstants;

public class Assertions {

    public static void assertion(boolean condition, String msg) {
        if (BuildConstants.ASSERTIONS) {
            if (!condition) {
                throw new AssertionError(msg);
            }
        }
    }

    public static void assertionBetween(int val, int min, int max) {
        if (BuildConstants.ASSERTIONS) {
            assertion(val >= min && val <= max, String.format("value %s is not within [%s, %s]", val, min, max));
        }
    }

    public static void assertFieldNum(int val) {
        assertionBetween(val, 0, 63);
    }

    public static void assertFigureCode(byte figureCode) {
        assertionIsOneOf(figureCode,
                W_Pawn.figureCode, W_Knight.figureCode, W_Bishop.figureCode,
                W_Rook.figureCode, W_Queen.figureCode, W_King.figureCode,
                B_Pawn.figureCode, B_Knight.figureCode, B_Bishop.figureCode,
                B_Rook.figureCode, B_Queen.figureCode, B_King.figureCode);
    }

    public static void assertFigureCodeOrEmpty(byte figureCode) {
        assertionIsOneOf(figureCode, 0,
                W_Pawn.figureCode, W_Knight.figureCode, W_Bishop.figureCode,
                W_Rook.figureCode, W_Queen.figureCode, W_King.figureCode,
                B_Pawn.figureCode, B_Knight.figureCode, B_Bishop.figureCode,
                B_Rook.figureCode, B_Queen.figureCode, B_King.figureCode);
    }

    private static void assertionIsOneOf(int val, int... oneOfThisVals) {
        if (BuildConstants.ASSERTIONS) {
            for (int oneOfThisVal : oneOfThisVals) {
                if (val == oneOfThisVal) {
                    return;
                }
            }

            String valList = Arrays.stream(oneOfThisVals)
                    .mapToObj(Integer::toString)
                    .collect(Collectors.joining(","));
            throw new AssertionError(String.format("value %s is not one of [%s]", val, valList));
        }
    }

    public static void assertFigureType(byte figureType) {
        assertionIsOneOf(figureType, FT_PAWN, FT_KNIGHT, FT_BISHOP, FT_ROOK, FT_QUEEN, FT_KING);
    }
}
