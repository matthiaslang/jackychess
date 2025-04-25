package org.mattlang.jc;

import static org.mattlang.tuning.data.epdparser.EpdOpCode.BM;
import static org.mattlang.tuning.data.epdparser.EpdOpCode.ID;

import java.util.List;

import org.mattlang.jc.board.Move;
import org.mattlang.tuning.data.epdparser.EpdWithOpCodes;

import lombok.Value;

@Value
public class TestPosition {

    public final String fen;

    public final String fenPosition;

    public final String name;

    public final List<Move> expectedBestMoves;

    public TestPosition(String fenPosition) {
        fen = null;
        this.fenPosition = fenPosition;
        this.name = null;
        this.expectedBestMoves = null;
    }

    public TestPosition(EpdWithOpCodes epd) {
        this(epd.getEpd().fen, epd.getOpCodeMoves(BM), epd.getOpCodeAsStr(ID));
    }

    public TestPosition(String fen, List<Move> expectedBestMoves, String name) {
        this.fen = fen;
        this.fenPosition = "position fen " + fen + " 0 0";
        this.expectedBestMoves = expectedBestMoves;
        this.name = name;
    }

    @Override
    public String toString() {
        return name + ": " + fen;
    }
}
