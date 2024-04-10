package org.mattlang.tuning.evaluate;

import static java.util.stream.Collectors.joining;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;

import org.mattlang.jc.engine.evaluation.annotation.configure.ConfigFilter;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;
import org.mattlang.tuning.TuningParameter;
import org.mattlang.tuning.TuningParameterGroup;
import org.mattlang.tuning.tuner.OptParameters;

import lombok.Getter;

/**
 * Bundles all parameters to tune.
 */
public class ParameterSet {

    private static final Function<Integer, Boolean> ONLY_PAWN_FIELDS = pos -> pos >= 0 + 8 && pos <= 64 - 8;

    /**
     * all parameter groups.
     */
    private ArrayList<TuningParameterGroup> groups = new ArrayList<>();

    /**
     * flat redundant list of all parameters of all groups.
     */
    @Getter
    private ArrayList<TuningParameter> params = new ArrayList<>();

    /**
     * Inits a parameter set by its tuning parameter definitions and with values read from a parameterized
     * evaluation.
     *
     * @param optParams
     * @param parameterizedEvaluation
     */
    public ParameterSet(OptParameters optParams, ParameterizedEvaluation parameterizedEvaluation) {

        TuningParamConfigurator tuningParamConfigurator = new TuningParamConfigurator();
        ConfigFilter configFilter = new ConfigFilter();

        if (optParams.isTunePawnEval()) {
            configFilter.addFilter("pawn\\..*");
        }

        if (optParams.isTunePassedPawnEval()) {
            configFilter.addFilter("pawn\\.passedPawn\\..*");
        }

        if (optParams.isTuneAdjustments()) {
            configFilter.addFilter("adjustments\\..*(tempo|Pair).*");
        }
        if (optParams.isTuneAdjustmentsFactors()) {
            configFilter.addFilter("adjustments\\..*Adj");
        }

        if (optParams.isTuneMaterial()) {
            configFilter.addFilter("mat\\..*");
        }

        if (optParams.isTunePst()) {
            configFilter.addFilter("pst\\..*");
        }

        if (optParams.isTuneMobility()) {
            configFilter.addFilter("mob\\..*Mob.*");
        }
        if (optParams.isTuneMobilityTropism()) {
            configFilter.addFilter("mob\\..*(Tropism).*");
        }
        if (optParams.isTuneThreats()) {
            configFilter.addFilter("threats\\..*");
        }
        if (optParams.isTuneKingSafety()) {
            configFilter.addFilter("king\\..*");
        }
        if (optParams.isTuneComplexity()) {
            configFilter.addFilter("complexity\\..*");
        }

        if (optParams.isTunePositional()) {
            configFilter.addFilter("mob\\.positional\\..*");
        }

        if (optParams.isTuneKingAttack()) {
            configFilter.addFilter("mob\\..*KingAttack.*");
        }

        groups.addAll(tuningParamConfigurator.configure(parameterizedEvaluation, configFilter));

        createRedundantParamsList();

    }

    private ParameterSet() {

    }

    private void createRedundantParamsList() {
        params.clear();

        for (TuningParameterGroup group : groups) {
            params.addAll(group.getParameters());
        }
        for (int i = 0; i < params.size(); i++) {
            params.get(i).setParamNo(i);
        }
    }

    public String collectParamDescr() {
        // make them distinct (since some params may be related and return a description of the whole related parameters:
        Set<String> distinctSet = new LinkedHashSet<>();
        for (TuningParameterGroup group : groups) {
            distinctSet.add(group.getParamDef());
        }
        return distinctSet.stream()
                .collect(joining("\n"));
    }

    public void writeParamDescr(File outputDir) {
        outputDir.mkdirs();
        for (TuningParameterGroup group : groups) {
            group.writeParamDef(outputDir);
        }
    }

    public ParameterSet copy() {
        ParameterSet c = new ParameterSet();
        for (TuningParameterGroup group : groups) {
            c.groups.add(group.copy());
        }
        c.createRedundantParamsList();
        return c;
    }
}
