package org.mattlang.tuning.data.builder;

import java.util.ArrayList;
import java.util.List;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.tuning.data.pgnparser.Ending;
import org.mattlang.tuning.data.pgnparser.MoveDescr;
import org.mattlang.tuning.data.pgnparser.PgnGame;
import org.mattlang.tuning.data.pgnparser.PgnMove;

import lombok.Getter;

@Getter
public class BoardAndMoves {

    private BoardRepresentation board = new BitBoard();
    private BoardRepresentation startBoard = new BitBoard();

    @Getter
    private String startFen = null;

    @Getter
    private List<Move> moves = new ArrayList<>();

    @Getter
    private Ending ending;

    public BoardAndMoves(PgnGame game) {
        String fenStr = game.getTag(PgnGame.TAG_FEN);
        if (fenStr != null) {
            board.setFenPosition(fenStr);
            startFen = fenStr;
        } else {
            board.setStartPosition();
        }
        startBoard = board.copy();

        for (PgnMove pgnMove : game.getMoves()) {

            doMove(pgnMove.getWhite(), board);
            if (pgnMove.getBlack() != null) {
                doMove(pgnMove.getBlack(), board);
            }

        }
    }

    private void doMove(MoveDescr moveDesr, BoardRepresentation board) {
        Move move = moveDesr.createMove(board);
        board.domove(move);
        moves.add(move);
        ending = moveDesr.getEnding();
    }
}
