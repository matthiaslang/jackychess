package org.mattlang.jc.moves;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Figure;
import org.mattlang.jc.engine.CheckChecker;
import org.mattlang.jc.engine.MoveCursor;
import org.mattlang.jc.engine.sorting.MoveIterator;

/**
 * Helper to iterate over a move list and do/undo the moves in a loop.
 */
public final class MoveBoardIterator implements MoveCursor, AutoCloseable {

    private MoveIterator moveIterator;
    private BoardRepresentation board;

    private CheckChecker checkChecker;
    Color siteToMove;

    private boolean moveDone = false;

    private boolean nextStepped = false;

    private int currMove;
    private int orderOfCurrentMove;

    private MoveImpl currMoveObj = new MoveImpl("a1a2");

    public MoveBoardIterator() {
    }

    public MoveBoardIterator(MoveIterator moveIterator, BoardRepresentation board, CheckChecker checkChecker) {
        this.moveIterator = moveIterator;
        this.board = board;
        siteToMove = board.getSiteToMove();
        this.checkChecker = checkChecker;
        nextStepped = false;
    }

    public void init(MoveIterator moveIterator, BoardRepresentation board, CheckChecker checkChecker) {
        this.moveIterator = moveIterator;
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
        if (moveIterator.hasNext()) {
            doMove();
            while (checkChecker.isInChess(board, siteToMove)) {
                undoMove();
                if (moveIterator.hasNext()) {
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
     *
     * @return
     */
    public boolean nextMove() {
        if (moveDone) {
            undoMove();
        }
        if (moveIterator.hasNext()) {
            prepareNext();
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
        board.domove(currMoveObj);
        moveDone = true;
        return !checkChecker.isInChess(board, siteToMove);
    }

    private void undoMove() {
        board.undo(currMoveObj);
        moveDone = false;
    }

    private void doMove() {
        prepareNext();
        board.domove(currMoveObj);
        moveDone = true;
    }

    private void prepareNext() {
        currMove = moveIterator.next();
        orderOfCurrentMove = moveIterator.getOrder();
        currMoveObj.fromLongEncoded(currMove);
    }

    @Override
    public int getMoveInt() {
        return currMove;
    }

    @Override
    public byte getPromotedFigureByte() {
        return currMoveObj.getPromotedFigureByte();
    }

    @Override
    public byte getCastlingType() {
        return currMoveObj.getCastlingType();
    }

    @Override
    public int getOrder() {
        return orderOfCurrentMove;
    }

    @Override
    public boolean isCapture() {
        return currMoveObj.isCapture();
    }

    @Override
    public boolean isPromotion() {
        return currMoveObj.isPromotion();
    }

    @Override
    public boolean isEnPassant() {
        return currMoveObj.isEnPassant();
    }

    @Override
    public boolean isCastling() {
        return currMoveObj.isCastling();
    }

    @Override
    public byte getCapturedFigure() {
        return currMoveObj.getCapturedFigure();
    }

    @Override
    public Figure getPromotedFigure() {
        return currMoveObj.getPromotedFigure();
    }

    @Override
    public byte getFigureType() {
        return currMoveObj.getFigureType();
    }

    @Override
    public int getFromIndex() {
        return currMoveObj.getFromIndex();
    }

    @Override
    public int getToIndex() {
        return currMoveObj.getToIndex();
    }

    @Override
    public String toStr() {
        return currMoveObj.toStr();
    }

    @Override
    public String toUCIString(BoardRepresentation board) {
        return currMoveObj.toUCIString(board);
    }

    @Override
    public void close() {
        // undo a currently done move on the board
        if (moveDone) {
            undoMove();
        }
    }
}
