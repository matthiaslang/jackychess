package org.mattlang.jc;

public enum SearchAlgorithms {

    STABLE {
        @Override
        public SearchParameter createSearchParameter() {
            return Factory.createStable();
        }
    },
    BITBOARD {
        @Override
        public SearchParameter createSearchParameter() {
            return Factory.createBitboard();
        }
    };

    public abstract SearchParameter createSearchParameter();
}
