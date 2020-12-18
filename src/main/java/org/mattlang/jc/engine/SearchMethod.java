package org.mattlang.jc.engine;

import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;

public interface SearchMethod {

    Move search(GameState gameState, int depth);
}
