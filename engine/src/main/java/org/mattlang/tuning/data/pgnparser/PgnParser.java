package org.mattlang.tuning.data.pgnparser;

import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.tuning.data.pgnparser.OrdinarySymbol.BRACKET_OPEN;
import static org.mattlang.tuning.data.pgnparser.OrdinarySymbol.DOT;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.board.bitboard.BitBoard;

public class PgnParser {

    private static final Logger LOGGER = Logger.getLogger(PgnParser.class.getSimpleName());

    public List<PgnGame> parse(File file) throws IOException {

        try (InputStream fis = new BufferedInputStream(new FileInputStream(file))) {
            return parse(fis);
        } catch (PgnParserException parseException) {
            LOGGER.log(Level.SEVERE,
                    "Error " + fmtFilePosLink(file, parseException));
            throw parseException;
        }
    }

    /**
     * Returns a file + text position in a format, that could be interpreted by intellij to jump directly to that
     * position from a log.
     *
     * @param file
     * @param ppe
     * @return
     */
    public String fmtFilePosLink(File file, PgnParserException ppe) {
        return file.getAbsolutePath() + ":" + ppe.getLineNo() + ":" + ppe.getColNo();
    }

    public List<PgnGame> parse(InputStream in) throws IOException {
        List<PgnGame> result = new ArrayList<>();
        Scanner scanner = new Scanner(in);
        Matcher matcher = new Matcher(scanner);
        int counter = 0;
        while (scanner.hasNext() && matcher.match(BRACKET_OPEN)) {
            PgnGame game = parseGame(matcher);
            result.add(game);
            counter++;
            if (counter % 500 == 0) {
                LOGGER.info("parsed " + counter + " games...");
            }
        }
        if (scanner.hasNext()) {
            throw new PgnParserException("Symbols after expected end:" + scanner.getCurr(), scanner);
        }

        return result;
    }

    private PgnGame parseGame(Matcher matcher) throws IOException {
        PgnGame game = new PgnGame();

        // parse parameters:  the first open bracke is already consumed by the caller
        do {
            Word word = matcher.matchWord();
            Quote quote = matcher.matchQuote();
            matcher.expectSymbol(OrdinarySymbol.BRACKET_CLOSE);
            game.addTag(word.getText(), quote.getText());
        } while (matcher.match(OrdinarySymbol.BRACKET_OPEN));

        // parse the plys:
        int moveNum = 1;
        BoardRepresentation board = new BitBoard();
        board.setStartPosition();
        while (parseMove(game, matcher, moveNum, board)) {
            moveNum++;
        }
        return game;

    }

    private boolean parseMove(PgnGame game, Matcher matcher, int moveNum, BoardRepresentation board)
            throws IOException {
        Optional<IntegerNumber> optNumber = matcher.optMatchNumber();
        if (optNumber.isPresent()) {
            try {
                if (moveNum != optNumber.get().getNumber()) {
                    throw new PgnParserException("Expecting move number " + moveNum, optNumber.get());
                }
                matcher.expectSymbol(DOT);

                MoveDescr moveWhite = parseMove(matcher, board, WHITE);

                if (moveWhite.getEnding() != null) {
                    game.addMove(new PgnMove(moveWhite, null));
                    return false;
                }

                MoveDescr moveBlack = parseMove(matcher, board, BLACK);
                game.addMove(new PgnMove(moveWhite, moveBlack));
                if (moveBlack.getEnding() != null) {
                    return false;
                }
                return true;

            } catch (Exception e) {
                throw new PgnParserException("Error Parsing Move " + moveNum, e, matcher);
            }
        } else {
            return false;
        }

    }

    private MoveDescr parseMove(Matcher matcher, BoardRepresentation board, Color color) throws IOException {
        MoveText moveText = matcher.matchMoveText();
        try {
            Move move = AlgebraicNotation.moveFromAN(board, color, moveText);
            board.domove(move);
            Optional<Comment> optComment = matcher.optMatchComment();
            Optional<Ending> optEnding = matcher.optMatchEnding();

            return new MoveDescr(moveText, optComment.orElse(null), optEnding.orElse(null));
        } catch (RuntimeException e) {
            throw new PgnParserException(
                    "Error parsing move " + moveText.getText() + " board:\n" + board.toUniCodeStr(), e, moveText);
        }
    }

}
