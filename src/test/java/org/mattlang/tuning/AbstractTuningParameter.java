package org.mattlang.tuning;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public abstract class AbstractTuningParameter implements TuningParameter {

    private List<FenEntry> dependingFens = new ArrayList<>();

    private int depCounter = 0;

    @Getter

    /** number of adjustments during tuning. */
    private int adjCounter=0;

    @Getter
    @Setter
    private int paramNo;

    @Override
    public void addDependingFen(FenEntry fen) {
        depCounter++;
        //        dependingFens.add(fen);
    }

    @Override
    public boolean hasDependingFens() {
        return depCounter > 0;
        //        return !dependingFens.isEmpty();
    }

    @Override
    public int getDependingFenCount() {
        return depCounter;
    }

    @Override
    public void incAdjCounter() {
        adjCounter++;
    }
}
