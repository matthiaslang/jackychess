package org.mattlang.jc.poc.qbbSpeedComparison;

/*
 This perft implementation is based on QBBEngine by Fabio Gobbato and ported to Java by Emanuel Torres.

 The purpose is to compare the speed differences of various languages for a chess programming workload.

 Used to compare this code with the speed of jacky chess perft to analyse for probably optimizations.
*/

class QbbPerft {
    static final long WHITE = 0;
    static final long BLACK = 8;

    /* define the piece type: empty, pawn, knight, bishop, rook, queen, king */
    static final long EMPTY = 0;
    static final long PAWN = 1;
    static final long KNIGHT = 2;
    static final long BISHOP = 3;
    static final long ROOK = 4;
    static final long QUEEN = 5;
    static final long KING = 6;

    /* define the move type, for example
       KING|CASTLE is a castle move
       PAWN|CAPTURE|EP is an enpassant move
       PAWN|PROMO|CAPTURE is a promotion with a capture */
    static final long CASTLE = 0x40;
    static final long PROMO = 0x20;
    static final long EP = 0x10;
    static final long CAPTURE = 0x08;

    /*
    Board structure definition

    PM,P0,P1,P2 are the 4 bitboards that contain the whole board
    PM is the bitboard with the side to move pieces
    P0,P1 and P2: with these bitboards you can obtain every type of pieces and every pieces combinations.
    */
    static class TBoard {
        long PM;
        long P0;
        long P1;
        long P2;
        byte CastleFlags; /* ..sl..SL  short long opponent SHORT LONG side to move */
        byte EnPassant; /* enpassant column, =8 if not set */
        byte STM; /* side to move */
    };

    /*
    Into Game are saved all the positions from the last 50 move counter reset
    Position is the pointer to the last position of the game
    */
    static TBoard[] Game = new TBoard[512];
    static int pPosition;
    static {
        for (int n = 0; n < Game.length; ++n)
            Game[n] = new TBoard();
    }

    /* array of bitboards that contains all the knight destination for every square */
    static long[] KnightDest= {0x0000000000020400L,0x0000000000050800L,0x00000000000a1100L,0x0000000000142200L,
            0x0000000000284400L,0x0000000000508800L,0x0000000000a01000L,0x0000000000402000L,
            0x0000000002040004L,0x0000000005080008L,0x000000000a110011L,0x0000000014220022L,
            0x0000000028440044L,0x0000000050880088L,0x00000000a0100010L,0x0000000040200020L,
            0x0000000204000402L,0x0000000508000805L,0x0000000a1100110aL,0x0000001422002214L,
            0x0000002844004428L,0x0000005088008850L,0x000000a0100010a0L,0x0000004020002040L,
            0x0000020400040200L,0x0000050800080500L,0x00000a1100110a00L,0x0000142200221400L,
            0x0000284400442800L,0x0000508800885000L,0x0000a0100010a000L,0x0000402000204000L,
            0x0002040004020000L,0x0005080008050000L,0x000a1100110a0000L,0x0014220022140000L,
            0x0028440044280000L,0x0050880088500000L,0x00a0100010a00000L,0x0040200020400000L,
            0x0204000402000000L,0x0508000805000000L,0x0a1100110a000000L,0x1422002214000000L,
            0x2844004428000000L,0x5088008850000000L,0xa0100010a0000000L,0x4020002040000000L,
            0x0400040200000000L,0x0800080500000000L,0x1100110a00000000L,0x2200221400000000L,
            0x4400442800000000L,0x8800885000000000L,0x100010a000000000L,0x2000204000000000L,
            0x0004020000000000L,0x0008050000000000L,0x00110a0000000000L,0x0022140000000000L,
            0x0044280000000000L,0x0088500000000000L,0x0010a00000000000L,0x0020400000000000L};
    /* The same for the king */
    static long[] KingDest = {0x0000000000000302L,0x0000000000000705L,0x0000000000000e0aL,0x0000000000001c14L,
            0x0000000000003828L,0x0000000000007050L,0x000000000000e0a0L,0x000000000000c040L,
            0x0000000000030203L,0x0000000000070507L,0x00000000000e0a0eL,0x00000000001c141cL,
            0x0000000000382838L,0x0000000000705070L,0x0000000000e0a0e0L,0x0000000000c040c0L,
            0x0000000003020300L,0x0000000007050700L,0x000000000e0a0e00L,0x000000001c141c00L,
            0x0000000038283800L,0x0000000070507000L,0x00000000e0a0e000L,0x00000000c040c000L,
            0x0000000302030000L,0x0000000705070000L,0x0000000e0a0e0000L,0x0000001c141c0000L,
            0x0000003828380000L,0x0000007050700000L,0x000000e0a0e00000L,0x000000c040c00000L,
            0x0000030203000000L,0x0000070507000000L,0x00000e0a0e000000L,0x00001c141c000000L,
            0x0000382838000000L,0x0000705070000000L,0x0000e0a0e0000000L,0x0000c040c0000000L,
            0x0003020300000000L,0x0007050700000000L,0x000e0a0e00000000L,0x001c141c00000000L,
            0x0038283800000000L,0x0070507000000000L,0x00e0a0e000000000L,0x00c040c000000000L,
            0x0302030000000000L,0x0705070000000000L,0x0e0a0e0000000000L,0x1c141c0000000000L,
            0x3828380000000000L,0x7050700000000000L,0xe0a0e00000000000L,0xc040c00000000000L,
            0x0203000000000000L,0x0507000000000000L,0x0a0e000000000000L,0x141c000000000000L,
            0x2838000000000000L,0x5070000000000000L,0xa0e0000000000000L,0x40c0000000000000L};

