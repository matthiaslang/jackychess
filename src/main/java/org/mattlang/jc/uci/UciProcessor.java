package org.mattlang.jc.uci;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;

public class UciProcessor {

    private BoardRepresentation board = Factory.getDefaults().boards.create();

    private GameState gameState;

    public static final String OP_THINKTIME="thinktime";
    public static final String OP_QUIESCENCE="quiescence";

    private Map<String, Long> options = new HashMap<>();

    private boolean finished = false;

    private AsyncEngine asyncEngine = new AsyncEngine();

    public void start() {
        while (!finished) {
            Optional<String> optCmd = UCI.instance.readCommand();
            if (optCmd.isPresent()) {
                processCmd(optCmd.get());
            }
        }
    }

    public void processCmd(String cmdStr) {
        cmdStr = cmdStr.trim();
        if ("uci".equals(cmdStr)) {
            identifyYourself();
        } else if ("quit".equals(cmdStr)) {
            quit();
        } else if ("isready".equals(cmdStr)) {
            UCI.instance.putCommand("readyok");
        } else if ("ucinewgame".equals(cmdStr)) {
            // dont need to response anything
        } else if (cmdStr.startsWith("position")) {
            gameState = setPosition(cmdStr);
        } else if (cmdStr.startsWith("setoption name ")) {
            // setoption name thinktime value 16
            parseOption(cmdStr);
        } else if (cmdStr.startsWith("go ")) {
            GoParameter goParams = parseGoParams(cmdStr);
            CompletableFuture<Move> result = asyncEngine.start(gameState, goParams, options);
            result.thenAccept(move ->sendBestMove(move));

        } else if ("stop".equals(cmdStr)) {
            asyncEngine.stop();
        }

    }

    private void quit() {
        UCI.instance.quit();
        finished = true;

    }

    public void parseOption(String cmdStr) {
        //example: setoption name thinktime value 16
        String[] result = cmdStr.split("\\s");
        String option = result[2];
        String value = result[4];
        long lval = Long.parseLong(value);
        options.put(option, lval);

    }

    public GoParameter parseGoParams(String cmdStr) {
        String[] result = cmdStr.split("\\s");
        GoParameter param = new GoParameter();
        // example: go wtime 567860 btime 584661 winc 0 binc 0 movestogo 39
        int x = 0;
        while (x< result.length){
            String tok = result[x];
            if ("go".equals(tok)) {
                // overread
                x++;
            }
            if ("infinite".equals(tok)) {
                param.infinite=true;
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
        }
        return param;

    }


    private GameState setPosition(String positionStr) {
        FenParser fenParser = new FenParser();
        return fenParser.setPosition(positionStr, board);
    }

    private void identifyYourself() {
        UCI.instance.putCommand("id name JackyChess " + Factory.getAppProps().getProperty("version"));
        UCI.instance.putCommand("id author Matthias Lang");

        UCI.instance.putCommand("option name " + OP_THINKTIME + " type spin default 15 min 5 max 600");
        UCI.instance.putCommand("option name " + OP_QUIESCENCE + " type spin default 0 min 0 max 6");
        UCI.instance.putCommand("uciok");
    }

    private void sendBestMove(Move bestMove) {
        UCI.instance.putCommand("bestmove " + bestMove.toStr());
    }

    public Map<String, Long> getOptions() {
        return options;
    }
}
