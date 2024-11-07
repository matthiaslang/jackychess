package org.mattlang.jc.uci;

import static java.util.logging.Level.SEVERE;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

import org.mattlang.jc.AppConfiguration;
import org.mattlang.jc.ConfigValues;
import org.mattlang.jc.Factory;
import org.mattlang.jc.JCExecutors;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.Configurator;
import org.mattlang.jc.engine.search.SearchException;
import org.mattlang.jc.engine.search.SearchThreadContexts;

public class UciProcessor {

    private static final Logger LOGGER = Logger.getLogger(UciProcessor.class.getSimpleName());

    public static final String CMD_UCI = "uci";
    public static final String CMD_QUIT = "quit";
    public static final String CMD_ISREADY = "isready";
    public static final String CMD_UCIOK = "uciok";
    public static final String CMD_READYOK = "readyok";
    public static final String CMD_UCINEWGAME = "ucinewgame";
    public static final String CMD_BESTMOVE = "bestmove";

    private GameState gameState;

    private GameContext gameContext = new GameContext();

    private ConfigValues configValues = new ConfigValues();

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
        } else if (cmdStr.startsWith("position")) {
            gameState = setPosition(cmdStr);
        } else if (cmdStr.startsWith("setoption name ")) {
            // setoption name thinktime value 16
            parseOption(cmdStr);
        } else if (cmdStr.startsWith("go ")) {
            GoParameter goParams = parseGoParams(cmdStr);
            CompletableFuture<Move> result = asyncEngine.start(gameState, goParams, configValues, gameContext);
            result.thenAccept(move -> sendBestMove(gameState, move));

        } else if ("stop".equals(cmdStr)) {
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
        return new GameContext(configValues);
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

        UCIOption.parseOption(configValues.getAllOptions(), option, value);
    }

    public GoParameter parseGoParams(String cmdStr) {
        String[] result = cmdStr.split("\\s");
        GoParameter param = new GoParameter();
        // example: go wtime 567860 btime 584661 winc 0 binc 0 movestogo 39
        int x = 0;
        while (x < result.length) {
            String tok = result[x];
            if ("go".equals(tok)) {
                // overread
                x++;
            }
            if ("infinite".equals(tok)) {
                param.infinite = true;
                x++;
            }
            if ("wtime".equals(tok)) {
                x++;
                param.wtime = Long.parseLong(result[x]);
                x++;
            }
            if ("btime".equals(tok)) {
                x++;
                param.btime = Long.parseLong(result[x]);
                x++;
            }
            if ("winc".equals(tok)) {
                x++;
                param.winc = Long.parseLong(result[x]);
                x++;
            }
            if ("binc".equals(tok)) {
                x++;
                param.binc = Long.parseLong(result[x]);
                x++;
            }
            if ("movestogo".equals(tok)) {
                x++;
                param.movestogo = Long.parseLong(result[x]);
                x++;
            }
            if ("movetime".equals(tok)) {
                x++;
                param.movetime = Long.parseLong(result[x]);
                x++;
            }
        }
        return param;

    }

    private GameState setPosition(String positionStr) {
        try {
            FenParser fenParser = new FenParser();
            BoardRepresentation board = Configurator.createBoard();
            boolean isChess960 = Factory.getDefaults().getConfig().uciChess960.getValue().booleanValue();
            return fenParser.setPosition(positionStr, board, isChess960);
        } catch (RuntimeException re) {
            throw new RuntimeException("Error parsing UCI postion: " + positionStr, re);
        }
    }

    private void identifyYourself() {
        String version = AppConfiguration.getAppProps().getProperty("version");
        UCI.instance.putCommand("id name JackyChess " + version);
        UCI.instance.putCommand("id author Matthias Lang");

        UCIOption.writeOptionsDescriptions(configValues.getAllOptions());

        UCI.instance.putCommand(CMD_UCIOK);
    }

    private void sendBestMove(GameState gameState, Move bestMove) {
        UCI.instance.putCommand(CMD_BESTMOVE + " " + bestMove.toUCIString(gameState.getBoard()));
    }

    public ConfigValues getConfigValues() {
        return configValues;
    }
}
