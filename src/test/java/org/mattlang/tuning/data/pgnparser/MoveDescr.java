package org.mattlang.tuning.data.pgnparser;

import lombok.Getter;

@Getter
public class MoveDescr {

    private MoveText moveText;
    private Comment comment;

    public MoveDescr(MoveText moveText, Comment comment) {
        this.moveText = moveText;
        this.comment = comment;
    }
}
