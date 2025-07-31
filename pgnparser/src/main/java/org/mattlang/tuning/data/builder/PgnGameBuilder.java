package org.mattlang.tuning.data.builder;

import static org.mattlang.jc.moves.MoveToStringConverter.toLongAlgebraic;
import static org.mattlang.tuning.data.builder.EmptyPos.EMPTYPOS;
import static org.mattlang.tuning.data.pgnparser.PgnGame.TAG_FEN;
import static org.mattlang.tuning.data.pgnparser.PgnGame.TAG_SETUP;

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
        builder.setEnding(boardAndMoves.getEnding());
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
        Ending determinedEnding = determineEnding();
        if (determinedEnding != null) {
            setEnding(determinedEnding);
        }

        MoveDescr moveDescr = new MoveDescr(new MoveText(toLongAlgebraic(move), EMPTYPOS), null, ending);

        return appendMoveDescr(moveDescr);
    }

    private Ending determineEnding() {
        Ending determinedEnding = null;
        if (isCheckMate()) {
            determinedEnding = board.getSiteToMove() == Color.BLACK ? Ending.MATE_WHITE : Ending.MATE_BLACK;
        } else if (isStaleMate()) {
            determinedEnding = Ending.DRAW;
        }
        return determinedEnding;
    }

    public PgnGameBuilder addMove(Move move, Ending ending) {

        executeMoveOnBoard(move);

        setEnding(ending);

        MoveDescr moveDescr = new MoveDescr(new MoveText(toLongAlgebraic(move), EMPTYPOS), null, ending);

        return appendMoveDescr(moveDescr);
    }

    private void executeMoveOnBoard(Move move) {
        if (ending != null && ending != Ending.UNTERMINATED) {
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
        // add last white ply if any:
        if (white != null) {
            game.addMove(new PgnMove(white, null));
        }

        if (ending == null) {
            setEnding(Ending.UNTERMINATED);
        }
        if (ending == Ending.UNTERMINATED) {
            updateUnterminated();
        }
    }

    private MoveDescr exchangeEnding(MoveDescr moveDescr, Ending ending) {
        return new MoveDescr(moveDescr.getMoveText(), moveDescr.getComment(), ending);
    }

    /**
     * set ending unterminated to last ply in the list.
     */
    private void updateUnterminated() {
        if (game.getMoves().size() > 0) {
            PgnMove lastMove = game.getMoves().get(game.getMoves().size() - 1);
            MoveDescr lastWhite = lastMove.getWhite();
            MoveDescr lastBlack = lastMove.getBlack();
            if (lastBlack != null) {
                lastBlack = exchangeEnding(lastBlack, ending);
            } else {
                lastWhite = exchangeEnding(lastWhite, ending);
            }

            lastMove = new PgnMove(lastWhite, lastBlack);
            game.getMoves().set(game.getMoves().size() - 1, lastMove);
        }
    }

    private void setEnding(Ending ending) {
        this.ending = ending;
        addTag(PgnGame.TAG_RESULT, ending.getPgnResultString());
    }

    public void addTag(String tag, String value) {
        game.addTag(tag, value);
    }

}
