package org.mattlang.jc.command.commands;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Logger;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import org.mattlang.jc.util.FenComposer;
import org.mattlang.tuning.DataSet;
import org.mattlang.tuning.FenEntry;
import org.mattlang.tuning.data.pgnparser.Ending;
import org.mattlang.tuning.tuner.DatasetPreparer;

@Parameters(commandNames = { CommandPgn2Epd.CMD_PGN_2_EPD },
        separators = "=",
        commandDescription = "Convert/Extract pgn file to epd files")
public class CommandPgn2Epd implements JCTCommand {

    private static final Logger LOGGER = Logger.getLogger(CommandPgn2Epd.class.getSimpleName());

    public static final String CMD_PGN_2_EPD = "pgn2epd";
    @Parameter(description = "List of pgn input files")
    private List<String> files;

    @Parameter(names = { "--output", "-o" }, description = "EPD Output file")
    private String outputFile;

    @Override
    public String getCmdName() {
        return CMD_PGN_2_EPD;
    }

    @Override
    public void executeCommand() throws IOException {
        DataSet dataSet = loadDataset(files);
        writeEpd(dataSet, outputFile);
    }

    private void writeEpd(DataSet dataSet, String outputFile) throws IOException {
        FenComposer fenComposer = new FenComposer();
        File outFile = new File(outputFile);
        try (FileWriter writer = new FileWriter(outFile);
                PrintWriter printWriter = new PrintWriter(writer)) {
            for (FenEntry fen : dataSet.getFens()) {
                fenComposer.createRawFenFromBoard(fen.getBoard());
                printWriter.print(fenComposer.createFenStr());
                printWriter.print(" ");
                printWriter.println(convertEnding(fen.getEnding()));
            }
        }
    }

    private String convertEnding(Ending ending) {
        switch (ending) {
        case MATE_WHITE:
            return "[1.0]";
        case MATE_BLACK:
            return "[0.0]";
        case DRAW:
            return "[0.5]";
        }
        throw new IllegalStateException("unsupported ending" + ending);
    }

    private DataSet loadDataset(List<String> args) throws IOException {
        DatasetPreparer preparer = new DatasetPreparer(null);
        DataSet result = new DataSet(null);
        for (String arg : args) {
            LOGGER.info("parsing file " + arg);
            result.add(preparer.prepareLoadFromFile(new File(arg)));
        }
        return result;
    }
}
