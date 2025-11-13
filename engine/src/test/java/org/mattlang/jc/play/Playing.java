package org.mattlang.jc.play;

import static org.mattlang.jc.play.EndStatus.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mattlang.jc.SearchParameter;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.Engine;
import org.mattlang.jc.engine.search.IterativeSearchResult;
import org.mattlang.jc.engine.search.SearchThreadContexts;
import org.mattlang.jc.movegenerator.Captures;
import org.mattlang.jc.moves.MoveImpl;
import org.mattlang.jc.uci.GameContext;
import org.mattlang.jc.util.MoveValidator;

/**
 * To "play" a game in unit tests.
 */
public class Playing {

    private String fenPosition;

    private List<Move> playedMoves = new ArrayList<>();
    private BoardRepresentation startPosition;

    public Playing(String fenPosition) {
        this.fenPosition = fenPosition;
    }

    public GameStatusResult playGameTillEnd(SearchParameter params) {

        Engine engine = new Engine();
        engine.getBoard().setFenPosition(fenPosition);
        System.out.println("Begin playing game from position: ");
        System.out.println(engine.getBoard().toUniCodeStr());

        startPosition = engine.getBoard().copy();

        GameStatusResult currentState = checkGameStatus(engine.getBoard().copy());

        while (!currentState.isEnd()) {

            String moveInfo = engine.getBoard().getSiteToMove() + " Ply: " + playedMoves.size();
            System.out.println(moveInfo);

            // reset all static data
            SearchThreadContexts.CONTEXTS.reset();
            GameContext gameContext = new GameContext();
            // iterative search
            IterativeSearchResult result =
                    engine.goIterative(params, new GameState(engine.getBoard()), gameContext);
            Move move = result.getSavedMove();

            if (!engine.getBoard().isvalidmove(engine.getBoard().getSiteToMove().ordinal(), move.getMoveInt())) {
                System.out.println("no valid Move!!!");
            }
            playedMoves.add(new MoveImpl(move.getMoveInt()));
            engine.getBoard().domove(move);
            System.out.println(moveInfo + " Move " + move);
            System.out.println(engine.getBoard().toUniCodeStr());

            currentState = checkGameStatus(engine.getBoard().copy());
        }

        System.out.println("Played Moves: " + playedMoves);
        return currentState;
    }

    private GameStatusResult checkGameStatus(BoardRepresentation board) {
        MoveValidator moveValidator = new MoveValidator();

        // first check all strange states of the board:
        if (Captures.canKingCaptured(board, board.getSiteToMove().invert().ordinal())) {
            System.out.println("Illegal Chess State or Move! " + board.getSiteToMove().invert()
                               + " is after his move still in check!");
            return new GameStatusResult(WEIRD_STATE);
        }

        try {
            board.getBoard().doAssertions();
        } catch (AssertionError assertionError) {
            assertionError.printStackTrace();
            return new GameStatusResult(WEIRD_STATE);
        }

        if (board.isRepetition()) {
            System.out.println("Attention! This is a repetition!");
        }

        if (isDrawByRepetition()) {
            System.out.println("Draw by 3 times Repetition!");
            return new GameStatusResult(DRAW_BY_3_REPETITIONS);
        }

        if (!moveValidator.hasLegalMoves(board)) {
            if (Captures.canKingCaptured(board, board.getSiteToMove().ordinal())) {
                System.out.println(board.getSiteToMove() + " is matt!");
                return new GameStatusResult(MATT);
            } else {
                System.out.println(board.getSiteToMove() + " is patt!");
                return new GameStatusResult(PATT);
            }
        }
        if (playedMoves.size() > 500) {
            System.out.println("abort after 500 Moves!!!");
            return new GameStatusResult(DRAW_BY_TOO_MUCH_MOVES);
        }

        return new GameStatusResult();
    }

    private boolean isDrawByRepetition() {
        HashMap<Long, Integer> repCounts = new HashMap<>();

        BoardRepresentation board = startPosition.copy();
        repCounts.putIfAbsent(board.getZobristHash(), 1);
        repCounts.computeIfPresent(board.getZobristHash(), (key, oldVal) -> oldVal + 1);

        for (Move move : playedMoves) {
            board.domove(move);

            repCounts.putIfAbsent(board.getZobristHash(), 1);
            Integer newValue = repCounts.computeIfPresent(board.getZobristHash(), (key, oldVal) -> oldVal + 1);
            if (newValue >= 3) {
                return true;
            }
        }
        return false;
    }

}
