package org.mattlang.jc.command.commands;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Optional;

import org.junit.Test;
import org.mattlang.jc.command.Main;
import org.mattlang.tuning.tuner.OptParameters;

public class CommandTuneTest {

    @Test
    public void testTune() throws IOException {
        String[] args =
                "tune adjustK delta=0.001 tuneAll exceptions=tuneMat,tunePst -o target/testtuningoutput src/test/resources/quiet-labeled_debug_short.epd ".split(
                        " ");
        Main.main(args);
    }

    @Test
    public void parseTuneAndExceptArgs() {
        String[] args =
                "tune adjustK delta=0.001 tuneAll exceptions=tuneMat,tunePst -o target/testtuningoutput src/test/resources/quiet-labeled_debug_short.epd ".split(
                        " ");
        Optional<JCTCommand> optCmd = Main.parseCommandFromArgs(args);
        assertThat(optCmd).isPresent();
        JCTCommand cmd = optCmd.get();
        assertThat(cmd).isInstanceOf(CommandTune.class);
        CommandTune cmdTune = (CommandTune) cmd;
        OptParameters params = cmdTune.buildParams();
        assertThat(params.isTunePst()).isFalse();
        assertThat(params.isTuneMaterial()).isFalse();
        assertThat(cmdTune.isTuneAll()).isTrue();
        assertThat(params.isTuneAdjustments()).isTrue();
        assertThat(params.isTuneComplexity()).isTrue();
        assertThat(params.isTuneMobility()).isTrue();
        assertThat(params.isTunePawnEval()).isTrue();
        assertThat(params.isTunePassedPawnEval()).isTrue();
        assertThat(params.isTuneKingAttack()).isTrue();
        assertThat(params.isTuneKingSafety()).isTrue();
        assertThat(params.isTuneThreats()).isTrue();
        assertThat(params.isTuneMobilityTropism()).isTrue();
    }
}