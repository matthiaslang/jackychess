package org.mattlang.jc;

public enum SearchAlgorithms {

    ALPHA_BETA {
        @Override
        public SearchParameter createSearchParameter() {
            return Factory.createIterativeDeepeningAlphaBeta();
        }
    },
    MTDF {
        @Override
        public SearchParameter createSearchParameter() {
            return Factory.createIterativeDeepeningMtdf();
        }
    },
    ALPHA_BETA_PVS {
        @Override
        public SearchParameter createSearchParameter() {
            return Factory.createIterativeDeepeningPVS();
        }
    };

    public abstract SearchParameter createSearchParameter();
}
