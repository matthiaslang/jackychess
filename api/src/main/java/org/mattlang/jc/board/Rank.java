package org.mattlang.jc.board;

public enum Rank {
    RANK_1(BB.rank1),
    RANK_2(BB.rank2),
    RANK_3(BB.rank3),
    RANK_4(BB.rank4),
    RANK_5(BB.rank5),
    RANK_6(BB.rank6),
    RANK_7(BB.rank7),
    RANK_8(BB.rank8);

    private static final Rank[] RANKS = Rank.values();

    public final long rankMask;

    Rank(long rankMask) {
        this.rankMask = rankMask;
    }

    public static Rank rank(int rankOrdinal) {
        return RANKS[rankOrdinal];
    }
}
