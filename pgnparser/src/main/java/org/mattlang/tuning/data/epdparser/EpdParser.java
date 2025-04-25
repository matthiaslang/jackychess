package org.mattlang.tuning.data.epdparser;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.tuning.data.pgnparser.Ending;

/**
 * Simple epd parsing (only relevant parts for our tests...)
 * https://www.chessprogramming.org/Extended_Position_Description
 * https://www.thechessdrum.net/PGN_Reference.txt
 */

public class EpdParser {

    public static List<Epd> getEPDTests(String rawEpds) {
        List<String> epds = Arrays.asList(rawEpds.split("\n"));
        return epds.stream().map(epd -> parseEpd(epd)).collect(Collectors.toList());
    }

    public static List<EpdWithOpCodes> parseEPDTests(String rawEpds) {
        List<String> epds = Arrays.asList(rawEpds.split("\n"));
        return epds.stream()
                .map(epd -> parseOpCodes(parseEpd(epd)))
                .collect(Collectors.toList());
    }

    private static EpdWithOpCodes parseOpCodes(Epd epd) {
        return new EpdWithOpCodes(epd, OpCodeParser.parse(epd.unparsedOpcodes));
    }

    private static Epd parseEpd(String epd) {
        String[] parts = epd.split(" ");
        String partialFen = parts[0] + " " + parts[1] + " " + parts[2] + " " + parts[3];
        String opCodesPart = epd.replace(partialFen, "").trim();
        return new Epd(partialFen, opCodesPart);
    }

    private static String[] splitEpd(String epd) {
        String[] split = epd.split("bm ");
        if (split.length != 2) {
            split = epd.split("am ");
        }
        if (split.length != 2) {
            System.out.println(epd);
        }
        String position = split[0];
        String cmdPart = split[1];
        String[] cmds = cmdPart.split(";");
        String expectedBestMove = cmds[0];
        String testName = cmds[1];
        return new String[] { position, expectedBestMove, testName };
    }

    public static String cleanupFen(String line) {
        BoardRepresentation board = new BitBoard();

        Ending ending;
        if (line.contains("\"1/2-1/2\"") || line.contains("pgn=0.5")) {
            ending = Ending.DRAW;
            line = line.replace("\"1/2-1/2\"", "");
            line = line.replace("pgn=0.5", "");
        } else if (line.contains("\"0-1\"")) {
            ending = Ending.MATE_BLACK;
            line = line.replace("\"0-1\"", "");
        } else if (line.contains("\"1-0\"")) {
            ending = Ending.MATE_WHITE;
            line = line.replace("\"1-0\"", "");
        } else if (line.contains("pgn=0.0")) {
            ending = Ending.MATE_WHITE;
            line = line.replace("pgn=0.0", "");
        } else if (line.contains("pgn=1.0")) {
            ending = Ending.MATE_BLACK;
            line = line.replace("pgn=1.0", "");
        } else if (line.contains("[1.0]")) {
            line = line.replace("[1.0]", "");
            ending = Ending.MATE_WHITE;
        } else if (line.contains("[0.5]")) {
            line = line.replace("[0.5]", "");
            ending = Ending.DRAW;
        } else if (line.contains("[0.0]")) {
            line = line.replace("[0.0]", "");
            ending = Ending.MATE_BLACK;
        } else {
            throw new RuntimeException("Error Parsing pgn file: no ending could be found in " + line);
        }

        // replace noise in the zurich test set:
        // replace everything after ";":
        if (line.contains(";")) {
            line = line.split(";")[0];
        }

        return line;
    }

}
