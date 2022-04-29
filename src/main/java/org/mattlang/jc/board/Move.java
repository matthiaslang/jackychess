package org.mattlang.jc.board;

import org.mattlang.jc.moves.CastlingMove;

public interface Move {

    byte getCapturedFigure();

    boolean isCapture();

    byte getFigureType();

    int getFromIndex();

    int getToIndex();

    /**
     * String UCI move representation.
     * @return
     */
    String toStr();

    boolean isEnPassant();

    boolean isCastling();

    boolean isPromotion();

    Figure getPromotedFigure();

    /**
     * Representation of the move as int value.
     */
    int getMoveInt();

    int getEnPassantCapturePos();

    byte getPromotedFigureByte();

    CastlingMove getCastlingMove();
}
