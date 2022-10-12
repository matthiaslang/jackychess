package org.mattlang.jc.moves;

import org.mattlang.jc.movegenerator.CastlingDef;

import lombok.Getter;

@Getter
public class CastlingMove {

    private final CastlingDef def;

    private byte type;

    private byte kingFrom;

    private byte kingTo;

    private byte rookFrom;

    private byte rookTo;



    public CastlingMove(CastlingDef def, byte type, int kingFrom, int kingTo, int rookFrom, int rookTo) {
        this.def = def;
        this.type = type;
        this.kingFrom = (byte) kingFrom;
        this.kingTo = (byte) kingTo;
        this.rookFrom = (byte) rookFrom;
        this.rookTo = (byte) rookTo;
    }

}
