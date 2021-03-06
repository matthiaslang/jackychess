package org.mattlang.jc.board;

import org.mattlang.jc.board.bitboard.BitChessBoard;
import org.mattlang.jc.material.Material;

public interface BoardRepresentation {
    void setStartPosition();

    void setPosition(String[] fenPosition);

    GameState setFenPosition(String fen);

    void setPos(int row, int col, char figureChar);

    void setPos(int index, Figure figure);

    void setPos(int index, byte figure);

    char getPos(int row, int col);

    Figure getPos(int i);

    Figure getFigurePos(int row, int col);

    void clearPosition();

    String toUniCodeStr();

    void println();

    void switchSiteToMove();

    Color getSiteToMove();

    Figure getFigure(int i);

    byte getFigureCode(int i);

    BoardRepresentation copy();

    int findPosOfFigure(byte figureCode);

    /**
     * Returns true, if n is an en passant target move field.
     *
     * @param n
     * @return
     */
    boolean isEnPassantCapturePossible(int n);

    /**
     * Returns the en passant capture position, if any...
     *
     * @return
     */
    int getEnPassantCapturePos();


    int getEnPassantMoveTargetPos();

    void setEnPassantOption(int enPassantOption);

    boolean isCastlingAllowed(Color color, RochadeType type);

    void setCastlingAllowed(Color color, RochadeType type);

    byte getCastlingRights();

    long getZobristHash();

    void domove(Move move);

    void undo(Move move);

    void undoNullMove();

    void doNullMove();

    boolean isRepetition();

    /**
     * Returns the inner bitboard representation.
     * @return
     */
    BitChessBoard getBoard();

    boolean isDrawByMaterial();

    boolean isvalidmove(int pvMove);

    Material getMaterial();
}
