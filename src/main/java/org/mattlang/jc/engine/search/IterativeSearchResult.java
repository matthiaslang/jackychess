package org.mattlang.jc.engine.search;

import java.util.List;

import org.mattlang.jc.board.Move;

import lombok.Getter;

@Getter
public class IterativeSearchResult {

    private Move savedMove;
    private NegaMaxResult rslt;

    private List<IterativeDeepeningPVS.IterativeRoundResult> rounds;

    public IterativeSearchResult(List<IterativeDeepeningPVS.IterativeRoundResult> rounds) {
        this.rounds = rounds;
        this.rslt = findLastResult(rounds);
        this.savedMove = findLastSavedMove(rounds);
    }

    private Move findLastSavedMove(List<IterativeDeepeningPVS.IterativeRoundResult> rounds) {
        for (int i = rounds.size() - 1; i >= 0; i--) {
            IterativeDeepeningPVS.IterativeRoundResult round = rounds.get(i);
            if (round.getRslt() != null && round.getRslt().savedMove != null) {
                return round.getRslt().savedMove;
            }
        }
        // todo that should probably never be the case; only if we had 0 time to search?
        return null;
    }

    private NegaMaxResult findLastResult(List<IterativeDeepeningPVS.IterativeRoundResult> rounds) {
        for (int i = rounds.size() - 1; i >= 0; i--) {
            if (rounds.get(i).getRslt() != null) {
                return rounds.get(i).getRslt();
            }
        }
        // todo that should probably never be the case; only if we had 0 time to search?
        return null;
    }
}
