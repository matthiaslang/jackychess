package org.mattlang.jc.board.bitboard;

public final class MagicBitboards {

    static {
        for (int i = 0; i < 64; i++) {
            MagicValues.BMagic[i].calcHashAttacks();
            MagicValues.RMagic[i].calcHashAttacks();
        }
    }

    public static long genRookAttacs(int rook, long opponentMask) {
        return MagicValues.RMagic[rook].calcAttacs(opponentMask);
    }

    public static long genBishopAttacs(int rook, long opponentMask) {
        return MagicValues.BMagic[rook].calcAttacs(opponentMask);
    }
}
