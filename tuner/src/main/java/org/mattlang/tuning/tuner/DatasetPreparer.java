package org.mattlang.tuning.tuner;

import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.jc.engine.CheckChecker;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;
import org.mattlang.jc.movegenerator.BBCheckCheckerImpl;
import org.mattlang.jc.moves.MoveImpl;
import org.mattlang.jc.util.MoveValidator;
import org.mattlang.tuning.BitBoardForTuning;
import org.mattlang.tuning.DataSet;
import org.mattlang.tuning.FenEntry;
import org.mattlang.tuning.data.pgnparser.*;

public class DatasetPreparer {

    private static final Logger LOGGER = Logger.getLogger(DatasetPreparer.class.getSimpleName());
    private final OptParameters params;

    private ParameterizedEvaluation parameterizedEvaluation = new ParameterizedEvaluation();

    private CheckChecker checkChecker = new BBCheckCheckerImpl();

    public static Random rand = new Random(47L);

    public DatasetPreparer(OptParameters params) {
        this.params = params;
    }

    public DataSet prepareLoadFromFile(File file) throws IOException {
        if (file.getName().endsWith(".pgn")) {
            return prepareLoadPgn(file);
        } else if (file.getName().endsWith(".epd") || file.getName().endsWith(".book")) {
            return prepareFromEpd(file);
        } else {
            throw new RuntimeException("can only parse pgn or epd files!");
        }
    }

    public DataSet prepareLoadPgn(File file, PgnPrepareConfig config) throws IOException {
        PgnParser parser = new PgnParser();
        List<PgnGame> games = parser.parse(file);

        return prepareGames(games, file.getName(), config);
    }

    public DataSet prepareLoadPgn(File file) throws IOException {
        return prepareLoadPgn(file, PgnPrepareConfig.DEFAULT);
    }

