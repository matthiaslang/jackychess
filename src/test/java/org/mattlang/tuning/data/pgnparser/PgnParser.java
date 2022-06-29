package org.mattlang.tuning.data.pgnparser;

import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
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

    private AlgebraicNotation algebraicNotation = new AlgebraicNotation();

    public List<PgnGame> parse(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            return parse(fis);
        }
    }

    public List<PgnGame> parse(InputStream in) throws IOException {
        List<PgnGame> result = new ArrayList<>();
        Scanner scanner = new Scanner(in);
        Matcher matcher = new Matcher(scanner);
        while (scanner.hasNext()) {
            PgnGame game = parseGame(matcher);
            result.add(game);

        }
        return result;
    }

    private PgnGame parseGame(Matcher matcher) throws IOException {
        PgnGame game = new PgnGame();

        // parse parameters:
        while (matcher.match(OrdinarySymbol.BRACKET_OPEN)) {
            Word word = matcher.matchWord();
            Quote quote = matcher.matchQuote();
            matcher.expectSymbol(OrdinarySymbol.BRACKET_CLOSE);
            game.addTag(word.getWord(), quote.getQuote());
        }

        // parse the plys:
        int moveNum = 1;
        BoardRepresentation board = new BitBoard();
        board.setStartPosition();
        while (parseMove(game, matcher, moveNum, board)) {
            moveNum++;
        }
        return game;

    }

    private boolean parseMove(PgnGame game, Matcher matcher, int ply, BoardRepresentation board)
            throws IOException {
        Optional<IntegerNumber> optNumber = matcher.optMatchNumber();
        if (optNumber.isPresent()) {
            if (ply != optNumber.get().getI()) {
                throw new PgnParserException("Expecting Ply " + ply);
            }
            matcher.expectSymbol(DOT);

            MoveDescr moveWhite = parseMove(matcher, board, WHITE);
            if (moveWhite.getMoveText().getType().isEnd()) {
                game.addMove(new PgnMove(moveWhite, null));
                return false;
            }

            MoveDescr moveBlack = parseMove(matcher, board, BLACK);
            game.addMove(new PgnMove(moveWhite, moveBlack));
            if (moveBlack.getMoveText().getType().isEnd()) {
                return false;
            }
            return true;

        } else {
            return false;
        }

    }

    private MoveDescr parseMove(Matcher matcher, BoardRepresentation board, Color color) throws IOException {
        MoveText moveText = matcher.matchMoveText();
        try {
            if (!moveText.getType().isEnd()) {
                Move move = algebraicNotation.moveFromAN(board, color, moveText);
                board.domove(move);
            }
            Optional<Comment> optComment = matcher.optMatchComment();

            return new MoveDescr(moveText, optComment.orElse(null));
        } catch (RuntimeException e) {
            throw new PgnParserException("Error parsing move " + moveText.getStr(), e);
        }
    }

}
