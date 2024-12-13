package org.mattlang.tuning;

import static org.mattlang.jc.AppConfiguration.LOGGING_ACTIVATE;
import static org.mattlang.jc.AppConfiguration.LOGGING_DIR;
import static org.mattlang.jc.util.Logging.initLogging;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import org.mattlang.jc.engine.evaluation.parameval.EvalConfig;
import org.mattlang.jc.tools.MarkdownAppender;
import org.mattlang.tuning.tuner.DatasetPreparer;
import org.mattlang.tuning.tuner.OptParameters;

public abstract class AbstractTuner {

    private static final Logger LOGGER = Logger.getLogger(AbstractTuner.class.getSimpleName());

    protected File outputDir;

    public static ExecutorService executorService = Executors.newFixedThreadPool(7);
    /**
     * Params, set to some standard params:
     */
    protected final OptParameters params;

    protected boolean continuingTuningRun;

    protected MarkdownAppender markdownAppender;

    protected AbstractTuner(OptParameters params) {
        this.params = params;
    }

    protected void initRun() {
        System.setProperty("opt.evalParamSet", params.getEvalParamSet());

        // set output dir to pst config dir:
        final String path = determineOutputPath();
        File filepath = new File(path);
        outputDir = new File(filepath, params.getEvalParamSet().toLowerCase());
        outputDir.mkdirs();
        File mdFile = new File(outputDir, params.getName() + ".md");
        continuingTuningRun = mdFile.exists();
        markdownAppender = new MarkdownAppender(mdFile);

        if (continuingTuningRun) {
            markdownAppender.append(w -> {
                w.h1("continuing tuning run");
            });
        } else {
            markdownAppender.append(w -> {
                w.h1("Tuning Run");
            });
        }

        System.setProperty(LOGGING_ACTIVATE, "true");
        System.setProperty(LOGGING_DIR, ".");
        initLogging("/tuningLogging.properties");
    }

    protected DataSet loadAndPrepareData() throws IOException {
        LOGGER.info("Load & Prepare Data...");
        DataSet dataset = loadDataset(params.getInputFiles());
        dataset.setMultithreaded(params.isMultiThreading());
        // log dataset statistics + infos only for the first run, not for continuing runs:
        if (!continuingTuningRun) {
            dataset.logInfos();
        }
        if (params.isRemoveDuplicateFens()) {
            dataset.removeDuplidateFens();
            if (!continuingTuningRun) {
                LOGGER.info("Statistics after removing duplicates:");
                dataset.logInfos();
            }
        }
        return dataset;
    }

    private DataSet loadDataset(List<String> args) throws IOException {
        DatasetPreparer preparer = new DatasetPreparer(params);
        DataSet result = new DataSet(params);
        for (String arg : args) {
            LOGGER.info("parsing file " + arg);
            result.add(preparer.prepareLoadFromFile(new File(arg)));
        }
        return result;
    }

    protected String determineOutputPath() {
        return params.getOutputdir() != null ? params.getOutputdir() : "./hce/src/main/resources/config/";
    }

    /**
     * copy the main config file to the output dir, if a external output is given.
     *
     * @param outputDir
     */
    protected void copySourceConfigFile(File outputDir) {
        // check if external output is requested by parameter
        if (params.getOutputdir() != null) {

            File configFile = new File(outputDir, EvalConfig.CONFIG_PROPERTIES_FILE);
            configFile.getParentFile().mkdirs();
            Path targetConfigFile = configFile.toPath();
            new EvalConfig().copyConfig(targetConfigFile);
        }
    }
}
