package org.mattlang.jc.uci;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.logging.Level.FINE;
import static java.util.logging.Level.SEVERE;
import static org.mattlang.jc.Constants.MAX_PLY;
import static org.mattlang.jc.Constants.MIN_DEPTH;
import static org.mattlang.jc.uci.UciKeyWords.*;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

import org.mattlang.jc.AppConfiguration;
import org.mattlang.jc.ConfigValues;
import org.mattlang.jc.JCExecutors;
import org.mattlang.jc.UCILogger;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.Configurator;
import org.mattlang.jc.engine.search.NegaMaxResult;
import org.mattlang.jc.engine.search.Pondering;
import org.mattlang.jc.engine.search.SearchException;
import org.mattlang.jc.engine.search.SearchThreadContexts;
import org.mattlang.jc.moves.MoveToStringConverter;

public class UciProcessor {

    private static final Logger LOGGER = Logger.getLogger(UciProcessor.class.getSimpleName());

    private GameState gameState;

    private GameContext gameContext = new GameContext();

    private boolean finished = false;

    private AsyncEngine asyncEngine = new AsyncEngine();

    public void start() {
        try {
            while (!finished) {
                Optional<String> optCmd = UCI.instance.readCommand();
                if (optCmd.isPresent()) {
                    processCmd(optCmd.get());
                }
            }
        } catch (SearchException se) {
            LOGGER.log(SEVERE, se.toStringAllInfos(), se);
        } catch (Exception e) {
            LOGGER.log(SEVERE, "Error in main uci processing loop!", e);
        }
    }

    private void processCmd(String cmdStr) {
        cmdStr = cmdStr.trim();
        if (CMD_UCI.equals(cmdStr)) {
            identifyYourself();
        } else if (CMD_QUIT.equals(cmdStr)) {
            quit();
        } else if (CMD_ISREADY.equals(cmdStr)) {
            UCI.instance.putCommand(CMD_READYOK);
        } else if (CMD_UCINEWGAME.equals(cmdStr)) {
            gameContext = createNewGameContext();
        } else if (cmdStr.startsWith(POSITION)) {
            gameState = setPosition(cmdStr);
        } else if (cmdStr.startsWith("setoption name ")) {
            // setoption name thinktime value 16
            parseOption(cmdStr);
        } else if (cmdStr.startsWith("go ")) {
            GoParameter goParams = parseGoParams(cmdStr);
            Pondering.pondering = goParams.pondering;
            CompletableFuture<NegaMaxResult> result = asyncEngine.start(gameState, goParams, gameContext);
            // when the search stops regularly within its search time, deliver the best move
            result.thenAccept(negaMaxResult -> {
                if (LOGGER.isLoggable(FINE)) {
                    LOGGER.fine(String.format("future completed with best move: %s", negaMaxResult.savedMove));
                }
                sendBestMove(gameState, negaMaxResult);
            });

        } else if (CMD_STOP.equals(cmdStr)) {
            stop(gameState);
        } else if (CMD_PONDERHIT.equals(cmdStr)) {
            ponderhit(gameState);
        } else if (cmdStr.startsWith(CMD_DEBUG)) {
            handleDebugMode(cmdStr);
        }

    }

    private void ponderhit(GameState gameState) {
        Pondering.pondering = false;

    }

    private void handleDebugMode(String cmdStr) {
        String[] result = cmdStr.split("\\s");
        String onOff = result.length >= 2 ? result[1] : "";
        UCILogger.uciDebugMode = onOff.equals(CMD_DEBUG_ON);
    }

    private void stop(GameState gameState) {
        LOGGER.info("got uci stop, stopping async running engine...");
        NegaMaxResult bestMove = asyncEngine.stop();
        sendBestMove(gameState, bestMove);
    }

    private GameContext createNewGameContext() {
        gameContext.logStatistics();

        LOGGER.info("start new game");
        SearchThreadContexts.CONTEXTS.reset();
        return new GameContext();
    }

    private void quit() {
        gameContext.logStatistics();

        UCI.instance.quit();
        finished = true;
        JCExecutors.EXECUTOR_SERVICE.shutdownNow();
        System.exit(0);
    }

    public void parseOption(String cmdStr) {
        //example: setoption name thinktime value 16
        String[] result = cmdStr.split("\\s");
        String option = result[2];
        String value = result[4];
        try {
            UCIOption.parseOption(ConfigValues.getConfigValues().getAllOptions(), option, value);
        } catch (IllegalArgumentException iae) {
            LOGGER.warning(iae.getMessage());
            UCILogger.log(iae.getMessage());
        }
    }

    public GoParameter parseGoParams(String cmdStr) {
        UciStringParser parser = new UciStringParser(cmdStr);

        GoParameter param = new GoParameter();
        // example: go wtime 567860 btime 584661 winc 0 binc 0 movestogo 39

        while (parser.hasNext()) {
            if (parser.match(CMD_GO)) {
                // overread
            } else if (parser.match(INFINITE)) {
                param.infinite = true;
            } else if (parser.match(PONDER)) {
                param.pondering = true;
            } else if (parser.match(WTIME)) {
                param.wtime = parser.matchLong();
            } else if (parser.match(BTIME)) {
                param.btime = parser.matchLong();
            } else if (parser.match(WINC)) {
                param.winc = parser.matchLong();
            } else if (parser.match(BINC)) {
                param.binc = parser.matchLong();
            } else if (parser.match(MOVESTOGO)) {
                param.movestogo = parser.matchLong();
            } else if (parser.match(MOVETIME)) {
                param.movetime = parser.matchLong();
            } else if (parser.match(MATE)) {
                param.mate = parser.matchInt();
            } else if (parser.match(DEPTH)) {
                param.depth = min(MAX_PLY - 1, max(MIN_DEPTH, parser.matchInt()));
            } else if (parser.match(NODES)) {
                param.nodes = parser.matchInt();
            } else if (parser.match(SEARCHMOVES)) {
                param.searchMoves = parser.collectRestTokens();
            } else {
                // overread unknown token to not endless parse in loop
                parser.match();
            }
        }
        return param;

    }

    private GameState setPosition(String positionStr) {
        try {
            BoardRepresentation board = Configurator.createBoard();
            boolean isChess960 = ConfigValues.getConfigValues().uciChess960.getValue().booleanValue();
            return FenParser.setPosition(positionStr, board, isChess960);
        } catch (RuntimeException re) {
            throw new RuntimeException("Error parsing UCI postion: " + positionStr, re);
        }
    }

    private void identifyYourself() {
        String version = AppConfiguration.getAppProps().getProperty("version");
        UCI.instance.putCommand("id name JackyChess " + version);
        UCI.instance.putCommand("id author Matthias Lang");

        UCIOption.writeOptionsDescriptions(ConfigValues.getConfigValues().getAllOptions());

        UCI.instance.putCommand(CMD_UCIOK);
    }

    private void sendBestMove(GameState gameState, NegaMaxResult negaMaxResult) {
        Move bestMove = negaMaxResult.savedMove;
        if (ConfigValues.getConfigValues().ponder.getValue()) {
            String ponderMoveStr = MoveToStringConverter.toUCIString(negaMaxResult.ponderMove, gameState.getBoard());
            UCI.instance.putCommand(CMD_BESTMOVE + " " + bestMove.toUCIString(gameState.getBoard())
                                    + " ponder " +  ponderMoveStr);
        } else {
            UCI.instance.putCommand(CMD_BESTMOVE + " " + bestMove.toUCIString(gameState.getBoard()));
        }
    }

    // analyse:
    // position startpos moves usw..
    // go showeval infinite
    // oder "go infinite" ohne showeval
}
