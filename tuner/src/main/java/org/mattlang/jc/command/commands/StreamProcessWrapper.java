package org.mattlang.jc.command.commands;

import java.util.ArrayList;
import java.util.List;

import org.mattlang.jc.command.Main;

public class StreamProcessWrapper {

    private List<StreamProcessInfo> processInfos = new ArrayList<>();

    public void add(StreamProcessInfo processInfo) {
        processInfos.add(processInfo);
        processInfo.setParent(this);
    }

    public void writeInfo() {
        StringBuilder out = new StringBuilder();
        for (StreamProcessInfo processInfo : processInfos) {
            out.append(processInfo.createBasicInfo());
            out.append(";");
        }

        Main.consoleOut(out.toString());
    }
}
