package org.mattlang.attic;

import org.mattlang.jc.board.BoardRepresentation;

/**
 * Tracks state of repetitions during search.
 */
@Deprecated
public interface RepetitionChecker {


    void push(BoardRepresentation board);

    void pop();

    boolean isRepetition();
}
