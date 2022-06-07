package org.mattlang.jc.play;

public enum EndStatus {
    /**
     * game ended with mate.
     */
    MATT(true),
    /**
     * game ended with patt.
     */
    PATT(false),
    /**
     * draw by 3 repetitions rule.
     */
    DRAW_BY_3_REPETITIONS(false),

    /**
     * draw by 50 moves rule.
     */
    DRAW_BY_50_RULE(false),

    /**
     * the player stopped because of too much moves without any official end.
     */
    DRAW_BY_TOO_MUCH_MOVES(false),

    /**
     * the player stopped because of a weird internal state happened.
     */
    WEIRD_STATE(false);

    private boolean win;

    EndStatus(boolean win) {
        this.win = win;
    }

    public boolean isDraw() {
        return !win;
    }
}