    /* masks for finding the pawns that can capture with an enpassant (in move generation) */
    static long[] EnPassant = {
            0x0000000200000000L,0x0000000500000000L,0x0000000A00000000L,0x0000001400000000L,
            0x0000002800000000L,0x0000005000000000L,0x000000A000000000L,0x0000004000000000L
    };

    /* masks for finding the pawns that can capture with an enpassant (in make move) */
    static long[] EnPassantM = {
            0x0000000002000000L,0x0000000005000000L,0x000000000A000000L,0x0000000014000000L,
            0x0000000028000000L,0x0000000050000000L,0x00000000A0000000L,0x0000000040000000L
    };

    /* extract the least significant bit of the bitboard */
    static long ExtractLSB(long bb) { return bb & -bb; }
    /* reset the least significant bit of bb */
    static long ClearLSB(long bb) { return bb & (bb-1); }

    /* Macro to check and reset the castle rights:
       CastleSM: short castling side to move
       CastleLM: long castling side to move
       CastleSO: short castling opponent
       CastleLO: long castling opponent
     */
    static boolean CastleSM(TBoard Position) { return (Position.CastleFlags&0x02) != 0; }
    static boolean CastleLM(TBoard Position) { return (Position.CastleFlags&0x01) != 0; }

    /* these Macros are used to calculate the bitboard of a particular kind of piece

       P2 P1 P0
        0  0  0    empty
        0  0  1    pawn
        0  1  0    knight
        0  1  1    bishop
        1  0  0    rook
        1  0  1    queen
        1  1  0    king
    */
    static long Occupation(TBoard Position) { return Position.P0 | Position.P1 | Position.P2; } /* board occupation */
    static long Pawns(TBoard Position) { return Position.P0 & ~Position.P1 & ~Position.P2; } /* all the pawns on the board */
    static long Knights(TBoard Position) { return ~Position.P0 & Position.P1 & ~Position.P2; }
    static long Bishops(TBoard Position) { return Position.P0 & Position.P1; }
    static long Rooks(TBoard Position) { return ~Position.P0 & ~Position.P1 & Position.P2; }
    static long Queens(TBoard Position) { return Position.P0 & Position.P2; }
    static long QueenOrRooks(TBoard Position) { return ~Position.P1 & Position.P2; }
    static long QueenOrBishops(TBoard Position) { return Position.P0 & (Position.P2 | Position.P1); }
    static long Kings(TBoard Position) { return Position.P1 & Position.P2; } /* a bitboard with the 2 kings */

    /* calculate the square related to the opponent */
    static long OppSq(long sp) { return sp^0x38; }

