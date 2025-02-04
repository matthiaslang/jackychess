package org.mattlang.jc.uci;

import static java.util.logging.Level.SEVERE;
import static org.mattlang.jc.uci.UciKeyWords.*;

import java.util.Arrays;
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
import org.mattlang.jc.engine.search.SearchException;
import org.mattlang.jc.engine.search.SearchThreadContexts;

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

    public void processCmd(String cmdStr) {
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
            CompletableFuture<Move> result = asyncEngine.start(gameState, goParams, gameContext);
            // when the search stops regularly within its search time, deliver the best move
            result.thenAccept(move -> {
                LOGGER.fine(String.format("future completed with best move: %s", move));
                sendBestMove(gameState, move);
            });

        } else if (CMD_STOP.equals(cmdStr)) {
            stop(gameState);
        }

    }

    private void stop(GameState gameState) {
        LOGGER.info("got uci stop, stopping async running engine...");
        Move bestMove = asyncEngine.stop();
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
        String[] result = cmdStr.split("\\s");
        GoParameter param = new GoParameter();
        // example: go wtime 567860 btime 584661 winc 0 binc 0 movestogo 39
        int x = 0;
        while (x < result.length) {
            String tok = result[x];
            if (CMD_GO.equals(tok)) {
                // overread
                x++;
            } else if (INFINITE.equals(tok)) {
                param.infinite = true;
                x++;
            } else if (WTIME.equals(tok)) {
                x++;
                param.wtime = Long.parseLong(result[x]);
                x++;
            } else if (BTIME.equals(tok)) {
                x++;
                param.btime = Long.parseLong(result[x]);
                x++;
            } else if (WINC.equals(tok)) {
                x++;
                param.winc = Long.parseLong(result[x]);
                x++;
            } else if (BINC.equals(tok)) {
                x++;
                param.binc = Long.parseLong(result[x]);
                x++;
            } else if (MOVESTOGO.equals(tok)) {
                x++;
                param.movestogo = Long.parseLong(result[x]);
                x++;
            } else if (MOVETIME.equals(tok)) {
                x++;
                param.movetime = Long.parseLong(result[x]);
                x++;
            } else if (SEARCHMOVES.equals(tok)) {
                x++;
                param.searchMoves = Arrays.copyOfRange(result, x, result.length);
                x = result.length;
            } else {
                // overread unknown token to not endless parse in loop
                x++;
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

    private void sendBestMove(GameState gameState, Move bestMove) {
        UCI.instance.putCommand(CMD_BESTMOVE + " " + bestMove.toUCIString(gameState.getBoard()));
    }

    // analyse:
    // position startpos moves usw..
    // go showeval infinite
    // oder "go infinite" ohne showeval
}
