package org.mattlang.jc.command.commands;

import java.util.List;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandNames = { CommandPgn2Epd.CMD_PGN_2_EPD },
        separators = "=",
        commandDescription = "Convert/Extract pgn file to epd files")
public class CommandPgn2Epd implements JCTCommand {

    public static final String CMD_PGN_2_EPD = "pgn2epd";
    @Parameter(description = "List of pgn input files")
    private List<String> files;

    @Parameter(names = "--output", description = "EPD Output file")
    private String outputFile;



    @Override
    public String getCmdName() {
        return CMD_PGN_2_EPD;
    }

    @Override
    public void executeCommand() {

    }
}
