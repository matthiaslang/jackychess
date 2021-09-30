package org.mattlang.jc;

import org.mattlang.jc.uci.*;

import lombok.Getter;

public class ConfigValues {

    @Getter
    private UCIOptions allOptions = new UCIOptions();

    public final UCIGroup common = new UCIGroup("Common", "Common parameter");
    public final UCIGroup experimental = new UCIGroup("Experimental", "Experimental parameter used during development");

    public final UCIGroup limits = new UCIGroup("Limits", "Parameter which limit the search or search time in some way.");

    public final UCISpinOption timeout = new UCITimeoutOption(allOptions, limits);
    public final UCISpinOption maxDepth = new UCISpinOption(allOptions, limits, "maxdepth",
            "the maximum search depth to use if there is enough search time",
            3, 50, 20);
    public final UCISpinOption maxQuiescence =
            new UCISpinOption(allOptions, limits, "quiescence",
                    "the maximum search depth in quiescence",
                    0, 10, 10);

    public final UCIGroup caching = new UCIGroup("Caching", "Parameter for caching of information during search.");
    public final UCICheckOption useTTCache = new UCICheckOption(allOptions, caching, "useTTCache",
            "Flag, if the tt cache to store scores should be activated",
            true);

    public final UCIGroup search = new UCIGroup("Search", "Parameter that influence search.");

    public final UCIComboOption<SearchAlgorithms> searchAlgorithm =
            new UCIComboOption(allOptions, search, "searchalg",
                    "the search algorithm to use. Only for development testing.",
                    SearchAlgorithms.class, SearchAlgorithms.STABLE);
    public final UCICheckOption activatePvsSearch = new UCICheckOption(allOptions, search, "activatePvsSearch",
            "should principal variation search be used",
            true);

    public final UCIComboOption<EvalFunctions> evluateFunctions =
            new UCIComboOption(allOptions, search, "evaluateFunction",
                    "the evaluation function to use. Only for development testing",
                    EvalFunctions.class, EvalFunctions.MINIMAL_PST);

    public final UCIComboOption<MoveListImpls> moveListImpls =
            new UCIComboOption(allOptions, experimental, "MoveListImpl",
                    "internally. Only for development testing",
                    MoveListImpls.class, MoveListImpls.OPTIMIZED);

    public final UCIComboOption<CacheImpls> cacheImpls =
            new UCIComboOption(allOptions, experimental, "TTCacheImpl",
                    "internally. Only for development testing",
                    CacheImpls.class, CacheImpls.STANDARD);


    public final UCIGroup moveOrder = new UCIGroup("Move Order", "Parameter influencing the move order in alpha beta search");

    public final UCICheckOption useHistoryHeuristic =
            new UCICheckOption(allOptions, moveOrder, "useHistoryHeuristic",
                    "should history heuristic be used for move ordering",
                    true);
    public final UCICheckOption useKillerMoves = new UCICheckOption(allOptions, moveOrder, "useKillerMoves",
            "should killer moves heuristic be used for move ordering", true);
    public final UCICheckOption useMvvLvaSorting = new UCICheckOption(allOptions, moveOrder, "useMvvLvaSorting",
            "should mvv lva sorting be used for move ordering",
            true);
    public final UCICheckOption usePvSorting = new UCICheckOption(allOptions, moveOrder, "usePvSorting",
            "should principal variation information used for move ordering",
            true);

    public final UCIGroup pruning = new UCIGroup("Pruning", "Parameter influencing the pruning during alpha beta search");

    public final UCICheckOption aspiration = new UCICheckOption(allOptions, pruning, "aspiration",
            "should aspiration windows be used during iterative deepening",
            true);
    public final UCICheckOption useNullMoves = new UCICheckOption(allOptions, pruning, "useNullMoves",
            "should null move pruning be used during search",
            false);
    public final UCICheckOption useLateMoveReductions =
            new UCICheckOption(allOptions, pruning, "useLateMoveReductions",
                    "should late move reductions be used during search",
                    false);

}
