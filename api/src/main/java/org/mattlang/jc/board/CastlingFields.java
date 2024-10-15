package org.mattlang.jc.board;

public class CastlingFields {


    /* small castling (g Side) white king target pos.*/
    public static int gWKingTargetPos = Long.numberOfTrailingZeros(BB.G & BB.rank1);
    public static int gWRookTargetPos = Long.numberOfTrailingZeros(BB.F & BB.rank1);

    /* small castling (g Side) black king target pos.*/
    public static int gBKingTargetPos = Long.numberOfTrailingZeros(BB.G & BB.rank8);
    public static int gBRookTargetPos = Long.numberOfTrailingZeros(BB.F & BB.rank8);

    /* long castling (c Side) white king target pos.*/
    public static int cWKingTargetPos = Long.numberOfTrailingZeros(BB.C & BB.rank1);
    public static int cWRookTargetPos = Long.numberOfTrailingZeros(BB.D & BB.rank1);

    /* long castling (c Side) black king target pos.*/
    public static int cBKingTargetPos = Long.numberOfTrailingZeros(BB.C & BB.rank8);
    public static int cBRookTargetPos = Long.numberOfTrailingZeros(BB.D & BB.rank8);
}
