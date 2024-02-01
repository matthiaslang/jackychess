package org.mattlang.jc.engine.sorting;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mattlang.jc.board.FigureConstants.FT_PAWN;
import static org.mattlang.jc.moves.TestTools.getAllMoves;

import java.util.List;

import org.junit.Test;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.moves.MoveImpl;
import org.mattlang.jc.moves.Tuple;

public class MovePickerTest {

    @Test
    public void testEagerSorting(){

        MovePicker picker=new MovePicker();

        MoveList movelist=new MoveList();
        movelist.addMoveWithOrder(500, 500);
        movelist.addMoveWithOrder(200, 200);
        movelist.addMoveWithOrder(100, 100);
        movelist.addMoveWithOrder(70, 70);
        movelist.addMoveWithOrder(80, 80);
        
        picker.init(movelist, 0, 3);
        List<Tuple> moves = getAllMoves(picker);

        assertThat(moves).extracting("moveInt").containsExactly(70, 80, 100);

    }


    @Test
    public void testEagerSorting2(){

        MovePicker picker=new MovePicker();

        MoveList movelist=new MoveList();
        movelist.addMoveWithOrder(500, 3);
        movelist.addMoveWithOrder(200, 45);
        movelist.addMoveWithOrder(100, 50);
        movelist.addMoveWithOrder(80, 80);
        movelist.addMoveWithOrder(70, 70);

        picker.init(movelist, 3, 2);
        List<Tuple> moves = getAllMoves(picker);
        assertThat(moves).extracting("moveInt").containsExactly(70, 80);

    }


    @Test
    public void testEagerSorting3(){

        MovePicker picker=new MovePicker();

        MoveList movelist=new MoveList();
        int m1 = MoveImpl.createNormalMove(FT_PAWN, 0, 0, (byte) 0);
        int m2 = MoveImpl.createNormalMove(FT_PAWN, 1, 4, (byte) 0);
        int m3 = MoveImpl.createNormalMove(FT_PAWN, 4, 5, (byte) 0);
        int m4 = MoveImpl.createNormalMove(FT_PAWN, 5, 6, (byte) 0);
        int m5 = MoveImpl.createNormalMove(FT_PAWN, 6, 7, (byte) 0);
        movelist.addMoveWithOrder(m1, 1);
        movelist.addMoveWithOrder(m2, 200);
        movelist.addMoveWithOrder(m3, 100);
        movelist.addMoveWithOrder(m4, 80);
        movelist.addMoveWithOrder(m5, 70);

        picker.init(movelist, 1, 2);
        List<Tuple> moves = getAllMoves(picker);
        assertThat(moves).extracting("moveInt").containsExactly(m5, m4);


        System.out.println(movelist.extractList());
    }

    @Test
    public void emptyPickerHasNoNext() {
        MovePicker picker = new MovePicker();

        MoveList movelist = new MoveList();
        picker.init(movelist, 0, 0);

        assertThat(picker.hasNext()).isFalse();
    }
}