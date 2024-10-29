package org.mattlang.tuning.data.pgnparser;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Move;

import lombok.Getter;

@Getter
public class MoveDescr {

    private MoveText moveText;
    private Comment comment;

    private Ending ending;

    public MoveDescr(MoveText moveText, Comment comment, Ending ending) {
        this.moveText = moveText;
        this.comment = comment;
        this.ending = ending;
    }

    public Move createMove(BoardRepresentation board) {
        return AlgebraicNotation.moveFromAN(board, board.getSiteToMove(), moveText);
    }
}
