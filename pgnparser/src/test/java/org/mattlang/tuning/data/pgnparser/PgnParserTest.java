package org.mattlang.tuning.data.pgnparser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.moves.MoveImpl;
import org.mattlang.jc.util.MoveValidator;
import org.mattlang.tuning.data.builder.BoardAndMoves;
import org.mattlang.tuning.data.builder.PgnGameBuilder;
import org.mattlang.tuning.data.pgnwriter.PgnWriter;

public class PgnParserTest {

    @Test
    public void testParsing() throws IOException {
        PgnParser parser = new PgnParser();

        InputStream in = PgnParserTest.class.getResourceAsStream("/testpgn/example.pgn");
        List<PgnGame> games = parser.parse(in);

        assertThat(games).hasSize(400);

        // try the pgn builder and write functionality:
        List<PgnGame> buildedGames = new ArrayList<>();
        List<BoardAndMoves> boardAndMovesList = new ArrayList<>();
        for (PgnGame game : games) {
            BoardAndMoves boardAndMoves = new BoardAndMoves(game);
            PgnGame buildedGame = PgnGameBuilder.from(boardAndMoves).toGame();
            buildedGames.add(buildedGame);
            boardAndMovesList.add(boardAndMoves);
        }

        PgnWriter pgnWriter = new PgnWriter();
        pgnWriter.writeGames(new File("outputpgn.pgn"), buildedGames);

        // re-read the written games and compare them
        List<PgnGame> reReadGames = parser.parse(new FileInputStream(new File("outputpgn.pgn")));
        List<BoardAndMoves> reReadBoardsAndMoves = new ArrayList<>();
        for (PgnGame game : reReadGames) {
            BoardAndMoves boardAndMoves = new BoardAndMoves(game);
            reReadBoardsAndMoves.add(boardAndMoves);
        }

        // now compare them:
        assertThat(reReadBoardsAndMoves.size()).isEqualTo(boardAndMovesList.size());
        for (int i = 0; i < reReadBoardsAndMoves.size(); i++) {
            assertThat(reReadBoardsAndMoves.get(i).getBoard().getZobristHash()).isEqualTo(boardAndMovesList.get(i).getBoard().getZobristHash());
        }
    }

    @Test
    public void testWrite() throws IOException {
        BoardRepresentation board = new BitBoard();

        board.setStartPosition();

        MoveValidator moveValidator = new MoveValidator();
        MoveList moves = moveValidator.generateLegalMoves(board, WHITE.ordinal());
        MoveImpl ply1 = new MoveImpl(moves.get(0));
        board.domove(ply1);

        moves = moveValidator.generateLegalMoves(board, BLACK.ordinal());
        MoveImpl ply2 = new MoveImpl(moves.get(0));
        board.domove(ply2);

        PgnGameBuilder builder = new PgnGameBuilder();
        builder.addMove(ply1);
        builder.addMove(ply2);
        PgnGame game = builder.toGame();
        PgnWriter writer = new PgnWriter();
        String str = writer.gameToString(game);

        System.out.println(str);
    }
}