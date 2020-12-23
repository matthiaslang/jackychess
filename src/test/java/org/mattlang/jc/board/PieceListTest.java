package org.mattlang.jc.board;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PieceListTest {

    @Test
    public void insertAndRemove() {
        PieceList pieceList = new PieceList();
        pieceList.set(7, FigureConstants.FT_PAWN);
        pieceList.set(10, FigureConstants.FT_PAWN);
        pieceList.set(15, FigureConstants.FT_PAWN);

        assertThat(pieceList.getPawns().arr).containsExactly(7, 10, 15, -1, -1, -1, -1, -1);

        pieceList.remove(7, FigureConstants.FT_PAWN);
        assertThat(pieceList.getPawns().arr).containsExactly(10, 15, -1, -1, -1, -1, -1, -1);


    }
}