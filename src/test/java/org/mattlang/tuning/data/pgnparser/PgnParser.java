package org.mattlang.tuning.data.pgnparser;

import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.tuning.data.pgnparser.OrdinarySymbol.BRACKET_OPEN;
import static org.mattlang.tuning.data.pgnparser.OrdinarySymbol.DOT;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.board.bitboard.BitBoard;

public class PgnParser {

    public List<PgnGame> parse(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            return parse(fis);
        }
    }

    public List<PgnGame> parse(InputStream in) throws IOException {
        List<PgnGame> result = new ArrayList<>();
        Scanner scanner = new Scanner(in);
        Matcher matcher = new Matcher(scanner);
        while (scanner.hasNext() && matcher.match(BRACKET_OPEN)) {
            PgnGame game = parseGame(matcher);
            result.add(game);
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

            } catch(Exception e){
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
            throw new PgnParserException("Error parsing move " + moveText.getText() + " board:\n" + board.toUniCodeStr(), e, moveText);
        }
    }

}