    /*
    The board is always saved with the side to move in the lower part of the bitboards to use the same generation and
    make for the Black and the White side.
    This needs the inversion of the 4 bitboards, roll the Castle rights and update the side to move.
    */
    static void ChangeSide(TBoard Position) {
        Position.PM^=Occupation(Position); /* update the side to move pieces */
        Position.PM=Long.reverseBytes(Position.PM);
        Position.P0=Long.reverseBytes(Position.P0);
        Position.P1=Long.reverseBytes(Position.P1);
        Position.P2=Long.reverseBytes(Position.P2);/* reverse the board */
        Position.CastleFlags=(byte)((Position.CastleFlags>>>4)|(Position.CastleFlags<<4));/* roll the castle rights */
        Position.STM ^= BLACK; /* change the side to move */
    }

    /* return the bitboard with the rook destinations */
    static long GenRook(long sq,long occupation) {
        long piece = 1L<<sq;
        occupation ^= piece; /* remove the selected piece from the occupation */
        long piecesup=(0x0101010101010101L<<sq)&(occupation|0xFF00000000000000L); /* find the pieces up */
        long piecesdo=(0x8080808080808080L>>>(63-sq))&(occupation|0x00000000000000FFL); /* find the pieces down */
        long piecesri=(0x00000000000000FFL<<sq)&(occupation|0x8080808080808080L); /* find pieces on the right */
        long piecesle=(0xFF00000000000000L>>>(63-sq))&(occupation|0x0101010101010101L); /* find pieces on the left */
        return (((0x8080808080808080L>>>(63-Long.numberOfTrailingZeros(piecesup)))&(0x0101010101010101L<<(0x3F ^ Long.numberOfLeadingZeros(piecesdo)))) |
                ((0xFF00000000000000L>>>(63-Long.numberOfTrailingZeros(piecesri)))&(0x00000000000000FFL<<(0x3F ^ Long.numberOfLeadingZeros(piecesle)))))^piece;
      /* From every direction find the first piece and from that piece put a mask in the opposite direction.
         Put togheter all the 4 masks and remove the moving piece */
    }

    /* return the bitboard with the bishops destinations */
    static long GenBishop(long sq,long occupation) {
        /* it's the same as the rook */
        long piece = 1L<<sq;
        occupation ^= piece;
        long piecesup=(0x8040201008040201L<<sq)&(occupation|0xFF80808080808080L);
        long piecesdo=(0x8040201008040201L>>>(63-sq))&(occupation|0x01010101010101FFL);
        long piecesle=(0x8102040810204081L<<sq)&(occupation|0xFF01010101010101L);
        long piecesri=(0x8102040810204081L>>>(63-sq))&(occupation|0x80808080808080FFL);
        return (((0x8040201008040201L>>>(63-Long.numberOfTrailingZeros(piecesup)))&(0x8040201008040201L<<(0x3F ^ Long.numberOfLeadingZeros(piecesdo)))) |
                ((0x8102040810204081L>>>(63-Long.numberOfTrailingZeros(piecesle)))&(0x8102040810204081L<<(0x3F ^ Long.numberOfLeadingZeros(piecesri)))))^piece;
    }

    /* return the bitboard with pieces of the same type */
    static long BBPieces(TBoard Position,long piece) {
        // find the bb with the pieces of the same type
        if (piece == PAWN) return Pawns(Position);
        if (piece == KNIGHT) return Knights(Position);
        if (piece == BISHOP) return Bishops(Position);
        if (piece == ROOK) return Rooks(Position);
        if (piece == QUEEN) return Queens(Position);
        /*if (piece == KING)*/ return Kings(Position);
    }

    /* return the bitboard with the destinations of a piece in a square (exept for pawns) */
    static long BBDestinations(long piece,long sq,long occupation) {
        // generate the destination squares of the piece
        if (piece == KNIGHT) return KnightDest[(int)sq];
        if (piece == BISHOP) return GenBishop(sq,occupation);
        if (piece == ROOK) return GenRook(sq,occupation);
        if (piece == QUEEN) return GenRook(sq,occupation)|GenBishop(sq,occupation);
        /*if (piece == KING)*/ return KingDest[(int)sq];
    }

