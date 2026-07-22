package org.mattlang.tuning.data.pgnparser;

import java.util.List;
import java.util.stream.Collectors;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Move;

import lombok.Getter;

@Getter
public class MoveDescr {

    private MoveText moveText;
    private List<Comment> comments;

    private Ending ending;

    public MoveDescr(MoveText moveText, List<Comment> comments, Ending ending) {
        this.moveText = moveText;
        this.comments = comments;
        this.ending = ending;
    }

    public Move createMove(BoardRepresentation board) {
        return AlgebraicNotation.moveFromAN(board, board.getSiteToMove(), moveText);
    }

    public String getAllComments() {
        return comments.stream().map(Comment::getText).collect(Collectors.joining());
    }
}
