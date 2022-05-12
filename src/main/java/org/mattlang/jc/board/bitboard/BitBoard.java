package org.mattlang.jc.board.bitboard;

import static java.lang.Character.isDigit;
import static java.lang.Integer.parseInt;
import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.FigureConstants.*;
import static org.mattlang.jc.board.RochadeType.LONG;
import static org.mattlang.jc.board.RochadeType.SHORT;
import static org.mattlang.jc.board.bitboard.BitChessBoard.nBlack;
import static org.mattlang.jc.board.bitboard.BitChessBoard.nWhite;

import java.util.Objects;

import org.mattlang.jc.board.*;
import org.mattlang.jc.engine.IncrementalEvaluateFunction;
import org.mattlang.jc.material.Material;
import org.mattlang.jc.moves.CastlingMove;
import org.mattlang.jc.moves.MoveImpl;
import org.mattlang.jc.uci.FenParser;
import org.mattlang.jc.zobrist.Zobrist;

import lombok.Getter;

public class BitBoard implements BoardRepresentation {

    public static final String[] FEN_START_POSITION = {
            "rnbqkbnr",
            "pppppppp",
            "8",
            "8",
            "8",
            "8",
            "PPPPPPPP",
            "RNBQKBNR"
    };
    public static final int NO_EN_PASSANT_OPTION = -1;
    public static final int MAXMOVES = 1024;

    @Getter
    private BitChessBoard board = new BitChessBoard();

    private CastlingRights castlingRights = new CastlingRights();

    private long zobristHash = 0L;

    private Material material = new Material();

    @Getter
    private Color siteToMove;

    private int[] historyEp = new int[MAXMOVES];
    private int[] historyMaterial = new int[MAXMOVES];
    private long[] historyZobrist = new long[MAXMOVES];
    private byte[] historyCastling = new byte[MAXMOVES];

    private int moveCounter = 0;
    /**
     * the target pos of the en passant move that could be taken as next move on the board.
     * -1 if no en passant is possible.
     */
    private int enPassantMoveTargetPos = NO_EN_PASSANT_OPTION;

    /**
     * a registered evaluate function to use for incremental updates.
     */
    private IncrementalEvaluateFunction evaluateFunction = new EmptyIncrementalEvaluateFunction();

    public BitBoard() {
        for (int i = 0; i < 64; i++) {
            board.set(i, FT_EMPTY);
        }
        siteToMove = WHITE;
    }

    public BitBoard(BitChessBoard board, CastlingRights castlingRights, int enPassantMoveTargetPos, Color siteToMove) {
        this.board = board;
        this.castlingRights = castlingRights;
        this.enPassantMoveTargetPos = enPassantMoveTargetPos;
        this.siteToMove = siteToMove;

    }

    @Override
    public void setStartPosition() {
        setPosition(FEN_START_POSITION);
    }

    @Override
    public void setPosition(String[] fenPosition) {
        for (int i = 0; i < 8; i++) {
            String row = expandRow(fenPosition[i]);
            for (int j = 0; j < 8; j++) {
                setPos(i, j, row.charAt(j));
            }
        }
        moveCounter = 0;
        siteToMove = WHITE;
        castlingRights = new CastlingRights();
        zobristHash = Zobrist.hash(this);
        material.init(this);
    }

    @Override
    public GameState setFenPosition(String fen) {
        FenParser parser = new FenParser();
        return parser.setPosition(fen, this);
    }

    private void set(int pos, byte figureCode) {
        byte oldFigure = board.get(pos);
        board.set(pos, figureCode);
        // remove from piece list, if this is a "override/capture" of this field:
        if (oldFigure != FigureConstants.FT_EMPTY && oldFigure != 0) {
            zobristHash = Zobrist.removeFig(zobristHash, pos, oldFigure);
            material.subtract(oldFigure);
            evaluateFunction.removeFigure(pos, oldFigure);
        }
        // add this piece to piece list:
        if (figureCode != FigureConstants.FT_EMPTY && figureCode != 0) {
            zobristHash = Zobrist.addFig(zobristHash, pos, figureCode);
            material.add(figureCode);
            evaluateFunction.addFigure(pos, figureCode);
        }
    }

    /**
     * Sets position based on coordinate system (0,0 is the left lower corner, the white left corner)
     *
     * @param row
     * @param col
     * @param figureChar
     */
    @Override
    public void setPos(int row, int col, char figureChar) {
        set((7 - row) * 8 + col, Figure.convertFigureChar(figureChar));
    }

    @Override
    public void setPos(int index, Figure figure) {
        set(index, figure.figureCode);
    }

    @Override
    public void setPos(int index, byte figure) {
        set(index, figure);
    }

