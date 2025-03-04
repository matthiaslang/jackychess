package org.mattlang.jc.command.commands;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import org.junit.Test;
import org.mattlang.jc.command.Main;
import org.mattlang.tuning.tuner.OptParameters;

public class CommandTuneTest {

    @Test
    public void testTune() throws IOException {
        String[] args = ("tune adjustK delta=0.01 tuneAll exceptions=tuneMat,tunePst "
                + "-o target/tuning/tuneTest src/test/resources/quiet-labeled_debug_short.epd ")
                .split(" ");
        Main.main(args);

        assertThat(new File("target/tuning/tuneTest")).exists();
        assertThat(new File("target/tuning/tuneTest/current")).exists();
        assertThat(new File("target/tuning/tuneTest/current/config.properties")).exists();
        assertThat(new File("target/tuning/tuneTest/current/king")).isDirectoryContaining("glob:**.csv");
        assertThat(new File("target/tuning/tuneTest/current/pawn")).isDirectoryContaining("glob:**.csv");
        assertThat(new File("target/tuning/tuneTest/current/pst")).isDirectoryContaining("glob:**.csv");
        assertThat(new File("target/tuning/tuneTest/current/tune.md")).exists();

        args = ("tune adjustK delta=0.001 tuneAll exceptions=tuneMat,tunePst optimizeMode "
                + "-o target/tuning/tuneTest src/test/resources/quiet-labeled_debug_short.epd ")
                .split(" ");
        Main.main(args);


    }


    @Test
    public void testTuneOptimizingMode() throws IOException {
        String[] args = ("tune delta=0.0001 threads 1 tuneAll "
                + "-o target/tuning/test1 src/test/resources/quiet-labeled_debug.epd")
                .split(" ");
        Main.main(args);

        assertThat(new File("target/tuning/test1")).exists();
        assertThat(new File("target/tuning/test1/current")).exists();
        assertThat(new File("target/tuning/test1/current/config.properties")).exists();
        assertThat(new File("target/tuning/test1/current/king")).isDirectoryContaining("glob:**.csv");
        assertThat(new File("target/tuning/test1/current/pawn")).isDirectoryContaining("glob:**.csv");
        assertThat(new File("target/tuning/test1/current/pst")).isDirectoryContaining("glob:**.csv");
        assertThat(new File("target/tuning/test1/current/tune.md")).exists();

        // do same again: should deliver the same results:
        args = ("tune delta=0.0001 threads 1 tuneAll "
                + "-o target/tuning/test2 src/test/resources/quiet-labeled_debug.epd")
                .split(" ");
        Main.main(args);
        // check some result files: they should be equal:
        assertThat(new File("target/tuning/test2/current/config.properties"))
                .hasSameContentAs(new File("target/tuning/test1/current/config.properties"));
        assertThat(new File("target/tuning/test2/current/king/kingStormNonBlockedEG.csv"))
                .hasSameContentAs(new File("target/tuning/test1/current/king/kingStormNonBlockedEG.csv"));
        assertThat(new File("target/tuning/test2/current/pawn/weakPawnEG.csv"))
                .hasSameContentAs(new File("target/tuning/test1/current/pawn/weakPawnEG.csv"));
        assertThat(new File("target/tuning/test2/current/pst/rookEG.csv"))
                .hasSameContentAs(new File("target/tuning/test1/current/pst/rookEG.csv"));


       args = ("tune delta=0.0001 threads 1 tuneAll optimizeMode "
                + "-o target/tuning/testOpt src/test/resources/quiet-labeled_debug.epd")
                .split(" ");
        Main.main(args);
        assertThat(new File("target/tuning/testOpt/current/config.properties"))
                .hasSameContentAs(new File("target/tuning/test1/current/config.properties"));
        assertThat(new File("target/tuning/testOpt/current/king/kingStormNonBlockedEG.csv"))
                .hasSameContentAs(new File("target/tuning/test1/current/king/kingStormNonBlockedEG.csv"));
        assertThat(new File("target/tuning/testOpt/current/pawn/weakPawnEG.csv"))
                .hasSameContentAs(new File("target/tuning/test1/current/pawn/weakPawnEG.csv"));
        assertThat(new File("target/tuning/testOpt/current/pst/rookEG.csv"))
                .hasSameContentAs(new File("target/tuning/test1/current/pst/rookEG.csv"));
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