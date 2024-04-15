package org.mattlang.jc.moves;

import lombok.Data;

@Data
public class Tuple {

    final String move;

    final int moveInt;

    final int order;

    @Override
    public String toString() {
        return "(" +
                move  +
                ", " + moveInt +
                ", " + order +
                ')';
    }
}
