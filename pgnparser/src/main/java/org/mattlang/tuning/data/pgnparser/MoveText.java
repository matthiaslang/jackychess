package org.mattlang.tuning.data.pgnparser;

import lombok.Getter;

@Getter
/**
 * Represents (probably) an algebraic notated Move (usually within a pgn file).
 * Since the pgn syntax is not context free, the scanner can also "match" a movetext symbol as opcode (e.g. c4 opcode)
 * where the matcher/parser logic needs to take care then. Thats a bit ugly currently in parsing opcodes, but it works...
 */
public class MoveText extends TextualSymbol {

    public MoveText(String str, TextPosition textPosition) {
        super(str.trim(), textPosition);
    }
}
