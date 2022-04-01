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
import org.mattlang.jc.tools.FenComposer;
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

        epds.addAll(gen("KQk", 50));
        epds.addAll(gen("KQBk", 50));
        epds.addAll(gen("KQkr", 50));
        epds.addAll(gen("KQkp", 50));
        epds.addAll(gen("KRkp", 50));
        epds.addAll(gen("KRkb", 50));
        epds.addAll(gen("KRkn", 50));
        epds.addAll(gen("KRk", 50));
        epds.addAll(gen("KBBk", 50));
        epds.addAll(gen("KRNk", 50));

        Files.write(Paths.get("endgames.epd"), epds, Charset.defaultCharset());
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
