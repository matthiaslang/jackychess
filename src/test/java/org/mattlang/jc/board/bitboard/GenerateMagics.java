package org.mattlang.jc.board.bitboard;

import static java.util.Arrays.fill;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;

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

    static long index_to_long(int index, int bits, long m) {
        int i, j;
        long result = 0L;
        for (i = 0; i < bits; i++) {
            //            j = pop_1st_bit(m);
                        j = Long.numberOfTrailingZeros(m);
//            j = Long.numberOfLeadingZeros(m);
            m &= (m - 1);
            if ((index & (1L << i)) != 0)
                result |= (1L << j);
        }
        return result;
    }

    static long rmask(int sq) {
        long result = 0L;
        int rk = sq / 8, fl = sq % 8, r, f;
        for (r = rk + 1; r <= 6; r++)
            result |= (1L << (fl + r * 8));
        for (r = rk - 1; r >= 1; r--)
            result |= (1L << (fl + r * 8));
        for (f = fl + 1; f <= 6; f++)
            result |= (1L << (f + rk * 8));
        for (f = fl - 1; f >= 1; f--)
            result |= (1L << (f + rk * 8));
        return result;
    }

    static long bmask(int sq) {
        long result = 0L;
        int rk = sq / 8, fl = sq % 8, r, f;
        for (r = rk + 1, f = fl + 1; r <= 6 && f <= 6; r++, f++)
            result |= (1L << (f + r * 8));
        for (r = rk + 1, f = fl - 1; r <= 6 && f >= 1; r++, f--)
            result |= (1L << (f + r * 8));
        for (r = rk - 1, f = fl + 1; r >= 1 && f <= 6; r--, f++)
            result |= (1L << (f + r * 8));
        for (r = rk - 1, f = fl - 1; r >= 1 && f >= 1; r--, f--)
            result |= (1L << (f + r * 8));
        return result;
    }

    static long ratt(int sq, long block) {
        long result = 0L;
        int rk = sq / 8, fl = sq % 8, r, f;
        for (r = rk + 1; r <= 7; r++) {
            result |= (1L << (fl + r * 8));
            if ((block & (1L << (fl + r * 8))) != 0)
                break;
        }
        for (r = rk - 1; r >= 0; r--) {
            result |= (1L << (fl + r * 8));
            if ((block & (1L << (fl + r * 8))) != 0)
                break;
        }
        for (f = fl + 1; f <= 7; f++) {
            result |= (1L << (f + rk * 8));
            if ((block & (1L << (f + rk * 8))) != 0)
                break;
        }
        for (f = fl - 1; f >= 0; f--) {
            result |= (1L << (f + rk * 8));
            if ((block & (1L << (f + rk * 8))) != 0)
                break;
        }
        return result;
    }

    static long batt(int sq, long block) {
        long result = 0L;
        int rk = sq / 8, fl = sq % 8, r, f;
        for (r = rk + 1, f = fl + 1; r <= 7 && f <= 7; r++, f++) {
            result |= (1L << (f + r * 8));
            if ((block & (1L << (f + r * 8))) != 0)
                break;
        }
        for (r = rk + 1, f = fl - 1; r <= 7 && f >= 0; r++, f--) {
            result |= (1L << (f + r * 8));
            if ((block & (1L << (f + r * 8))) != 0)
                break;
        }
        for (r = rk - 1, f = fl + 1; r >= 0 && f <= 7; r--, f++) {
            result |= (1L << (f + r * 8));
            if ((block & (1L << (f + r * 8))) != 0)
                break;
        }
        for (r = rk - 1, f = fl - 1; r >= 0 && f >= 0; r--, f--) {
            result |= (1L << (f + r * 8));
            if ((block & (1L << (f + r * 8))) != 0)
                break;
        }
        return result;
    }

    static int transform(long b, long magic, int bits) {
        long val = ((b * magic) >>> (64 - bits));
        return (int) val;
    }

    /**
     * Result of a magic search.
     */
    @AllArgsConstructor
    @Getter
    static class MagicResult {

        /**
         * the found magic number.
         */
        long magic;

        /**
         * the hash indices to the attack tables.
         */
        long hashIndex[] = new long[4096];

        /**
         * maximum index of hashIndex (as usually not all 4096 entries are filled)
         */
        int maxIndex;

        /**
         * shift bits used for calculating the index.
         */
        int bits;

        /** the attack mask on empty board for the figure in that position.*/
        long mask;

    }

    static MagicResult find_magic(int sq, int m, boolean bishop) {
        long mask;

        long b[] = new long[4096];
        long a[] = new long[4096];
        long used[] = new long[4096];
        long magic;

        int i, j, k, n;
        boolean fail;

        mask = bishop ? bmask(sq) : rmask(sq);
        n = Long.bitCount(mask);

        int combinations = 1 << n;
        for (i = 0; i < combinations; i++) {
            b[i] = index_to_long(i, n, mask);
            a[i] = bishop ? batt(sq, b[i]) : ratt(sq, b[i]);
        }
        for (k = 0; k < 100000000; k++) {
            magic = random_long_fewbits();
            if (Long.bitCount((mask * magic) & 0xFF00000000000000L) < 6)
                continue;

            fill(used, 0L);

            int maxIndex = 0;

            for (i = 0, fail = false; !fail && i < combinations; i++) {
                j = transform(b[i], magic, m);
                if (used[j] == 0L) {
                    used[j] = a[i];
                    maxIndex = Math.max(maxIndex, j);
                } else if (used[j] != a[i])
                    fail = true;
            }
            if (!fail)
                return new MagicResult(magic, used, maxIndex, m, mask);
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

        out.println("class MagicValues{\n");

        out.printf("public static final Magix RMagic[] = new Magix[]{\n");
        for (square = 0; square < 64; square++) {
            MagicResult magicResult = find_magic(square, RBits[square], false);
            out.printf(" new Magix(0x%sL, 0x%sL, %s, %s),\n", Long.toHexString(magicResult.magic),
                    Long.toHexString(magicResult.mask), magicResult.bits,
                    fmtHashIndex(magicResult.hashIndex, magicResult.maxIndex));
        }
        out.printf("};\n\n");

        out.printf("public static final Magix BMagic[] = new Magix[]{\n");
        for (square = 0; square < 64; square++) {
            MagicResult magicResult = find_magic(square, BBits[square], true);
            out.printf("  new Magix(0x%sL, 0x%sL, %s, %s),\n", Long.toHexString(magicResult.magic),
                    Long.toHexString(magicResult.mask), magicResult.bits,
                    fmtHashIndex(magicResult.hashIndex, magicResult.maxIndex));
        }
        out.printf("};\n\n");
        out.printf("};\n\n");
    }

    private static String fmtHashIndex(long[] hashIndex, int max) {
        return " new long[] {" +
                Arrays.stream(hashIndex)
                        .limit(max)
                        .mapToObj(l -> "0x" + Long.toHexString(l) + "L")
                        .collect(Collectors.joining(","))
                + "}";
    }
}
