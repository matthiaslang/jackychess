package org.mattlang.jc;

import java.util.function.Supplier;

import org.mattlang.jc.engine.tt.*;

public enum CacheImpls {

    STANDARD {
        @Override
        public Supplier<TTCacheInterface> createSupplier() {
            return TTCache::new;
        }
    },

    V2 {
        @Override
        public Supplier<TTCacheInterface> createSupplier() {
            return TTCache2::new;
        }
    },

    V3 {
        @Override
        public Supplier<TTCacheInterface> createSupplier() {
            return TTCache3::new;
        }
    },

    V4 {
        @Override
        public Supplier<TTCacheInterface> createSupplier() {
            return TTCache4::new;
        }
    },

    V5 {
        @Override
        public Supplier<TTCacheInterface> createSupplier() {
            return TTCache5::new;
        }
    },
    BUCKETS {
        @Override
        public Supplier<TTCacheInterface> createSupplier() {
            return TTBucketCache::new;
        }
    };

    public abstract Supplier<TTCacheInterface> createSupplier();
}
