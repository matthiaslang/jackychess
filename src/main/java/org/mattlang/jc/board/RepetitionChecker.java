package org.mattlang.jc.board;

/**
 * Tracks state of repetitions during search.
 */
public interface RepetitionChecker {


    void push(Board board);

    void pop();

    boolean isRepetition();
}
