package org.mattlang.jc.board;

import java.util.Arrays;

public class PieceList {

    private Array pawns = new Array(8);
    private Array bishops = new Array(9);
    private Array knights = new Array(9);
    private Array rooks = new Array(9);
    private Array queens = new Array(9);

    private Array[] arrays = {pawns, knights, bishops, rooks, queens};

    private int king;

    public void clean() {
        pawns.clean();
        bishops.clean();
        knights.clean();
        rooks.clean();
        queens.clean();
    }

    public void set(int pos, byte figureCode) {
        if (figureCode == FigureConstants.FT_KING) {
            king = pos;
        } else {
            arrays[figureCode].insert(pos);
        }
    }

    public void remove(int pos, byte figureCode) {
        if (figureCode == FigureConstants.FT_KING) {
            king = -1;
        } else {
            arrays[figureCode].remove(pos);
        }
    }

    public static class Array {
        int[] arr;
        private int size = 0;

        public Array(int maxSize) {
            arr = new int[maxSize];
            Arrays.fill(arr, -1);
        }

        public final void insert(int val) {
            arr[size] = val;
            size++;
        }

        public final void remove(int val) {
            for (int i = 0; i < size; i++) {
                if (arr[i] == val) {
                    removeFromArray(i);
                    break;
                }
            }
        }

        private void removeFromArray(int pos) {
            arr[pos] = -1;
            for (int i = pos; i < size - 1; i++) {
                arr[i] = arr[i + 1];
            }
            arr[size-1] = -1;
            size--;
        }

        public final void clean() {
            Arrays.fill(arr, -1);
            size = 0;
        }

        public final int size() {
            return size;
        }
    }

    public final Array getPawns() {
        return pawns;
    }

    public final Array getBishops() {
        return bishops;
    }

    public final Array getKnights() {
        return knights;
    }

    public final Array getRooks() {
        return rooks;
    }

    public final Array getQueens() {
        return queens;
    }

    public final int getKing() {
        return king;
    }

    public final PieceList setKing(int king) {
        this.king = king;
        return this;
    }
}
