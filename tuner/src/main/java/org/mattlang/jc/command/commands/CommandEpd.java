package org.mattlang.jc.command.commands;

import static org.mattlang.jc.command.Main.consoleOut;
import static org.mattlang.jc.command.commands.EpdWriter.writeFenEntry;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
import org.mattlang.jc.utils.NoWriter;
import org.mattlang.tuning.FenEntry;
import org.mattlang.tuning.tuner.DatasetPreparer;

@Parameters(commandNames = { CommandEpd.CMD_EPD },
        separators = "=",
        commandDescription = "Processes epd files")
public class CommandEpd implements JCTCommand {

    /**
     * max fens to hold in memory before flushing to file during creation.
     */
    private static final int MAX_MEM_FENS = 250000;

    private static final Logger LOGGER = Logger.getLogger(CommandEpd.class.getSimpleName());

    public static final String CMD_EPD = "epd";
    @Parameter(description = "List of epd input files (epd files or directories containing epd files)", required = true)
    private List<String> files;

    @Parameter(names = { "--output", "-o" }, description = "EPD Output file")
    private String outputFile;

    @Parameter(names = { "--removeDuplicates" }, description = "removes duplicate epds")
    private boolean removeDuplicates = false;

    @Parameter(names = { "--lenient" },
            description = "lenient parsing epds; try to read epds even if there are minor semantical issues")
    private boolean lenient = false;

    @Parameter(names = { "--analyze" }, description = "analyze the epds")
    private boolean analyze = false;

    private FenComposer fenComposer = new FenComposer();

    HashSet<Long> hashes = new HashSet<>();
    StreamProcessInfo remDupsInfo = new StreamProcessInfo("Duplicates");
    StreamProcessInfo writerInfo = new StreamProcessInfo("Written");
    StreamProcessInfo readInfo = new StreamProcessInfo("Read");

    StreamProcessWrapper wrapper = new StreamProcessWrapper();

    StreamAnalyzer streamAnalyzer = new StreamAnalyzer();

    @Override
    public String getCmdName() {
        return CMD_EPD;
    }

    @Override
    public void executeCommand() throws Exception {

        wrapper.add(readInfo);
        wrapper.add(remDupsInfo);
        if (outputFile != null) {
            wrapper.add(writerInfo);
        } else {
            if (!analyze) {
                consoleOut("no output specified and no analyze specified does not make sense!");
                System.exit(1);
            }
        }

        FenParser.setLenient(lenient);

        try (PrintWriter printWriter = createWriter()) {

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
        if (outputFile != null) {
            consoleOut("written " + writerInfo.size() + " entries to " + outputFile);
        }

        if (analyze) {
            streamAnalyzer.writeAnalyzeOutput();
        }
    }

    /**
     * Creaes an output writer or a non-output-writing facade if no output is required.
     *
     * @return
     * @throws IOException
     */
    private PrintWriter createWriter() throws IOException {
        if (outputFile != null) {
            File outFile = new File(outputFile);
            if (outFile.exists()) {
                outFile.delete();
            }
            FileWriter writer = new FileWriter(outFile, true);
            return new PrintWriter(writer);
        } else {
            return new PrintWriter(new NoWriter());
        }
    }

    private void streamProcessFenEntry(FenEntry entry, PrintWriter printWriter) {
        if (analyze) {
            streamAnalyzer.analyze(entry);
        }
        if (removeDuplicates) {
            entry = filterDuplicates(entry);
        }

        if (entry != null) {
            doWritingFen(entry, printWriter);
        }
    }

    private FenEntry filterDuplicates(FenEntry entry) {
        if (!hashes.contains(entry.getBoard().getZobristHash())) {
            hashes.add(entry.getBoard().getZobristHash());
            return entry;
        } else {
            remDupsInfo.increment();
            return null;
        }
    }

    private void doWritingFen(FenEntry entry, PrintWriter printWriter) {
        if (outputFile != null) {
            writerInfo.increment();
            writeFenEntry(entry, fenComposer, printWriter);
        }
    }

}
