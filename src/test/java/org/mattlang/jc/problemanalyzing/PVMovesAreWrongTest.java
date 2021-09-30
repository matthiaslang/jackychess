package org.mattlang.jc.problemanalyzing;

import static org.mattlang.jc.Main.initLogging;

import java.io.IOException;

import org.junit.Test;
import org.mattlang.jc.EvalFunctions;
import org.mattlang.jc.Factory;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.Engine;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.SearchMethod;
import org.mattlang.jc.engine.search.IterativeDeepeningPVS;
import org.mattlang.jc.engine.search.IterativeSearchResult;
import org.mattlang.jc.movegenerator.LegalMoveGeneratorImpl3;
import org.mattlang.jc.moves.MoveImpl;
import org.mattlang.jc.uci.GameContext;
import org.mattlang.jc.uci.UCI;

/**
 * PV Moves returned seem to be invalid. looks like the last pv ist invalid (sometimes)...
 */
public class PVMovesAreWrongTest {

    int maxDepth = 7;

    @Test
    public void pvMovesWrongTest() throws IOException {

        initLogging();
        UCI.instance.attachStreams();
        Factory.setDefaults(Factory.createDefaultParameter()
                .config(c -> c.maxDepth.setValue(maxDepth))
                .config(c -> c.useTTCache.setValue(false))
                .config(c -> c.evluateFunctions.setValue(EvalFunctions.MINIMAL_PST))
                .config(c -> c.timeout.setValue(600)));
        // now starting engine:
        Engine engine = new Engine();
        GameState gameState = engine.getBoard().setFenPosition("position startpos moves d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 f2f3 e8g8 c1e3 b8d7 d1d2 c7c5 g1h3 d8a5 e1c1 c5d4 e3d4 a5d8 c1b1 f8e8 h3f2 a7a6 c3d5 e7e5 d5f6 g7f6 d4c3 e8e6 f1e2 f6g7 f2g4 d8h4 h1f1 d7f6 d2e1 h4g5 f3f4 g5h5 g4f6 e6f6 e2h5 g6h5 f4f5 b7b6 c3b4 c8b7 d1d6 f6d6 b4d6 a8d8 c4c5 b6c5 d6c5 d8c8 b2b4 f7f6 e1e2 g7f8 c5f8 g8f8 f1d1 c8c7 b1b2 c7g7 d1d8 f8e7 d8b8 e7d6   ");
        System.out.println(engine.getBoard().toUniCodeStr());

        SearchMethod searchMethod = Factory.getDefaults().searchMethod.create();

        IterativeDeepeningPVS itDeep = (IterativeDeepeningPVS) searchMethod;
        IterativeSearchResult itResult =
                itDeep.iterativeSearch(gameState, new GameContext(), maxDepth);

        System.out.println("best move + " + itResult.getSavedMove());
        // execute moves on board of pv:

        BoardRepresentation board = engine.getBoard();

        System.out.println("board after search" + board.toUniCodeStr());

        Color who2Move = gameState.getWho2Move();
        for (Move move : itResult.getRslt().pvList.getPvMoves()) {
            System.out.println("Checking PV " + move.toStr());

            boolean legal = isLegalMove(board, move, who2Move);

            if (legal) {
                board.move(move);
            } else {
                System.out.println("is not legal!");
                System.out.println(move.toStr());
                System.out.println(board.toUniCodeStr());

                throw new IllegalStateException();
            }
            System.out.println(move.toStr());
            System.out.println(board.toUniCodeStr());

            who2Move= who2Move.invert();
        }

        Factory.setDefaults(Factory.createDefaultParameter());
    }

    public boolean isLegalMove(BoardRepresentation board, Move move, Color who2Move) {
        LegalMoveGeneratorImpl3 legalMoveGen = new LegalMoveGeneratorImpl3();
        MoveList legalMoves =
                legalMoveGen.generate(board, who2Move);

        // todo clean up and make nicer way to check this...
        for (MoveCursor legalMove : legalMoves) {
            if (legalMove.getMoveInt() == move.toInt()) {
                return true;
            }
        }
        String moves = "";
        for (MoveCursor legalMove : legalMoves) {
            moves += new MoveImpl(legalMove.getMoveInt()).toStr();
            moves += ",";

        }
        System.out.println("move " + move.toStr() + " is not within the legal moves: " + moves);
        return false;
    }
}