    private DataSet prepareFromEpd(File file) {
        DataSet dataSet = new DataSet(params);

        try (Stream<String> stream = Files.lines(file.toPath())) {

            LOGGER.info("preparing Data now...");

            stream.forEach(line -> {
                try {
                    parseFen(dataSet, line);
                } catch (RuntimeException re) {
                    LOGGER.log(Level.SEVERE, "Error parsing/preparing fen " + line + ": " + re.getMessage());
                    //                    throw re;
                }
            });
            return dataSet;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void parseFen(DataSet dataSet, String line) {
        if (dataSet.getFens().size() % 5000 == 0) {
            LOGGER.info(" prepared " + dataSet.getFens().size() + " fens...");
        }

        BoardRepresentation board = new BitBoard();

        boolean isCcrlPgenEncoding = line.contains("pgn=");

        Ending ending;
        if (line.contains("\"1/2-1/2\"") || line.contains("pgn=0.5")) {
            ending = Ending.DRAW;
            line = line.replace("\"1/2-1/2\"", "");
            line = line.replace("pgn=0.5", "");
        } else if (line.contains("\"0-1\"")) {
            ending = Ending.MATE_BLACK;
            line = line.replace("\"0-1\"", "");
        } else if (line.contains("\"1-0\"")) {
            ending = Ending.MATE_WHITE;
            line = line.replace("\"1-0\"", "");
        } else if (line.contains("pgn=0.0")) {
            ending = Ending.MATE_WHITE;
            line = line.replace("pgn=0.0", "");
        } else if (line.contains("pgn=1.0")) {
            ending = Ending.MATE_BLACK;
            line = line.replace("pgn=1.0", "");
        } else if (line.contains("[1.0]")) {
            line = line.replace("[1.0]", "");
            ending = Ending.MATE_WHITE;
        } else if (line.contains("[0.5]")) {
            line = line.replace("[0.5]", "");
            ending = Ending.DRAW;
        } else if (line.contains("[0.0]")) {
            line = line.replace("[0.0]", "");
            ending = Ending.MATE_BLACK;
        } else {
            throw new RuntimeException("Error Parsing pgn file: no ending could be found in " + line);
        }

        // replace noise in the zurich test set:
        // replace everything after ";":
        if (line.contains(";")) {
            line = line.split(";")[0];
        }
        line = line.replace(";", "").replace("c9", "") + " 0 0";

        board.setFenPosition("position fen " + line);

        // by ccrl encoding mate is on perspective of site to move:
        if (isCcrlPgenEncoding && ending != Ending.DRAW) {
            // means for black invert the ending result:
            if (board.getSiteToMove() == BLACK) {
                ending = ending == Ending.MATE_WHITE ? Ending.MATE_BLACK : Ending.MATE_WHITE;
            }
        }

        if (!isEvalUsingEndGameFunction(board)) {
            FenEntry entry = new FenEntry(null, BitBoardForTuning.copy(board), ending, null);
            dataSet.addFen(entry);
        }
    }

    private DataSet prepareGames(List<PgnGame> games, String filename, PgnPrepareConfig config) {
        DataSet dataSet = new DataSet(params);
        int counter = 1;
        Iterator<PgnGame> iterator = games.iterator();
        while (iterator.hasNext()) {
            PgnGame game = iterator.next();
            if (game.getResult() != Ending.UNTERMINATED) {
                try {
                    addGame(dataSet, filename, game, counter, config);
                } catch (PgnParserException ppe) {
                    throw new RuntimeException("Error parsing game " + counter + " " + game.getTagStr(), ppe);
                }
            }
            iterator.remove();
            counter++;
        }
        LOGGER.info(" prepared " + counter + " games; " + dataSet.getFens().size() + " fens...");
        return dataSet;
    }

    private void addGame(DataSet dataSet, String filename, PgnGame game, int gameCounter, PgnPrepareConfig config) {

        BoardRepresentation board = new BitBoard();
        board.setStartPosition();
        // play game and add all relevant positions:
        int halfMoveCounter = 1;
        int moveCount = game.getMoves().size() * 2;
        int moveLast = moveCount;
        if (config.getSkipLastNHalfMoves() > 0) {
            moveLast -= config.getSkipLastNHalfMoves();

        }
        List<FenEntry> fens = new ArrayList<>();

        for (PgnMove pgnMove : game.getMoves()) {

            doMove(pgnMove.getWhite(), board);
            if (decideAddMove(config, halfMoveCounter, moveLast)) {
                String comment = config.isWriteComments() ? filename + ":" + gameCounter : null;
                Optional<FenEntry> fen = handleMove(comment, pgnMove.getWhite(), board, game.getResult());
                fen.ifPresent(fens::add);
            }
            halfMoveCounter++;

            if (pgnMove.getBlack() != null) {
                doMove(pgnMove.getBlack(), board);

                if (decideAddMove(config, halfMoveCounter, moveLast)) {
                    String comment = config.isWriteComments() ? filename + ":" + gameCounter : null;
                    Optional<FenEntry> fen = handleMove(comment, pgnMove.getBlack(), board, game.getResult());
                    fen.ifPresent(fens::add);
                }
            }
            halfMoveCounter++;
        }

        // add all prepared or only a limit of the fens to the dataset:
        if (config.getAddOnlyNHalfMoves() > 0 && fens.size() > config.getAddOnlyNHalfMoves()) {
            // if only a limit of moves should be add, then shuffle and add only n moves:
            Collections.shuffle(fens, rand);
            dataSet.add(fens.subList(0, config.getAddOnlyNHalfMoves()));
        } else {
            dataSet.add(fens);
        }
    }

    /**
     * Decide by the given config parameters, if this move should be added.
     *
     * @param config
     * @param halfMoveCounter
     * @param moveLast
     * @return
     */
    private static boolean decideAddMove(PgnPrepareConfig config, int halfMoveCounter, int moveLast) {
        return halfMoveCounter > config.getSkipFirstNHalfMoves() && halfMoveCounter < moveLast;
    }

    private void doMove(MoveDescr moveDesr, BoardRepresentation board) {
        Move move = moveDesr.createMove(board);
        board.domove(move);
    }

    private Optional<FenEntry> handleMove(String comment, MoveDescr moveDesr, BoardRepresentation board,
            Ending ending) {
        MoveValidator moveValidator = new MoveValidator();
        MoveList moveList = moveValidator.generateLegalMoves(board, board.getSiteToMove());

        boolean anyLegalMoves = moveList.size() > 0;
        if (moveDesr.getEnding() == null
                && !isBookMove(moveDesr)
                && anyLegalMoves
                && !isEvalUsingEndGameFunction(board)
                && !isCheck(board)
                && isQuiet(moveList)
                && !isMateScore(moveDesr.getComment())) {
            return Optional.of(addFen(board, ending, comment));
        } else {
            return Optional.empty();
        }
    }

    private boolean isCheck(BoardRepresentation board) {
        return checkChecker.isInChess(board, WHITE) || checkChecker.isInChess(board, BLACK);
    }

    private boolean isEvalUsingEndGameFunction(BoardRepresentation board) {
        return parameterizedEvaluation.isUsingEndgameFunction(board);
    }

    private boolean isQuiet(MoveList moveList) {
        for (int i = 0; i < moveList.size(); i++) {
            int move = moveList.get(i);
            if (MoveImpl.isCapture(move) || MoveImpl.isPromotion(move)) {
                return false;
            }
        }

        return true;
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

    private FenEntry addFen(BoardRepresentation board, Ending ending, String comment) {
        //        String fen = FenComposer.buildFenPosition(board);
        FenEntry entry = new FenEntry(null, BitBoardForTuning.copy(board), ending, comment);
        return entry;
    }

    private boolean isBookMove(MoveDescr moveDesr) {
        return moveDesr.getComment() != null && moveDesr.getComment().getText().contains("book");
    }

}

