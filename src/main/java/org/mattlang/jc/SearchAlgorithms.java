package org.mattlang.jc;

public enum SearchAlgorithms {

    MTDF {
        @Override
        public SearchParameter createSearchParameter() {
            return Factory.createIterativeDeepeningMtdf();
        }
    },
    STABLE {
        @Override
        public SearchParameter createSearchParameter() {
            return Factory.createStable();
        }
    };

    public abstract SearchParameter createSearchParameter();
}
