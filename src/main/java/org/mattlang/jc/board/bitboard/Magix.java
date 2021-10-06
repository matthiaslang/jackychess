package org.mattlang.jc.board.bitboard;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Combines a magic hash number + the hash map of opponent attacs of a particular source square to a hash index.
 * These values are calculated and statically prefilled via generated code.
 */
@Getter
@AllArgsConstructor
public class Magix {

    long magic;

    /** the attack mask of the figure on the empty board. */
    long mask;

    int bits;

    /** the hash table with the attacs by opponent occupancy, indexed by the hash generated via the magic value. */
    long[] hash;

    public long calcAttacs(long opponentMask) {
        return hash[calcIndex(mask & opponentMask, magic, bits)];
    }

    static int calcIndex(long b, long magic, int bits) {
        long val = ((b * magic) >>> (64 - bits));
        return (int) val;
    }
}


