package org.mattlang.jc;

import java.util.function.Supplier;

import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.moves.MoveListPool;

public enum MoveListImpls {

    OPTIMIZED {
        @Override
        public Supplier<MoveList> createSupplier() {
            return MoveListPool.instance::newOne;
        }
    };

    public abstract Supplier<MoveList> createSupplier();
}