    /* try the move and see if the king is in check. If so return the attacking pieces, if not return 0 */
    static boolean Illegal(TBoard Position, int move) {
        int moveType = move & 0xff;
        int moveFrom = (move >> 8) & 0xff;
        int moveTo = (move >> 16) & 0xff;

        long From = 1L<<moveFrom;
        long To = 1L<<moveTo;
        long occupation,opposing;
        occupation = Occupation(Position);
        opposing = Position.PM^occupation;
        long kings = Kings(Position);
        long king, kingsq;
        long newoccupation = (occupation^From)|To;
        long newopposing = opposing&~To;
        if ((moveType&0x07)==KING) {
            king = To;
            kingsq = moveTo;
        } else {
            king = kings & Position.PM;
            kingsq = Long.numberOfTrailingZeros(king);
            if ((moveType&EP) != 0) {newopposing^=To>>8; newoccupation^=To>>8;}
        }
        if ((KnightDest[(int)kingsq] & Knights(Position) & newopposing) != 0) return true;
        if (((((king<<9)&0xFEFEFEFEFEFEFEFEL) | ((king<<7)&0x7F7F7F7F7F7F7F7FL)) & Pawns(Position) & newopposing) != 0) return true;
        long qob = QueenOrBishops(Position) & newopposing; if (qob != 0) { if ((GenBishop(kingsq,newoccupation) & qob) != 0) return true; }
        long qor = QueenOrRooks(Position) & newopposing; if (qor != 0) { if ((GenRook(kingsq,newoccupation) & qor) != 0) return true; }
        if ((KingDest[(int)kingsq] & kings & newopposing) != 0) return true;
        return false;
    }