    /**
     * Gets position based on coordinate system (0,0 is the left lower corner, the white left corner)
     *
     * @param row
     * @param col
     * @return
     */
    @Override
    public char getPos(int row, int col) {
        return Figure.toFigureChar(board.get((7 - row) * 8 + col));
    }

    @Override
    public Figure getPos(int i) {
        return Figure.getFigureByCode(board.get(i));
    }

    @Override
    public Figure getFigurePos(int row, int col) {
        return Figure.getFigureByCode(board.get((7 - row) * 8 + col));
    }

    @Override
    public void clearPosition() {
        for (int i = 0; i < 64; i++) {
            board.set(i, Figure.EMPTY.figureCode);
        }
        moveCounter = 0;
    }

    private String expandRow(String row) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < row.length(); i++) {
            char ch = row.charAt(i);
            if (isDigit(ch)) {
                int empties = parseInt(String.valueOf(ch));
                for (int e = 1; e <= empties; e++) {
                    b.append(" ");
                }
            } else {
                b.append(ch);
            }
        }
        return b.toString();
    }

    @Override
    public String toUniCodeStr() {
        return BoardPrinter.toUniCodeStr(this);
    }

    @Override
    public void println() {
        System.out.println(toUniCodeStr());
    }

    /**
     * Simple move of one figure from one field to another.
     *
     * @param from
     * @param to
     */
    private void move(byte figType, int from, int to, byte capturedFigure) {

        long fromMask = 1L << from;
        boolean isWhiteFigure = (board.getColorMask(nWhite) & fromMask) != 0;

        // remove castling rights when rooks or kings are moved:
        if (figType == FT_KING) {
            if (isWhiteFigure) {
                zobristHash = Zobrist.updateCastling(zobristHash, getCastlingRights());
                castlingRights.retain(WHITE, SHORT);
                castlingRights.retain(WHITE, LONG);
                zobristHash = Zobrist.updateCastling(zobristHash, getCastlingRights());
            } else {
                zobristHash = Zobrist.updateCastling(zobristHash, getCastlingRights());
                castlingRights.retain(BLACK, SHORT);
                castlingRights.retain(BLACK, LONG);
                zobristHash = Zobrist.updateCastling(zobristHash, getCastlingRights());
            }
        } else if (figType == FT_ROOK) {
            if (isWhiteFigure) {
                if (from == 0) {
                    zobristHash = Zobrist.updateCastling(zobristHash, getCastlingRights());
                    castlingRights.retain(WHITE, LONG);
                    zobristHash = Zobrist.updateCastling(zobristHash, getCastlingRights());
                } else if (from == 7) {
                    zobristHash = Zobrist.updateCastling(zobristHash, getCastlingRights());
                    castlingRights.retain(WHITE, SHORT);
                    zobristHash = Zobrist.updateCastling(zobristHash, getCastlingRights());
                }
            } else {
                if (from == 56) {
                    zobristHash = Zobrist.updateCastling(zobristHash, getCastlingRights());
                    castlingRights.retain(BLACK, LONG);
                    zobristHash = Zobrist.updateCastling(zobristHash, getCastlingRights());
                } else if (from == 63) {
                    zobristHash = Zobrist.updateCastling(zobristHash, getCastlingRights());
                    castlingRights.retain(BLACK, SHORT);
                    zobristHash = Zobrist.updateCastling(zobristHash, getCastlingRights());
                }
            }
        }

        board.move(from, to, figType, isWhiteFigure ? nWhite : nBlack, capturedFigure);

        byte figCode = (byte) (figType | (isWhiteFigure ? WHITE.code : BLACK.code));

        zobristHash = Zobrist.removeFig(zobristHash, from, figCode);
        zobristHash = Zobrist.addFig(zobristHash, to, figCode);
        evaluateFunction.moveFigure(from, to, figCode);
        if (capturedFigure != 0) {
            zobristHash = Zobrist.removeFig(zobristHash, to, capturedFigure);
            material.subtract(capturedFigure);
            evaluateFunction.removeFigure(to, capturedFigure);
        }

        resetEnPassant();

        // check double pawn move. here we need to mark an possible en passant follow up move:
        // be careful: we must not set the en passant option by undoing a double pawn move:
        if (figType == FT_PAWN) {
            if (isWhiteFigure && to - from == 16) {
                setEnPassantOption((from + to) / 2);
            } else if (!isWhiteFigure && from - to == 16) {
                setEnPassantOption((from + to) / 2);
            }
        }
    }

    @Override
    public void switchSiteToMove() {
        siteToMove = siteToMove.invert();
        zobristHash = Zobrist.colorFlip(zobristHash);
    }

    @Override
    public Figure getFigure(int i) {
        return Figure.getFigureByCode(board.get(i));
    }

    @Override
    public final byte getFigureCode(int i) {
        return board.get(i);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        BitBoard bitBoard = (BitBoard) o;
        return enPassantMoveTargetPos == bitBoard.enPassantMoveTargetPos && board.equals(bitBoard.board)
                && castlingRights.equals(bitBoard.castlingRights) && siteToMove == bitBoard.siteToMove;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, castlingRights, siteToMove, enPassantMoveTargetPos);
    }

    @Override
    public BitBoard copy() {
        BitBoard copied = new BitBoard(board.copy(), castlingRights.copy(), enPassantMoveTargetPos, siteToMove);
        copied.zobristHash = Zobrist.hash(copied);
        copied.material.init(copied);
        return copied;
    }

    @Override
    public int findPosOfFigure(byte figureCode) {
        // todo that method should be renamed to something like getKingPos()
        long kingBB = board.getPieceSet(FT_KING, (figureCode & BLACK.code) == BLACK.code ? BLACK : WHITE);
        final int king = Long.numberOfTrailingZeros(kingBB);
        return king;
    }

    @Override
    public boolean isEnPassantCapturePossible(int n) {
        return enPassantMoveTargetPos == n;
    }

    @Override
    public int getEnPassantCapturePos() {
        return calcEnPassantCaptureFromEnPassantOption();
    }

    @Override
    public int getEnPassantMoveTargetPos() {
        return enPassantMoveTargetPos;
    }

    @Override
    public void setEnPassantOption(int enPassantOption) {
        zobristHash = Zobrist.updateEnPassant(zobristHash, enPassantMoveTargetPos);
        this.enPassantMoveTargetPos = enPassantOption;

        zobristHash = Zobrist.updateEnPassant(zobristHash, enPassantMoveTargetPos);
    }

    private int calcEnPassantCaptureFromEnPassantOption() {
        if (enPassantMoveTargetPos >= 16 && enPassantMoveTargetPos <= 23) {
            return enPassantMoveTargetPos + 8;
        } else {
            return enPassantMoveTargetPos - 8;
        }
    }

    private void resetEnPassant() {
        zobristHash = Zobrist.updateEnPassant(zobristHash, enPassantMoveTargetPos);
        enPassantMoveTargetPos = NO_EN_PASSANT_OPTION;
        zobristHash = Zobrist.updateEnPassant(zobristHash, enPassantMoveTargetPos);
    }

    @Override
    public boolean isCastlingAllowed(Color color, RochadeType type) {
        return castlingRights.isAllowed(color, type);
    }

    @Override
    public void setCastlingAllowed(Color color, RochadeType type) {
        zobristHash = Zobrist.updateCastling(zobristHash, getCastlingRights());
        castlingRights.setAllowed(color, type);
        zobristHash = Zobrist.updateCastling(zobristHash, getCastlingRights());
    }

    @Override
    public byte getCastlingRights() {
        return castlingRights.getRights();
    }

    @Override
    public long getZobristHash() {
        return zobristHash;
    }

    @Override
    public void domove(Move move) {
        pushHistory();

        move(move.getFigureType(), move.getFromIndex(), move.getToIndex(),
                move.isEnPassant() ? 0 : move.getCapturedFigure());

        if (move.isEnPassant()) {
            set(move.getEnPassantCapturePos(), FigureConstants.FT_EMPTY);
            evaluateFunction.removeFigure(move.getEnPassantCapturePos(), move.getCapturedFigure());
        } else if (move.isPromotion()) {
            set(move.getToIndex(), move.getPromotedFigureByte());
        } else if (move.isCastling()) {
            CastlingMove castlingMove = move.getCastlingMove();
            move(FT_ROOK, castlingMove.getFromIndex2(), castlingMove.getToIndex2(),
                    (byte) 0);
        }

        switchSiteToMove();
    }

    @Override
    public void undo(Move move) {

        long fromMask = 1L << move.getToIndex();
        boolean isWhiteFigure = (board.getColorMask(nWhite) & fromMask) != 0;
        Color color = isWhiteFigure? WHITE: BLACK;

        byte figureType = move.getFigureType();
        if (move.isPromotion()) {
            figureType = (byte) (move.getPromotedFigureByte() & MASK_OUT_COLOR);
        }
        board.move(move.getToIndex(), move.getFromIndex(), figureType, isWhiteFigure ? nWhite : nBlack, (byte) 0);
        evaluateFunction.moveFigure(move.getToIndex(), move.getFromIndex(),  (byte) (move.getFigureType() | color.code) );

        if (move.getCapturedFigure() != 0) {
            board.setOnEmptyField(move.getToIndex(), move.getCapturedFigure());
            evaluateFunction.addFigure(move.getToIndex(), move.getCapturedFigure());
        }
        if (move.isEnPassant()) {
            // override the "default" overrider field with empty..
            board.set(move.getToIndex(), FigureConstants.FT_EMPTY);
            // because we have the special en passant capture pos which we need to reset with the captured figure
            board.set(move.getEnPassantCapturePos(), move.getCapturedFigure());

            evaluateFunction.removeFigure(move.getToIndex(), move.getCapturedFigure());
            evaluateFunction.addFigure(move.getEnPassantCapturePos(), move.getCapturedFigure());

        } else if (move.isPromotion()) {
            Figure promotedFigure = getFigure(move.getFromIndex());
            byte pawn = promotedFigure.color == Color.WHITE ? Figure.W_Pawn.figureCode : Figure.B_Pawn.figureCode;
            board.set(move.getFromIndex(), pawn);
        } else if (move.isCastling()) {
            CastlingMove castlingMove = move.getCastlingMove();
            board.move(castlingMove.getToIndex2(), castlingMove.getFromIndex2(), FT_ROOK,
                    isWhiteFigure ? nWhite : nBlack, (byte) 0);
        }

        siteToMove = siteToMove.invert();

        popHistory();

    }

    private void popHistory() {
        moveCounter--;
        castlingRights.setRights(historyCastling[moveCounter]);
        enPassantMoveTargetPos = historyEp[moveCounter];
        zobristHash = historyZobrist[moveCounter];
        material.setMaterial(historyMaterial[moveCounter]);
    }

    @Override
    public void undoNullMove() {
        siteToMove = siteToMove.invert();
        popHistory();
    }

    @Override
    public void doNullMove() {
        pushHistory();
        switchSiteToMove();

        // reset ep option, otherwise the same side would use its own ep option for an ep move..
        int enPassantBeforeNullMove = getEnPassantMoveTargetPos();
        if (enPassantBeforeNullMove != BitBoard.NO_EN_PASSANT_OPTION) {
            setEnPassantOption(BitBoard.NO_EN_PASSANT_OPTION);
        }
    }

    private void pushHistory() {
        historyCastling[moveCounter] = castlingRights.getRights();
        historyEp[moveCounter] = enPassantMoveTargetPos;
        historyMaterial[moveCounter] = material.getMaterial();
        historyZobrist[moveCounter] = zobristHash;
        moveCounter++;
    }

    @Override
    public boolean isRepetition() {
        // if we would have infos about the latest move, we could also use this condition first:
        //        if (!MoveUtil.isQuiet(move) || figure == PAWN) {
        //            return false;
        //        }
        final int moveCountMin = Math.max(0, moveCounter - 50);
        for (int i = moveCounter - 2; i >= moveCountMin; i -= 2) {
            if (zobristHash == historyZobrist[i]) {
                return true;
            }
        }
        return false;

    }

    private MoveImpl move = new MoveImpl("a1a2");

    @Override
    public boolean isvalidmove(int aMove) {
        move.fromLongEncoded(aMove);
        int from = move.getFromIndex();
        int to = move.getToIndex();

        if (!board.isFigureType(from, move.getFigureType())) {
            return false;
        }

        if (move.isEnPassant()) {
            if (to != enPassantMoveTargetPos) {
                return false;
            }
        } else if (move.isCastling()) {
            if (!move.getCastlingMove().getDef().checkRochade(this)) {
                return false;
            }
        } else {

            if (move.isCapture()) {
                if (board.get(to) != move.getCapturedFigure()) {
                    return false;
                }
                if (!board.isDifferentColor(from, to)) {
                    return false;
                }

            } else {
                if (!board.isEmpty(to)) {
                    return false;
                }
            }
            long allPieces = board.getColorMask(nWhite) | board.getColorMask(nBlack);
            if (move.getFigureType() == FigureType.Bishop.figureCode
                    || move.getFigureType() == FigureType.Rook.figureCode
                    || move.getFigureType() == FigureType.Queen.figureCode) {
                if ((BB.IN_BETWEEN[from][to] & allPieces) != 0) {
                    return false;
                }
            }
        }

        // todo validate promotions...eps further?

        return true;
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public void registerIncrementalEval(IncrementalEvaluateFunction evaluateFunction) {
        this.evaluateFunction = evaluateFunction;
    }

    @Override
    public void unregisterIncrementalEval() {
        this.evaluateFunction = new EmptyIncrementalEvaluateFunction();
    }

    @Override
    public boolean isDrawByMaterial() {
        return material.isDrawByMaterial();
    }

    public void doAssertLogs() {
        try {
            board.doAssertions();
        } catch (AssertionError e) {
             println();
             e.printStackTrace();
        }
    }
}
