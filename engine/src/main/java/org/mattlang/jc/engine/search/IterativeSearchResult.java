package org.mattlang.jc.engine.search;

import java.util.List;

import org.mattlang.jc.board.Move;

import lombok.Getter;

@Getter
public class IterativeSearchResult {

    private Move savedMove;
    private NegaMaxResult rslt;
    private String ebfReport;
    private List<IterativeDeepeningPVS.IterativeRoundResult> rounds;

    public IterativeSearchResult(List<IterativeDeepeningPVS.IterativeRoundResult> rounds, String ebfReport) {
        this.rounds = rounds;
        this.rslt = findLastResult(rounds);
        this.savedMove = findLastSavedMove(rounds);
        this.ebfReport = ebfReport;
    }

    private Move findLastSavedMove(List<IterativeDeepeningPVS.IterativeRoundResult> rounds) {
        NegaMaxResult result = findLastResult(rounds);
        return result != null ? result.savedMove : null;
    }

    private NegaMaxResult findLastResult(List<IterativeDeepeningPVS.IterativeRoundResult> rounds) {
        for (int i = rounds.size() - 1; i >= 0; i--) {
            if (rounds.get(i).getRslt() != null && rounds.get(i).getRslt().savedMove != null) {
                return rounds.get(i).getRslt();
            }
        }
        // todo that should probably never be the case; only if we had 0 time to search?
        return null;
    }
}
