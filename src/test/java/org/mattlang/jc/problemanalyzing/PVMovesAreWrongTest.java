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
    public void pvMovesWrongTest2() throws IOException {

        initLogging();
        UCI.instance.attachStreams();
        Factory.setDefaults(Factory.createDefaultParameter()
                .config(c -> c.maxDepth.setValue(4))
                .config(c -> c.maxQuiescence.setValue(4))
                .config(c -> c.useTTCache.setValue(false))
                .config(c -> c.aspiration.setValue(false))
                .config(c -> c.evluateFunctions.setValue(EvalFunctions.MINIMAL_PST))
                .config(c -> c.timeout.setValue(600)));
        // now starting engine:
        Engine engine = new Engine();
        GameState gameState = engine.getBoard().setFenPosition("position startpos moves d2d4 g8f6 c2c4 e7e6 b1c3 d7d5 c1g5 f8e7 e2e3 e8g8 g1f3 h7h6 g5f6 e7f6 a1c1 c7c6 f1d3 d5c4 d3c4 b7b5 c4b3 b5b4 c3e4 c8a6 d1d2 f6e7 h2h3 a6b5 f3e5 d8c7 d2c2 f8e8 a2a4 b5a6 e5c6 b8c6 c2c6 c7c6 c1c6 a6b7 d4d5 b7c6 d5c6 a8c8 a4a5 c8c6 e1e2 c6a6 h1a1 e7d8 a1a4 d8e7 e2f3 e8c8 f3f4 f7f5 e4g3 g8h7 e3e4 e7g5 f4f3 f5f4 g3h5 e6e5 a4b4 h7g6 f3g4 a6a5 b4b7 g5f6 b3e6 c8c7 b7c7 g6h7 h5f6 h7g6 f6h5 g6h7 c7g7 h7h8 ");
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
