package org.mattlang.jc.board.bitboard;

import org.mattlang.jc.board.BoardFactory;
import org.mattlang.jc.board.BoardRepresentation;

public class BitBoardFactory implements BoardFactory {

    @Override
    public String getBoardImplName() {
        return "BitBoard";
    }

    @Override
    public BoardRepresentation createBoard() {
        return new BitBoard();
    }
}
