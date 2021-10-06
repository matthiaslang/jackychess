package org.mattlang.jc.board.bitboard;

public final class MagicBitboards {

    static long genRookAttacs(int rook, long opponentMask) {
        return MagicValues.RMagic[rook].calcAttacs(opponentMask);
    }

    static long genBishopAttacs(int rook, long opponentMask) {
        return MagicValues.BMagic[rook].calcAttacs(opponentMask);
    }
}
