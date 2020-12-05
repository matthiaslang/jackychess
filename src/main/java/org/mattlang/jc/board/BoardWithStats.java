package org.mattlang.jc.board;

import org.mattlang.jc.engine.evaluation.MaterialStats;

public class BoardWithStats extends Board {

    private MaterialStats materialStats = new MaterialStats();

    public BoardWithStats() {
    }

    public BoardWithStats(byte[] board, Rochade whiteRochade, Rochade blackRochace) {
        super(board, whiteRochade, blackRochace);
        materialStats.init(this);
    }

    @Override
    public void setPos(int row, int col, char figureChar) {
        super.setPos(row, col, figureChar);
        materialStats.add(Figure.getFigureByCode(Figure.convertFigureChar(figureChar)));
    }

    @Override
    public void setPos(int index, Figure figure) {
        super.setPos(index, figure);
        materialStats.add(figure);
    }

    @Override
    public void setPos(int index, byte figure) {
        super.setPos(index, figure);
        materialStats.add(Figure.getFigureByCode(figure));
    }

    @Override
    public void clearPosition() {
        super.clearPosition();
        materialStats.clear();
    }

    @Override
    public byte move(int from, int to) {
        materialStats.remove(getPos(to));
        return super.move(from, to);

    }

    public int[][] getCounts() {
        return materialStats.getCounts();
    }

    @Override
    public Board copy() {
        return super.copy();
    }
}
