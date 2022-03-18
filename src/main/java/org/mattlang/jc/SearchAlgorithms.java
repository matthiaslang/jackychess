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
    },
    STAGED_MOVE_GEN {
        @Override
        public SearchParameter createSearchParameter() {
            return Factory.createStagedMoveGen();
        }
    };

    public abstract SearchParameter createSearchParameter();
}
