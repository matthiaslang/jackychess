package org.mattlang.jc;

import java.util.function.Supplier;

import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.moves.MoveListImpl;
import org.mattlang.jc.moves.StagedMoveListImpl;

public enum MoveListImpls {

    OPTIMIZED {
        @Override
        public Supplier<MoveList> createSupplier() {
            return MoveListImpl::new;
        }
    },

    STAGED {
        @Override
        public Supplier<MoveList> createSupplier() {
            return StagedMoveListImpl::new;
        }
    };

    public abstract Supplier<MoveList> createSupplier();
}