    /* Generate all pseudo-legal quiet moves */
    static int GenerateQuiets(TBoard Position, int[] quiets, int pquiets) {
        long occupation,opposing;
        occupation = Occupation(Position);
        opposing = occupation^Position.PM;

        for (long piece=KING;piece>=KNIGHT;piece--) { // generate moves from king to knight
            // generate moves for every piece of the same type of the side to move
            for (long pieces=BBPieces(Position,piece)&Position.PM;pieces!=0;pieces=ClearLSB(pieces)) {
                long sq = Long.numberOfTrailingZeros(pieces);
                // for every destinations on a free square generate a move
                for (long destinations = ~occupation&BBDestinations(piece,sq,occupation);destinations!=0;destinations=ClearLSB(destinations)) {
                    quiets[pquiets++] = (int)(piece + (sq << 8) + (Long.numberOfTrailingZeros(destinations) << 16) + (EMPTY << 24));
                }
            }
        }

        /* one pawns push */
        long push1 = (((Pawns(Position)&Position.PM)<<8) & ~occupation)&0x00FFFFFFFFFFFFFFL;
        for (long pieces=push1;pieces!=0;pieces=ClearLSB(pieces)) {
            long lsb = Long.numberOfTrailingZeros(pieces);
            quiets[pquiets++] = (int)(PAWN + ((lsb-8) << 8) + (lsb << 16) + (EMPTY << 24));
        }

        /* double pawns pushes */
        for (long push2 = (push1<<8) & ~occupation & 0x00000000FF000000L;push2!=0;push2=ClearLSB(push2)) {
            long lsb = Long.numberOfTrailingZeros(push2);
            quiets[pquiets++] = (int)(PAWN + ((lsb-16) << 8) + (lsb << 16) + (EMPTY << 24));
        }

        /* check if long castling is possible */
        if (CastleLM(Position) && (occupation & 0x0EL) == 0) {
            long roo,bis;
            roo = ExtractLSB(0x1010101010101000L&occupation); /* column e */
            roo |= ExtractLSB(0x0808080808080800L&occupation); /*column d */
            roo |= ExtractLSB(0x0404040404040400L&occupation); /*column c */
            roo |= ExtractLSB(0x00000000000000E0L&occupation);  /* row 1 */
            bis = ExtractLSB(0x0000000102040800L&occupation); /*antidiag from e1/e8 */
            bis |= ExtractLSB(0x0000000001020400L&occupation); /*antidiag from d1/d8 */
            bis |= ExtractLSB(0x0000000000010200L&occupation); /*antidiag from c1/c8 */
            bis |= ExtractLSB(0x0000000080402000L&occupation); /*diag from e1/e8 */
            bis |= ExtractLSB(0x0000008040201000L&occupation); /*diag from d1/d8 */
            bis |= ExtractLSB(0x0000804020100800L&occupation); /*diag from c1/c8 */
            if ((((roo&QueenOrRooks(Position)) | (bis&QueenOrBishops(Position)) | (0x00000000003E7700L&Knights(Position))|
                    (0x0000000000003E00L&Pawns(Position)) | (Kings(Position)&0x0000000000000600L))&opposing) == 0) {
                /* check if c1/c8 d1/d8 e1/e8 are not attacked */
                quiets[pquiets++] = (int)((KING|CASTLE) + (4 << 8) + (2 << 16) + (EMPTY << 24));
            }
        }
        /* check if short castling is possible */
        if (CastleSM(Position) && (occupation & 0x60L) == 0) {
            long roo,bis;
            roo = ExtractLSB(0x1010101010101000L&occupation); /* column e */
            roo |= ExtractLSB(0x2020202020202000L&occupation); /* column f */
            roo |= ExtractLSB(0x4040404040404000L&occupation); /* column g */
            roo |= 1L<<(0x3F ^ Long.numberOfLeadingZeros(0x000000000000000FL&(occupation|0x1L)));/* row 1 */
            bis = ExtractLSB(0x0000000102040800L&occupation); /* antidiag from e1/e8 */
            bis |= ExtractLSB(0x0000010204081000L&occupation); /*antidiag from f1/f8 */
            bis |= ExtractLSB(0x0001020408102000L&occupation); /*antidiag from g1/g8 */
            bis |= ExtractLSB(0x0000000080402000L&occupation); /*diag from e1/e8 */
            bis |= ExtractLSB(0x0000000000804000L&occupation); /*diag from f1/f8 */
            bis |= 0x0000000000008000L; /*diag from g1/g8 */
            if ((((roo&QueenOrRooks(Position)) | (bis&QueenOrBishops(Position)) | (0x0000000000F8DC00L&Knights(Position))|
                    (0x000000000000F800L&Pawns(Position)) | (Kings(Position)&0x0000000000004000L))&opposing) == 0) {
                /* check if e1/e8 f1/f8 g1/g8 are not attacked */
                quiets[pquiets++] = (int)((KING|CASTLE) + (4 << 8) + (6 << 16) + (EMPTY << 24));
            }
        }
        return pquiets;
    }

