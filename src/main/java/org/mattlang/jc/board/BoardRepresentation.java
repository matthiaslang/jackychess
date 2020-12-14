package org.mattlang.jc.board;

public interface BoardRepresentation {
    void setStartPosition();

    void setPosition(String[] fenPosition);

    void setFenPosition(String fen);

    void setPos(int row, int col, char figureChar);

    void setPos(int index, Figure figure);

    void setPos(int index, byte figure);

    char getPos(int row, int col);

    Figure getPos(int i);

    Figure getFigurePos(int row, int col);

    void clearPosition();

    String toUniCodeStr();

    Move move(Move move);

    byte move(int from, int to);

    Figure getFigure(int i);

    byte getFigureCode(int i);

    Board copy();

    int findPosOfFigure(byte figureCode);
}
