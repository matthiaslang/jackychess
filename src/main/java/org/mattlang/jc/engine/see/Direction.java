package org.mattlang.jc.engine.see;

/**
 * Move Directions on the board.
 */
public enum Direction {
    S(0, -1),
    W(-1, 0),
    E(1, 0),
    N(0, 1),
    SE(1, -1),
    SW(-1, -1),
    NE(1, 1),
    NW(-1, 1),

    N_NW(N, NW),
    N_NE(N, NE),
    E_NE(E, NE),
    E_SE(E, SE),
    S_SW(S, SW),
    S_SE(S, SE),
    W_NW(W, NW),
    W_SW(W, SW);

    private final int x;
    private final int y;

    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    Direction(Direction d1, Direction d2) {
        this.x = d1.x + d2.x;
        this.y = d1.y + d2.y;
    }
}