    /* Generate all pseudo-legal capture and promotions */
    static int GenerateCapture(TBoard Position, int[] capture, int pcapture) {
        long opposing,occupation;
        occupation = Occupation(Position);
        opposing = Position.PM ^ occupation;

        for (long piece=KING;piece>=KNIGHT;piece--) { // generate moves from king to knight
            // generate moves for every piece of the same type of the side to move
            for (long pieces=BBPieces(Position,piece)&Position.PM;pieces!=0;pieces=ClearLSB(pieces)) {
                long sq = Long.numberOfTrailingZeros(pieces);
                // for every destinations on an opponent pieces generate a move
                for (long destinations = opposing&BBDestinations(piece,sq,occupation);destinations!=0;destinations=ClearLSB(destinations)) {
                    capture[pcapture++] = (int)((piece|CAPTURE) + (sq << 8) + (Long.numberOfTrailingZeros(destinations) << 16) + (EMPTY << 24));
                }
            }
        }

        /* Generate pawns right captures */
        long pieces = Pawns(Position)&Position.PM;
        for (long captureri = (pieces<<9) & 0x00FEFEFEFEFEFEFEL & opposing;captureri!=0;captureri = ClearLSB(captureri)) {
            long lsb = Long.numberOfTrailingZeros(captureri);
            capture[pcapture++] = (int)((PAWN|CAPTURE) + ((lsb-9) << 8) + (lsb << 16) + (EMPTY << 24));
        }
        /* Generate pawns left captures */
        for (long capturele = (pieces<<7) & 0x007F7F7F7F7F7F7FL & opposing;capturele!=0;capturele = ClearLSB(capturele)) {
            long lsb = Long.numberOfTrailingZeros(capturele);
            capture[pcapture++] = (int)((PAWN|CAPTURE) + ((lsb-7) << 8) + (lsb << 16) + (EMPTY << 24));
        }

        /* Generate pawns promotions */
        if ((pieces&0x00FF000000000000L)!=0) {
            /* promotions with left capture */
            for (long promo = (pieces<<9) & 0xFE00000000000000L & opposing;promo!=0;promo = ClearLSB(promo)) {
                long lsb = Long.numberOfTrailingZeros(promo);
                long withoutPiece = (PAWN|PROMO|CAPTURE) + ((lsb-9) << 8) + (lsb << 16);
                for (long piece=QUEEN;piece>=KNIGHT;piece--) { /* generate underpromotions */
                    capture[pcapture++] = (int)(withoutPiece + (piece << 24));
                }
            }
            /* promotions with right capture */
            for (long promo = (pieces<<7) & 0x7F00000000000000L & opposing;promo!=0;promo = ClearLSB(promo)) {
                long lsb = Long.numberOfTrailingZeros(promo);
                long withoutPiece = (PAWN|PROMO|CAPTURE) + ((lsb-7) << 8) + (lsb << 16);
                for (long piece=QUEEN;piece>=KNIGHT;piece--) { /* generate underpromotions */
                    capture[pcapture++] = (int)(withoutPiece + (piece << 24));
                }
            }
            /* no capture promotions */
            for (long promo = ((pieces<<8) & ~occupation)&0xFF00000000000000L;promo!=0;promo = ClearLSB(promo)) {
                long lsb = Long.numberOfTrailingZeros(promo);
                long withoutPiece = (PAWN|PROMO) + ((lsb-8) << 8) + (lsb << 16);
                for (long piece=QUEEN;piece>=KNIGHT;piece--) { /* generate underpromotions */
                    capture[pcapture++] = (int)(withoutPiece + (piece << 24));
                }
            }
        }

        if (Position.EnPassant!=8) {
            /* Generate EnPassant captures */
            for (long enpassant = pieces&EnPassant[Position.EnPassant]; enpassant!=0; enpassant=ClearLSB(enpassant)) {
                capture[pcapture++] = (int)((PAWN|EP|CAPTURE) + (Long.numberOfTrailingZeros(enpassant) << 8) + ((40+Position.EnPassant) << 16) + (EMPTY << 24));
            }
        }
        return pcapture;
    }

