package org.mattlang.jc.uci;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class UciProcessorTest {

    @Test
    public void optionsParsing() {
        UciProcessor ucip = new UciProcessor();
        ucip.parseOption("setoption name thinktime value 16000");
        assertThat(ucip.getConfigValues().timeout.getValue()).isEqualTo( 16000);
    }

    @Test
    public void goParameterParsing() {
        UciProcessor ucip = new UciProcessor();
        GoParameter goParams = ucip.parseGoParams("go wtime 567860 btime 584661 winc 0 binc 0 movestogo 39");
        assertThat(goParams.infinite).isFalse();
        assertThat(goParams.wtime).isEqualTo(567860L);
        assertThat(goParams.btime).isEqualTo(584661L);
        assertThat(goParams.winc).isEqualTo(0L);
        assertThat(goParams.binc).isEqualTo(0L);
        assertThat(goParams.movestogo).isEqualTo(39L);
    }


    @Test
    public void goParameterParsin2() {
        UciProcessor ucip = new UciProcessor();
        GoParameter goParams = ucip.parseGoParams("go wtime 19527 btime 21579 winc 1000 binc 1000");
        assertThat(goParams.infinite).isFalse();
        assertThat(goParams.wtime).isEqualTo(19527L);
        assertThat(goParams.btime).isEqualTo(21579L);
        assertThat(goParams.winc).isEqualTo(1000L);
        assertThat(goParams.binc).isEqualTo(1000L);
        assertThat(goParams.movestogo).isEqualTo(0L);
    }

    @Test
    public void goParameterParsing2() {
        UciProcessor ucip = new UciProcessor();
        GoParameter goParams = ucip.parseGoParams("go infinite");
        assertThat(goParams.infinite).isTrue();
    }
}