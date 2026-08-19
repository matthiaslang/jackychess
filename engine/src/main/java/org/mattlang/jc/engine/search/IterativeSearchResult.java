package org.mattlang.jc.engine.search;

import java.util.List;

import org.mattlang.jc.board.Move;

import lombok.Getter;

@Getter
public class IterativeSearchResult {

    private final Move savedMove;
    private final NegaMaxResult rslt;
    private final String ebfReport;
    private final List<IterativeRoundResult> rounds;

    public IterativeSearchResult(List<IterativeRoundResult> rounds, String ebfReport) {
        this.rounds = rounds;
        this.rslt = findLastResult(rounds);
        this.savedMove = findLastSavedMove(rounds);
        this.ebfReport = ebfReport;
    }

    private Move findLastSavedMove(List<IterativeRoundResult> rounds) {
        NegaMaxResult result = findLastResult(rounds);
        return result != null ? result.savedMove : null;
    }

    private NegaMaxResult findLastResult(List<IterativeRoundResult> rounds) {
        for (int i = rounds.size() - 1; i >= 0; i--) {
            if (rounds.get(i).rslt() != null && rounds.get(i).rslt().savedMove != null) {
                return rounds.get(i).rslt();
            }
        }
        // todo that should probably never be the case; only if we had 0 time to search?
        return null;
    }
}
