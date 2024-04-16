package org.mattlang.jc.command.commands;

import java.util.List;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandNames = { CommandTune.CMD_TUNE }, separators = "=", commandDescription = "Tune parameter")
public class CommandTune implements JCTCommand {

    public static final String CMD_TUNE = "tune";
    @Parameter(description = "Folder with parameter source")
    private List<String> files;

    @Override
    public String getCmdName() {
        return CMD_TUNE;
    }

    @Override
    public void executeCommand() {

    }
}
