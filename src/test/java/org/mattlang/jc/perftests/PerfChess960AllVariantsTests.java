package org.mattlang.jc.perftests;

import static org.mattlang.jc.board.Color.WHITE;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mattlang.jc.Factory;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.movegenerator.PseudoLegalMoveGenerator;

/**
 * PerfTests for fischer random chess.
 * https://www.chessprogramming.org/Chess960_Perft_Results
 */
@RunWith(Parameterized.class)
public class PerfChess960AllVariantsTests {

    private String fen;

    private long d1Comb;

    private long d2Comb;

    private long d3Comb;

    private long d4Comb;

    private long d5Comb;

    private long d6Comb;

    public PerfChess960AllVariantsTests(String fen, long d1Comb, long d2Comb, long d3Comb, long d4Comb, long d5Comb,
            long d6Comb) {
        this.fen = fen;
        this.d1Comb = d1Comb;
        this.d2Comb = d2Comb;
        this.d3Comb = d3Comb;
        this.d4Comb = d4Comb;
        this.d5Comb = d5Comb;
        this.d6Comb = d6Comb;
    }

    @Parameterized.Parameters
    public static Collection loadCombinations() throws IOException {

        return Files.lines(new File(PerfChess960AllVariantsTests.class.getResource("/chess960perfts.csv").getFile()).toPath())
                .map(line -> parse(line))
                .collect(Collectors.toList());
        //        return Arrays.asList(new Object[][] {
        //                { 2, true },
        //                { 6, false },
        //                { 19, true },
        //                { 22, false },
        //                { 23, true }
    }

    private static Object parse(String line) {
        String[] parts = line.split(";");

        String fen = parts[0];
        long d1 = Long.parseLong(parts[1].replace("D1", "").trim());
        long d2 = Long.parseLong(parts[2].replace("D2", "").trim());
        long d3 = Long.parseLong(parts[3].replace("D3", "").trim());
        long d4 = Long.parseLong(parts[4].replace("D4", "").trim());
        long d5 = Long.parseLong(parts[5].replace("D5", "").trim());
        long d6 = Long.parseLong(parts[6].replace("D6", "").trim());

        return new Object[] { fen, d1, d2, d3, d4, d5, d6 };
    }

    @Test
    public void testCombination() {

        PseudoLegalMoveGenerator generator = new PseudoLegalMoveGenerator();
        Perft perft = new Perft();
        Factory.setDefaults(Factory.createStable());

        BitBoard board = new BitBoard();
        board.setFenPosition("position fen " + fen);
        System.out.println(board.toUniCodeStr());

        perft.assertPerft(generator, board, WHITE, 1, (int)d1Comb);
        perft.assertPerft(generator, board, WHITE, 2, (int)d2Comb);
        perft.assertPerft(generator, board, WHITE, 3, (int)d3Comb);
        perft.assertPerft(generator, board, WHITE, 4, (int)d4Comb);
//        perft.assertPerft(generator, board, WHITE, 5, d5Comb);
//        perft.assertPerft(generator, board, WHITE, 6, d6Comb);
    }

}
