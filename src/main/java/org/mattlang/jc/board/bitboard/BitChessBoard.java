package org.mattlang.jc.board.bitboard;

import static java.util.stream.Collectors.joining;
import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.FigureConstants.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.mattlang.jc.board.Color;
import org.mattlang.jc.board.FigureConstants;
import org.mattlang.jc.board.FigureType;
import org.mattlang.jc.board.IndexConversion;

/**
 * The chess board as bit board implementation.
 *
 * Encapsulates a bit board representation via 64 field access (as in existing 64 byte representation).
 * Of course this needs then step-by-step been refactored to make real use of all the bitboard benefits.
 */
public class BitChessBoard {

    private long[] colorBB = new long[2];
    private long[] pieceBB = new long[6];

    public static final int nWhite = Color.WHITE.ordinal();     // any white piece
    public static final int nBlack = Color.BLACK.ordinal();     // any black piece

    public BitChessBoard() {
    }

    public BitChessBoard(long[] colorBB, long[] pieceBB) {
        this.colorBB = colorBB;
        this.pieceBB = pieceBB;
    }

    public long getPieceSet(int pt) {
        return pieceBB[pt];
    }

    public long getPieceSet(int pt, int color) {
        return pieceBB[pt] & colorBB[color];
    }

    public long getPieceSet(int pt, Color color) {
        return pieceBB[pt] & colorBB[color == WHITE ? nWhite : nBlack];
    }

    public int getKnightsCount() {
        return Long.bitCount(pieceBB[FT_KNIGHT]);
    }

    public int getBishopsCount() {
        return Long.bitCount(pieceBB[FT_BISHOP]);
    }

    public int getRooksCount() {
        return Long.bitCount(pieceBB[FT_ROOK]);
    }

    public int getQueensCount() {
        return Long.bitCount(pieceBB[FT_QUEEN]);
    }

    public int getPawnsCount() {
        return Long.bitCount(pieceBB[FT_PAWN]);
    }

    public int getKnightsCount(int color) {
        return Long.bitCount(getPieceSet(FT_KNIGHT, color));
    }

    public int getKnightsCount(Color color) {
        return Long.bitCount(getPieceSet(FT_KNIGHT, color));
    }

    public int getPawnsCount(int color) {
        return Long.bitCount(getPieceSet(FT_PAWN, color));
    }

    public int getPawnsCount(Color color) {
        return Long.bitCount(getPieceSet(FT_PAWN, color));
    }

    public int getBishopsCount(int color) {
        return Long.bitCount(getPieceSet(FT_BISHOP, color));
    }

    public int getBishopsCount(Color color) {
        return Long.bitCount(getPieceSet(FT_BISHOP, color));
    }

    public int getRooksCount(int color) {
        return Long.bitCount(getPieceSet(FT_ROOK, color));
    }

    public int getRooksCount(Color color) {
        return Long.bitCount(getPieceSet(FT_ROOK, color));
    }

    public int getQueensCount(int color) {
        return Long.bitCount(getPieceSet(FT_QUEEN, color));
    }

    public int getQueensCount(Color color) {
        return Long.bitCount(getPieceSet(FT_QUEEN, color));
    }

    public long getPawns(int color) {
        return getPieceSet(FT_PAWN, color);
    }

    public long getPawns(Color color) {
        return getPieceSet(FT_PAWN, color);
    }

    public long getKnights(int color) {
        return getPieceSet(FT_KNIGHT, color);
    }

    public long getKnights(Color color) {
        return getPieceSet(FT_KNIGHT, color);
    }

    public long getBishops(int color) {
        return getPieceSet(FT_BISHOP, color);
    }

    public long getBishops(Color color) {
        return getPieceSet(FT_BISHOP, color);
    }

    public long getRooks(int color) {
        return getPieceSet(FT_ROOK, color);
    }

    public long getRooks(Color color) {
        return getPieceSet(FT_ROOK, color);
    }

    public long getQueens(int color) {
        return getPieceSet(FT_QUEEN, color);
    }

    public long getKings(int color) {
        return getPieceSet(FT_KING, color);
    }

    public long getKings(Color color) {
        return getPieceSet(FT_KING, color);
    }

