package org.mattlang.jc.engine.evaluation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class ToolsTest {

    @Test
    public void testManhattenDistance() {
        assertThat(Tools.manhattanDistance(0, 63)).isEqualTo(14);
        assertThat(Tools.manhattanDistance(0, 1)).isEqualTo(1);
        assertThat(Tools.manhattanDistance(0, 8)).isEqualTo(1);
        assertThat(Tools.manhattanDistance(0, 9)).isEqualTo(2);
        assertThat(Tools.manhattanDistance(0, 10)).isEqualTo(3);

    }
}