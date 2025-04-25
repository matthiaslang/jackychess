package org.mattlang.tuning.data.epdparser;

import lombok.Value;

@Value
public class Epd {

    public String fen;

    public String unparsedOpcodes;

    public Epd(String rawPartialFen, String unparsedOpcodes) {
        this.fen = rawPartialFen;
        this.unparsedOpcodes = unparsedOpcodes;
    }

    public String getPositionFen() {
        return "position fen " + fen + " 0 1";
    }
}
