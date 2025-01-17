package org.mattlang.jc;

import static org.mattlang.jc.Constants.MAX_PLY;
import static org.mattlang.jc.Constants.MAX_THREADS;

import org.mattlang.jc.uci.*;

import lombok.Getter;

public class ConfigValues {

    @Getter
    private final UCIOptions allOptions = new UCIOptions();

    public final UCIGroup common = allOptions.createGroup("Common", "Common parameter");
    public final UCIGroup variants = allOptions.createGroup("Variants", "Game Variant parameter");

    public final UCIGroup internal =
            allOptions.createInternalGroup("Internal", "Internal Test Parameter for Development");

    public final UCIGroup limits =
            allOptions.createGroup("Limits", "Parameter which limit the search or search time in some way.");

    public final UCISpinOption timeout = new UCITimeoutOption(allOptions, limits);

    public final UCISpinOption maxDepth = limits.createSpinOpt("maxdepth",
            "the maximum search depth to use if there is enough search time",
            3, MAX_PLY - 1, MAX_PLY - 1);

    public final UCISpinOption maxQuiescence =
            limits.createSpinOpt("quiescence",
                    "the maximum search depth in quiescence",
                    0, MAX_PLY - 1, MAX_PLY - 1);

    public final UCISpinOption maxThreads = limits.createSpinOpt("maxThreads",
            "the maximum search threads when multi threading search is activated",
            1, MAX_THREADS, 1);

    /**
     * to inform the gui that we support chess960. There is no other uci support from our side necessary, since the
     * board representation and fen parser itself is "compatible" with frc by itself.
     */
    public final UCICheckOption uciChess960 = variants.createCheckOpt("UCI_Chess960",
            "indicates support for Chess960",
            false);

    public final UCIGroup caching =
            allOptions.createGroup("Caching", "Parameter for caching of information during search.");

    public final UCISpinOption hash = caching.createSpinOpt("Hash",
            "TT Hash Size in MB",
            1, 2048, 128);

    public final UCICheckOption useTTCache = internal.createCheckOpt("useTTCache",
            "Flag, if the tt cache to store scores should be activated",
            true);

    public final UCIGroup search = allOptions.createInternalGroup("Search", "Parameter that influence search.");

    public final UCIComboOption<SearchAlgorithms> searchAlgorithm =
            limits.createComboOpt("searchalg",
                    "the search algorithm to use.",
                    SearchAlgorithms.class, SearchAlgorithms.MULTITHREAD);

    public final UCICheckOption activatePvsSearch = search.createCheckOpt("activatePvsSearch",
            "should principal variation search be used",
            true);

    public final UCIGroup moveOrder =
            allOptions.createInternalGroup("Move Order", "Parameter influencing the move order in alpha beta search");

    public final UCICheckOption useHistoryHeuristic =
            moveOrder.createCheckOpt("useHistoryHeuristic",
                    "should history heuristic be used for move ordering",
                    true);
    public final UCICheckOption useKillerMoves = moveOrder.createCheckOpt("useKillerMoves",
            "should killer moves heuristic be used for move ordering", true);
    public final UCICheckOption useCounterMoves = moveOrder.createCheckOpt("useCounterMoves",
            "should counter moves heuristic be used for move ordering", true);
    public final UCICheckOption useMvvLvaSorting = moveOrder.createCheckOpt("useMvvLvaSorting",
            "should mvv lva sorting be used for move ordering",
            true);

    public final UCIGroup pruning =
            allOptions.createInternalGroup("Pruning", "Parameter influencing the pruning during alpha beta search");
    public final UCICheckOption mateDistancePruning = internal.createCheckOpt("mateDistancePruning",
            "should mate distance pruning be activated?",
            true);

    public final UCICheckOption internalIterativeDeepening = internal.createCheckOpt("iid",
            "should internal iterative deepening be activated?",
            true);

    public final UCICheckOption aspiration = pruning.createCheckOpt("aspiration",
            "should aspiration windows be used during iterative deepening",
            true);
    public final UCICheckOption useNullMoves = pruning.createCheckOpt("useNullMoves",
            "should null move pruning be used during search",
            true);

    public final UCICheckOption staticNullMove = pruning.createCheckOpt("staticNullMove",
            "should static null move pruning be used during search",
            true);

    public final UCICheckOption razoring = pruning.createCheckOpt("razoring",
            "should razoring be used during search",
            true);

    public final UCICheckOption futilityPruning = pruning.createCheckOpt("futilityPruning",
            "should futility pruning be used during search",
            true);

    public final UCICheckOption deltaCutoff = pruning.createCheckOpt("deltaCutoff",
            "should delta cutoff be used during quiescence search",
            true);

    public final UCICheckOption useLateMoveReductions =
            pruning.createCheckOpt("useLateMoveReductions",
                    "should late move reductions be used during search",
                    true);

    public final UCIGroup extensions =
            allOptions.createInternalGroup("Extensions", "Parameter influencing the extension of the search tree");

    public final UCICheckOption chessExtension =
            extensions.createCheckOpt("useCheckExtension",
                    "on check, extend the search depth",
                    false);
}
