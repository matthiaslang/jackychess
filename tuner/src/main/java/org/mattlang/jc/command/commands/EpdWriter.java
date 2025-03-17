package org.mattlang.jc.command.commands;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.mattlang.jc.util.FenComposer;
import org.mattlang.tuning.DataSet;
import org.mattlang.tuning.FenEntry;
import org.mattlang.tuning.data.pgnparser.Ending;

public class EpdWriter {

    public static void writeEpd(DataSet dataSet, String outputFile) throws IOException {
        FenComposer fenComposer = new FenComposer();
        File outFile = new File(outputFile);
        try (FileWriter writer = new FileWriter(outFile, true);
                PrintWriter printWriter = new PrintWriter(writer)) {
            for (FenEntry fen : dataSet.getFens()) {
                fenComposer.createRawFenFromBoard(fen.getBoard());
                printWriter.print(fenComposer.createFenStr());
                printWriter.print(" ");
                printWriter.print(convertEnding(fen.getEnding()));
                if (fen.getComment() != null) {
                    printWriter.print(" c0 " + fen.getComment());
                }
                printWriter.println();
            }
        }
    }

    private static String convertEnding(Ending ending) {
        switch (ending) {
        case MATE_WHITE:
            return "[1.0]";
        case MATE_BLACK:
            return "[0.0]";
        case DRAW:
            return "[0.5]";
        }
        throw new IllegalStateException("unsupported ending" + ending);
    }
}
