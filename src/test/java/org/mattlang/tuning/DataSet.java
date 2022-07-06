package org.mattlang.tuning;

import static java.lang.Math.pow;
import static org.mattlang.jc.board.Color.WHITE;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Logger;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.EvaluateFunction;
import org.mattlang.tuning.tuner.DatasetPreparer;

import lombok.Data;

/**
 * Contains all FEN Positions used as datas set for tuning.
 */
@Data
public class DataSet {

    private static final Logger LOGGER = Logger.getLogger(DatasetPreparer.class.getSimpleName());

    /**
     * scaling Constant.
     */
    private static final double K = 1.13;
    private List<FenEntry> fens = new ArrayList<>();
    /**
     * hold to create copies for the trhead local.
     */
    private TuneableEvaluateFunction evaluate;
    private List<TuningParameter> params;

    private boolean multithreaded = false;

    private double k = K;

    public double calcError(TuneableEvaluateFunction evaluate, List<TuningParameter> params) {
        if (multithreaded) {
            return calcMultiThreaded2(evaluate, params);
        } else {
            return calcSingleThreaded(evaluate, params);
        }
    }

    private double calcSingleThreaded(TuneableEvaluateFunction evaluate, List<TuningParameter> params) {
        evaluate.saveValues(params);
        // build sum:
        double sum = 0;
        for (FenEntry fen : fens) {
            sum += pow(fen.getResult() - sigmoid(evaluate.eval(fen.getBoard(), WHITE)), 2);
        }
        return sum / fens.size();

    }

    /**
     * this version of multi threading does not work very well for the followin reasons:
     *
     * - our evaluation function is itself not thread safe currenty (not a pure function);
     * - therefore we need to use a thread local pattern to use an own function for each worker thread
     * - thread lokals contract does not work in for join pools, as they seem to destroy the thread local contexts
     * - therefore each worker call recreates the evaluation function again. -> a lot of overhead just for recreation
     * stuff.
     *
     * @param evaluate
     * @param params
     * @return
     */
    public double calcMultiThreaded(TuneableEvaluateFunction evaluate, List<TuningParameter> params) {
        evaluate.saveValues(params);
        this.evaluate = evaluate;
        this.params = params;
        // "delete" all "previous" thread locals, so that we get new ones, where we again call "saveValues" on creation:
        threadLocalEvaluateFunction = new ThreadLocal<EvaluateFunction>() {

            @Override
            protected EvaluateFunction initialValue() {
                LOGGER.info("initialize thread local evaluation function");
                TuneableEvaluateFunction copy = evaluate.copy();
                copy.saveValues(params);
                return copy;
            }
        };
        // build sum:

        Optional<Double> sum2 = fens.stream()
                .parallel()
                .map(fen -> pow(fen.getResult() - sigmoid(getThreadLocalEval().eval(fen.getBoard(), WHITE)), 2))
                .reduce(Double::sum);

        return sum2.get() / fens.size();

    }


    public double calcMultiThreaded2(TuneableEvaluateFunction evaluate, List<TuningParameter> params) {
        evaluate.saveValues(params);
        this.evaluate = evaluate;
        this.params = params;
        deque.clear();

        // build sum:

        Optional<Double> sum2 = fens.stream()
                .parallel()
                .map(fen -> pow(fen.getResult() - sigmoid(eval(fen.getBoard(), WHITE)), 2))
                .reduce(Double::sum);

        return sum2.get() / fens.size();

    }


    private final static ConcurrentLinkedDeque<TuneableEvaluateFunction> deque = new ConcurrentLinkedDeque();

    private int eval(BoardRepresentation board, Color color) {
//        LOGGER.info("queue size = " + deque.size());
        TuneableEvaluateFunction evalFun = deque.poll();
        if (evalFun == null) {

//            LOGGER.info("initialize new pooled evaluation function");
            evalFun = evaluate.copy();
            evalFun.saveValues(params);
        }  else {
//            LOGGER.info("reusing existing pooled eval function");
        }

        int val = evalFun.eval(board, color);

        deque.push(evalFun);

        return val;
    }

    private ThreadLocal<EvaluateFunction> threadLocalEvaluateFunction = new ThreadLocal<EvaluateFunction>() {

        @Override
        protected EvaluateFunction initialValue() {
            LOGGER.info("initialize thread local evaluation function");
            TuneableEvaluateFunction copy = evaluate.copy();
            copy.saveValues(params);
            return copy;
        }
    };
    
    private EvaluateFunction getThreadLocalEval() {
        return threadLocalEvaluateFunction.get();
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
}
