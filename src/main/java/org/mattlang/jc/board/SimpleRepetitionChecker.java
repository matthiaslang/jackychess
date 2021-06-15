package org.mattlang.jc.board;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class SimpleRepetitionChecker implements RepetitionChecker {

    private Stack<Long> boardStates = new Stack<>();
    private Map<Long, Integer> repCounts = new HashMap<>();

    @Override
    public void push(BoardRepresentation board) {
        long hash = board.getZobristHash();
        boardStates.push(hash);
        Integer count = repCounts.get(hash);
        if (count == null) {
            repCounts.put(hash, 1);
        } else {
            repCounts.put(hash, count + 1);
        }
    }

    @Override
    public void pop() {
        long hash = boardStates.pop();
        Integer count = repCounts.get(hash);
        if (count == 1) {
            repCounts.remove(hash);
        } else {
            repCounts.put(hash, count - 1);
        }
    }

    @Override
    public boolean isRepetition() {
        // no states (changes) happened yet, so no repetition:
        if (boardStates.size() == 0) {
            return false;
        }
        return repCounts.get(boardStates.peek()).intValue() == 3;
    }
}
