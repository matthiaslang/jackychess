package org.mattlang.jc.board.bitboard;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * see https://www.chessprogramming.org/Looking_for_Magics
 */
public class GenerateMagics {

    static Random random = new Random(42);

    static long random() {
        return random.nextLong();
    }

    static long random_long() {
        long u1, u2, u3, u4;
        u1 = (long) (random()) & 0xFFFF;
        u2 = (long) (random()) & 0xFFFF;
        u3 = (long) (random()) & 0xFFFF;
        u4 = (long) (random()) & 0xFFFF;
        return u1 | (u2 << 16) | (u3 << 32) | (u4 << 48);
    }

    static long random_long_fewbits() {
        //        return random();
        return random_long() & random_long() & random_long();
    }

    static int transform(long b, long magic, int bits) {
        long val = ((b * magic) >>> (64 - bits));
        return (int) val;
    }

    static Magix find_magic(int sq, int bits, boolean bishop) {

        MagicCalcInfo calcInfo = new MagicCalcInfo(sq, bishop);

        for (int k = 0; k < 100000000; k++) {
            long magic = random_long_fewbits();
            if (Long.bitCount((calcInfo.mask * magic) & 0xFF00000000000000L) < 6)
                continue;

            long used[] = calcInfo.calcHashAttacksArray(bits, magic);
            if (used != null) {
                return new Magix(sq, bishop, magic, calcInfo.mask, bits);
            }
        }
        System.out.printf("***Failed***\n");
        throw new IllegalStateException("nothing found!!");
    }

    static int RBits[] = new int[] {
            12, 11, 11, 11, 11, 11, 11, 12,
            11, 10, 10, 10, 10, 10, 10, 11,
            11, 10, 10, 10, 10, 10, 10, 11,
            11, 10, 10, 10, 10, 10, 10, 11,
            11, 10, 10, 10, 10, 10, 10, 11,
            11, 10, 10, 10, 10, 10, 10, 11,
            11, 10, 10, 10, 10, 10, 10, 11,
            12, 11, 11, 11, 11, 11, 11, 12
    };

    static int BBits[] = new int[] {
            6, 5, 5, 5, 5, 5, 5, 6,
            5, 5, 5, 5, 5, 5, 5, 5,
            5, 5, 7, 7, 7, 7, 5, 5,
            5, 5, 7, 9, 9, 7, 5, 5,
            5, 5, 7, 9, 9, 7, 5, 5,
            5, 5, 7, 7, 7, 7, 5, 5,
            5, 5, 5, 5, 5, 5, 5, 5,
            6, 5, 5, 5, 5, 5, 5, 6
    };

    public static void main(String[] args) throws IOException {
        int square;

        File outFile = new File("src/main/java/org/mattlang/jc/board/bitboard/MagicValues.java");
        try (FileOutputStream fos = new FileOutputStream(outFile); PrintStream ps = new PrintStream(fos)) {
            calc(ps);
        }
    }

    public static void calc(PrintStream out) {
        int square;

        out.println("package org.mattlang.jc.board.bitboard;\n\n");

        out.println("/**\n");
        out.println(" * Generated with GenerateMagics.java class\n");
        out.println(" * Contains precalculated magic hash values.\n");
        out.println(" */\n\n");

        out.println("public class MagicValues{\n");

        out.printf("public static final Magix RMagic[] = new Magix[]{\n");
        for (square = 0; square < 64; square++) {
            Magix magicResult = find_magic(square, RBits[square], false);
            printMagicDeclaration(out, magicResult);
        }
        out.printf("};\n\n");

        out.printf("public static final Magix BMagic[] = new Magix[]{\n");
        for (square = 0; square < 64; square++) {
            Magix magicResult = find_magic(square, BBits[square], true);
            printMagicDeclaration(out, magicResult);
        }
        out.printf("};\n\n");
        out.printf("};\n\n");
    }

    private static void printMagicDeclaration(PrintStream out, Magix magix) {
        out.printf(" new Magix(%s, %s, 0x%sL, 0x%sL, %s),\n", magix.sq, magix.bishop, Long.toHexString(magix.magic),
                Long.toHexString(magix.mask), magix.bits);
    }

    private static String fmtHashIndex(long[] hashIndex) {
        return " new long[] {" +
                Arrays.stream(hashIndex)
                        .mapToObj(l -> "0x" + Long.toHexString(l) + "L")
                        .collect(Collectors.joining(","))
                + "}";
    }
}
