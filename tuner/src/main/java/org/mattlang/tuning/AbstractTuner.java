package org.mattlang.tuning;

import java.io.File;
import java.nio.file.Path;

import org.mattlang.jc.engine.evaluation.parameval.EvalConfig;
import org.mattlang.tuning.tuner.OptParameters;

public abstract class AbstractTuner {

    /**
     * Params, set to some standard params:
     */
    protected final OptParameters params;

    protected AbstractTuner(OptParameters params) {
        this.params = params;
    }

    protected String determineOutputPath() {
        return params.getOutputdir() != null ? params.getOutputdir() : "./engine/src/main/resources/config/";
    }

    /**
     * copy the main config file to the output dir, if a external output is given.
     * @param outputDir
     */
    protected void copySourceConfigFile(File outputDir){
        if (params.getOutputdir() != null) {
            File configFile = new File(outputDir, EvalConfig.CONFIG_PROPERTIES_FILE);
            Path targetConfigFile = configFile.toPath();
            new EvalConfig().copyConfig(targetConfigFile);
        }
    }
}
