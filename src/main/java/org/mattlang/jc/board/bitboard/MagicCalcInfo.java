package org.mattlang.jc.board.bitboard;

import lombok.Getter;

/**
 * Information needed to calculate (and test) hash sets for magic bitboard hashes.
 * Contains the information for one source square for a figure type.
 */
@Getter
public class MagicCalcInfo {

    long mask;

    long b[] = new long[4096];
    long a[] = new long[4096];

    public MagicCalcInfo(int sq, boolean bishop) {

        mask = bishop ? bmask(sq) : rmask(sq);
        int n = Long.bitCount(mask);

        int combinations = 1 << n;
        for (int i = 0; i < combinations; i++) {
            b[i] = index_to_long(i, n, mask);
            a[i] = bishop ? batt(sq, b[i]) : ratt(sq, b[i]);
        }

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
}
