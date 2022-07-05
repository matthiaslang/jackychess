package org.mattlang.tuning;

import java.util.List;

/**
 * Group related Parameters.
 */
public interface TuningParameterGroup {

    List<TuningParameter> getParameters();

    /**
     * Returns a string describing/defining the parameter. Usually a Text fragment in the form of the property
     * configuration of that parameter.
     *
     * @return
     */
    String getParamDef();
}
