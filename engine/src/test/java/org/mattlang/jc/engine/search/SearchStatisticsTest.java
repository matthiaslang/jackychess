package org.mattlang.jc.engine.search;

import org.junit.Test;

public class SearchStatisticsTest {

    @Test
    public void fmtStats() {
        SearchStatistics searchStatistics = new SearchStatistics();
        searchStatistics.deltaCutoffCount = 400;
        searchStatistics.quiescenceNodesVisited = 5000;

        System.out.println(searchStatistics.formatStats("statistics"));
    }
}