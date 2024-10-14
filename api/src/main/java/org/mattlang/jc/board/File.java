package org.mattlang.jc.board;

public enum File {
    FILE_A(BB.A),
    FILE_B(BB.B),
    FILE_C(BB.C),
    FILE_D(BB.D),
    FILE_E(BB.E),
    FILE_F(BB.F),
    FILE_G(BB.G),
    FILE_H(BB.H);

    private static final File[] FILES = File.values();

    public final long fileMask;

    File(long fileMask) {
        this.fileMask = fileMask;
    }

    public static File file(int fileOrdinal) {
        return FILES[fileOrdinal];
    }
}
