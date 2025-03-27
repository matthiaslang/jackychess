package org.mattlang.jc.command.commands;

import static org.mattlang.jc.command.Main.consoleOut;
import static org.mattlang.jc.command.commands.EpdWriter.writeFenEntry;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import org.mattlang.jc.uci.FenParser;
import org.mattlang.jc.util.FenComposer;
import org.mattlang.tuning.FenEntry;
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

    @Parameter(names = { "--lenient" }, description = "lenient parsing pgns; try to read pgns even if there are minor semantical issues")
    private boolean lenient = false;

    private FenComposer fenComposer = new FenComposer();

    HashSet<Long> hashes = new HashSet<>();
    StreamProcessInfo remDupsInfo = new StreamProcessInfo("Duplicates");
    StreamProcessInfo writerInfo = new StreamProcessInfo("Written");
    StreamProcessInfo readInfo = new StreamProcessInfo("Read");

    StreamProcessWrapper wrapper = new StreamProcessWrapper();

    @Override
    public String getCmdName() {
        return CMD_PGN;
    }

    @Override
    public void executeCommand() throws Exception {

        wrapper.add(readInfo);
        wrapper.add(remDupsInfo);
        wrapper.add(writerInfo);

        File outFile = new File(outputFile);
        if (outFile.exists()) {
            outFile.delete();
        }

        FenParser.setLenient(lenient);

        try (FileWriter writer = new FileWriter(outFile, true);
                PrintWriter printWriter = new PrintWriter(writer)) {

            for (String file : files) {
                consoleOut("parsing file " + file);
                try (Stream<String> linesStream = Files.lines(new File(file).toPath())) {
                    linesStream.forEach(line -> {
                        readInfo.increment();
                        FenEntry entry = DatasetPreparer.parseFen(line);
                        streamProcessFenEntry(entry, printWriter);
                    });
                }
            }

        }

        wrapper.writeInfo();

        consoleOut("processed " + files.size() + " files");
        if (removeDuplicates) {
            consoleOut("removed " + remDupsInfo.size() + " duplicates");
        }
        consoleOut("written " + writerInfo.size() + " entries to " + outFile);
    }

    private void streamProcessFenEntry(FenEntry entry, PrintWriter printWriter) {
        if (removeDuplicates) {
            if (!hashes.contains(entry.getBoard().getZobristHash())) {
                writerInfo.increment();
                writeFenEntry(entry, fenComposer, printWriter);
                hashes.add(entry.getBoard().getZobristHash());
            } else {
                remDupsInfo.increment();
            }
        } else {
            writeFenEntry(entry, fenComposer, printWriter);
        }
    }

}
