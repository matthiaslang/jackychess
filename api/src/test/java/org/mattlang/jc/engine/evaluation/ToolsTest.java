package org.mattlang.jc.engine.evaluation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.mattlang.jc.board.Tools;

public class ToolsTest {

    @Test
    public void testManhattenDistance() {
        assertThat(Tools.calcManhattanDistance(0, 63)).isEqualTo(14);
        assertThat(Tools.calcManhattanDistance(0, 1)).isEqualTo(1);
        assertThat(Tools.calcManhattanDistance(0, 8)).isEqualTo(1);
        assertThat(Tools.calcManhattanDistance(0, 9)).isEqualTo(2);
        assertThat(Tools.calcManhattanDistance(0, 10)).isEqualTo(3);

    }

}