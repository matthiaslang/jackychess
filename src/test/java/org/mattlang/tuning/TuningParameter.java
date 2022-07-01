package org.mattlang.tuning;

public interface TuningParameter {

    void change(int i);

    /**
     * Returns a string describing/defining the parameter. Usually a Text fragment in the form of the propery
     * configuration of that parameter.
     *
     * @return
     */
    String getParamDef();
}
