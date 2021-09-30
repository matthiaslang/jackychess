package org.mattlang.jc;

import java.util.function.Supplier;

import org.mattlang.jc.engine.tt.TTBucketCache;
import org.mattlang.jc.engine.tt.TTCache;
import org.mattlang.jc.engine.tt.TTCache2;
import org.mattlang.jc.engine.tt.TTCacheInterface;

public enum CacheImpls {

    STANDARD {
        @Override
        public Supplier<TTCacheInterface> createSupplier() {
            return () -> new TTCache();
        }
    },

    V2 {
        @Override
        public Supplier<TTCacheInterface> createSupplier() {
            return () -> new TTCache2();
        }
    },

    BUCKETS {
        @Override
        public Supplier<TTCacheInterface> createSupplier() {
            return () -> new TTBucketCache();
        }
    };

    public abstract Supplier<TTCacheInterface> createSupplier();
}
