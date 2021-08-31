package org.mattlang.jc;

import org.mattlang.jc.uci.*;

import lombok.Getter;

public class ConfigValues {

    @Getter
    private UCIOptions allOptions = new UCIOptions();

    public final UCIGroup common = new UCIGroup("Common");

    public final UCIGroup limits = new UCIGroup("Limits");

    public final UCISpinOption timeout = new UCITimeoutOption(allOptions, limits);
    public final UCISpinOption maxDepth = new UCISpinOption(allOptions, limits, "maxdepth", 3, 50, 20);
    public final UCISpinOption maxQuiescence = new UCISpinOption(allOptions, limits, "quiescence", 0, 10, 10);


    public final UCIGroup caching = new UCIGroup("Caching");
    public final UCICheckOption useTTCache = new UCICheckOption(allOptions, caching, "useTTCache", true);

    public final UCIGroup search = new UCIGroup("Search");

    public final UCIComboOption<SearchAlgorithms> searchAlgorithm =
            new UCIComboOption(allOptions, search, "searchalg", SearchAlgorithms.class, SearchAlgorithms.STABLE);
    public final UCICheckOption activatePvsSearch = new UCICheckOption(allOptions, search, "activatePvsSearch", true);

    public final UCIComboOption<EvalFunctions> evluateFunctions =
            new UCIComboOption(allOptions, search, "evaluateFunction", EvalFunctions.class, EvalFunctions.MINIMAL_PST);


    public final UCIComboOption<MoveListImpls> moveListImpls =
            new UCIComboOption(allOptions, common, "MoveListImpl", MoveListImpls.class, MoveListImpls.OPTIMIZED);

    public final UCIGroup moveOrder = new UCIGroup("Move Order");

    public final UCICheckOption useHistoryHeuristic =
            new UCICheckOption(allOptions, moveOrder, "useHistoryHeuristic", true);
    public final UCICheckOption useKillerMoves = new UCICheckOption(allOptions, moveOrder, "useKillerMoves", true);
    public final UCICheckOption useMvvLvaSorting = new UCICheckOption(allOptions, moveOrder, "useMvvLvaSorting", true);
    public final UCICheckOption usePvSorting = new UCICheckOption(allOptions, moveOrder, "usePvSorting", true);


    public final UCIGroup pruning = new UCIGroup("Pruning");

    public final UCICheckOption aspiration = new UCICheckOption(allOptions, pruning, "aspiration", true);
    public final UCICheckOption useNullMoves = new UCICheckOption(allOptions, pruning, "useNullMoves", false);
    public final UCICheckOption useLateMoveReductions =
            new UCICheckOption(allOptions, pruning, "useLateMoveReductions", false);

}
