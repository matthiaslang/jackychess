package org.mattlang.jc.engine.evaluation.parameval;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mattlang.jc.board.Color.nBlack;
import static org.mattlang.jc.board.Color.nWhite;
import static org.mattlang.jc.board.Tools.fileOf;
import static org.mattlang.jc.engine.evaluation.parameval.ParameterizedKingEvaluation.*;

import org.junit.Test;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.board.bitboard.BitChessBoard;

public class ParameterizedKingEvaluationTest {

    @Test
    public void testPawnKingRankDelta() {

        BitBoard board = new BitBoard();
        board.setFenPosition("position fen 3k4/6p1/3pp3/8/6P1/4PP2/8/6K1 w - - 1 56 ");
        board.println();

        BitChessBoard bb = board.getBoard();

        // eval for white:
        int US = nWhite, THEM = Color.invert(nWhite);
        int kingSq = Long.numberOfTrailingZeros(bb.getKings(US));

        long myPawns = bb.getPawns(US);
        long enemyPawns = bb.getPawns(THEM);
        int adjacentMinFile = calcMinAdjFile(kingSq);
        int adjacentMaxFile = calcMaxAdjFile(kingSq);

        assertThat(fileOf(kingSq)).isEqualTo(6);
        assertThat(adjacentMinFile).isEqualTo(5);
        assertThat(adjacentMaxFile).isEqualTo(7);

        // Find closest friendly pawn at or above our King on a given file
        int ourDist = calcPawnKingRankDelta(myPawns, adjacentMinFile, US, kingSq);
        assertThat(ourDist).isEqualTo(2);
        // Find closest enemy pawn at or above our King on a given file
        int theirDist = calcPawnKingRankDelta(enemyPawns, adjacentMinFile, US, kingSq);
        assertThat(theirDist).isEqualTo(7);

        ourDist = calcPawnKingRankDelta(myPawns, fileOf(kingSq), US, kingSq);
        assertThat(ourDist).isEqualTo(3);
        // Find closest enemy pawn at or above our King on a given file
        theirDist = calcPawnKingRankDelta(enemyPawns, fileOf(kingSq), US, kingSq);
        assertThat(theirDist).isEqualTo(6);

        ourDist = calcPawnKingRankDelta(myPawns, adjacentMaxFile, US, kingSq);
        assertThat(ourDist).isEqualTo(7);
        // Find closest enemy pawn at or above our King on a given file
        theirDist = calcPawnKingRankDelta(enemyPawns, adjacentMaxFile, US, kingSq);
        assertThat(theirDist).isEqualTo(7);

        // eval for black:

        US = nBlack;
        THEM = Color.invert(nBlack);
        kingSq = Long.numberOfTrailingZeros(bb.getKings(US));

        myPawns = bb.getPawns(US);
        enemyPawns = bb.getPawns(THEM);
        adjacentMinFile = calcMinAdjFile(kingSq);
        adjacentMaxFile = calcMaxAdjFile(kingSq);

        assertThat(fileOf(kingSq)).isEqualTo(3);
        assertThat(adjacentMinFile).isEqualTo(2);
        assertThat(adjacentMaxFile).isEqualTo(4);

        // Find closest friendly pawn at or above our King on a given file
        ourDist = calcPawnKingRankDelta(myPawns, adjacentMinFile, US, kingSq);
        assertThat(ourDist).isEqualTo(7);
        // Find closest enemy pawn at or above our King on a given file
        theirDist = calcPawnKingRankDelta(enemyPawns, adjacentMinFile, US, kingSq);
        assertThat(theirDist).isEqualTo(7);

        ourDist = calcPawnKingRankDelta(myPawns, fileOf(kingSq), US, kingSq);
        assertThat(ourDist).isEqualTo(2);
        // Find closest enemy pawn at or above our King on a given file
        theirDist = calcPawnKingRankDelta(enemyPawns, fileOf(kingSq), US, kingSq);
        assertThat(theirDist).isEqualTo(7);

        ourDist = calcPawnKingRankDelta(myPawns, adjacentMaxFile, US, kingSq);
        assertThat(ourDist).isEqualTo(2);
        // Find closest enemy pawn at or above our King on a given file
        theirDist = calcPawnKingRankDelta(enemyPawns, adjacentMaxFile, US, kingSq);
        assertThat(theirDist).isEqualTo(5);

    }

}