package org.mattlang.tuning.data.epdparser;

import static org.mattlang.tuning.data.epdparser.OpCodeType.*;
import static org.mattlang.tuning.data.epdparser.OptArgCardinality.MULTIPLE;
import static org.mattlang.tuning.data.epdparser.OptArgCardinality.OPTIONAL;

/**
 * all allowed epd op codes.
 * see https://www.thechessdrum.net/PGN_Reference.txt
 */
public enum EpdOpCode {

    ACD(INTEGER, "acd analysis count depth"),
    ACN(INTEGER, "acn analysis count nodes"),
    ACS(INTEGER, "acs analysis count seconds"),
    AM(MOVE, MULTIPLE, "am avoid move(s)"),
    BM(MOVE, MULTIPLE, " bm best move(s)"),
    C0(TEXT, " c0 comment (primary, also c1 though c9)"),
    C1(TEXT, " c0 comment (primary, also c1 though c9)"),
    C2(TEXT, " c0 comment (primary, also c1 though c9)"),
    C3(TEXT, " c0 comment (primary, also c1 though c9)"),
    C4(TEXT, " c0 comment (primary, also c1 though c9)"),
    C5(TEXT, " c0 comment (primary, also c1 though c9)"),
    C6(TEXT, " c0 comment (primary, also c1 though c9)"),
    C7(TEXT, " c0 comment (primary, also c1 though c9)"),
    C8(TEXT, " c0 comment (primary, also c1 though c9)"),
    C9(TEXT, " c0 comment (primary, also c1 though c9)"),
    CE(SIGNED_INTEGER, "ce centipawn evaluation"),
    DM(INTEGER, "dm direct mate fullmove count"),
    DRAW_ACCEPT(NO_ARG, " draw_accept accept a draw offer"),
    DRAW_CLAIM(NO_ARG, "draw_claim claim a draw"),
    DRAW_OFFER(NO_ARG, "draw_offer offer a draw"),
    DRAW_REJECT(NO_ARG, "draw_reject reject a draw offer"),
    ECO(TEXT, OPTIONAL, "eco Encyclopedia of Chess Openings opening code"),
    FMVN(INTEGER, "fmvn fullmove number"),
    HMVC(INTEGER, "hmvc halfmove clock"),
    ID(TEXT, "id position identification"),
    NIC(TEXT, OPTIONAL, "nic New In Chess opening code"),
    NOOP(NO_ARG, "noop no operation"),
    PM(MOVE, "pm predicted move"),
    PV(MOVE, MULTIPLE, "pv predicted variation"),
    RC(INTEGER, "rc repetition count"),
    RESIGN(NO_ARG, "resign game resignation"),
    SM(MOVE, "sm supplied move"),
    TCGS(INTEGER, "tcgs telecommunication game selector"),
    TCRI(TEXT, "tcri telecommunication receiver identification"),
    TCSI(TEXT, "tcsi telecommunication sender identification"),
    V0(TEXT, "v0 variation name (primary, also v1 though v9)"),
    V1(TEXT, "v0 variation name (primary, also v1 though v9)"),
    V2(TEXT, "v0 variation name (primary, also v1 though v9)"),
    V3(TEXT, "v0 variation name (primary, also v1 though v9)"),
    V4(TEXT, "v0 variation name (primary, also v1 though v9)"),
    V5(TEXT, "v0 variation name (primary, also v1 though v9)"),
    V6(TEXT, "v0 variation name (primary, also v1 though v9)"),
    V7(TEXT, "v0 variation name (primary, also v1 though v9)"),
    V8(TEXT, "v0 variation name (primary, also v1 though v9)"),
    V9(TEXT, "v0 variation name (primary, also v1 though v9)"),
    ;

    public final OpCodeType opCodeType;
    public final String description;

    public final OptArgCardinality cardinality;

    EpdOpCode(OpCodeType opCodeType, OptArgCardinality cardinality, String description) {
        this.opCodeType = opCodeType;
        this.description = description;
        this.cardinality = cardinality;
    }

    EpdOpCode(OpCodeType opCodeType, String description) {
        this.opCodeType = opCodeType;
        this.description = description;
        this.cardinality = OptArgCardinality.SINGLE;
    }
}
