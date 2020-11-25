package org.mattlang.jc.engine;

import java.util.List;
import java.util.Random;

import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.uci.UCI;

public class Engine {

    private Board board = new Board();

    public void go() {
        MoveGenerator moveGenerator = new MoveGenerator();
        List<Move> moves = moveGenerator.generate(board, Color.WHITE);
        Move move = moves.get(new Random().nextInt(moves.size()));
        UCI.instance.putCommand("bestmove " + move.toStr());
    }

    public void stop() {
        MoveGenerator moveGenerator = new MoveGenerator();
        List<Move> moves = moveGenerator.generate(board, Color.WHITE);
        Move move = moves.get(new Random().nextInt(moves.size()));
        UCI.instance.putCommand("bestmove " + move.toStr());
    }

    public void setStartPosition() {
        board.setStartPosition();
    }

    public void move(Move move) {
        board.move(move);
    }

    public void clearPosition() {
        board.clearPosition();
    }

    public void setPosition(String[] fenPosition) {
        board.setPosition(fenPosition);
    }
}
