package org.mattlang.jc.uci;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.mattlang.jc.ConfigValues;

public class UciProcessorTest {

    @Test
    public void optionsParsing() {
        UciProcessor ucip = new UciProcessor();
        ucip.parseOption("setoption name quiescence value 16");
        assertThat(ConfigValues.getConfigValues().maxQuiescence.getValue()).isEqualTo(16);
        ConfigValues.resetConfigValues();
    }

    @Test
    public void goParameterParsing() {
        UciProcessor ucip = new UciProcessor();
        GoParameter goParams = ucip.parseGoParams("go wtime 567860 btime 584661 winc  0 binc 0 movestogo 39");
        assertThat(goParams.infinite).isFalse();
        assertThat(goParams.wtime).isEqualTo(567860L);
        assertThat(goParams.btime).isEqualTo(584661L);
        assertThat(goParams.winc).isEqualTo(0L);
        assertThat(goParams.binc).isEqualTo(0L);
        assertThat(goParams.movestogo).isEqualTo(39L);
    }

    @Test
    public void goParameterParsing2() {
        UciProcessor ucip = new UciProcessor();
        GoParameter goParams = ucip.parseGoParams("go wtime 19527 btime  21579 winc 1000 binc 1000");
        assertThat(goParams.infinite).isFalse();
        assertThat(goParams.wtime).isEqualTo(19527L);
        assertThat(goParams.btime).isEqualTo(21579L);
        assertThat(goParams.winc).isEqualTo(1000L);
        assertThat(goParams.binc).isEqualTo(1000L);
        assertThat(goParams.movestogo).isEqualTo(0L);
    }

    @Test
    public void goInfiniteParameterParsing1() {
        UciProcessor ucip = new UciProcessor();
        GoParameter goParams = ucip.parseGoParams("go infinite");
        assertThat(goParams.infinite).isTrue();
    }

    @Test
    public void goParameterInfiniteParsing2() {
        UciProcessor ucip = new UciProcessor();
        // test overread unknown directive showeval:
        GoParameter goParams = ucip.parseGoParams("go showeval infinite");
        assertThat(goParams.infinite).isTrue();
    }

    @Test
    public void goInfiniteAndSearchMovesParsing() {
        UciProcessor ucip = new UciProcessor();
        GoParameter goParams = ucip.parseGoParams("go infinite searchmoves e2e4 g1f3 ");
        assertThat(goParams.infinite).isTrue();
        assertThat(goParams.searchMoves).containsExactly("e2e4", "g1f3");
    }
}