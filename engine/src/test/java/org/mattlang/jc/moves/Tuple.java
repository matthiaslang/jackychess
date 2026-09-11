package org.mattlang.jc.moves;

import lombok.Data;

@Data
public class Tuple implements Comparable<Tuple>{

    final String move;

    final String orderStr;

    final int order;

    @Override
    public String toString() {
        return "(" +
                move  +
                ", " + orderStr +
                ", " + order +
                ')';
    }

    @Override
    public int compareTo(Tuple o) {
        return o.order - order;
    }
}
