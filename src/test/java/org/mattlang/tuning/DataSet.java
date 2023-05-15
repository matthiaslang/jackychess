package org.mattlang.tuning;

import lombok.Getter;
import lombok.Setter;
import org.mattlang.jc.tools.MarkdownTable;
import org.mattlang.jc.tools.MarkdownWriter;
import org.mattlang.tuning.data.pgnparser.Ending;
import org.mattlang.tuning.tuner.DatasetPreparer;
import org.mattlang.tuning.tuner.OptParameters;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import static java.lang.Math.pow;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.tuning.tuner.LocalOptimizationTuner.executorService;

/**
 * Contains all FEN Positions used as datas set for tuning.
 */
@Getter
@Setter
public class DataSet {

    private static final Logger LOGGER = Logger.getLogger(DatasetPreparer.class.getSimpleName());

    /**
     * scaling Constant.
     */
//            private static final double K = 1.13;
    /**
     * calculated to 1.09 by pre-scaling. now using this fixed value.
     */
    private static final double K = 1.5800000000000003;


    private List<DataSet> workers = new ArrayList<>();

    private List<FenEntry> fens = new ArrayList<>();

    private TuneableEvaluateFunction evaluate;
    private List<TuningParameter> params;

    private boolean multithreaded = false;

    private double k = K;

    private OptParameters optParameters;

    public DataSet(OptParameters optParameters) {
        this.optParameters = optParameters;
    }

    public double calcError(TuneableEvaluateFunction evaluate, List<TuningParameter> params) {
        this.evaluate = evaluate;
        if (multithreaded) {
            return calcMultiThreaded(evaluate, params);
        } else {
            return calcSingleThreaded(evaluate, params);
        }
    }

    private double calcSingleThreaded(TuneableEvaluateFunction evaluate, List<TuningParameter> params) {
        evaluate.saveValues(params);
        // build sum:
        double sum = calcSum();
        return sum / fens.size();
    }

    /**
     * Calc multithreaded with n Workers woring in n Threads.
     * Uses copies of the evaluation function, since our evaluation function is not thread safe (not pure functionl uses internal state).
     * @param evaluate
     * @param params
     * @return
     */
    private double calcMultiThreaded(TuneableEvaluateFunction evaluate, List<TuningParameter> params) {
        evaluate.saveValues(params);
        this.evaluate = evaluate;
        this.params = params;
        updateWorker();

        List<Future<Double>> futures = new ArrayList<>();
        for (DataSet worker : workers) {
            futures.add(executorService.submit(() -> worker.calcSum()));
        }

        double sum = 0;
        for (Future<Double> future : futures) {
            try {
                sum += future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }

        return sum / fens.size();

    }

    private Double calcSum() {
        // build sum:
        double sum = 0;
        for (FenEntry fen : fens) {
            double errorValue = pow(fen.getResult() - sigmoid(evaluate.eval(fen.getBoard(), WHITE)), 2);
            fen.setErrorValue(errorValue);
            sum += errorValue;
        }
        return sum;

    }

    private void updateWorker() {
        // create if not alreay done:
        if (workers.size() == 0) {

            for (int i = 0; i < optParameters.getThreadCount(); i++) {
                DataSet worker = new DataSet(optParameters);
                worker.setEvaluate(evaluate.copy());
                workers.add(worker);
            }
            // distribute the fens to the workers:
            int i = 0;
            for (FenEntry fen : fens) {
                workers.get(i % optParameters.getThreadCount()).addFen(fen);
                i++;
            }
        }                                                                       

        // update the evaluation functions of the workers with the current parameter settings:
        for (DataSet worker : workers) {
            worker.getEvaluate().saveValues(params);
            worker.setK(k);
        }
    }

    private double sigmoid(int eval) {
        double deval = eval;
        return 1 / (1 + pow(10, -k * deval / 400));
    }

    public void addFen(FenEntry entry) {
        fens.add(entry);
    }

    public void add(DataSet other) {
        fens.addAll(other.getFens());
    }

    public void logInfos() {
        LinkedHashMap<String, Object> stats = new LinkedHashMap<>();

        stats.put("Num Fens", fens.size());

        Map<Ending, Long> countsByEnding = fens.stream()
                .map(f -> f.getEnding())
                .collect(groupingBy(identity(), counting()));

        stats.put("MATE White", countsByEnding.get(Ending.MATE_WHITE));
        stats.put("MATE Black", countsByEnding.get(Ending.MATE_BLACK));
        stats.put("Draws", countsByEnding.get(Ending.DRAW));

        Long dupCount = fens.stream()
                .collect(groupingBy(f -> f.getBoard().getZobristHash(), counting()))
                .values()
                .stream()
                .filter(v -> v > 1)  // filter out those which have duplicates
                .map(v -> v - 1)    // remove one from count to get the right numer of duplicates
                .reduce(new Long(0), (a, b) -> a + b);

        stats.put("Duplicate Fens", dupCount);

        LOGGER.info("Data set statistics:");
        for (Map.Entry<String, Object> entry : stats.entrySet()) {
            LOGGER.info(entry.getKey() + ": " + entry.getValue());
        }

    }

    public void removeDuplidateFens() {
        List<FenEntry> result = new ArrayList<>();
        HashSet<Long> hashes = new HashSet<>();
        for (FenEntry fen : fens) {
            if (!hashes.contains(fen.getBoard().getZobristHash())) {
                result.add(fen);
                hashes.add(fen.getBoard().getZobristHash());
            }
        }
        fens = result;
    }

    public void writeLogInfos(MarkdownWriter w) throws IOException {

        w.h1("Dataset Information");

        MarkdownTable table = new MarkdownTable().header("", "");

        table.row("Num Fens", fens.size());

        Map<Ending, Long> countsByEnding = fens.stream()
                .map(f -> f.getEnding())
                .collect(groupingBy(identity(), counting()));

        table.row("MATE White", countsByEnding.get(Ending.MATE_WHITE));
        table.row("MATE Black", countsByEnding.get(Ending.MATE_BLACK));
        table.row("Draws", countsByEnding.get(Ending.DRAW));

        Long dupCount = fens.stream()
                .collect(groupingBy(f -> f.getBoard().getZobristHash(), counting()))
                .values()
                .stream()
                .filter(v -> v > 1)  // filter out those which have duplicates
                .map(v -> v - 1)    // remove one from count to get the right numer of duplicates
                .reduce(new Long(0), (a, b) -> a + b);

        table.row("Duplicate Fens", dupCount);

        w.writeTable(table);

    }
}
