package org.mattlang.jc.board.bitboard;

import org.mattlang.jc.board.*;

import lombok.Getter;

@Getter
public enum Fields {

    A1(BB.A1),
    A2(BB.A2),
    A3(BB.A3),
    A4(BB.A4),
    A5(BB.A5),
    A6(BB.A6),
    A7(BB.A7),
    A8(BB.A8),

    B1(BB.B1),
    B2(BB.B2),
    B3(BB.B3),
    B4(BB.B4),
    B5(BB.B5),
    B6(BB.B6),
    B7(BB.B7),
    B8(BB.B8),

    C1(BB.C1),
    C2(BB.C2),
    C3(BB.C3),
    C4(BB.C4),
    C5(BB.C5),
    C6(BB.C6),
    C7(BB.C7),
    C8(BB.C8),

    D1(BB.D1),
    D2(BB.D2),
    D3(BB.D3),
    D4(BB.D4),
    D5(BB.D5),
    D6(BB.D6),
    D7(BB.D7),
    D8(BB.D8),

    E1(BB.E1),
    E2(BB.E2),
    E3(BB.E3),
    E4(BB.E4),
    E5(BB.E5),
    E6(BB.E6),
    E7(BB.E7),
    E8(BB.E8),

    F1(BB.F1),
    F2(BB.F2),
    F3(BB.F3),
    F4(BB.F4),
    F5(BB.F5),
    F6(BB.F6),
    F7(BB.F7),
    F8(BB.F8),

    G1(BB.G1),
    G2(BB.G2),
    G3(BB.G3),
    G4(BB.G4),
    G5(BB.G5),
    G6(BB.G6),
    G7(BB.G7),
    G8(BB.G8),

    H1(BB.H1),
    H2(BB.H2),
    H3(BB.H3),
    H4(BB.H4),
    H5(BB.H5),
    H6(BB.H6),
    H7(BB.H7),
    H8(BB.H8);

    private final long whiteFieldBB;
    private final long blackFieldBB;
    public final int square;
    public final int rankNum;
    public final int fileNum;
    public final Rank rank;
    public final File file;

    Fields(long whiteFieldBB) {
        this.whiteFieldBB = whiteFieldBB;
        this.blackFieldBB = BB.flipVertical(whiteFieldBB);

        square = Long.numberOfTrailingZeros(whiteFieldBB);
        rankNum = Tools.rankOf(square);
        fileNum = Tools.fileOf(square);

        rank = Rank.rank(rankNum);
        file = File.file(fileNum);
    }

    public static Fields find(File file, Rank rank) {
        for (Fields field : Fields.values()) {
            if (field.file == file && field.rank == rank) {
                return field;
            }
        }
        throw new IllegalArgumentException("no matching Field!!");
    }

    public boolean isSet(long bitboard, Color side) {
        return side == Color.WHITE ? (bitboard & whiteFieldBB) != 0 : (bitboard & blackFieldBB) != 0;
    }
}