    /* Make the move */
    static void Make(int move) {
        int moveType = move & 0xff;
        int moveTypePiece = moveType & 7;
        int moveFrom = (move >> 8) & 0xff;
        int moveTo = (move >> 16) & 0xff;
        int moveProm = move >> 24;

        TBoard Position = Game[++pPosition];

        /* copy the previous position into the last one */
        Position.PM = Game[pPosition-1].PM;
        Position.P0 = Game[pPosition-1].P0;
        Position.P1 = Game[pPosition-1].P1;
        Position.P2 = Game[pPosition-1].P2;
        Position.CastleFlags = Game[pPosition-1].CastleFlags;
        Position.EnPassant = Game[pPosition-1].EnPassant;
        Position.STM = Game[pPosition-1].STM;

        long part = 1L<<moveFrom;
        long dest = 1L<<moveTo;
        if (moveTypePiece == PAWN) {
            if ((moveType&EP) != 0) {
                /* EnPassant */
                Position.PM^=part|dest;
                Position.P0^=part|dest;
                Position.P0^=dest>>8; /* delete the captured pawn */
                Position.EnPassant=8;
            } else {
                if ((moveType&CAPTURE) != 0) {
                    /* Delete the captured piece */
                    Position.P0&=~dest;
                    Position.P1&=~dest;
                    Position.P2&=~dest;
                }
                if ((moveType&PROMO) != 0) {
                    Position.PM^=part|dest;
                    Position.P0^=part;
                    Position.P0|=(long)(moveProm&1)<<(moveTo);
                    Position.P1|=(long)(((moveProm)>>1)&1)<<(moveTo);
                    Position.P2|=(long)((moveProm)>>2)<<(moveTo);
                    Position.EnPassant=8; /* clear enpassant */
                } else { /* capture or push */
                    Position.PM^=part|dest;
                    Position.P0^=part|dest;
                    Position.EnPassant=8; /* clear enpassant */
                    if (moveTo==moveFrom+16 && (EnPassantM[moveTo&0x07]&Pawns(Position)&(Position.PM^(Occupation(Position)))) != 0)
                        Position.EnPassant=(byte)(moveTo&0x07); /* save enpassant column */
                }
                if ((moveType&CAPTURE) != 0) {
                    if (moveTo==63) Position.CastleFlags&=0xDF; // ResetCastleSO /* captured the opponent king side rook */
                    else if (moveTo==56) Position.CastleFlags&=0xEF; // ResetCastleLO /* captured the opponent quuen side rook */
                }
            }
            ChangeSide(Position);
        } else if (moveTypePiece == KNIGHT || moveTypePiece == BISHOP || moveTypePiece == ROOK || moveTypePiece == QUEEN) {
            if ((moveType&CAPTURE) != 0) {
                Position.P0&=~dest;
                Position.P1&=~dest;
                Position.P2&=~dest;
            }
            Position.PM^=part|dest;
            Position.P0^=(moveType&1) != 0 ? part|dest : 0;
            Position.P1^=(moveType&2) != 0 ? part|dest : 0;
            Position.P2^=(moveType&4) != 0 ? part|dest : 0;
            Position.EnPassant=8;
            if (moveTypePiece==ROOK) { /* update the castle rights */
                if (moveFrom==7) Position.CastleFlags&=0xFD; // ResetCastleSM
                else if (moveFrom==0) Position.CastleFlags&=0xFE; // ResetCastleLM
            }
            if ((moveType&CAPTURE) != 0) { /* update the castle rights */
                if (moveTo==63) Position.CastleFlags&=0xDF; // ResetCastleSO
                else if (moveTo==56) Position.CastleFlags&=0xEF; // ResetCastleLO
            }
            ChangeSide(Position);
        } else if (moveTypePiece == KING) {
            if ((moveType&CAPTURE) != 0) {
                Position.P0&=~dest;
                Position.P1&=~dest;
                Position.P2&=~dest;
            }
            Position.PM^=part|dest;
            Position.P1^=part|dest;
            Position.P2^=part|dest;
            /* update the castle rights */
            Position.CastleFlags&=0xFD; // ResetCastleSM
            Position.CastleFlags&=0xFE; // ResetCastleLM
            Position.EnPassant=8;
            if ((moveType&CAPTURE) != 0) {
                if (moveTo==63) Position.CastleFlags&=0xDF; // ResetCastleSO
                else if (moveTo==56) Position.CastleFlags&=0xEF; // ResetCastleLO
            } else if ((moveType&CASTLE) != 0) {
                if (moveTo==6) { Position.PM^=0x00000000000000A0L; Position.P2^=0x00000000000000A0L; } /* short castling */
                else { Position.P2^=0x0000000000000009L; Position.PM^=0x0000000000000009L; } /* long castling */
            }
            ChangeSide(Position);
        }
    }

