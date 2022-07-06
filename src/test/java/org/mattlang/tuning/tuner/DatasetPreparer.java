package org.mattlang.tuning.tuner;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.util.FenComposer;
import org.mattlang.tuning.BitBoardForTuning;
import org.mattlang.tuning.DataSet;
import org.mattlang.tuning.FenEntry;
import org.mattlang.tuning.data.pgnparser.*;

public class DatasetPreparer {

    private static final Logger LOGGER = Logger.getLogger(DatasetPreparer.class.getSimpleName());

    /**
     * Prepares a data set from a pgn file as source.
     *
     * @param in
     * @return
     */
    public DataSet prepareDatasetFromPgn(InputStream in) throws IOException {
        PgnParser parser = new PgnParser();
        List<PgnGame> games = parser.parse(in);

        return prepareGames(games);
    }

    public DataSet prepareLoadFromFile(File file) throws IOException {
        PgnParser parser = new PgnParser();
        List<PgnGame> games = parser.parse(file);

        return prepareGames(games);
    }

    private DataSet prepareGames(List<PgnGame> games) {
        LOGGER.info("preparing Data now...");
        DataSet dataSet = new DataSet();
        int counter = 0;
        Iterator<PgnGame> iterator=games.iterator();
        while(iterator.hasNext()){
            PgnGame game= iterator.next();
            addGame(dataSet, game);
            iterator.remove();
            counter++;
            if (counter % 500 == 0) {
                LOGGER.info(" prepared " + counter + " games; " + dataSet.getFens().size() + " fens...");
            }
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
        if (comment != null && comment.getText() != null && comment.getText().contains("/")) {
            String scoreStr = comment.getText().split("/")[0];
            if (scoreStr != null) {
                if (scoreStr.startsWith("+M") || scoreStr.startsWith("-M")) {
                    // a "mate in x moves" Syntax:
                    return true;
                }
                double score = Double.parseDouble(scoreStr);
                return score < -319.0 || score > 319;
            }
        }
        // todo we need to decide it ourself somehow... e.g. let our engine calculate a score of that position and decide on that..
        return false;
    }

    private void addFen(DataSet dataSet, BoardRepresentation board, double result) {
        String fen = FenComposer.buildFenPosition(board);
        FenEntry entry = new FenEntry(fen, BitBoardForTuning.copy(board), result);
        dataSet.addFen(entry);
    }

    private boolean isBookMove(MoveDescr moveDesr) {
        return moveDesr.getComment() != null && moveDesr.getComment().getText().contains("book");
    }

}
