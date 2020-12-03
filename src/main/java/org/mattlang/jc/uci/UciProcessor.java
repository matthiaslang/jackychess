package org.mattlang.jc.uci;

import java.util.Optional;

import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.Engine;

public class UciProcessor {

    private Engine engine = new Engine();

    private boolean finished = false;

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
            setPosition(cmdStr);
        } else if (cmdStr.startsWith("go ")) {
            Move bestMove = engine.go();
            sendBestMove(bestMove);
        } else if ("stop".equals(cmdStr)) {
            Move bestMove = engine.stop();
            sendBestMove(bestMove);
        }

    }

    private void setPosition(String positionStr) {
        FenParser fenParser = new FenParser();
        fenParser.setPosition(positionStr, engine.getBoard());
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
