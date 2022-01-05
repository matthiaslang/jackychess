package org.mattlang.jc.board;

import org.mattlang.jc.moves.CastlingMove;

public interface Move {

    byte getCapturedFigure();

    boolean isCapture();

    byte getFigureType();

    int getFromIndex();

    int getToIndex();

    String toStr();

    boolean isEnPassant();

    boolean isCastling();

    boolean isPromotion();

    Figure getPromotedFigure();

    void setOrder(int order);

    int getOrder();

    /**
     * Representation of the move as int value.
     */
    int toInt();

    int getEnPassantCapturePos();

    byte getPromotedFigureByte();

    CastlingMove getCastlingMove();
}
