package org.mattlang.jc;

import java.util.function.Supplier;

import org.mattlang.jc.moves.MoveIterationPreparer;
import org.mattlang.jc.moves.RegularMoveIterationPreparer;
import org.mattlang.jc.moves.StagedMoveIterationPreparer;

public enum MoveIterationImpls {

    NORMAL {
        @Override
        public Supplier<MoveIterationPreparer> createSupplier() {
            return RegularMoveIterationPreparer::new;
        }
    },

    STAGED {
        @Override
        public Supplier<MoveIterationPreparer> createSupplier() {
            return StagedMoveIterationPreparer::new;
        }
    };

    public abstract Supplier<MoveIterationPreparer> createSupplier();
}
