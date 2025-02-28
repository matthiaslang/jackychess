package org.mattlang.tuning;

import lombok.Getter;
import lombok.Setter;

public abstract class AbstractTuningParameter implements TuningParameter {

    @Getter
    /** number of adjustments during tuning. */
    private int adjCounter = 0;

    @Getter
    @Setter
    private int paramNo;

    @Override
    public void incAdjCounter() {
        adjCounter++;
    }
}
