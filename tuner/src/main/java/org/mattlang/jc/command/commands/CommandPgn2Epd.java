package org.mattlang.jc.command.commands;

import static java.util.stream.Collectors.groupingBy;
import static org.mattlang.tuning.data.pgnparser.Ending.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.logging.Logger;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import org.mattlang.jc.util.FenComposer;
import org.mattlang.tuning.DataSet;
import org.mattlang.tuning.FenEntry;
import org.mattlang.tuning.data.pgnparser.Ending;
import org.mattlang.tuning.tuner.DatasetPreparer;
import org.mattlang.tuning.tuner.PgnPrepareConfig;

@Parameters(commandNames = { CommandPgn2Epd.CMD_PGN_2_EPD },
        separators = "=",
        commandDescription = "Convert/Extract pgn file to epd files")
public class CommandPgn2Epd implements JCTCommand {

    /**
     * max fens to hold in memory before flushing to file during creation.
     */
    private static final int MAX_MEM_FENS = 250000;

    private static final Logger LOGGER = Logger.getLogger(CommandPgn2Epd.class.getSimpleName());

    public static final String CMD_PGN_2_EPD = "pgn2epd";
    @Parameter(description = "List of pgn input files (pgn files or directories containing pgn files)", required = true)
    private List<String> files;

    @Parameter(names = { "--output", "-o" }, description = "EPD Output file", required = true)
    private String outputFile;

    @Parameter(names = { "--skipFirstMoves" }, description = "skip first n half moves")
    private int skipFirstNHalfMoves = 0;

    @Parameter(names = { "--skipLastMoves" }, description = "skip last n half moves")
    private int skipLastNHalfMoves = 0;

    @Parameter(names = { "--addNMoves" }, description = "add only n half moves at max per game")
    private int addOnlyNHalfMoves = 0;

    @Parameter(names = { "--writeComments" }, description = "write comment to epd about the source pgn")
    private boolean writeComments = false;

    @Parameter(names = { "--endingDistribution" },
            description = "distribute result fens: ratio of endings should be about that percentage")
    private int endingDistribution = 100;

    @Override
    public String getCmdName() {
        return CMD_PGN_2_EPD;
    }

    @Override
    public void executeCommand() throws Exception {
        DataSet result = new DataSet(null);
        File outFile = new File(outputFile);
        if (outFile.exists()) {
            outFile.delete();
        }
        loadDataset(files, createConfig(), dataSet -> {
            result.add(dataSet);
            if (result.getFens().size() > MAX_MEM_FENS) {
                // write collected fens to output
                distributeDataset(result);
                writeEpd(result, outputFile);
                result.getFens().clear();
            }
        });

    }

    private void distributeDataset(DataSet dataSet) {
        if (endingDistribution < 100) {
            Map<Ending, List<FenEntry>> fensByEnding = dataSet.getFens().stream()
                    .collect(groupingBy(FenEntry::getEnding));

            distributeEnding(dataSet, fensByEnding.get(MATE_WHITE), fensByEnding.get(MATE_BLACK));

            fensByEnding = dataSet.getFens().stream()
                    .collect(groupingBy(FenEntry::getEnding));
            distributeEnding(dataSet, fensByEnding.get(MATE_WHITE), fensByEnding.get(DRAW));
        }

    }

    private void distributeEnding(DataSet dataSet, List<FenEntry> group1, List<FenEntry> group2) {
        List<FenEntry> smallerGroup = group1.size() > group2.size() ? group2 : group1;
        List<FenEntry> biggerGroup = group1.size() > group2.size() ? group1 : group2;
        if (biggerGroup.isEmpty()) {
            return;
        }
        int ratio = 100 * smallerGroup.size() / biggerGroup.size();
        if (ratio < endingDistribution) {

            LOGGER.info("adjust ending ratio: " + ratio + " to " + endingDistribution);
            int newShrinkedSize = biggerGroup.size() * ratio / endingDistribution;
            // remove ratioDiff percent from the bigger group:
            int numToRemove = biggerGroup.size() - newShrinkedSize;
            LOGGER.info("distribute ending ratio adjusting: removing " + numToRemove + " fens ");

            Collections.shuffle(biggerGroup);
            Set<FenEntry> fenSet = new LinkedHashSet<>(dataSet.getFens());
            biggerGroup.subList(0, numToRemove).forEach(fenSet::remove);
            dataSet.getFens().clear();
            dataSet.getFens().addAll(fenSet);
        }
    }

    private PgnPrepareConfig createConfig() {
        return new PgnPrepareConfig(skipFirstNHalfMoves, skipLastNHalfMoves, addOnlyNHalfMoves, writeComments);
    }

    private void writeEpd(DataSet dataSet, String outputFile) throws IOException {
        FenComposer fenComposer = new FenComposer();
        File outFile = new File(outputFile);
        try (FileWriter writer = new FileWriter(outFile, true);
                PrintWriter printWriter = new PrintWriter(writer)) {
            for (FenEntry fen : dataSet.getFens()) {
                fenComposer.createRawFenFromBoard(fen.getBoard());
                printWriter.print(fenComposer.createFenStr());
                printWriter.print(" ");
                printWriter.print(convertEnding(fen.getEnding()));
                if (fen.getComment() != null) {
                    printWriter.print(" c0 " + fen.getComment());
                }
                printWriter.println();
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

    private void loadDataset(List<String> args, PgnPrepareConfig config, DataSetConsumer resultConsumer)
            throws Exception {
        DatasetPreparer preparer = new DatasetPreparer(null);

        for (String arg : args) {
            LOGGER.info("parsing file " + arg);
            File file = new File(arg);
            if (file.isDirectory()) {
                for (File fileOfDir : file.listFiles()) {
                    resultConsumer.accept(preparer.prepareLoadPgn(fileOfDir, config));
                }
            } else {
                resultConsumer.accept(preparer.prepareLoadPgn(file, config));
            }
        }

    }
}
