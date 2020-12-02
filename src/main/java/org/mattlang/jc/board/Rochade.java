package org.mattlang.jc.board;

import java.util.Objects;

public class Rochade {

    private boolean hAllowed = true;

    private boolean aAllowed = true;

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
}
