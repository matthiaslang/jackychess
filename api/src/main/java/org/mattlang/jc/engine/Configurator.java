package org.mattlang.jc.engine;

import org.mattlang.jc.board.BoardFactory;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.util.ServiceLoaderUtil;

public class Configurator {

    private static final EvaluateFunctionFactory factory =
            ServiceLoaderUtil.determineSingleService(EvaluateFunctionFactory.class);

    private static final BoardFactory boardFactory = ServiceLoaderUtil.determineSingleService(BoardFactory.class);

    public static EvaluateFunction createConfiguredEvaluateFunction() {
        return factory.createEvaluateFunction();
    }

    public static String determineEvalImplName() {
        return factory.getEvalImplName();
    }

    public static BoardRepresentation createBoard() {
        return boardFactory.createBoard();
    }

    public static String determineBoardImplName() {
        return boardFactory.getBoardImplName();
    }
}
