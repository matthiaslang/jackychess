package org.mattlang.jc.command.commands;

import org.mattlang.jc.command.Main;

public class StreamProcessInfo {

    private final String title;
    private int counter = 0;
    private StreamProcessWrapper parent;

    public StreamProcessInfo(String title) {
        this.title = title;
    }

    public void increment() {
        counter++;
        if (counter % 50000 == 0) {
            writeInfo();
        }
    }

    public void writeInfo() {
        if (parent != null) {
            parent.writeInfo();
        } else {
            Main.consoleOut("process " + title + ": " + counter);
        }
    }

    public int size() {
        return counter;
    }

    public void setParent(StreamProcessWrapper parent) {
        this.parent = parent;
    }

    public StreamProcessWrapper getParent() {
        return parent;
    }

    public String createBasicInfo() {
        return title + ": " + counter;
    }
}
