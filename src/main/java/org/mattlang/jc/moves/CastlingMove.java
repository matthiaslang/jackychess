package org.mattlang.jc.moves;

import org.mattlang.jc.movegenerator.CastlingDef;

import lombok.Getter;

@Getter
public class CastlingMove {

    private final CastlingDef def;

    private byte type;

    private byte fromIndex;

    private byte toIndex;

    private byte fromIndex2;

    private byte toIndex2;



    public CastlingMove(CastlingDef def, byte type, int fromIndex, int toIndex, int fromIndex2, int toIndex2) {
        this.def = def;
        this.type = type;
        this.fromIndex = (byte) fromIndex;
        this.toIndex = (byte) toIndex;
        this.fromIndex2 = (byte) fromIndex2;
        this.toIndex2 = (byte) toIndex2;
    }

}
