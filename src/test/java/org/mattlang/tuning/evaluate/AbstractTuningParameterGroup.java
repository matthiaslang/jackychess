package org.mattlang.tuning.evaluate;

import java.util.ArrayList;
import java.util.List;

import org.mattlang.tuning.TuningParameter;
import org.mattlang.tuning.TuningParameterGroup;

public abstract class AbstractTuningParameterGroup implements TuningParameterGroup {

    /**
     * list of all individual parameter of that group.
     */
    private List<TuningParameter> parameters = new ArrayList<>();

    @Override
    public List<TuningParameter> getParameters() {
        return parameters;
    }

}
