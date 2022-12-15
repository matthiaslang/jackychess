package org.mattlang.jc.engine.sorting;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mattlang.jc.moves.TestTools.getAllMoves;

import java.util.List;

import org.junit.Test;
import org.mattlang.jc.moves.MoveListImpl;
import org.mattlang.jc.moves.Tuple;

public class MovePickerTest {

    @Test
    public void testEagerSorting(){

        MovePicker picker=new MovePicker();

        MoveListImpl movelist=new MoveListImpl();
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

        MoveListImpl movelist=new MoveListImpl();
        movelist.addMoveWithOrder(500, 500);
        movelist.addMoveWithOrder(200, 200);
        movelist.addMoveWithOrder(100, 100);
        movelist.addMoveWithOrder(80, 80);
        movelist.addMoveWithOrder(70, 70);

        picker.init(movelist, 3, 2);
        List<Tuple> moves = getAllMoves(picker);
        assertThat(moves).extracting("moveInt").containsExactly(70, 80);

    }


    @Test
    public void testEagerSorting3(){

        MovePicker picker=new MovePicker();

        MoveListImpl movelist=new MoveListImpl();
        movelist.addMoveWithOrder(1, 1);
        movelist.addMoveWithOrder(200, 200);
        movelist.addMoveWithOrder(100, 100);
        movelist.addMoveWithOrder(80, 80);
        movelist.addMoveWithOrder(70, 70);

        picker.init(movelist, 1, 2);
        List<Tuple> moves = getAllMoves(picker);
        assertThat(moves).extracting("moveInt").containsExactly(70, 80);


        System.out.println(movelist.extractList());
    }

    @Test
    public void emptyPickerHasNoNext() {
        MovePicker picker = new MovePicker();

        MoveListImpl movelist = new MoveListImpl();
        picker.init(movelist, 0, 0);

        assertThat(picker.hasNext()).isFalse();
    }
}