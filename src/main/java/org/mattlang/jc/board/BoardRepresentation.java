package org.mattlang.jc.board;

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

    void move(Move move);

    void move(int from, int to);

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

    void setCastlingRights(byte castlingRights);


    PieceList getBlackPieces();

    PieceList getWhitePieces();

    long getZobristHash();
}