    public long getColorMask(Color color) {
        return color == WHITE ? colorBB[nWhite] : colorBB[nBlack];
    }

    public long getColorMask(int color) {
        return colorBB[color];
    }

    public long getAllPieces() {
        return colorBB[nWhite] | colorBB[nBlack];
    }

    public void set(int i, byte figureCode) {
        long posMask = 1L << i;

        long invPosMask = ~posMask;
        if (figureCode == FigureConstants.FT_EMPTY || figureCode == 0) {
            colorBB[nWhite] &= invPosMask;
            colorBB[nBlack] &= invPosMask;
            for (int figType = 0; figType < 6; figType++) {
                pieceBB[figType] &= invPosMask;
            }
        } else {
            int colorIdx = ((figureCode & BLACK.code) == BLACK.code) ? nBlack : nWhite;
            int otherColor = colorIdx == nWhite ? nBlack : nWhite;
            byte figType = (byte) (figureCode & MASK_OUT_COLOR);
            colorBB[colorIdx] |= posMask;
            colorBB[otherColor] &= invPosMask;

            for (int ft = 0; ft < 6; ft++) {
                if (ft == figType) {
                    pieceBB[ft] |= posMask;
                } else {
                    pieceBB[ft] &= invPosMask;
                }
            }
        }

    }

    /**
     * Clears a field. equal to calling set(i, FigureConstants.FT_EMPTY), but maybe a bit faster.
     *
     * @param i
     */
    public void setEmpty(int i) {
        long invPosMask = ~(1L << i);
        colorBB[nWhite] &= invPosMask;
        colorBB[nBlack] &= invPosMask;
        for (int figType = 0; figType < 6; figType++) {
            pieceBB[figType] &= invPosMask;
        }
    }

    /**
     * Set a figure on an EMPTY field.
     * Note it does not take care to clear the field before, because it assumes it is empty.
     * the set(..) method on the other hand takes care for that (does a implicit remove of a figure that is on the field
     * before setting the new figure)
     *
     * @param i
     * @param figureCode
     */
    public void setOnEmptyField(int i, byte figureCode) {
        long posMask = 1L << i;
        int colorIdx = ((figureCode & BLACK.code) == BLACK.code) ? nBlack : nWhite;
        byte figType = (byte) (figureCode & MASK_OUT_COLOR);
        colorBB[colorIdx] |= posMask;
        pieceBB[figType] |= posMask;
    }

    public byte get(int i) {
        long posMask = 1L << i;
        byte colorOffset;
        if ((colorBB[nWhite] & posMask) != 0) {
            colorOffset = Color.WHITE.code;
        } else if ((colorBB[nBlack] & posMask) != 0) {
            colorOffset = BLACK.code;
        } else {
            return FigureConstants.FT_EMPTY;
        }

        for (int figType = 0; figType < 6; figType++) {
            if ((pieceBB[figType] & posMask) != 0) {
                return (byte) (colorOffset + figType);
            }
        }
        throw new IllegalStateException("no valid bit mask code existing on pos " + IndexConversion.convert(i));
    }

    public byte getFigType(int i) {
        long posMask = 1L << i;

        for (int figType = 0; figType < 6; figType++) {
            if ((pieceBB[figType] & posMask) != 0) {
                return (byte) (figType);
            }
        }
        return FigureConstants.FT_EMPTY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        BitChessBoard that = (BitChessBoard) o;
        return Arrays.equals(colorBB, that.colorBB) && Arrays.equals(pieceBB, that.pieceBB);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(colorBB);
        result = 31 * result + Arrays.hashCode(pieceBB);
        return result;
    }

    public BitChessBoard copy() {
        return new BitChessBoard(colorBB.clone(), pieceBB.clone());
    }

    public static String toStrBoard(long bb) {
        return BB.toStrBoard(bb);
    }

    public static String toStr(long bb) {
        return BB.toStr(bb);
    }

