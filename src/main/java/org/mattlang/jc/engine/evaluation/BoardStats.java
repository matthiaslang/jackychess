package org.mattlang.jc.engine.evaluation;

public class BoardStats {

    public final int mobility;
    public final int captures;
    public final long kingMobilityBitBoard;
    public final long mobilityBitBoard;

    public BoardStats(long mobilityBitBoard, long capturesBitBoard, long kingMobilityBitBoard) {
        this.mobility = Long.bitCount(mobilityBitBoard);
        this.captures = Long.bitCount(capturesBitBoard);
        this.kingMobilityBitBoard = kingMobilityBitBoard;
        this.mobilityBitBoard = mobilityBitBoard;
    }
}
