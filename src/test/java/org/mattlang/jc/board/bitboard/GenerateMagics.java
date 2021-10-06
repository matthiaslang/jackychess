package org.mattlang.jc.board.bitboard;

import java.util.Random;

/**
 * see https://www.chessprogramming.org/Looking_for_Magics
 */
public class GenerateMagics {

    static Random random = new Random();

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

    static int count_1s(long b) {
        //       return Long.bitCount(b);
        int r;
        for (r = 0; b != 0; r++, b &= b - 1)
            ;
        return r;
    }

    //    public static final int[] BitTable = new int[] {
    //            63, 30, 3, 32, 25, 41, 22, 33, 15, 50, 42, 13, 11, 53, 19, 34, 61, 29, 2,
    //            51, 21, 43, 45, 10, 18, 47, 1, 54, 9, 57, 0, 35, 62, 31, 40, 4, 49, 5, 52,
    //            26, 60, 6, 23, 44, 46, 27, 56, 16, 7, 39, 48, 24, 59, 14, 12, 55, 38, 28,
    //            58, 20, 37, 17, 36, 8 };

    //    static int pop_1st_bit(long bb) {
    //        long b = bb ^ (bb - 1);
    //        long fold = ((b & 0xffffffffL) ^ (b >> 32));
    //        return BitTable[(int) (fold * 0x783a9b23L) >> 26];
    //    }

    static long index_to_long(int index, int bits, long m) {
        int i, j;
        long result = 0L;
        for (i = 0; i < bits; i++) {
            //            j = pop_1st_bit(m);
            //            j = Long.numberOfTrailingZeros(m);
            j = Long.numberOfLeadingZeros(m);
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

    static long find_magic(int sq, int m, boolean bishop) {
        long mask;

        long b[] = new long[4096];
        long a[] = new long[4096];
        long used[] = new long[4096];
        long magic;

        int i, j, k, n;
        boolean fail;

        mask = bishop ? bmask(sq) : rmask(sq);
        n = count_1s(mask);

        for (i = 0; i < (1 << n); i++) {
            b[i] = index_to_long(i, n, mask);
            a[i] = bishop ? batt(sq, b[i]) : ratt(sq, b[i]);
        }
        for (k = 0; k < 100000000; k++) {
            magic = random_long_fewbits();
            if (count_1s((mask * magic) & 0xFF00000000000000L) < 6)
                continue;
            for (i = 0; i < 4096; i++)
                used[i] = 0L;
            for (i = 0, fail = false; !fail && i < (1 << n); i++) {
                j = transform(b[i], magic, m);
                if (used[j] == 0L)
                    used[j] = a[i];
                else if (used[j] != a[i])
                    fail = true;
            }
            if (!fail)
                return magic;
        }
        System.out.printf("***Failed***\n");
        return 0L;
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

    public static void main(String[] args) {
        int square;

        System.out.printf("public static final long RMagic[] = new long[]{\n");
        for (square = 0; square < 64; square++)
            System.out.printf("  0x%sL,\n", Long.toHexString(find_magic(square, RBits[square], false)));
        System.out.printf("};\n\n");

        System.out.printf("public static final long BMagic[] = new long[]{\n");
        for (square = 0; square < 64; square++)
            System.out.printf("  0x%sL,\n", Long.toHexString(find_magic(square, BBits[square], true)));
        System.out.printf("};\n\n");

    }
}
