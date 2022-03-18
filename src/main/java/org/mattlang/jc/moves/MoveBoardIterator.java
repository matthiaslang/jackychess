package org.mattlang.jc.moves;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Figure;
import org.mattlang.jc.engine.CheckChecker;
import org.mattlang.jc.engine.MoveCursor;

/**
 * Helper to iterate over a move list and do/undo the moves in a loop.
 */
public class MoveBoardIterator implements MoveCursor, AutoCloseable {

    private MoveCursor moveCursor;
    private BoardRepresentation board;

    private CheckChecker checkChecker;
    Color siteToMove;

    private boolean moveDone = false;

    public MoveBoardIterator() {
    }

    public MoveBoardIterator(MoveCursor moveCursor, BoardRepresentation board, CheckChecker checkChecker) {
        this.moveCursor = moveCursor;
        this.board = board;
        siteToMove = board.getSiteToMove();
        this.checkChecker = checkChecker;
    }

    public void init(MoveCursor moveCursor, BoardRepresentation board, CheckChecker checkChecker) {
        this.moveCursor = moveCursor;
        this.board = board;
        siteToMove = board.getSiteToMove();
        this.checkChecker = checkChecker;
        moveDone = false;
    }

    public boolean doNextMove() {
        if (moveDone) {
            undoMove();
        }
        if (moveCursor.hasNext()) {
            doMove();
            while (checkChecker.isInChess(board, siteToMove)) {
                undoMove();
                if (moveCursor.hasNext()) {
                    doMove();
                } else {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private void undoMove() {
        moveCursor.undoMove(board);
        moveDone = false;
    }

    private void doMove() {
        moveCursor.next();
        moveCursor.move(board);
        moveDone = true;
    }

    @Override
    public void move(BoardRepresentation board) {
        moveCursor.move(board);
    }

    @Override
    public int getMoveInt() {
        return moveCursor.getMoveInt();
    }

    @Override
    public int getOrder() {
        return moveCursor.getOrder();
    }

    @Override
    public void undoMove(BoardRepresentation board) {
        moveCursor.undoMove(board);
    }

    @Override
    public boolean isCapture() {
        return moveCursor.isCapture();
    }

    @Override
    public boolean isPawnPromotion() {
        return moveCursor.isPawnPromotion();
    }

    @Override
    public boolean isEnPassant() {
        return moveCursor.isEnPassant();
    }

    @Override
    public boolean isCastling() {
        return moveCursor.isCastling();
    }

    @Override
    public byte getCapturedFigure() {
        return moveCursor.getCapturedFigure();
    }

    @Override
    public Figure getPromotedFigure() {
        return moveCursor.getPromotedFigure();
    }

    @Override
    public byte getFigureType() {
        return moveCursor.getFigureType();
    }

    @Override
    public int getFromIndex() {
        return moveCursor.getFromIndex();
    }

    @Override
    public int getToIndex() {
        return moveCursor.getToIndex();
    }

    @Override
    public void next() {
        throw new IllegalStateException("use doNextMove!");
    }

    @Override
    public boolean hasNext() {
        throw new IllegalStateException("use doNextMove!");
    }

    @Override
    public void close() {
        // undo a currently done move on the board
        if (moveDone) {
            undoMove();
        }
    }
}
