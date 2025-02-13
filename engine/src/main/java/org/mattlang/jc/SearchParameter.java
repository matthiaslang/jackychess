package org.mattlang.jc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.mattlang.jc.engine.Configurator;
import org.mattlang.jc.engine.IterativeDeepeningSearch;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.search.IterativeDeepeningPVS;
import org.mattlang.jc.engine.search.MultiThreadedIterativeDeepening;
import org.mattlang.jc.uci.GoParameter;
import org.mattlang.jc.uci.TimeCalc;
import org.mattlang.jc.uci.UCIGroup;
import org.mattlang.jc.uci.UCIOption;

import lombok.Getter;

/**
 * Parameter of one search of a best move.
 */
public class SearchParameter {

    private static final Logger LOGGER = Logger.getLogger(SearchParameter.class.getSimpleName());
    public static final int DEFAULT_SEARCHTIME = 15000;

    @Getter
    private int timeout = DEFAULT_SEARCHTIME;

    @Getter
    private int depth = ConfigValues.getConfigValues().maxDepth.getValue();

    @Getter
    private int nodes;

    public final Impl<IterativeDeepeningSearch> searchMethod = new Impl<>(this, IterativeDeepeningPVS::new);

    /**
     * an optional list with legal moves  when coming from async engine. If searchmoves is set, it contains only the
     * legal search moves.
     * It may be null in test cases.
     */
    @Getter
    private MoveList legalMovesToSearch;

    public SearchParameter(int timeout, int depth) {
        this.timeout = timeout;
        this.depth = depth;
    }

    public SearchParameter(int timeout, int depth, MoveList legalMovesToSearch) {
        this.timeout = timeout;
        this.depth = depth;
        this.legalMovesToSearch = legalMovesToSearch;
    }

    public SearchParameter(int timeout, MoveList legalMovesToSearch, GoParameter goParams) {
        this.timeout = timeout;
        this.legalMovesToSearch = legalMovesToSearch;
        if (goParams.depth > 0) {
            this.depth = goParams.depth;
            this.timeout = TimeCalc.INFINITE_TIMEOUT;
        }
        if (goParams.nodes > 0) {
            this.nodes = goParams.nodes;
            this.timeout = TimeCalc.INFINITE_TIMEOUT;
        }
    }

    public SearchParameter(int timeout) {
        this.timeout = timeout;
    }

    public SearchParameter() {
    }

    public static SearchParameter params(int timeout) {
        return new SearchParameter(timeout);
    }

    public static SearchParameter params(int timeout, int depth) {
        return new SearchParameter(timeout, depth);
    }

    public static SearchParameter params(int timeout, int depth, MoveList legalMovesToSearch) {
        return new SearchParameter(timeout, depth, legalMovesToSearch);
    }

    public static SearchParameter createMultiThread(int timeout, MoveList legalMovesToSearch, GoParameter goParams) {
        return new SearchParameter(timeout, legalMovesToSearch, goParams)
                .searchMethod.set(MultiThreadedIterativeDeepening::new);
    }

    public void log() {
        UCILogger.log("Search Method: " + searchMethod.instance().getClass().getSimpleName()
                + " Evaluation: " + Configurator.determineEvalImplName());
        for (Map.Entry<UCIGroup, List<UCIOption>> entry : ConfigValues.getConfigValues()
                .getAllOptions()
                .getOptionsByGroup()
                .entrySet()) {
            UCIGroup group = entry.getKey();
            List<UCIOption> opts = entry.getValue();
            if (group != ConfigValues.getConfigValues().common) {
                StringBuilder b = new StringBuilder();
                b.append(group.getName()).append(": ");
                b.append(opts.stream()
                        .map(o -> o.getName() + ": " + o.getValue())
                        .collect(Collectors.joining("; ")));
                UCILogger.log(b.toString());

            } else {
                for (UCIOption option : opts) {
                    UCILogger.log(option.getName() + ": " + option.getValue());
                }
            }
        }

        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("Board: " + Configurator.determineBoardImplName());
            LOGGER.info("Search Method: " + searchMethod.instance().getClass().getSimpleName());
            LOGGER.info("Evaluation: " + Configurator.determineEvalImplName());
            for (UCIOption option : ConfigValues.getConfigValues().getAllOptions().getAllOptions()) {
                LOGGER.info(option.getName() + ": " + option.getValue());
            }
        }

    }

    public void log(StringBuilder b) {
        b.append("Board: " + Configurator.determineBoardImplName());
        b.append("\n");
        b.append("Search Method: " + searchMethod.instance().getClass().getSimpleName());
        b.append("\n");
        b.append("Evaluation: " + Configurator.determineEvalImplName());
        b.append("\n");
        for (UCIOption option : ConfigValues.getConfigValues().getAllOptions().getAllOptions()) {
            b.append(option.getName() + ": " + option.getValue());
            b.append("\n");
        }

    }

    public Map collectStatistics() {
        HashMap stats = new HashMap();

        return stats;
    }

    public static void printStats(String prefix, Map stats) {
        StringBuilder b = new StringBuilder();
        stats.forEach((key, value) -> {
            if (value instanceof Map) {
                printStats(prefix + "." + key.toString(), (Map) value);
            } else {
                if (b.length() > 0) {
                    b.append(", ");
                }
                b.append(key + ":" + value);
            }
        });
        if (b.length() > 0) {
            System.out.println(prefix + ": " + b.toString());
        }
    }

}
