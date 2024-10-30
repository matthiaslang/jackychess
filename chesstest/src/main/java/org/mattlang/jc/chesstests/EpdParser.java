package org.mattlang.jc.chesstests;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.mattlang.jc.TestPosition;

/**
 * Simple epd parsing (only relevant parts for our tests...)
 */

public class EpdParser {

    public static List<String[]> getEPDTests(String rawEpds) {
        List<String> epds = Arrays.asList(rawEpds.split("\n"));
        return epds.stream().map(epd -> splitEpd(epd)).collect(Collectors.toList());
    }

    public static List<TestPosition> convertTests(String rawEpds) {
        return getEPDTests(rawEpds).stream()
                .map(arr -> new TestPosition(arr[0], arr[1], arr[2]))
                .collect(Collectors.toList());
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

}
