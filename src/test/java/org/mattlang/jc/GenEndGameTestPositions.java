package org.mattlang.jc;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Figure;
import org.mattlang.jc.board.FigureType;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.engine.CheckChecker;
import org.mattlang.jc.movegenerator.BBCheckCheckerImpl;
import org.mattlang.jc.util.FenComposer;
import org.mattlang.jc.util.MoveValidator;

/**
 * Generate some simple end game test positions for usage in test tournaments.
 */
public class GenEndGameTestPositions {

    private static Random random = new Random(2317);

    private static CheckChecker checkChecker = new BBCheckCheckerImpl();

    private static MoveValidator moveValidator = new MoveValidator();

    public static void main(String[] args) throws IOException {
        ArrayList<String> epds = new ArrayList<>();

        Collection<String> kBNk = genWrite("KBNk", 50);
        epds.addAll(kBNk);
        Collection<String> kQk = genWrite("KQk", 50);
        epds.addAll(kQk);
        Collection<String> kqBk = genWrite("KQBk", 50);
        epds.addAll(kqBk);
        Collection<String> kQkr = genWrite("KQkr", 50);
        epds.addAll(kQkr);
        Collection<String> kQkp = genWrite("KQkp", 50);
        epds.addAll(kQkp);
        Collection<String> kRkp = genWrite("KRkp", 50);
        epds.addAll(kRkp);
        Collection<String> kRkb = genWrite("KRkb", 50);
        epds.addAll(kRkb);
        Collection<String> kRkn = genWrite("KRkn", 50);
        epds.addAll(kRkn);
        Collection<String> kRk = genWrite("KRk", 50);
        epds.addAll(kRk);
        Collection<String> kbBk = gen("KBBk", 50);
        epds.addAll(kbBk);
        Collection<String> krNk = gen("KRNk", 50);
        epds.addAll(krNk);
        Collection<String> kBBk = genWrite("KBBk", 50);
        epds.addAll(kBBk);
        Collection<String> kNNkp = genWrite("KNNkp", 50);
        epds.addAll(kNNkp);

        Files.write(Paths.get("endgames.epd"), epds, Charset.defaultCharset());
    }

    private static Collection<String> genWrite(String figs, int numPositions) throws IOException {
        Collection<String> result=gen(figs,numPositions);
        Files.write(Paths.get("endgames" + figs + ".epd"), result, Charset.defaultCharset());
        return result;
    }

    private static Collection<String> gen(String figs, int numPositions) {
        ArrayList<String> result = new ArrayList<>();

        for (int i = 0; i < numPositions; i++) {
            result.add(genPosition(figs));
        }
        return result;
    }

    private static String genPosition(String figs) {
        BoardRepresentation board = new BitBoard();
        board.clearPosition();

        do {
            board = new BitBoard();
            board.clearPosition();
            for (int i = 0; i < figs.length(); i++) {
                Figure figure = Figure.getFigureByCode(Figure.convertFigureChar(figs.charAt(i)));
                int rangeFrom = 0;
                int rangeTo = 64;
                if (figure.figureType == FigureType.Pawn) {
                    rangeFrom = 0 + 16;
                    rangeTo = 64 - 16;
                }
                int index = randomIndex(rangeFrom, rangeTo);
                while (board.getPos(index) != Figure.EMPTY) {
                    index = randomIndex(rangeFrom, rangeTo);
                }
                board.setPos(index, figure);
            }
            board.println();

        } while (!isGood(board));

        return composeEpdStr(board);
    }

    private static boolean isGood(BoardRepresentation board) {
        return !checkChecker.isInChess(board, Color.WHITE)
                && !checkChecker.isInChess(board, Color.BLACK)
                && hasLegalMoves(board);
    }

    private static boolean hasLegalMoves(BoardRepresentation board) {

        if (!moveValidator.hasLegalMoves(board)) {
            return false;
        }
        board.switchSiteToMove();
        return moveValidator.hasLegalMoves(board);
    }

    private static int randomIndex(int rangeFrom, int rangeTo) {
        int tst = random.nextInt(rangeTo);
        while (tst < rangeFrom) {
            tst = random.nextInt(rangeTo);
        }
        return tst;
    }

    private static String composeEpdStr(BoardRepresentation board) {
        return FenComposer.buildFenPosition(board) + " w - - 0 60";
    }

}
