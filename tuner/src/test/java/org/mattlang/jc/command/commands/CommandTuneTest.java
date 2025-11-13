package org.mattlang.jc.command.commands;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.command.Main;
import org.mattlang.jc.engine.Configurator;
import org.mattlang.jc.engine.TuningCache;
import org.mattlang.jc.engine.evaluation.parameval.EvalConfig;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;
import org.mattlang.jc.uci.FenParser;
import org.mattlang.tuning.tuner.LocalOptimizationTuner;
import org.mattlang.tuning.tuner.OptParameters;

public class CommandTuneTest {

    @Test
    public void testTuningAndRetuning() throws IOException {
        Main.main(args("tune delta=0.001 threads 1 tuneAll exceptions=tuneMat,tunePst steps 50,20,5,1 "
                       + "-o target/tuning/tuneTest src/test/resources/quiet-labeled_debug.epd"));

        assertOutputFilesExist("target/tuning/tuneTest");

        // continue and retune further:
        Main.main(args("tune delta=0.0001 threads 1 tuneAll exceptions=tuneMat,tunePst steps 50,20,5,1 "
                       + "-o target/tuning/tuneTest src/test/resources/quiet-labeled_debug.epd"));

    }

    /**
     * Tests that multithreading is delivering the same values as single threaded.
     * Normally ignored, since it take 10min to finish the test.
     *
     * @throws IOException
     */
    @Test
    @Disabled
    public void testCompareMultithreadingTuning() throws IOException {
        Main.main(args("tune delta=0.001 threads 1 tuneMat tunePst steps 50,20,5,1 "
                       + "-o target/tuning/testthread1 src/test/resources/quiet-labeled.epd"));

        // do same again: should deliver the same results:
        Main.main(args("tune delta=0.001 threads 4 tuneMat tunePst steps 50,20,5,1 "
                       + "-o target/tuning/testthread4 src/test/resources/quiet-labeled.epd"));
        // check some result files: they should be equal:
        assertSameFileContent("target/tuning/testthread4", "target/tuning/testthread1");

    }

    @Test
    public void testTuneOptimizingMode() throws IOException {
        Main.main(args("tune delta=0.0001 threads 1 tuneAll "
                       + "-o target/tuning/test1 src/test/resources/quiet-labeled_debug.epd"));

        assertOutputFilesExist("target/tuning/test1");

        // do same again: should deliver the same results:
        Main.main(args("tune delta=0.0001 threads 1 tuneAll "
                       + "-o target/tuning/test2 src/test/resources/quiet-labeled_debug.epd"));
        // check some result files: they should be equal:
        assertSameFileContent("target/tuning/test2", "target/tuning/test1");

        Main.main(args("tune delta=0.0001 threads 1 tuneAll optimizeMode "
                       + "-o target/tuning/testOpt src/test/resources/quiet-labeled_debug.epd"));

        assertSameFileContent("target/tuning/testOpt", "target/tuning/test1");

    }

    @Test
    @Disabled
    public void testTuneOptimizingModeBig() throws IOException {

        Main.main(args("tune delta=0.00001 threads 4 tuneAll exceptions=tuneMat steps 50,20 "
                       + "-o target/tuning/test1 src/test/resources/quiet-labeled.epd"));

        assertOutputFilesExist("target/tuning/test1");

        Main.main(args("tune delta=0.00001 threads 4 tuneAll exceptions=tuneMat steps 50,20 optimizeMode "
                       + "-o target/tuning/testOpt src/test/resources/quiet-labeled.epd"));

        assertSameFileContent("target/tuning/testOpt", "target/tuning/test1");

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

    @Test
    public void writeAndReadConfig() {

        OptParameters params = OptParameters.builder()
                .outputdir("target/tuning/configTest")
                .build();
        LocalOptimizationTuner tuner = new LocalOptimizationTuner(params);

        ParameterizedEvaluation evaluate =
                new ParameterizedEvaluation();

        File outputDir = new File("target/tuning/configTest");
        tuner.copySourceConfigFile(outputDir, evaluate);
        assertOutputConfigFilesExist("target/tuning/configTest");

        // now read the written configuration:
        EvalConfig config = new EvalConfig(outputDir);
        ParameterizedEvaluation evaluateRead = new ParameterizedEvaluation(config, true);
    }

    @Test
    public void testOptimizeModeEvaluation() {
        BoardRepresentation board = Configurator.createBoard();
        FenParser parser = new FenParser();
        GameState gameState =
                parser.setPosition("position fen 1nbqkbnr/r3P3/7P/pB3N2/P7/8/1PP3PP/RNBQ1RK1 b k - 2 17 ", board);

        System.out.println(board.toUniCodeStr());

        ParameterizedEvaluation evaluation = ParameterizedEvaluation.createForTuning(new EvalConfig(), true);

        assertThat(evaluation.getTuningCache().getEvalSum()).isEqualTo(0);
        int eval = evaluation.eval(board, Color.WHITE.ordinal());

        assertThat(evaluation.getTuningCache().getEvalSum()).isNotEqualTo(0);
        int eval2 = evaluation.eval(board, Color.WHITE.ordinal());
        assertThat(eval2).isEqualTo(eval);

        evaluation.getTuningCache().clear(TuningCache.EvalComponentName.PAWN);
        eval2 = evaluation.eval(board, Color.WHITE.ordinal());
        assertThat(eval2).isEqualTo(eval);
    }

    private void assertOutputFilesExist(String targetDir) {
        assertOutputConfigFilesExist(targetDir);
        assertThat(new File(targetDir + "/tune.md")).exists();
    }

    private void assertOutputConfigFilesExist(String targetDir) {
        assertThat(new File(targetDir)).exists();
        assertThat(new File(targetDir + "/config.properties")).exists();
        assertThat(new File(targetDir + "/king")).isDirectoryContaining("glob:**.csv");
        assertThat(new File(targetDir + "/pawn")).isDirectoryContaining("glob:**.csv");
        assertThat(new File(targetDir + "/pst")).isDirectoryContaining("glob:**.csv");
    }

    private void assertSameFileContent(String dir1, String dir2) {
        assertThat(new File(dir1 + "/config.properties"))
                .hasSameContentAs(new File(dir2 + "/config.properties"));
        File fdir1 = new File(dir1);
        File fdir2 = new File(dir2);
        assertSameSubFiles(new File(fdir1, "king"), new File(fdir2, "king"));
        assertSameSubFiles(new File(fdir1, "pawn"), new File(fdir2, "pawn"));
        assertSameSubFiles(new File(fdir1, "pst"), new File(fdir2, "pst"));
    }

    private void assertSameSubFiles(File d1, File d2) {
        File[] files1 = d1.listFiles();
        File[] files2 = d2.listFiles();
        assertThat(files1.length).isEqualTo(files2.length);
        Arrays.sort(files1);
        Arrays.sort(files2);
        for (int i = 0; i < files1.length; i++) {
            assertThat(files1[i]).hasSameContentAs(files2[i]);
        }
    }

    public static String[] args(String str) {
        return Arrays.stream(str.split(" "))
                .map(s -> s.trim())
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new);
    }
}