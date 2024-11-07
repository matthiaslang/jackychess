package org.mattlang.jc.board;

/**
 * Factory to create a board implementation.
 * Used to configure Factories and implementations of Boards via ServiceLoader.
 */
public interface BoardFactory {

    String getBoardImplName();

    /**
     * Creates a new instance of a board.
     *
     * @return
     */
    BoardRepresentation createBoard();

}
