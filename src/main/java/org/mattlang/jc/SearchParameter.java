package org.mattlang.jc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Logger;

import org.mattlang.jc.board.Board3;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.engine.*;
import org.mattlang.jc.engine.evaluation.BoardStatsGenerator;
import org.mattlang.jc.engine.evaluation.DefaultEvaluateFunction;
import org.mattlang.jc.engine.evaluation.SimpleBoardStatsGenerator;
import org.mattlang.jc.engine.evaluation.StalemateCheckerImpl;
import org.mattlang.jc.engine.search.IterativeDeepeningPVS;
import org.mattlang.jc.movegenerator.*;
import org.mattlang.jc.uci.UCIOption;

public class SearchParameter {

    private static final Logger LOGGER = Logger.getLogger(SearchParameter.class.getSimpleName());

    private List<Impl> impls = new ArrayList<>();

    private ConfigValues config = new ConfigValues();

    public final Impl<MoveList> moveList = new Impl<>(this, BasicMoveList::new);

    public final Impl<BoardRepresentation> boards = new Impl<>(this, Board3::new);

    public final Impl<EvaluateFunction> evaluateFunction = new Impl<>(this, () -> new DefaultEvaluateFunction());

    public final Impl<SearchMethod> searchMethod = new Impl<>(this, IterativeDeepeningPVS::new);

    public final Impl<LegalMoveGenerator> legalMoveGenerator = new Impl<>(this, LegalMoveGeneratorImpl3::new);

    public final Impl<MoveGenerator> moveGenerator = new Impl<>(this, MoveGeneratorImpl2::new);

    public final Impl<BoardStatsGenerator> boardStatsGenerator = new Impl<>(this, SimpleBoardStatsGenerator::new);

    public final Impl<StalemateChecker> stalemateChecker = new Impl<>(this, StalemateCheckerImpl::new);

    public final Impl<CheckChecker> checkChecker = new Impl<>(this, CheckCheckerImpl::new);


    public void log() {
        UCILogger.log("Search Method: " + searchMethod.instance().getClass().getSimpleName());
        UCILogger.log("Evaluation: " + evaluateFunction.instance().getClass().getSimpleName());
        for (UCIOption option : config.getAllOptions().values()) {
            UCILogger.log(option.getName() + ": " + option.getValue());
        }

        LOGGER.info("Search Method: " + searchMethod.instance().getClass().getSimpleName());
        LOGGER.info("Evaluation: " + evaluateFunction.instance().getClass().getSimpleName());
        for (UCIOption option : config.getAllOptions().values()) {
            LOGGER.info(option.getName() + ": " + option.getValue());
        }

    }

    public Map collectStatistics() {
        HashMap stats = new HashMap();
        for (Impl impl : impls) {
            impl.collectStatistics(stats);
        }
        return stats;
    }

    public <T> void register(Impl impl) {
        impls.add(impl);
    }

    public static void printStats(Map stats) {
        printStats("", stats);
    }

    public void printStats() {
        printStats(collectStatistics());
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

    public ConfigValues getConfig() {
        return config;
    }

    public SearchParameter config(Consumer<ConfigValues> cfgChange) {
        cfgChange.accept(config);
        return this;
    }

    public SearchParameter setConfig(ConfigValues config) {
        this.config = config;
        return this;
    }
}
