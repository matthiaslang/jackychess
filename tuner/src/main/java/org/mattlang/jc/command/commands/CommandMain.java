package org.mattlang.jc.command.commands;

import com.beust.jcommander.Parameter;

import lombok.Getter;

@Getter
public class CommandMain {
    @Parameter(names = "--help", help = true)
    private boolean help;
}
