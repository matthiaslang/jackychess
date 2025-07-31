package org.mattlang.tuning.data.pgnwriter;

import java.io.*;
import java.util.List;
import java.util.Map;

import org.mattlang.tuning.data.pgnparser.Ending;
import org.mattlang.tuning.data.pgnparser.MoveDescr;
import org.mattlang.tuning.data.pgnparser.PgnGame;
import org.mattlang.tuning.data.pgnparser.PgnMove;

public class PgnWriter {

    public void writeGame(PrintWriter writer, PgnGame game) throws IOException {
        for (Map.Entry<String, String> entry : game.getTags().entrySet()) {
            writeTag(writer, entry.getKey(), entry.getValue());
        }
        writer.println();
        int movecounter = 1;
        for (PgnMove move : game.getMoves()) {
            writeMove(writer, movecounter, move);
            movecounter++;
            // lf after 3moves, except the last one:
            if (movecounter % 3 == 0 && movecounter < game.getMoves().size()-1) {
                writer.println();
            }
        }
        // after the last one, write the result
        writeResult(writer, game.getResult());

        writer.println();
        writer.println();
    }

    private void writeMove(PrintWriter writer, int movecounter, PgnMove move) {
        writer.print(movecounter);
        writer.print(". ");
        writeMovePly(writer, move.getWhite());
        if (move.getBlack() != null) {
            writer.print(" ");
            writeMovePly(writer, move.getBlack());
        }
        writer.print(" ");

    }

    private void writeMovePly(PrintWriter writer, MoveDescr moveDescr) {
        writer.print(moveDescr.getMoveText().getText());
    }

    private void writeResult(PrintWriter writer, Ending result) {
        writer.print(" ");
        writer.print(result.getPgnResultString());
    }

    private void writeTag(PrintWriter writer, String key, String value) throws IOException {
        writer.write("[");
        writer.write(key);
        writer.write(" ");
        writer.write("\"");
        writer.write(value);
        writer.write("\"");
        writer.write("]");
        writer.println();
    }

    public String gameToString(PgnGame game) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(out);
        writeGame(writer, game);
        writer.close();
        return out.toString();
    }

    public void writeGames(File file, List<PgnGame> buildedGames) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(file))) {
            for (PgnGame game : buildedGames) {
                writeGame(writer, game);
            }
        }
    }
}
