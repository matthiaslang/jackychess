package org.mattlang.jc;

import java.util.LinkedHashMap;
import java.util.Map;

import org.mattlang.jc.uci.*;

public class ConfigValues {

    private Map<String, UCIOption> allOptions = new LinkedHashMap<>();

    public final UCISpinOption timeout = new UCITimeoutOption(allOptions);
    public final UCISpinOption maxQuiescence = new UCISpinOption(allOptions, "quiescence", 0, 10, 10);
    public final UCISpinOption maxDepth = new UCISpinOption(allOptions, "maxdepth", 3, 50, 20);

    public final UCICheckOption activatePvsSearch = new UCICheckOption(allOptions, "activatePvsSearch", true);
    public final UCICheckOption useTTCache = new UCICheckOption(allOptions, "useTTCache", true);

    public final UCICheckOption aspiration = new UCICheckOption(allOptions, "aspiration", true);

    public final UCIComboOption<SearchAlgorithms> searchAlgorithm =
            new UCIComboOption(allOptions, "searchalg", SearchAlgorithms.class, SearchAlgorithms.STABLE);

    public final UCIComboOption<EvalFunctions> evluateFunctions =
            new UCIComboOption(allOptions, "evaluateFunction", EvalFunctions.class, EvalFunctions.MINIMAL_PST);

    public final UCIComboOption<MoveListImpls> moveListImpls =
            new UCIComboOption(allOptions, "MoveListImpl", MoveListImpls.class, MoveListImpls.OPTIMIZED);


    public final UCICheckOption useHistoryHeuristic = new UCICheckOption(allOptions, "useHistoryHeuristic", true);
    public final UCICheckOption useKillerMoves = new UCICheckOption(allOptions, "useKillerMoves", true);
    public final UCICheckOption useMvvLvaSorting = new UCICheckOption(allOptions, "useMvvLvaSorting", true);
    public final UCICheckOption usePvSorting = new UCICheckOption(allOptions, "usePvSorting", true);

    public final UCICheckOption useNullMoves = new UCICheckOption(allOptions, "useNullMoves", false);
    public final UCICheckOption useLateMoveReductions = new UCICheckOption(allOptions, "useLateMoveReductions", true);

    public Map<String, UCIOption> getAllOptions() {
        return allOptions;
    }
}