    public void move(int from, int to, byte figType, int color, byte capturedPiece) {

        long fromBB = 1L << from;
        long toBB = 1L << to;
        long fromToBB = fromBB ^ toBB; // |+
        pieceBB[figType] ^= fromToBB;   // update piece bitboard
        colorBB[color] ^= fromToBB;   // update white or black color bitboard

        if (capturedPiece != 0) {
            byte cPiece = (byte) (capturedPiece & MASK_OUT_COLOR);
            pieceBB[cPiece] ^= toBB;       // reset the captured piece
            colorBB[opponentColor(color)] ^= toBB;       // update color bitboard by captured piece
        }

        //occupiedBB            ^=  fromBB;     // update occupied, only from becomes empty
        //emptyBB               ^=  fromBB;     // update empty bitboard
    }

    private int opponentColor(int color) {
        return color == nWhite ? nBlack : nWhite;
    }

    public long getPieces() {
        return colorBB[0] | colorBB[1];
    }

    public boolean isFigureType(int pos, byte figureType) {
        long posMask = 1L << pos;
        return (pieceBB[figureType] & posMask) != 0;
    }

    public boolean isEmpty(int pos) {
        long posMask = 1L << pos;
        return ((colorBB[nWhite] & posMask) == 0 && (colorBB[nBlack] & posMask) == 0);
    }

    public boolean isDifferentColor(int p1, int p2) {
        long posMask1 = 1L << p1;
        long posMask2 = 1L << p2;
        return ((colorBB[nWhite] & posMask1) != 0 && (colorBB[nBlack] & posMask2) != 0
                || (colorBB[nWhite] & posMask2) != 0 && (colorBB[nBlack] & posMask1) != 0);
    }

    public Color getColorOfPos(int p1) {
        long posMask1 = 1L << p1;
        return (colorBB[nWhite] & posMask1) != 0 ? WHITE : BLACK;
    }

    /**
     * Do some consistency checks. useful for debugging.
     */
    public void doAssertions() {

        if ((colorBB[nWhite] & colorBB[nBlack]) != 0) {
            throw new AssertionError("Inconsistency: Overlap white/black bitmasks!");
        }
        for (FigureType t1 : FigureType.values()) {
            for (FigureType t2 : FigureType.values()) {
                if (t1 != t2 && t1 != FigureType.EMPTY && t2 != FigureType.EMPTY) {
                    long overlap = pieceBB[t1.figureCode] & pieceBB[t2.figureCode];
                    if (overlap != 0) {

                        throw new AssertionError(
                                "Inconsistency: Overlap piece bitmasks between ! \n" +
                                        formatAsTabs(Arrays.asList(createMask(overlap, "Overlap"),
                                                createMask(pieceBB[t1.figureCode], t1.name()),
                                                createMask(pieceBB[t2.figureCode], t2.name())), "          "));
                    }
                }

            }
        }

        if (getKings(WHITE) == 0L) {
            throw new AssertionError("Inconsistency: white king mask is 0!");
        }
        if (getKings(BLACK) == 0L) {
            throw new AssertionError("Inconsistency: black king mask is 0!");
        }
    }

    public String toLogStr() {
        List<String> masks = new ArrayList<>();
        for (Color color : Color.values()) {
            for (FigureType type : FigureType.values()) {
                if (type != FigureType.EMPTY) {
                    byte figureCode = type.figureCode;
                    long mask = getPieceSet(figureCode, color);

                    masks.add(createMask(mask, color.name() + " " + type.figureChar));
                }
            }
        }
        masks.add(createMask(getColorMask(nWhite), "White"));
        masks.add(createMask(getColorMask(nBlack), "Black"));

        String fmt = formatAsTabs(masks, "    ");
        return fmt;
    }

    public static String formatAsTabs(List<String> masks, String tab) {

        List<StringBuilder> rows = new ArrayList<>();

        for (String mask : masks) {
            String[] maskRows = mask.split("\n");
            int maxLine = 0;
            for (int i = 0; i < maskRows.length; i++) {
                StringBuilder row = i < rows.size() ? rows.get(i) : null;
                if (row == null) {
                    row = new StringBuilder();
                    rows.add(row);
                }
                row.append(maskRows[i]);
                row.append(tab);
                maxLine = Math.max(maxLine, row.length());
            }
            for (StringBuilder row : rows) {
                while (row.length() < maxLine) {
                    row.append(" ");
                }

            }
        }

        return rows.stream()
                .map(b -> b.toString())
                .collect(joining("\n"));
    }

    public static String createMask(long mask, String title) {
        return title + "\n" + toStr(mask);
    }
}
