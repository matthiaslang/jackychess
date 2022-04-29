package org.mattlang.jc.problemanalyzing;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.CheckChecker;
import org.mattlang.jc.engine.Engine;
import org.mattlang.jc.movegenerator.BBCheckCheckerImpl;
import org.mattlang.jc.uci.GameContext;
import org.mattlang.jc.util.MoveValidator;

public class Playing {

    private String fenPosition;

    public Playing(String fenPosition) {
        this.fenPosition = fenPosition;
    }

    public void playGameTillEnd() {

        Engine engine = new Engine();
         engine.getBoard().setFenPosition(fenPosition);
        System.out.println("Begin playing game from position: ");
        System.out.println(engine.getBoard().toUniCodeStr());

        GameContext gameContext = new GameContext();

        while (!endOfGame(engine.getBoard())) {

            Move move = engine.go(new GameState(engine.getBoard()), gameContext);

            if (!engine.getBoard().isvalidmove(move.getMoveInt())) {
                System.out.println("no valid Move!!!");
            }
            engine.getBoard().domove(move);
            System.out.println("Move " + move);
            System.out.println(engine.getBoard().toUniCodeStr());

        }

    }

    private boolean endOfGame(BoardRepresentation board) {
        CheckChecker checkChecker = new BBCheckCheckerImpl();
        MoveValidator moveValidator = new MoveValidator();

        // first check all strange states of the board:
        if (checkChecker.isInChess(board, board.getSiteToMove().invert())) {
            System.out.println("Illegal Chess State or Move! " + board.getSiteToMove().invert()
                    + " is after his move still in check!");
            return true;
        }
        if (board.isRepetition()) {
            System.out.println("Attention! This is a repetition!");
        }

        if (!moveValidator.hasLegalMoves(board)) {
            if (checkChecker.isInChess(board, board.getSiteToMove())) {
                System.out.println(board.getSiteToMove() + " is matt!");
            } else {
                System.out.println(board.getSiteToMove() + " is patt!");
            }

            return true;
        }

        return false;
    }

}
