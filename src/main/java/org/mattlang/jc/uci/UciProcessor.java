package org.mattlang.jc.uci;

import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.GameState;
import org.mattlang.jc.board.Move;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class UciProcessor {

    private Board board = new Board();

    private GameState gameState;

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

    private void processCmd(String cmdStr) {
        cmdStr = cmdStr.trim();
        if ("uci".equals(cmdStr)) {
            identifyYourself();
        } else if ("isready".equals(cmdStr)) {
            UCI.instance.putCommand("readyok");
        } else if ("ucinewgame".equals(cmdStr)) {
            // dont need to response anything
        } else if (cmdStr.startsWith("position")) {
           gameState= setPosition(cmdStr);
        } else if (cmdStr.startsWith("go ")) {
            CompletableFuture<Move> result = asyncEngine.start(gameState);
            result.thenAccept(move ->sendBestMove(move));

        } else if ("stop".equals(cmdStr)) {
            Optional<Move> bm = asyncEngine.stop();
            if (bm.isPresent()) {
                sendBestMove(bm.get());
            }
        }

    }


    private GameState setPosition(String positionStr) {
        FenParser fenParser = new FenParser();
        return fenParser.setPosition(positionStr, board);
    }

    private void identifyYourself() {
        UCI.instance.putCommand("id name JackyChess 0.3.0");
        UCI.instance.putCommand("id author Matthias Lang");
        //        UCI.instance.putCommand("option ");
        UCI.instance.putCommand("uciok");
    }

    private void sendBestMove(Move bestMove) {
        UCI.instance.putCommand("bestmove " + bestMove.toStr());
    }
}
