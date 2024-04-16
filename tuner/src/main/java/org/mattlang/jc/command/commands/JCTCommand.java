package org.mattlang.jc.command.commands;

/**
 * interface for a "jacky chess tool" subcommand.
 */
public interface JCTCommand {

    String getCmdName();

    void executeCommand();
}
