package org.mattlang.jc.board;

/**
 * Tracks state of repetitions during search.
 */
public interface RepetitionChecker {


    void push(BoardRepresentation board);

    void pop();

    boolean isRepetition();
}
