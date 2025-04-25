package org.mattlang.tuning.data.epdparser;

import static org.mattlang.tuning.data.pgnparser.AlgebraicNotation.moveFromAN;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.mattlang.jc.board.Move;
import org.mattlang.jc.board.bitboard.BitBoard;
import org.mattlang.tuning.data.pgnparser.MoveText;
import org.mattlang.tuning.data.pgnparser.TextualSymbol;

import lombok.Value;

@Value
public class EpdWithOpCodes {

    private final Epd epd;

    private final EpdOpCodesResult epdOpCodesResult;

    public String getOpCodeAsStr(EpdOpCode opCode) {
        List<? extends TextualSymbol> operand = getOpCode(opCode);
        if (operand == null) {
            return null;
        }
        return operand.stream().map(TextualSymbol::getText).collect(Collectors.joining(", "));
    }

    public List<? extends TextualSymbol> getOpCode(EpdOpCode opCode) {
        return epdOpCodesResult.getOpCodes().get(opCode);
    }

    public List<Move> getOpCodeMoves(EpdOpCode opCode) {
        List<Move> result = new ArrayList<>();
        List<? extends TextualSymbol> operand = getOpCode(opCode);
        if (operand == null) {
            return result;
        }
        BitBoard board = new BitBoard();
        board.setFenPosition(epd.getPositionFen());
        for (TextualSymbol textualSymbol : operand) {
            Move algExpectedMove = moveFromAN(board, board.getSiteToMove(), (MoveText) textualSymbol);
            result.add(algExpectedMove);
        }
        return result;

    }
}
