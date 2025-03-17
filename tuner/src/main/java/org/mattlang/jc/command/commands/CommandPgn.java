package org.mattlang.jc.command.commands;

import static org.mattlang.jc.command.commands.EpdWriter.writeEpd;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import org.mattlang.jc.command.Main;
import org.mattlang.tuning.DataSet;
import org.mattlang.tuning.tuner.DatasetPreparer;

@Parameters(commandNames = { CommandPgn.CMD_PGN },
        separators = "=",
        commandDescription = "Processes epd files")
public class CommandPgn implements JCTCommand {

    /**
     * max fens to hold in memory before flushing to file during creation.
     */
    private static final int MAX_MEM_FENS = 250000;

    private static final Logger LOGGER = Logger.getLogger(CommandPgn.class.getSimpleName());

    public static final String CMD_PGN = "pgn";
    @Parameter(description = "List of epd input files (epd files or directories containing epd files)", required = true)
    private List<String> files;

    @Parameter(names = { "--output", "-o" }, description = "EPD Output file", required = true)
    private String outputFile;

    @Parameter(names = { "--removeDuplicates" }, description = "removes duplicate epds")
    private boolean removeDuplicates = false;

    @Override
    public String getCmdName() {
        return CMD_PGN;
    }

    @Override
    public void executeCommand() throws Exception {
        DataSet result = new DataSet(null);
        File outFile = new File(outputFile);
        if (outFile.exists()) {
            outFile.delete();
        }
        loadDataset(files, dataSet -> {
            result.add(dataSet);
        });

        if (removeDuplicates) {
            Main.consoleOut("removing duplicates...");
            result.removeDuplicateFens();
        }

        writeEpd(result, outputFile);
    }



    private void loadDataset(List<String> args, DataSetConsumer resultConsumer)
            throws Exception {
        DatasetPreparer preparer = new DatasetPreparer(null);

        for (String arg : args) {
            LOGGER.info("parsing file " + arg);
            File file = new File(arg);
            if (file.isDirectory()) {
                for (File fileOfDir : file.listFiles()) {
                    resultConsumer.accept(preparer.prepareFromEpd(fileOfDir));
                }
            } else {
                resultConsumer.accept(preparer.prepareFromEpd(file));
            }
        }

    }
}
