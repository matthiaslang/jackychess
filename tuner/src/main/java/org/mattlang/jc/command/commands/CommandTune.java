package org.mattlang.jc.command.commands;

import static java.util.Arrays.asList;

import java.io.IOException;
import java.util.List;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import org.mattlang.tuning.tuner.LocalOptimizationTuner;
import org.mattlang.tuning.tuner.OptParameters;

@Parameters(commandNames = { CommandTune.CMD_TUNE }, separators = "=", commandDescription = "Tune parameter")
public class CommandTune implements JCTCommand {

    public static final String CMD_TUNE = "tune";

    @Parameter(names = { "--output", "-o" }, description = "Output directory")
    private String outputDir;

    @Parameter(description = "Input Data Files")
    private List<String> files;

    @Override
    public String getCmdName() {
        return CMD_TUNE;
    }

    @Override
    public void executeCommand() {

        OptParameters params = OptParameters.builder()
                .name("tune")
                .evalParamSet("CURRENT")
                .optimizeRecalcOnlyDependendFens(false)
                .resetParametersBeforeTuning(true)
                .adjustK(false)
                .multiThreading(true)
                .threadCount(4)
                .delta(0.00000001)
                .stepGranularity(asList(1))
                .removeDuplicateFens(true)
                .tunePst(true)
                .tuneMaterial(false)
                .tuneAdjustments(true)
                .tuneMobility(true)
                .tunePositional(true)
                .tunePawnEval(true)
                .tunePassedPawnEval(true)
                .tuneKingAttack(true)
                .tuneThreats(true)
                .inputFiles(files)
                .outputdir(outputDir)
                .build();

        try {
            LocalOptimizationTuner.run(params);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }

    }
}
