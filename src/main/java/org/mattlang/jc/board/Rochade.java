package org.mattlang.jc.board;

import java.util.Objects;

public class Rochade {

    public static enum RochadeType{
        LONG, SHORT
    }

    private boolean hAllowed = true;

    private boolean aAllowed = true;

    public Rochade(boolean hAllowed, boolean aAllowed) {
        this.hAllowed = hAllowed;
        this.aAllowed = aAllowed;
    }

    public Rochade() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Rochade rochade = (Rochade) o;
        return hAllowed == rochade.hAllowed &&
                aAllowed == rochade.aAllowed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hAllowed, aAllowed);
    }

    public Rochade copy() {
        return new Rochade(hAllowed, aAllowed);
    }

    public boolean isAllowed(RochadeType type) {
        return type == RochadeType.LONG ? aAllowed : hAllowed;
    }

    public boolean ishAllowed() {
        return hAllowed;
    }

    public Rochade sethAllowed(boolean hAllowed) {
        this.hAllowed = hAllowed;
        return this;
    }

    public boolean isaAllowed() {
        return aAllowed;
    }

    public Rochade setaAllowed(boolean aAllowed) {
        this.aAllowed = aAllowed;
        return this;
    }
}
