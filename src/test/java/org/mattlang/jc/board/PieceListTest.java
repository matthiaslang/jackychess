package org.mattlang.jc.board;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.mattlang.attic.board.PieceList;

public class PieceListTest {

    @Test
    public void insertAndRemove() {
        PieceList pieceList = new PieceList();
        pieceList.set(7, FigureConstants.FT_PAWN);
        pieceList.set(10, FigureConstants.FT_PAWN);
        pieceList.set(15, FigureConstants.FT_PAWN);

        assertThat(pieceList.getPawns().getArr()).containsExactly(7, 10, 15, -1, -1, -1, -1, -1);

        pieceList.remove(7, FigureConstants.FT_PAWN);
        assertThat(pieceList.getPawns().getArr()).containsExactly(10, 15, -1, -1, -1, -1, -1, -1);


    }
}