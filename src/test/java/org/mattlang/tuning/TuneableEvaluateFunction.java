package org.mattlang.tuning;

import java.io.File;
import java.util.List;

import org.mattlang.jc.engine.EvaluateFunction;

public interface TuneableEvaluateFunction extends EvaluateFunction {

    void saveValues(List<TuningParameter> params);

    List<TuningParameter> getParams();

    TuneableEvaluateFunction copy();

    String collectParamDescr();

    void writeParamDescr(File outputDir);
}
