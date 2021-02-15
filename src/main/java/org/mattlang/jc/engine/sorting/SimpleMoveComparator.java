package org.mattlang.jc.engine.sorting;

import static org.mattlang.jc.board.FigureConstants.MASK_OUT_COLOR;

import java.util.Comparator;

import org.mattlang.jc.board.FigureConstants;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.search.PVList;

public class SimpleMoveComparator implements Comparator<Move> {

    private final Move pvMove;

    public SimpleMoveComparator(final PVList prevPvlist, final int depth, int targetDepth) {
        int index = targetDepth - depth;
        pvMove =prevPvlist != null ? prevPvlist.get(index) : null;
    }

    @Override
    public int compare(Move o1, Move o2) {
        if (o1.getOrder() == 0) {
            o1.setOrder(fullCalcCmpVal(o1));
        }
        if (o2.getOrder() == 0) {
            o2.setOrder(fullCalcCmpVal(o2));
        }

        return o1.getOrder() - o2.getOrder();
    }

    private int fullCalcCmpVal(Move m) {
        if (pvMove != null) {
            if (pvMove.toStr().equals(m.toStr())) {
                return -1000000;
            }
        }
        return simpleCmpVal(m);
    }

    private int simpleCmpVal(Move m) {
        byte figureType = m.getFigureType();
        if (m.getCapturedFigure() != 0 && m.getCapturedFigure() != FigureConstants.FT_EMPTY) {
            byte captFigureType = (byte) (m.getCapturedFigure() & MASK_OUT_COLOR);

            if (figureType > captFigureType) {
                return -500 + (figureType - captFigureType);
            }
            if (captFigureType > figureType) {
                return -10000 - (captFigureType - figureType);
            }
            return -700;
        }
        return -figureType;
        //                if (m.getFigureType() == FigureConstants.FT_PAWN) {
        //                    return -3;
        //                }
        //                return  0;
    }
}
