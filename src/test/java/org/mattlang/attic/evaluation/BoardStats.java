package org.mattlang.attic.evaluation;

public class BoardStats {

    public final int mobility;
    public final int captures;
    public final long kingMobilityBitBoard;
    public final long mobilityBitBoard;
    public final long hypotheticalPawnCaptures;

    public BoardStats(long mobilityBitBoard, long capturesBitBoard, long kingMobilityBitBoard, long hypotheticalPawnCaptures) {
        this.mobility = Long.bitCount(mobilityBitBoard);
        this.captures = Long.bitCount(capturesBitBoard);
        this.kingMobilityBitBoard = kingMobilityBitBoard;
        this.mobilityBitBoard = mobilityBitBoard;
        this.hypotheticalPawnCaptures = hypotheticalPawnCaptures;
    }
}
