package org.mattlang.jc.uci;

import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.Move;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class UciProcessor {

    private Board board = new Board();

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
           Color who2Move= setPosition(cmdStr);
           asyncEngine.setWho2Move(who2Move);
        } else if (cmdStr.startsWith("go ")) {
            CompletableFuture<Move> result = asyncEngine.start(board);
            result.thenAccept(move ->sendBestMove(move));

        } else if ("stop".equals(cmdStr)) {
            sendBestMove(asyncEngine.stop());
        }

    }


    private Color setPosition(String positionStr) {
        FenParser fenParser = new FenParser();
        return fenParser.setPosition(positionStr, board);
    }

    private void identifyYourself() {
        UCI.instance.putCommand("id name JackyChess 1.0");
        UCI.instance.putCommand("id author Matthias Lang");
        //        UCI.instance.putCommand("option ");
        UCI.instance.putCommand("uciok");
    }

    private void sendBestMove(Move bestMove) {
        UCI.instance.putCommand("bestmove " + bestMove.toStr());
    }
}
