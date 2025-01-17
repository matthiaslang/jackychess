package org.mattlang.tuning;

import static java.lang.Math.pow;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static org.mattlang.jc.board.Color.*;
import static org.mattlang.tuning.tuner.LocalOptimizationTuner.executorService;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import org.mattlang.jc.board.FigureType;
import org.mattlang.jc.board.bitboard.BitChessBoard;
import org.mattlang.jc.engine.evaluation.PhaseCalculator;
import org.mattlang.jc.engine.evaluation.PinnedCalc;
import org.mattlang.jc.material.Material;
import org.mattlang.jc.tools.MarkdownTable;
import org.mattlang.jc.tools.MarkdownWriter;
import org.mattlang.tuning.data.pgnparser.Ending;
import org.mattlang.tuning.evaluate.ParameterSet;
import org.mattlang.tuning.tuner.DatasetPreparer;
import org.mattlang.tuning.tuner.OptParameters;

import lombok.Getter;
import lombok.Setter;

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

    private boolean multithreaded = false;

    private double k = K;

    private OptParameters optParameters;

    public DataSet(OptParameters optParameters) {
        this.optParameters = optParameters;
    }

    public double calcError(TuneableEvaluateFunction evaluate, ParameterSet params) {
        this.evaluate = evaluate;
        if (multithreaded) {
            return calcMultiThreaded(evaluate, params);
        } else {
            return calcSingleThreaded(evaluate, params);
        }
    }

    private double calcSingleThreaded(TuneableEvaluateFunction evaluate, ParameterSet params) {
        evaluate.saveValues(params.getParams());
        // build sum:
        double sum = calcSum();
        return sum / fens.size();
    }

    /**
     * Calc multithreaded with n Workers woring in n Threads.
     * Uses copies of the evaluation function, since our evaluation function is not thread safe (not pure functionl uses
     * internal state).
     *
     * @param evaluate
     * @param params
     * @return
     */
    private double calcMultiThreaded(TuneableEvaluateFunction evaluate, ParameterSet params) {
        evaluate.saveValues(params.getParams());
        this.evaluate = evaluate;

        updateWorker(params);

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
            int eval = calcEval(fen);
            double errorValue = pow(fen.getResult() - sigmoid(eval), 2);
            sum += errorValue;
        }
        return sum;

    }

    private int calcEval(FenEntry fen) {
        if (optParameters.isOptimizeRecalcOnlyDependendFens()) {
            int eval = fen.getLastEval();
            if (eval == FenEntry.NO_EVAL) {
                eval = evaluate.eval(fen.getBoard(), WHITE);
                fen.setLastEval(eval);
            }
            return eval;
        } else {
            return evaluate.eval(fen.getBoard(), WHITE);
        }
    }

    private void updateWorker(ParameterSet parameterSet) {
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
            worker.getEvaluate().saveValues(parameterSet.getParams());
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

        writeStatsNumberOfFensByGamePhase(w);

        writeStatsNumberOfFensByHavingFigures(w);

        writeNumberOfFensByMaterial(w);

        writeNumberOfFensByPinnedPieces(w);
    }

    private void writeStatsNumberOfFensByGamePhase(MarkdownWriter w) throws IOException {
        w.h2("Number of fens by Game Phase");
        // stats about mid/end game:
        Map<Long, Long> countsByPhase = fens.stream()
                .map(f -> calcPhaseStatistic(f))
                .collect(groupingBy(identity(), counting()));

        MarkdownTable table = new MarkdownTable().header("Phase", "Count", "%");
        for (Map.Entry<Long, Long> entry : countsByPhase.entrySet()) {
            table.row(entry.getKey(), entry.getValue(), entry.getValue() * 100 / fens.size());
        }
        w.writeTable(table);
    }

    private void writeStatsNumberOfFensByHavingFigures(MarkdownWriter w) throws IOException {
        w.h2("Number of fens by having figures");

        Map<FigureType, Long> fensByHavingFigures = new HashMap<>();
        for (FenEntry fen : fens) {
            for (FigureType ft : FigureType.values()) {
                if (ft != FigureType.EMPTY && ft != FigureType.King) {
                    if (fen.getBoard().getBoard().getPieceSet(ft.ordinal()) != 0L) {
                        fensByHavingFigures.compute(ft, (k, v) -> (v == null) ? 1 : v + 1);
                    }
                }
            }
        }
        MarkdownTable table = new MarkdownTable().header("FigureType", "Count", "%");
        for (Map.Entry<FigureType, Long> entry : fensByHavingFigures.entrySet()) {
            table.row(entry.getKey(), entry.getValue(), entry.getValue() * 100 / fens.size());
        }
        w.writeTable(table);
    }

    private void writeNumberOfFensByMaterial(MarkdownWriter w) throws IOException {
        w.h2("Number of fens by Material");
        // stats about mid/end game:
//        TreeMap<Material, Long> countsByPhase = fens.stream()
//                .map(f -> f.getBoard().getMaterial())
//                .collect(groupingBy(identity(), TreeMap::new, counting()));
        TreeMap<Material, Long> countsByPhase = new TreeMap<>();
        for (FenEntry fen : fens) {
            // add white and black mat separate to group by the material of one side (regardless of the side)
            Material mw=new Material(fen.getBoard().getMaterial().getWhiteMat());
            countsByPhase.compute(mw, (k, v) -> (v == null) ? 1 : v + 1);
            Material mb=new Material(fen.getBoard().getMaterial().getBlackAsWhitePart());
            countsByPhase.compute(mb, (k, v) -> (v == null) ? 1 : v + 1);
        }

        MarkdownTable table = new MarkdownTable().header("Material", "Count", "%");
        for (Map.Entry<Material, Long> entry : countsByPhase.entrySet()) {
            table.row(entry.getKey().toString(), entry.getValue(), entry.getValue() * 100 / fens.size());
        }
        w.writeTable(table);
    }

    private void writeNumberOfFensByPinnedPieces(MarkdownWriter w) throws IOException {
        w.h2("Number of fens by Pinned Pieces");
        // stats about mid/end game:
        //        TreeMap<Material, Long> countsByPhase = fens.stream()
        //                .map(f -> f.getBoard().getMaterial())
        //                .collect(groupingBy(identity(), TreeMap::new, counting()));
        TreeMap<Byte, Long> pinnedByFigureType = new TreeMap<>();
        TreeMap<Byte, Long> discoveredByFigureType = new TreeMap<>();
        for (FenEntry fen : fens) {
            PinnedCalc pinnedCalc=new PinnedCalc();
            BitChessBoard bb = fen.getBoard().getBoard();
            pinnedCalc.calcPinnedDiscoveredAndChecking(bb);
            if (pinnedCalc.getPinnedPieces() != 0) {
                long piece = pinnedCalc.getPinnedPieces() & bb.getPieceSet(nWhite);
                while (piece != 0) {
                    pinnedByFigureType.compute(bb.getFigType(Long.numberOfTrailingZeros(piece)), (k, v) -> (v == null) ? 1 : v + 1);
                    piece &= piece - 1;
                }
                piece = pinnedCalc.getPinnedPieces() & bb.getPieceSet(nBlack);
                while (piece != 0) {
                    pinnedByFigureType.compute(bb.getFigType(Long.numberOfTrailingZeros(piece)), (k, v) -> (v == null) ? 1 : v + 1);
                    piece &= piece - 1;
                }
            }

            if (pinnedCalc.getDiscoveredPieces() != 0) {
                long piece = pinnedCalc.getDiscoveredPieces() & bb.getPieceSet(nWhite);
                while (piece != 0) {
                    discoveredByFigureType.compute(bb.getFigType(Long.numberOfTrailingZeros(piece)), (k, v) -> (v == null) ? 1 : v + 1);
                    piece &= piece - 1;
                }
                piece = pinnedCalc.getDiscoveredPieces() & bb.getPieceSet(nBlack);
                while (piece != 0) {
                    discoveredByFigureType.compute(bb.getFigType(Long.numberOfTrailingZeros(piece)), (k, v) -> (v == null) ? 1 : v + 1);
                    piece &= piece - 1;
                }
            }
        }

        MarkdownTable table = new MarkdownTable().header("Figure Type", "Pinned Count", "%");
        for (Map.Entry<Byte, Long> entry : pinnedByFigureType.entrySet()) {
            table.row(FigureType.values()[entry.getKey()], entry.getValue(), entry.getValue() * 100 / fens.size());
        }
        w.writeTable(table);

        table = new MarkdownTable().header("Figure Type", "Discovered Count", "%");
        for (Map.Entry<Byte, Long> entry : discoveredByFigureType.entrySet()) {
            table.row(FigureType.values()[entry.getKey()], entry.getValue(), entry.getValue() * 100 / fens.size());
        }
        w.writeTable(table);
    }

    private long calcPhaseStatistic(FenEntry f) {
        double factor = PhaseCalculator.calcPhaseFactor(f.getBoard());
        long decFactor = Math.round(factor * 10);
        return decFactor;
    }

    public void resetDependingFens(TuningParameter param) {
        for (FenEntry fen : fens) {
            fen.resetIfDepending(param.getParamNo());
        }
    }
}
