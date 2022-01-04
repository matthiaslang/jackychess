package org.mattlang.jc;

import org.mattlang.jc.uci.*;

import lombok.Getter;

public class ConfigValues {

    @Getter
    private UCIOptions allOptions = new UCIOptions();

    public final UCIGroup common = allOptions.createGroup("Common", "Common parameter");
    public final UCIGroup experimental =
            allOptions.createGroup("Experimental", "Experimental parameter used during development");

    public final UCIGroup limits =
            allOptions.createGroup("Limits", "Parameter which limit the search or search time in some way.");

    public final UCISpinOption timeout = new UCITimeoutOption(allOptions, limits);

    public final UCISpinOption maxDepth = limits.createSpinOpt("maxdepth",
            "the maximum search depth to use if there is enough search time",
            3, 50, 20);

    public final UCISpinOption maxQuiescence =
            limits.createSpinOpt("quiescence",
                    "the maximum search depth in quiescence",
                    0, 50, 10);

    public final UCIGroup caching =
            allOptions.createGroup("Caching", "Parameter for caching of information during search.");

    public final UCICheckOption useTTCache = caching.createCheckOpt("useTTCache",
            "Flag, if the tt cache to store scores should be activated",
            true);

    public final UCIGroup search = allOptions.createGroup("Search", "Parameter that influence search.");

    public final UCIComboOption<SearchAlgorithms> searchAlgorithm =
            search.createComboOpt("searchalg",
                    "the search algorithm to use. Only for development testing.",
                    SearchAlgorithms.class, SearchAlgorithms.STABLE);

    public final UCICheckOption activatePvsSearch = search.createCheckOpt("activatePvsSearch",
            "should principal variation search be used",
            true);

    public final UCIComboOption<EvalFunctions> evluateFunctions =
            search.createComboOpt("evaluateFunction",
                    "the evaluation function to use. Only for development testing",
                    EvalFunctions.class, EvalFunctions.MINIMAL_PST);

    public final UCIComboOption<EvalParameterSet> evaluateParamSet =
            search.createComboOpt("evalParamSet",
                    "the evaluation parameter set used when evaluateFunction is set to Parameterized. Only for development testing",
                    EvalParameterSet.class, EvalParameterSet.DEFAULT);

    public final UCIComboOption<MoveListImpls> moveListImpls =
            experimental.createComboOpt("MoveListImpl",
                    "internally. Only for development testing",
                    MoveListImpls.class, MoveListImpls.OPTIMIZED);

    public final UCIComboOption<CacheImpls> cacheImpls =
            experimental.createComboOpt("TTCacheImpl",
                    "internally. Only for development testing",
                    CacheImpls.class, CacheImpls.STANDARD);

    public final UCIGroup moveOrder =
            allOptions.createGroup("Move Order", "Parameter influencing the move order in alpha beta search");

    public final UCICheckOption useHistoryHeuristic =
            moveOrder.createCheckOpt("useHistoryHeuristic",
                    "should history heuristic be used for move ordering",
                    true);
    public final UCICheckOption useKillerMoves = moveOrder.createCheckOpt("useKillerMoves",
            "should killer moves heuristic be used for move ordering", true);
    public final UCICheckOption useMvvLvaSorting = moveOrder.createCheckOpt("useMvvLvaSorting",
            "should mvv lva sorting be used for move ordering",
            true);
    public final UCICheckOption usePvSorting = moveOrder.createCheckOpt("usePvSorting",
            "should principal variation information used for move ordering",
            true);

    public final UCIGroup pruning =
            allOptions.createGroup("Pruning", "Parameter influencing the pruning during alpha beta search");

    public final UCICheckOption aspiration = pruning.createCheckOpt("aspiration",
            "should aspiration windows be used during iterative deepening",
            true);
    public final UCICheckOption useNullMoves = pruning.createCheckOpt("useNullMoves",
            "should null move pruning be used during search",
            false);

    public final UCICheckOption staticNullMove = pruning.createCheckOpt("staticNullMove",
            "should static null move pruning be used during search",
            false);

    public final UCICheckOption razoring = pruning.createCheckOpt("razoring",
            "should razoring be used during search",
            false);


    public final UCICheckOption deltaCutoff = pruning.createCheckOpt("deltaCutoff",
            "should delta cutoff be used during quiescence search",
            false);

    public final UCICheckOption useLateMoveReductions =
            pruning.createCheckOpt("useLateMoveReductions",
                    "should late move reductions be used during search",
                    false);

    public final UCIGroup extensions =
            allOptions.createGroup("Extensions", "Parameter influencing the extension of the search tree");

    public final UCICheckOption chessExtension =
            extensions.createCheckOpt("useCheckExtension",
                    "on check, extend the search depth",
                    false);
}
