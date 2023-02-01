package org.mattlang.jc.board;

import org.mattlang.jc.board.bitboard.BitChessBoard;
import org.mattlang.jc.board.bitboard.BoardCastlings;
import org.mattlang.jc.material.Material;
import org.mattlang.jc.moves.CastlingMove;

public interface BoardRepresentation {
    void setStartPosition();

    void setPosition(String[] fenPosition);

    GameState setFenPosition(String fen);

    void setPos(int row, int col, char figureChar);

    void setPos(int index, Figure figure);

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

    void setCastlingAllowed(CastlingType castlingType, CastlingMove castlingMove);

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

    boolean isvalidmove(Color color, int pvMove);

    Material getMaterial();

    BoardCastlings getBoardCastlings();

    void clearCastlingRights();

    boolean isChess960();

    void setChess960(boolean chess960);

}
