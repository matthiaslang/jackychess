package org.mattlang.jc;

public enum SearchAlgorithms {

    SINGLETHREAD {
        @Override
        public SearchParameter createSearchParameter() {
            return Factory.createStable();
        }
    },
    MULTITHREAD {
        @Override
        public SearchParameter createSearchParameter() {
            return Factory.createMultiThread();
        }
    };

    public abstract SearchParameter createSearchParameter();
}
