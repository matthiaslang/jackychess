package org.mattlang.jc.moves;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Figure;
import org.mattlang.jc.engine.CheckChecker;
import org.mattlang.jc.engine.MoveCursor;

/**
 * Helper to iterate over a move list and do/undo the moves in a loop.
 */
public final class MoveBoardIterator implements MoveCursor, AutoCloseable {

    private MoveCursor moveCursor;
    private BoardRepresentation board;

    private CheckChecker checkChecker;
    Color siteToMove;

    private boolean moveDone = false;

    private boolean nextStepped = false;

    public MoveBoardIterator() {
    }

    public MoveBoardIterator(MoveCursor moveCursor, BoardRepresentation board, CheckChecker checkChecker) {
        this.moveCursor = moveCursor;
        this.board = board;
        siteToMove = board.getSiteToMove();
        this.checkChecker = checkChecker;
        nextStepped = false;
    }

    public void init(MoveCursor moveCursor, BoardRepresentation board, CheckChecker checkChecker) {
        this.moveCursor = moveCursor;
        this.board = board;
        siteToMove = board.getSiteToMove();
        this.checkChecker = checkChecker;
        moveDone = false;
        nextStepped = false;
    }

    /**
     * Steps to the next valid move (if available) and moves the move on the board.
     * This way all valid moves can easily iterated and done/undone on the board in one loop.
     *
     * @return
     */
    public boolean doNextValidMove() {
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

    /**
     * steps to the next move if a next move is available.
     * @return
     */
    public boolean nextMove() {
        if (moveDone) {
            undoMove();
        }
        if (moveCursor.hasNext()) {
            moveCursor.next();
            nextStepped = true;
            return true;
        }
        nextStepped = false;
        return false;
    }

    /**
     * tries to move the current move on the board if it is a valid legal move.
     * Requires that a move is set via nextMove.
     *
     * @return true, if its a legal move and done on the board, false otherwise
     */
    public boolean doValidMove() {
        if (!nextStepped) {
            return false;
        }
        moveCursor.move(board);
        moveDone = true;
        return !checkChecker.isInChess(board, siteToMove);
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
    public int getEnPassantCapturePos() {
        return moveCursor.getEnPassantCapturePos();
    }

    @Override
    public byte getPromotedFigureByte() {
        return moveCursor.getPromotedFigureByte();
    }

    @Override
    public byte getCastlingType() {
        return moveCursor.getCastlingType();
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
    public boolean isPromotion() {
        return moveCursor.isPromotion();
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
    public String toStr() {
        return moveCursor.toStr();
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
