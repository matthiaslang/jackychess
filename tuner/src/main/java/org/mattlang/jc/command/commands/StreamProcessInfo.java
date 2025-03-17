package org.mattlang.jc.command.commands;

import org.mattlang.jc.command.Main;

public class StreamProcessInfo {

    private final String title;
    private int counter = 0;

    public StreamProcessInfo(String title) {
        this.title = title;
    }

    public void increment() {
        counter++;
        if (counter % 25000 == 0) {
            Main.consoleOut("process " + title + ": " + counter);
        }
    }

    public int size() {
        return counter;
    }
}
