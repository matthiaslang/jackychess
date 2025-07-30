package org.mattlang.tuning.data.builder;

import static org.mattlang.jc.moves.MoveToStringConverter.toLongAlgebraic;
import static org.mattlang.tuning.data.builder.EmptyPos.EMPTYPOS;
import static org.mattlang.tuning.data.pgnparser.PgnGame.*;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.engine.CheckChecker;
import org.mattlang.jc.movegenerator.BBCheckCheckerImpl;
import org.mattlang.jc.util.MoveValidator;
import org.mattlang.tuning.data.pgnparser.*;

/**
 * Builder for a pgn game with move validation functionality.
 */
public class PgnGameBuilder {

    private BoardRepresentation board = new BitBoard();

    private PgnGame game = new PgnGame();

    private MoveValidator validator = new MoveValidator();

    private boolean movesAdded = false;

    private MoveDescr white;
    private MoveDescr black;

    private Ending ending = null;

    public PgnGameBuilder() {
        board.setStartPosition();
    }

    public static PgnGameBuilder from(BoardAndMoves boardAndMoves) {
        PgnGameBuilder builder = new PgnGameBuilder();
        if (boardAndMoves.getStartFen() != null) {
            builder.withStartFen(boardAndMoves.getStartFen());
        }

        int index = 0;
        for (Move move : boardAndMoves.getMoves()) {
            if (index == boardAndMoves.getMoves().size() - 1) {
                builder.addMove(move, boardAndMoves.getEnding());
            } else {
                builder.addMove(move);
            }
            index++;
        }
        builder.game.addTag(TAG_RESULT, boardAndMoves.getEnding().getPgnResultString());
        return builder;
    }

    public PgnGameBuilder withStartFen(String fen) {
        if (movesAdded) {
            throw new IllegalStateException("set start fen before adding moves!");
        }
        board.setFenPosition(fen);
        // see https://www.saremba.de/chessgml/standards/pgn/pgn-complete.htm#c9.7

        game.addTag(TAG_FEN, fen);
        game.addTag(TAG_SETUP, "1");
        return this;
    }

    public PgnGameBuilder addMove(Move move) {

        executeMoveOnBoard(move);

        // check ending
        if (isCheckMate()) {
            ending = board.getSiteToMove() == Color.BLACK ? Ending.MATE_WHITE : Ending.MATE_BLACK;
        } else if (isStaleMate()) {
            ending = Ending.DRAW;
        }
        if (ending != null) {
            game.addTag(PgnGame.TAG_RESULT, ending.getPgnResultString());
        }

        MoveDescr moveDescr = new MoveDescr(new MoveText(toLongAlgebraic(move), EMPTYPOS), null, ending);

        return appendMoveDescr(moveDescr);
    }

    public PgnGameBuilder addMove(Move move, Ending ending) {

        executeMoveOnBoard(move);

        game.addTag(PgnGame.TAG_RESULT, ending.getPgnResultString());

        MoveDescr moveDescr = new MoveDescr(new MoveText(toLongAlgebraic(move), EMPTYPOS), null, ending);

        return appendMoveDescr(moveDescr);
    }

    private void executeMoveOnBoard(Move move) {
        if (ending != null) {
            throw new IllegalStateException("game already ended!");
        }
        // validate legality of move:
        if (!validator.isLegalMove(board, move, board.getSiteToMove())) {
            throw new IllegalArgumentException("Illegal move!");
        }
        // execute move on board:
        board.domove(move);
    }

    private PgnGameBuilder appendMoveDescr(MoveDescr moveDescr) {
        if (white == null) {
            white = moveDescr;
        } else if (black == null) {
            black = moveDescr;
            game.addMove(new PgnMove(white, black));
            white = null;
            black = null;
        }
        movesAdded = true;
        return this;
    }

    private boolean isCheckMate() {
        CheckChecker checkChecker = new BBCheckCheckerImpl();
        return checkChecker.isInChess(board, board.getSiteToMove())
               && validator.generateLegalMoves(board, board.getSiteToMove()).size() == 0;
    }

    private boolean isStaleMate() {
        CheckChecker checkChecker = new BBCheckCheckerImpl();
        return !checkChecker.isInChess(board, board.getSiteToMove())
               && validator.generateLegalMoves(board, board.getSiteToMove()).size() == 0;
    }

    public PgnGame toGame() {
        finish();
        return game;
    }

    private void finish() {
        if (white != null) {
            game.addMove(new PgnMove(white, null));
        }
    }
}