    /*
    Load a position starting from a fen.
    This function doesn't check the correctness of the fen.
    */
    static void LoadPosition(String fen) {
        /* Clear the board */
        pPosition = 0;
        TBoard Position=Game[0];
        Position.P0 = Position.P1 = Position.P2 = Position.PM = 0;
        Position.EnPassant=8;
        Position.STM=(byte)WHITE;
        Position.CastleFlags=0;

        /* translate the fen to the relative position */
        long pieceside=WHITE;
        long piece=PAWN;
        long sidetomove=WHITE;
        long square=0;
        int cursor;
        for (cursor=0;fen.charAt(cursor)!=' ';cursor++) {
            char c = fen.charAt(cursor);
            if (c>='1' && c<='8') square += c - '0';
            else if (c=='/') continue;
            else {
                long pos = OppSq(square);
                if (c=='p') { piece = PAWN; pieceside = BLACK; }
                else if (c=='n') { piece = KNIGHT; pieceside = BLACK; }
                else if (c=='b') { piece = BISHOP; pieceside = BLACK; }
                else if (c=='r') { piece = ROOK; pieceside = BLACK; }
                else if (c=='q') { piece = QUEEN; pieceside = BLACK; }
                else if (c=='k') { piece = KING; pieceside = BLACK; }
                else if (c=='P') { piece = PAWN; pieceside = WHITE; }
                else if (c=='N') { piece = KNIGHT; pieceside = WHITE; }
                else if (c=='B') { piece = BISHOP; pieceside = WHITE; }
                else if (c=='R') { piece = ROOK; pieceside = WHITE; }
                else if (c=='Q') { piece = QUEEN; pieceside = WHITE; }
                else if (c=='K') { piece = KING; pieceside = WHITE; }
                Position.P0|=(piece&1)<<pos;
                Position.P1|=((piece>>1)&1)<<pos;
                Position.P2|=(piece>>2)<<pos;
                if (pieceside==WHITE) { Position.PM |= 1L<<pos; piece|=BLACK; }
                square++;
            }
        }
        cursor++; /* read the side to move  */
        if (fen.charAt(cursor)=='w') sidetomove=WHITE;
        else if (fen.charAt(cursor)=='b') sidetomove=BLACK;
        cursor+=2;
        if (fen.charAt(cursor)!='-') { /* read the castle rights */
            for (;fen.charAt(cursor)!=' ';cursor++) {
                char c = fen.charAt(cursor);
                if (c=='K') Position.CastleFlags|=0x02;
                else if (c=='Q') Position.CastleFlags|=0x01;
                else if (c=='k') Position.CastleFlags|=0x20;
                else if (c=='q') Position.CastleFlags|=0x10;
            }
            cursor++;
        }
        else cursor+=2;
        if (fen.charAt(cursor)!='-') { /* read the enpassant column */
            Position.EnPassant= (byte)(fen.charAt(cursor) - 'a');
        }
        if (sidetomove==BLACK) ChangeSide(Position);
    }

    /* Check the correctness of the move generator with the Perft function */
    static long Perft(int depth) {
        int[] moves = new int[256 + 64];

        long tot = 0;
        TBoard Position = Game[pPosition];

        int numMoves = GenerateCapture(Position, moves, 0);
        numMoves = GenerateQuiets(Position, moves, numMoves);
        for (int m = 0; m < numMoves; ++m) {
            int move = moves[m];
            if (Illegal(Position, move)) continue;
            if (depth>1) {
                Make(move);
                tot+=Perft(depth-1);
                pPosition--;
            }
            else tot++;
        }
        return tot;
    }

    static class PerftSettings {
        String fen;
        int depth;
        long expectedCount;
        PerftSettings(String fen, int depth, long expectedCount) {
            this.fen = fen;
            this.depth = depth;
            this.expectedCount = expectedCount;
        }
    }

    /* Run the Perft with this 6 test positions */
    static void TestPerft() {
        PerftSettings[] Test = {
                new PerftSettings("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",6,119060324),
                new PerftSettings("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1",5,193690690),
                new PerftSettings("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1", 7,178633661),
                new PerftSettings("r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1",6,706045033),
                new PerftSettings("rnbqkb1r/pp1p1ppp/2p5/4P3/2B5/8/PPP1NnPP/RNBQK2R w KQkq - 0 6",3,53392),
                new PerftSettings("r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10",5,164075551)};

        long totalCount = 0;
        long totalTime = 0;

        for (PerftSettings test : Test) {
            LoadPosition(test.fen);
            long time = System.nanoTime();
            long actualCount = Perft(test.depth);
            time = System.nanoTime() - time;
            totalCount += actualCount;
            totalTime += time;
            System.out.printf("%5.0f ms, %.0f knps%s\n", time*1e-6, actualCount * 1e6 / time,
                    actualCount == test.expectedCount ? "" : (" -- ERROR: expected " + test.expectedCount + " got " + actualCount));
        }

        System.out.printf("Total: %.0f ms, %.0f knps\n", totalTime*1e-6, totalCount * 1e6 / totalTime);
    }

    public static void main(String[] args) {
        System.out.println("QBB Perft in Java");
        TestPerft();
    }
}