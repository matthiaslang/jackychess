package org.mattlang.jc.engine.evaluation;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;

public class PattChecker {

    private BoardStatsGenerator statsgenerator = Factory.getDefaults().boardStatsGenerator.instance();

    public final int eval(BoardRepresentation currBoard, Color who2Move) {

        BoardStats wstats = statsgenerator.gen(currBoard, Color.WHITE);
        BoardStats bstats = statsgenerator.gen(currBoard, Color.BLACK);

        int pattCheck = PattChecking.pattCheck(wstats, bstats, who2Move);
        return pattCheck;

    }

}
