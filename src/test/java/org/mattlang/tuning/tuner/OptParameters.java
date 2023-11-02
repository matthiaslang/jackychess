package org.mattlang.tuning.tuner;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.mattlang.jc.tools.MarkdownTable;
import org.mattlang.jc.tools.MarkdownWriter;
import org.mattlang.tuning.ProgressInfo;

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

    private int progressUpdatesInMinutes = ProgressInfo.DEFAULT_UPDATE_MINUTES;

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
    private boolean tuneMobilityTropism = true;
    private boolean tuneThreats = true;
    private boolean tunePositional = true;
    private boolean tunePawnEval = true;
    private boolean tunePassedPawnEval = true;
    private boolean tuneKingAttack = true;
    private boolean tunePst = true;
    private boolean tuneAdjustments = true;
    private boolean tuneAdjustmentsFactors = true;

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

    private GeneticParams geneticParams = GeneticParams.builder().build();

    public void writeMarkdownInfos(MarkdownWriter mdWriter)
            throws IOException {
        mdWriter.h1("Tuning Options");

        MarkdownTable mtable = new MarkdownTable();
        mtable.header("Parameter", "Value");

        writeParams(mtable, "", this);

        mdWriter.writeTable(mtable);

    }

    private void writeParams(MarkdownTable mtable, String subParam, Object paramObj) {
        try {
            for (PropertyDescriptor pd : Introspector.getBeanInfo(paramObj.getClass()).getPropertyDescriptors()) {
                if (pd.getReadMethod() != null && !"class".equals(pd.getName())) {
                    Object value = pd.getReadMethod().invoke(paramObj);
                    String paramName = createParamName(subParam, pd.getName());
                    if (isNestedParamClass(value)) {
                        writeParams(mtable, paramName, value);
                    } else {
                        mtable.row(paramName, value);
                    }
                }
            }
        } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private String createParamName(String subParam, String name) {
        if (subParam == null || subParam.length() == 0) {
            return name;
        } else {
            return subParam + "." + name;
        }
    }

    private boolean isNestedParamClass(Object value) {
        if (value == null) {
            return false;
        }
        if (value.getClass().isPrimitive()) {
            return false;
        }
        if (value.getClass().getName().startsWith("java.lang")) {
            return false;
        }
        if (value instanceof List) {
            return false;
        }
        if (value instanceof Map) {
            return false;
        }

        return true;
    }

}