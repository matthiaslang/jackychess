package org.mattlang.tuning.tuner;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import org.mattlang.jc.tools.MarkdownTable;
import org.mattlang.jc.tools.MarkdownWriter;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OptParameters {

    /**
     * Tuning name to have a unique name to reference to when continuing tuning, etc.
     */
    private String name;

    /**
     * the paramset name to tune.
     */
    private String evalParamSet = "TUNED01";
    private final List<String> inputFiles;

    /**
     * optimization to recalc only the fens which depend on that particular parameter.
     */
    private boolean optimizeRecalcOnlyDependendFens = false;

    private boolean multiThreading = true;

    private boolean adjustK = false;

    private boolean tuneMaterial = true;

    /**
     * safety delta value to ensure that error is not only better by a minor calculation precision issue.
     */
    private double delta = 0.00000001;
    /**
     * Steps for increment which should be used in sequence to optimize the error value.
     */
    private List<Integer> stepGranularity = Arrays.asList( /*20, 15, 10,*/ 5, 3, 1);

    private boolean tuneMobility = true;
    private boolean tuneThreats = true;
    private boolean tunePositional = true;
    private boolean tunePawnEval = true;
    private boolean tunePassedPawnEval = true;
    private boolean tuneKingAttack = true;
    private boolean tunePst = true;
    private boolean tuneAdjustments = true;

    private boolean removeDuplicateFens = true;

    private int threadCount = 7;

    /**
     * shuffle parameters before each optimization round to randomize local optimization.
     */
    private boolean shuffleTuningParameter = false;

    /**
     * reset all parameter values before starting tuning?
     */
    private boolean resetParametersBeforeTuning = false;

    /**
     * Genetic: Eval Configs to start from. Each of them will build one Individual of the start population.
     * The Rest of the population will be filled with random start data.
     */
    private List<String> startEvalConfigs;

    /**
     * Genetic: population size.
     */
    private int populationSize = 50;

    /**
     * creates for each start config n mutated clones.
     */
    private int mutateStartConfigs = 0;

    /**
     * mutation rate of genes.
     */
    private double mutationRate = 0.025;

    private double uniformRate = 0.5;
    private int tournamentSize = 5;
    private boolean elitism = true;

    private int maxGenCount = 100000;

    public void writeMarkdownInfos(MarkdownWriter mdWriter)
            throws IOException {
        mdWriter.h1("Tuning Options");
        try {
            MarkdownTable mtable = new MarkdownTable();
            mtable.header("Parameter", "Value");

            for (PropertyDescriptor pd : Introspector.getBeanInfo(OptParameters.class).getPropertyDescriptors()) {
                if (pd.getReadMethod() != null && !"class".equals(pd.getName())) {
                    Object value = pd.getReadMethod().invoke(this);
                    mtable.row(pd.getName(), value);
                }
            }

            mdWriter.writeTable(mtable);
        } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}