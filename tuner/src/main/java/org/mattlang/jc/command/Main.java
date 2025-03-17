package org.mattlang.jc.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import org.mattlang.jc.command.commands.*;

public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getSimpleName());

    public static void main(String[] args) throws IOException {
        Optional<JCTCommand> jctCommand = parseCommandFromArgs(args);
        if (jctCommand.isPresent()) {
            JCTCommand jct = jctCommand.get();
            try {
                jct.executeCommand();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    public static Optional<JCTCommand> parseCommandFromArgs(String[] args) {
        CommandMain main = new CommandMain();
        List<JCTCommand> commands = new ArrayList<>();
        CommandTune tune = new CommandTune();
        CommandPgn2Epd pgn2Epd = new CommandPgn2Epd();
        CommandPgn pgn = new CommandPgn();
        commands.add(tune);
        commands.add(pgn2Epd);
        commands.add(pgn);

        JCommander jc = JCommander.newBuilder()
                .addObject(main)
                .addCommand(tune)
                .addCommand(pgn2Epd)
                .addCommand(pgn)
                .build();

        try {
            jc.parse(args);
            String cmd = jc.getParsedCommand();

            Optional<JCTCommand> optMatch =
                    commands.stream().filter(c -> Objects.equals(cmd, c.getCmdName())).findFirst();

            if (main.isHelp()) {
                jc.usage();
            } else if (optMatch.isPresent()) {
                return optMatch;
            } else {
                System.err.println("Invalid command: " + cmd);
                jc.usage();
            }
        } catch (ParameterException e) {
            System.err.println(e.getLocalizedMessage());
            jc.usage();
        }
        return Optional.empty();
    }

    public static void consoleOut(String msg) {
        System.out.println(msg);
        LOGGER.info(msg);
    }
}
