package org.mattlang.jc.play;

import lombok.Getter;

@Getter
public class GameStatusResult {

    private GameStatus status;

    private EndStatus endStatus;

    public GameStatusResult() {
        this.status = GameStatus.RUNNING;
    }

    public GameStatusResult(EndStatus endStatus) {
        this.status = GameStatus.FINISHED;
        this.endStatus = endStatus;
    }

    public boolean isEnd() {
        return endStatus != null;
    }
}
