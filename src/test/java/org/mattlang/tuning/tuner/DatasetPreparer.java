package org.mattlang.tuning.tuner;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.util.FenComposer;
import org.mattlang.tuning.DataSet;
import org.mattlang.tuning.FenEntry;
import org.mattlang.tuning.data.pgnparser.*;

public class DatasetPreparer {

    /**
     * Prepares a data set from a pgn file as source.
     *
     * @param in
     * @return
     */
    public DataSet prepareDatasetFromPgn(InputStream in) throws IOException {
        PgnParser parser = new PgnParser();
        List<PgnGame> games = parser.parse(in);

        DataSet dataSet = new DataSet();
        for (PgnGame game : games) {
            addGame(dataSet, game);
        }
        return dataSet;
    }

    private void addGame(DataSet dataSet, PgnGame game) {

        double result = 0.5;
        if (game.getResult() == Ending.MATE_WHITE) {
            result = 1;
        }
        if (game.getResult() == Ending.MATE_BLACK) {
            result = 0;
        }

        BoardRepresentation board = new BitBoard();
        board.setStartPosition();
        // play game and add all relevant positions:
        for (PgnMove pgnMove : game.getMoves()) {

            doAndHandleMove(dataSet, pgnMove.getWhite(), board, result);

            if (pgnMove.getBlack() != null) {
                doAndHandleMove(dataSet, pgnMove.getBlack(), board, result);
            }

        }
    }

    private void doAndHandleMove(DataSet dataSet, MoveDescr moveDesr, BoardRepresentation board, double result) {
        Move move = moveDesr.createMove(board);
        board.domove(move);
        if (moveDesr.getEnding() == null && !isBookMove(moveDesr) && !isMateScore(moveDesr.getComment())) {
            addFen(dataSet, board, result);
        }
    }

    private boolean isMateScore(Comment comment) {

        // comment in cutechess has the form: -319.87/13 1.1s
        String scoreStr = comment.getText().split("/")[0];
        double score = Double.parseDouble(scoreStr);
        return score < -319.0 || score > 319;
    }

    private void addFen(DataSet dataSet, BoardRepresentation board, double result) {
        String fen = FenComposer.buildFenPosition(board);
        FenEntry entry = new FenEntry(fen, board.copy(), result);
        dataSet.addFen(entry);
    }

    private boolean isBookMove(MoveDescr moveDesr) {
        return moveDesr.getComment() != null && moveDesr.getComment().getText().contains("book");
    }
}

