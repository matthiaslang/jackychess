package org.mattlang.jc.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import org.mattlang.jc.command.commands.CommandMain;
import org.mattlang.jc.command.commands.CommandPgn2Epd;
import org.mattlang.jc.command.commands.CommandTune;
import org.mattlang.jc.command.commands.JCTCommand;

public class Main {

    public static void main(String[] args) throws IOException {
        parseCommandFromArgs(args).ifPresent(c -> c.executeCommand());
    }

    public static Optional<JCTCommand> parseCommandFromArgs(String[] args) {
        CommandMain main = new CommandMain();
        List<JCTCommand> commands = new ArrayList<>();
        CommandTune tune = new CommandTune();
        CommandPgn2Epd pgn2Epd = new CommandPgn2Epd();
        commands.add(tune);
        commands.add(pgn2Epd);

        JCommander jc = JCommander.newBuilder()
                .addObject(main)
                .addCommand(tune)
                .addCommand(pgn2Epd)
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
}
