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

    private final static Supplier<MoveList> DEFAULT_MOVELISTSUPPLIER = () -> new BasicMoveList();

    private static final Supplier<EvaluateFunction> DEFAULT_EVALUATEFUNCTIONSUPPLIER = () -> new SimpleNegaMaxEval();

    private static final Supplier<SearchMethod> DEFAULT_SEARCHMETHODSUPPLIER = () -> new NegaMaxAlphaBeta();

    private static final Supplier<LegalMoveGenerator> DEFAULT_LEGALMOVEGENERATORSUPPLIER =
            () -> new LegalMoveGeneratorImpl();

    private static Supplier<MoveList> moveListSupplier = DEFAULT_MOVELISTSUPPLIER;

    private static Supplier<EvaluateFunction> evaluateFunctionSupplier = DEFAULT_EVALUATEFUNCTIONSUPPLIER;

    private static Supplier<SearchMethod> searchMethodSupplier = DEFAULT_SEARCHMETHODSUPPLIER;

    private static Supplier<LegalMoveGenerator> legalMoveGeneratorSupplier = DEFAULT_LEGALMOVEGENERATORSUPPLIER;

    public static void reset() {
        moveListSupplier = DEFAULT_MOVELISTSUPPLIER;
        evaluateFunctionSupplier = DEFAULT_EVALUATEFUNCTIONSUPPLIER;
        searchMethodSupplier = DEFAULT_SEARCHMETHODSUPPLIER;
        legalMoveGeneratorSupplier = DEFAULT_LEGALMOVEGENERATORSUPPLIER;
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
