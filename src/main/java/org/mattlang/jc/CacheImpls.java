package org.mattlang.jc;

import java.util.function.Supplier;

import org.mattlang.jc.engine.tt.TTCache;
import org.mattlang.jc.engine.tt.TTCache3;
import org.mattlang.jc.engine.tt.TTCacheInterface;

public enum CacheImpls {

    OLD_STANDARD {
        @Override
        public Supplier<TTCacheInterface> createSupplier() {
            return TTCache::new;
        }
    },

    STANDARD {
        @Override
        public Supplier<TTCacheInterface> createSupplier() {
            return TTCache3::new;
        }
    };

    public abstract Supplier<TTCacheInterface> createSupplier();
}
