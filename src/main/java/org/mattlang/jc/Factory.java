package org.mattlang.jc;

import java.util.function.Supplier;

import org.mattlang.jc.engine.BasicMoveList;
import org.mattlang.jc.engine.EvaluateFunction;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.SearchMethod;
import org.mattlang.jc.engine.evaluation.SimpleNegaMaxEval;
import org.mattlang.jc.engine.search.NegaMaxAlphaBeta;
import org.mattlang.jc.movegenerator.LegalMoveGenerator;
import org.mattlang.jc.movegenerator.LegalMoveGeneratorImpl;

/**
 * Factory to switch between different implementations (mainly for tests).
 */
public class Factory {

    private static Supplier<MoveList> moveListSupplier = () -> new BasicMoveList();

    private static Supplier<EvaluateFunction> evaluateFunctionSupplier = () -> new SimpleNegaMaxEval();

    private static Supplier<SearchMethod> searchMethodSupplier = () -> new NegaMaxAlphaBeta();

    private static Supplier<LegalMoveGenerator> legalMoveGeneratorSupplier = () -> new LegalMoveGeneratorImpl();

    public static void reset() {
        moveListSupplier = () -> new BasicMoveList();
        evaluateFunctionSupplier = () -> new SimpleNegaMaxEval();
        searchMethodSupplier = () -> new NegaMaxAlphaBeta();
    }

    public static MoveList createMoveList() {
        return moveListSupplier.get();
    }

    public static SearchMethod createSearchMethod() {
        return searchMethodSupplier.get();
    }

    public static EvaluateFunction createEvaluateFunction() {
        return evaluateFunctionSupplier.get();
    }

    public static LegalMoveGenerator createLegalMoveGenerator() {
        return legalMoveGeneratorSupplier.get();
    }

    public static void setMoveListSupplier(Supplier<MoveList> moveListSupplier) {
        Factory.moveListSupplier = moveListSupplier;
    }

    public static void setEvaluateFunctionSupplier(
            Supplier<EvaluateFunction> evaluateFunctionSupplier) {
        Factory.evaluateFunctionSupplier = evaluateFunctionSupplier;
    }

    public static void setSearchMethodSupplier(
            Supplier<SearchMethod> searchMethodSupplier) {
        Factory.searchMethodSupplier = searchMethodSupplier;
    }

    public static void setLegalMoveGeneratorSupplier(
            Supplier<LegalMoveGenerator> legalMoveGeneratorSupplier) {
        Factory.legalMoveGeneratorSupplier = legalMoveGeneratorSupplier;
    }
}
