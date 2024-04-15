package org.mattlang.jc.board;

public interface Move {

    byte getCapturedFigure();

    boolean isCapture();

    byte getFigureType();

    int getFromIndex();

    int getToIndex();

    /**
     * String UCI move representation.
     * @deprecated use toUCIString method for uci output! This method does not correctly convert castlings in chess960!
     * @return
     */
    String toStr();

    /**
     * UCI move representation of the move.
     * The board parameter is necessary to convert castlings in chess960 correct.
     * @param board
     * @return
     */
    String toUCIString(BoardRepresentation board);

    boolean isEnPassant();

    boolean isCastling();

    boolean isPromotion();

    Figure getPromotedFigure();

    /**
     * Representation of the move as int value.
     */
    int getMoveInt();

    byte getPromotedFigureByte();

    byte getCastlingType();
}
