package org.mattlang.jc.command.commands;

import static java.util.Arrays.asList;

import java.io.IOException;
import java.util.List;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import org.mattlang.tuning.tuner.LocalOptimizationTuner;
import org.mattlang.tuning.tuner.OptParameters;

import lombok.Getter;

@Getter
@Parameters(commandNames = { CommandTune.CMD_TUNE }, separators = "=", commandDescription = "Tune parameter")
public class CommandTune implements JCTCommand {

    public static final String CMD_TUNE = "tune";
    public static final String TUNE_MAT = "tuneMat";
    public static final String TUNE_ADJ = "tuneAdj";
    public static final String TUNE_MOB = "tuneMob";
    public static final String TUNE_TROP = "tuneTrop";
    public static final String TUNE_POS = "tunePos";
    public static final String TUNE_PAWN = "tunePawn";
    public static final String TUNE_PASSED_PAWN = "tunePassedPawn";
    public static final String TUNE_KING_ATT = "tuneKingAtt";
    public static final String TUNE_KING_SAFETY = "tuneKingSafety";
    public static final String TUNE_COMPLEXITY = "tuneComplexity";
    public static final String TUNE_THREATS = "tuneThreats";
    public static final String TUNE_PST = "tunePst";

    @Parameter(names = { "--output", "-o" }, description = "Output directory")
    private String outputDir;

    @Parameter(description = "Input Data Files")
    private List<String> files;

    @Parameter(names = "adjustK")
    private boolean adjustK = false;

    @Parameter(names = "removeDuplicates")
    boolean removeDuplicates = true;

    @Parameter(names = "threads")
    private int threads = 4;

    @Parameter(names = "delta")
    double delta = 0.00000001;

    @Parameter(names = TUNE_PST)
    boolean tunePst = false;

    @Parameter(names = TUNE_MAT)
    private boolean tuneMat = false;
    @Parameter(names = TUNE_ADJ)
    private boolean tuneAdj = false;
    @Parameter(names = TUNE_MOB)
    private boolean tuneMob = false;
    @Parameter(names = TUNE_TROP)
    private boolean tuneTrop = false;
    @Parameter(names = TUNE_POS)
    private boolean tunePos = false;
    @Parameter(names = TUNE_PAWN)
    private boolean tunePawn = false;
    @Parameter(names = TUNE_PASSED_PAWN)
    private boolean tunePassedPawn = false;
    @Parameter(names = TUNE_KING_ATT)
    private boolean tuneKingAtt = false;
    @Parameter(names = TUNE_KING_SAFETY)
    private boolean tuneKingSafety = false;
    @Parameter(names = TUNE_COMPLEXITY)
    private boolean tuneComplexity = false;
    @Parameter(names = TUNE_THREATS)
    private boolean tuneThreats = false;

    @Parameter(names = "tuneAll")
    private boolean tuneAll = false;

    @Parameter(names = "exceptions")
    private String exceptions = null;

    OptParameters params;

    @Override
    public String getCmdName() {
        return CMD_TUNE;
    }

    @Override
    public void executeCommand() {

        params = buildParams();

        try {
            LocalOptimizationTuner.run(params);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }

    }

    public OptParameters buildParams() {
        return OptParameters.builder()
                .name("tune")
                .evalParamSet("CURRENT")
                .optimizeRecalcOnlyDependendFens(false)
                .resetParametersBeforeTuning(true)
                .adjustK(adjustK)
                .multiThreading(true)
                .threadCount(threads)
                .delta(delta)
                .stepGranularity(asList(1))
                .removeDuplicateFens(removeDuplicates)
                .tunePst(effectiveTuneFlag(tunePst, TUNE_PST))
                .tuneMaterial(effectiveTuneFlag(tuneMat, TUNE_MAT))
                .tuneAdjustments(effectiveTuneFlag(tuneAdj, TUNE_ADJ))
                .tuneMobility(effectiveTuneFlag(tuneMob, TUNE_MOB))
                .tuneMobilityTropism(effectiveTuneFlag(tuneTrop, TUNE_TROP))
                .tunePositional(effectiveTuneFlag(tunePos, TUNE_POS))
                .tunePawnEval(effectiveTuneFlag(tunePawn, TUNE_PAWN))
                .tunePassedPawnEval(effectiveTuneFlag(tunePassedPawn, TUNE_PASSED_PAWN))
                .tuneKingAttack(effectiveTuneFlag(tuneKingAtt, TUNE_KING_ATT))
                .tuneKingSafety(effectiveTuneFlag(tuneKingSafety, TUNE_KING_SAFETY))
                .tuneComplexity(effectiveTuneFlag(tuneComplexity, TUNE_COMPLEXITY))
                .tuneThreats(effectiveTuneFlag(tuneThreats, TUNE_THREATS))
                .inputFiles(files)
                .outputdir(outputDir)
                .build();
    }

    private boolean effectiveTuneFlag(boolean tunePst, String exceptionTag) {
        if (exceptions != null && exceptions.contains(exceptionTag)) {
            return false;
        }
        return tunePst || tuneAll;
    }
}
