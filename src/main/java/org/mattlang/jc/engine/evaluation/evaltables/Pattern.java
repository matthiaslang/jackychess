package org.mattlang.jc.engine.evaluation.evaltables;

import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.mattlang.jc.board.Color;

/**
 * A board pattern with evaluations per field.
 */
public final class Pattern {

    private final int[] boardPattern;

    private final int[] flippedPattern;

    /* The FLIP array is used to calculate the piece/square values for the oppiste pieces.
     The piece/square value of a LIGHT pawn is PAWN_PCSQ[FLIP[sq]] and the value of a
    DARK pawn is PAWN_PCSQ[sq]

     */
    private static final int[] FLIP = new int[] {
            56, 57, 58, 59, 60, 61, 62, 63,
            48, 49, 50, 51, 52, 53, 54, 55,
            40, 41, 42, 43, 44, 45, 46, 47,
            32, 33, 34, 35, 36, 37, 38, 39,
            24, 25, 26, 27, 28, 29, 30, 31,
            16, 17, 18, 19, 20, 21, 22, 23,
            8, 9, 10, 11, 12, 13, 14, 15,
            0, 1, 2, 3, 4, 5, 6, 7
    };

    public Pattern(int[] boardPattern) {
        this.boardPattern = Objects.requireNonNull(boardPattern);
        if (boardPattern.length != 64) {
            throw new IllegalArgumentException("Pattern has not 64 values!");
        }
        this.flippedPattern = new int[64];
        for (int i = 0; i < 64; i++) {
            flippedPattern[i] = boardPattern[FLIP[i]];
        }
    }

    public final int calcScore(long whiteFigures, long blackFigures, int who2mov) {
        int w = dotProduct(whiteFigures, WHITE);
        int b = dotProduct(blackFigures, BLACK);
        return (w - b) * who2mov;
    }

    public final int calcScore(long whiteFigures, long blackFigures) {
        int w = dotProduct(whiteFigures, WHITE);
        int b = dotProduct(blackFigures, BLACK);
        return w - b;
    }

    public final int getVal(int pos, Color color) {
        if (color == WHITE) {
            return flippedPattern[pos];
        } else {
            return boardPattern[pos];
        }
    }

    public void setVal(int pos, int value) {
        boardPattern[pos] = value;
        flippedPattern[FLIP[pos]] = value;
    }

    /**
     * Calc weighted population count of bitmasks; useful in evaluations.
     *
     * @param bb
     * @param color
     * @return
     */
    public int dotProduct(long bb, Color color) {

        int[] weights = color == WHITE ? flippedPattern : boardPattern;
        int accu = 0;
        while (bb != 0) {
            final int pos = Long.numberOfTrailingZeros(bb);
            accu += weights[pos];

            bb &= bb - 1;
        }

        return accu;
    }

    public int dotProductOld(long bb, Color color) {

        int[] weights = color == WHITE ? flippedPattern : boardPattern;

        long bit = 1;
        int accu = 0;
        for (int sq = 0; sq < 64; sq++, bit += bit) {
            if ((bb & bit) != 0)
                accu += weights[sq];
            // accu += weights[sq] & -((  bb & bit) == bit); // branchless 1
            // accu += weights[sq] & -(( ~bb & bit) == 0);   // branchless 2
        }
        return accu;
    }

    /**
     * Loads a pattern from a csv file.
     *
     * @param resourceFile a resource file path relative to the folder "pattern"
     * @return
     */
    public static Pattern load(String resourceFile) {
        String fullName = "/pattern/" + resourceFile;
        return loadFromFullPath(fullName);
    }

    public static Pattern loadFromFullPath(String fullName) {
        InputStream is = Pattern.class.getResourceAsStream(fullName);
        if (is == null) {
            throw new IllegalArgumentException("Could not load pst pattern from resource file " + fullName);
        }
        return parsePattern(is);
    }

    public static Pattern parsePattern(InputStream is) {
        List<Integer> values = new ArrayList<>();
        new BufferedReader(new InputStreamReader(is))
                .lines().forEach(line -> {
                    if (line.startsWith("//")) {

                    } else {
                        // parse and append the values:
                        values.addAll(Arrays.stream(line.split(";"))
                                .map(s -> s.trim())
                                .map(s -> Integer.parseInt(s))
                                .collect(Collectors.toList()));
                    }
                });

        return new Pattern(values.stream().mapToInt(i -> i.intValue()).toArray());

    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Pattern pattern = (Pattern) o;
        return Arrays.equals(boardPattern, pattern.boardPattern);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(boardPattern);
    }

    public String toPatternStr() {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < boardPattern.length; i++) {
            b.append(boardPattern[i]).append(";");
            if ((i + 1) % 8 == 0) {
                b.append("\n");
            }
        }
        return b.toString();
    }

    public Pattern copy() {
        return new Pattern(boardPattern.clone());
    }
}
