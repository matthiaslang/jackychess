package org.mattlang.jc.command.commands;

import java.io.IOException;

/**
 * interface for a "jacky chess tool" subcommand.
 */
public interface JCTCommand {

    String getCmdName();

    void executeCommand() throws IOException;
}
