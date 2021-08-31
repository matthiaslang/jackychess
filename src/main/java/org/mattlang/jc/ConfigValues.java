package org.mattlang.jc;

import org.mattlang.jc.uci.*;

import lombok.Getter;

public class ConfigValues {

    @Getter
    private UCIOptions allOptions = new UCIOptions();

    public final UCIGroup common = new UCIGroup("Common");

    public final UCISpinOption timeout = new UCITimeoutOption(allOptions, common);
    public final UCISpinOption maxQuiescence = new UCISpinOption(allOptions, common, "quiescence", 0, 10, 10);
    public final UCISpinOption maxDepth = new UCISpinOption(allOptions, common, "maxdepth", 3, 50, 20);

    public final UCICheckOption activatePvsSearch = new UCICheckOption(allOptions, common, "activatePvsSearch", true);
    public final UCICheckOption useTTCache = new UCICheckOption(allOptions, common, "useTTCache", true);

    public final UCICheckOption aspiration = new UCICheckOption(allOptions, common, "aspiration", true);

    public final UCIComboOption<SearchAlgorithms> searchAlgorithm =
            new UCIComboOption(allOptions, common, "searchalg", SearchAlgorithms.class, SearchAlgorithms.STABLE);

    public final UCIComboOption<EvalFunctions> evluateFunctions =
            new UCIComboOption(allOptions, common, "evaluateFunction", EvalFunctions.class, EvalFunctions.MINIMAL_PST);

    public final UCIComboOption<MoveListImpls> moveListImpls =
            new UCIComboOption(allOptions, common, "MoveListImpl", MoveListImpls.class, MoveListImpls.OPTIMIZED);

    public final UCIGroup moveOrder = new UCIGroup("Move Order");

    public final UCICheckOption useHistoryHeuristic =
            new UCICheckOption(allOptions, moveOrder, "useHistoryHeuristic", true);
    public final UCICheckOption useKillerMoves = new UCICheckOption(allOptions, moveOrder, "useKillerMoves", true);
    public final UCICheckOption useMvvLvaSorting = new UCICheckOption(allOptions, moveOrder, "useMvvLvaSorting", true);
    public final UCICheckOption usePvSorting = new UCICheckOption(allOptions, moveOrder, "usePvSorting", true);

    public final UCICheckOption useNullMoves = new UCICheckOption(allOptions, common, "useNullMoves", false);
    public final UCICheckOption useLateMoveReductions =
            new UCICheckOption(allOptions, common, "useLateMoveReductions", false);

}
