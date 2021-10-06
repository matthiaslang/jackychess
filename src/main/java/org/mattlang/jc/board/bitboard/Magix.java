package org.mattlang.jc.board.bitboard;

import lombok.Getter;

/**
 * Combines a magic hash number + the hash map of opponent attacs of a particular source square to a hash index.
 * These values are calculated and statically prefilled via generated code.
 */
@Getter
public class Magix {

    long magic;

    /** the attack mask of the figure on the empty board. */
    long mask;

    int bits;

    /** the hash table with the attacs by opponent occupancy, indexed by the hash generated via the magic value. */
    long[] hash;

    public Magix(long magic, long mask, int bits) {
        this.magic = magic;
        this.mask = mask;
        this.bits = bits;
    }

    public long calcAttacs(long opponentMask) {
        return hash[calcIndex(mask & opponentMask, magic, bits)];
    }

    static int calcIndex(long b, long magic, int bits) {
        long val = ((b * magic) >>> (64 - bits));
        return (int) val;
    }


    public void calcHashAttacks(){
//        MagicCalcInfo calcInfo = new MagicCalcInfo(sq, bishop);

    }
}


