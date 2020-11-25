package org.mattlang.jc.engine.search;

import java.util.List;
import java.util.Random;

import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.MoveGenerator;
import org.mattlang.jc.engine.SearchMethod;

public class RandomSearchMethod implements SearchMethod {

    @Override
    public Move search(Board currBoard, int depth, Color color) {
        MoveGenerator moveGenerator = new MoveGenerator();
        List<Move> moves = moveGenerator.generate(currBoard, color);
        Move move = moves.get(new Random().nextInt(moves.size()));
        return move;
    }
}
