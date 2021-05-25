package org.mattlang.jc.engine;

import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.uci.GameContext;

public interface SearchMethod {

    Move search(GameState gameState, GameContext context, int depth);
}
