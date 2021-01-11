package org.mattlang.jc;

import java.util.Map;

public interface StatisticsCollector {

    void resetStatistics();

    void collectStatistics(Map stats);
}
