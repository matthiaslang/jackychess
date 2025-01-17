package org.mattlang.jc.board.cow;

import static java.lang.Character.isDigit;
import static java.lang.Integer.parseInt;
import static org.mattlang.jc.board.CastlingType.*;
import static org.mattlang.jc.board.Color.*;
import static org.mattlang.jc.board.FigureConstants.*;
import static org.mattlang.jc.zobrist.Zobrist.isKingOrPawn;

import java.util.Objects;

import org.mattlang.jc.BuildConstants;
import org.mattlang.jc.board.*;
import org.mattlang.jc.board.bitboard.BitChessBoard;
import org.mattlang.jc.board.bitboard.BoardCastlings;
import org.mattlang.jc.material.Material;
import org.mattlang.jc.moves.CastlingMove;
import org.mattlang.jc.moves.MoveImpl;
import org.mattlang.jc.uci.FenParser;
import org.mattlang.jc.zobrist.Zobrist;

import lombok.Getter;

/**
 * Example Board variant which does "copy on write" on doMoves and on undoMove it just switches
 * back to the previous state.
 *
 * It seem to work, however there is are no speed benefits. The only benefit might be simpler undo logic.
 */
public final class CopyOnWriteBitBoard implements BoardRepresentation {

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
    private BitChessBoard board;

    private final CastlingRights castlingRights;

    private long zobristHash = 0L;

    /**
     * Zobrist hash based on only pawns and the kings for caching of pawn and king related evaluations.
     */
    private long pawnKingZobristHash = 0L;

    private final Material material = new Material();

    @Getter
    private Color siteToMove;

    private int[] historyEp = new int[MAXMOVES];
    private int[] historyMaterial = new int[MAXMOVES];
    private long[] historyZobrist = new long[MAXMOVES];
    private long[] historyPawnKingZobrist = new long[MAXMOVES];
    private byte[] historyCastling = new byte[MAXMOVES];

    private BitChessBoard[] historyBoard = new BitChessBoard[MAXMOVES];

    private int moveCounter = 0;
    /**
     * the target pos of the en passant move that could be taken as next move on the board.
     * -1 if no en passant is possible.
     */
    private int enPassantMoveTargetPos = NO_EN_PASSANT_OPTION;

    private final BoardCastlings boardCastlings = new BoardCastlings(this);
    private boolean chess960 = false;

    public CopyOnWriteBitBoard() {
        for (int i = 0; i < historyBoard.length; i++) {
            historyBoard[i] = new BitChessBoard();
        }
        board = historyBoard[0];
        castlingRights = new CastlingRights();
        for (int i = 0; i < 64; i++) {
            board.set(i, FT_EMPTY);
        }

        siteToMove = WHITE;
        chess960 = false;
    }

    public CopyOnWriteBitBoard(BitChessBoard board, CastlingRights castlingRights, int enPassantMoveTargetPos,
            Color siteToMove) {
        for (int i = 0; i < historyBoard.length; i++) {
            historyBoard[i] = new BitChessBoard();
        }
        historyBoard[0] = board.copy();
        this.board = historyBoard[0];

        this.castlingRights = castlingRights.copy();
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
        castlingRights.setAllCasltingRights();
        zobristHash = Zobrist.hash(this);
        pawnKingZobristHash = Zobrist.hashPawnsAndKings(this);
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
        if (oldFigure != FigureConstants.FT_EMPTY) {
            hashRemoveFig(pos, oldFigure);
        }
        // add this piece to piece list:
        if (figureCode != FigureConstants.FT_EMPTY) {
            hashAddFig(pos, figureCode);
        }
    }

    private void hashAddFig(int pos, byte figureCode) {
        zobristHash = Zobrist.updateFig(zobristHash, pos, figureCode);
        material.add(figureCode);
        if (isKingOrPawn(figureCode)) {
            pawnKingZobristHash = Zobrist.updateFig(pawnKingZobristHash, pos, figureCode);
        }
    }

    private void hashRemoveFig(int pos, byte oldFigure) {
        zobristHash = Zobrist.updateFig(zobristHash, pos, oldFigure);
        material.subtract(oldFigure);
        if (isKingOrPawn(oldFigure)) {
            pawnKingZobristHash = Zobrist.updateFig(pawnKingZobristHash, pos, oldFigure);
        }
    }

    /**
     * Optimized set(..) variant when we want to clear a field (from the effect identical to set(pos,
     * FigureConstants.FT_EMPTY).
     *
     * @param pos
     */
    private void setEmpty(int pos) {
        byte oldFigure = board.get(pos);
        board.setEmpty(pos);
        // remove from piece list, if this is a "override/capture" of this field:
        if (oldFigure != FigureConstants.FT_EMPTY) {
            hashRemoveFig(pos, oldFigure);
        }
    }

    /**
     * An optimized variant of set(..) when we know definitively that the field we set the figure on is empty.
     *
     * @param pos
     * @param figureCode
     */
    private void setOnEmptyField(int pos, byte figureCode) {
        board.setOnEmptyField(pos, figureCode);
        // add this piece to piece list:
        if (figureCode != FigureConstants.FT_EMPTY) {
            hashAddFig(pos, figureCode);
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
        if (castlingRights.hasAnyCastlings()) {
            if (figType == FT_KING) {
                if (isWhiteFigure) {
                    removeWhiteCastlingRights();
                } else {
                    removeBlackCastlingRights();
                }
            } else if (figType == FT_ROOK) {
                if (isWhiteFigure) {
                    if (from == boardCastlings.getCastlingWhiteLong().getRookFrom()) {
                        zobristHash = Zobrist.updateCastling(zobristHash, getCastlingRights());
                        castlingRights.removeRight(WHITE_LONG);
                        zobristHash = Zobrist.updateCastling(zobristHash, getCastlingRights());
                    } else if (from == boardCastlings.getCastlingWhiteShort().getRookFrom()) {
                        zobristHash = Zobrist.updateCastling(zobristHash, getCastlingRights());
                        castlingRights.removeRight(WHITE_SHORT);
                        zobristHash = Zobrist.updateCastling(zobristHash, getCastlingRights());
                    }
                } else {
                    if (from == boardCastlings.getCastlingBlackLong().getRookFrom()) {
                        zobristHash = Zobrist.updateCastling(zobristHash, getCastlingRights());
                        castlingRights.removeRight(BLACK_LONG);
                        zobristHash = Zobrist.updateCastling(zobristHash, getCastlingRights());
                    } else if (from == boardCastlings.getCastlingBlackShort().getRookFrom()) {
                        zobristHash = Zobrist.updateCastling(zobristHash, getCastlingRights());
                        castlingRights.removeRight(BLACK_SHORT);
                        zobristHash = Zobrist.updateCastling(zobristHash, getCastlingRights());
                    }
                }
            }
        }

        board.move(from, to, figType, isWhiteFigure ? nWhite : nBlack, capturedFigure);

        byte figCode = (byte) (figType | (isWhiteFigure ? WHITE.code : BLACK.code));

        zobristHash = Zobrist.updateFig(zobristHash, from, figCode);
        zobristHash = Zobrist.updateFig(zobristHash, to, figCode);
        if (capturedFigure != 0) {
            hashRemoveFig(to, capturedFigure);
        }

        resetEnPassant();

        // check double pawn move. here we need to mark an possible en passant follow up move:
        // be careful: we must not set the en passant option by undoing a double pawn move:
        if (figType == FT_PAWN) {
            pawnKingZobristHash = Zobrist.updateFig(pawnKingZobristHash, from, figCode);
            pawnKingZobristHash = Zobrist.updateFig(pawnKingZobristHash, to, figCode);

            if (isWhiteFigure && to - from == 16) {
                setEnPassantOption((from + to) / 2);
            } else if (!isWhiteFigure && from - to == 16) {
                setEnPassantOption((from + to) / 2);
            }
        } else if (figType == FT_KING) {
            pawnKingZobristHash = Zobrist.updateFig(pawnKingZobristHash, from, figCode);
            pawnKingZobristHash = Zobrist.updateFig(pawnKingZobristHash, to, figCode);
        }
    }

    private void removeWhiteCastlingRights() {
        zobristHash = Zobrist.updateCastling(zobristHash, getCastlingRights());
        castlingRights.removeRight(WHITE_SHORT);
        castlingRights.removeRight(WHITE_LONG);
        zobristHash = Zobrist.updateCastling(zobristHash, getCastlingRights());
    }

    private void removeBlackCastlingRights() {
        zobristHash = Zobrist.updateCastling(zobristHash, getCastlingRights());
        castlingRights.removeRight(BLACK_SHORT);
        castlingRights.removeRight(BLACK_LONG);
        zobristHash = Zobrist.updateCastling(zobristHash, getCastlingRights());
    }

    @Override
    public void switchSiteToMove() {
        siteToMove = siteToMove.invert();
        zobristHash = Zobrist.colorFlip(zobristHash);
        //        pawnZobristHash = Zobrist.colorFlip(pawnZobristHash);
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
        CopyOnWriteBitBoard bitBoard = (CopyOnWriteBitBoard) o;
        return enPassantMoveTargetPos == bitBoard.enPassantMoveTargetPos && board.equals(bitBoard.board)
                && castlingRights.equals(bitBoard.castlingRights) && siteToMove == bitBoard.siteToMove;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, castlingRights, siteToMove, enPassantMoveTargetPos);
    }

    @Override
    public CopyOnWriteBitBoard copy() {
        CopyOnWriteBitBoard
                copied = new CopyOnWriteBitBoard(board, castlingRights, enPassantMoveTargetPos, siteToMove);
        copied.zobristHash = Zobrist.hash(copied);
        copied.pawnKingZobristHash = Zobrist.hashPawnsAndKings(copied);
        copied.material.init(copied);
        copied.historyCastling = historyCastling.clone();
        copied.historyZobrist = historyZobrist.clone();
        copied.historyEp = historyEp.clone();
        copied.boardCastlings.initFrom(boardCastlings);
        copied.moveCounter = moveCounter;
        copied.chess960 = chess960;
        return copied;
    }

    @Override
    public int getKingPos(int color) {
        long kingBB = board.getPieceSet(FT_KING, color);
        return Long.numberOfTrailingZeros(kingBB);
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
    public boolean isCastlingAllowed(CastlingType castlingType) {
        return castlingRights.isAllowed(castlingType);
    }

    @Override
    public void setCastlingAllowed(CastlingType castlingType, CastlingMove castlingMove) {
        zobristHash = Zobrist.updateCastling(zobristHash, getCastlingRights());
        castlingRights.setAllowed(castlingType);
        zobristHash = Zobrist.updateCastling(zobristHash, getCastlingRights());

        boardCastlings.setCastlingMove(castlingType, castlingMove);
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
    public long getPawnKingZobristHash() {
        return pawnKingZobristHash;
    }

    @Override
    public void domove(Move move) {
        if (BuildConstants.ASSERTIONS) {
            board.doAssertions();
        }

        board = historyBoard[moveCounter + 1];
        board.copyFrom(historyBoard[moveCounter]);

        pushHistory();

        if (move.isCastling()) {
            /**
             * do a castling move by unsetting the rook and the king, and then setting the rook/king again.
             * We do this instead direkt moving, since this would not work in case of fisher random in all cases, e.g. when
             * just changing rook & king or when e.g. the king does not even move.
             */
            CastlingMove castlingMove = getCastlingMove(move);
            byte rook = getFigureCode(castlingMove.getRookFrom());
            setEmpty(castlingMove.getRookFrom());

            Figure king = getFigure(castlingMove.getKingFrom());
            setEmpty(castlingMove.getKingFrom());

            setOnEmptyField(castlingMove.getRookTo(), rook);
            setOnEmptyField(castlingMove.getKingTo(), king.figureCode);

            if (king.color == WHITE) {
                removeWhiteCastlingRights();
            } else {
                removeBlackCastlingRights();
            }
            resetEnPassant();
        } else if (move.isEnPassant()) {
            move(move.getFigureType(), move.getFromIndex(), move.getToIndex(), (byte) 0);
            set(getEnPassantCapturePos(move), FigureConstants.FT_EMPTY);
        } else if (move.isPromotion()) {
            move(move.getFigureType(), move.getFromIndex(), move.getToIndex(), move.getCapturedFigure());
            set(move.getToIndex(), move.getPromotedFigureByte());
        } else {
            move(move.getFigureType(), move.getFromIndex(), move.getToIndex(), move.getCapturedFigure());
        }

        switchSiteToMove();

        if (BuildConstants.ASSERTIONS) {
            board.doAssertions();
        }
    }

    @Override
    public void undo(Move move) {

        if (BuildConstants.ASSERTIONS) {
            board.doAssertions();
        }

        siteToMove = siteToMove.invert();
        popHistory();
        board = historyBoard[moveCounter];

        if (BuildConstants.ASSERTIONS) {
            board.doAssertions();
        }
    }

    private void popHistory() {
        moveCounter--;
        castlingRights.setRights(historyCastling[moveCounter]);
        enPassantMoveTargetPos = historyEp[moveCounter];
        zobristHash = historyZobrist[moveCounter];
        pawnKingZobristHash = historyPawnKingZobrist[moveCounter];
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
        if (enPassantBeforeNullMove != org.mattlang.jc.board.bitboard.BitBoard.NO_EN_PASSANT_OPTION) {
            setEnPassantOption(org.mattlang.jc.board.bitboard.BitBoard.NO_EN_PASSANT_OPTION);
        }
    }

    private void pushHistory() {
        historyCastling[moveCounter] = castlingRights.getRights();
        historyEp[moveCounter] = enPassantMoveTargetPos;
        historyMaterial[moveCounter] = material.getMaterial();
        historyZobrist[moveCounter] = zobristHash;
        historyPawnKingZobrist[moveCounter] = pawnKingZobristHash;
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

    @Override
    public boolean isvalidmove(Color color, int aMove) {

        int from = MoveImpl.getFromIndex(aMove);

        if (color != board.getColorOfPos(from)) {
            return false;
        }

        byte figureType = MoveImpl.getFigureType(aMove);
        if (!board.isFigureType(from, figureType)) {
            return false;
        }

        int to = MoveImpl.getToIndex(aMove);

        if (MoveImpl.isEnPassant(aMove)) {
            if (to != enPassantMoveTargetPos) {
                return false;
            }
        } else if (MoveImpl.isCastling(aMove)) {
            if (!boardCastlings.getCastlingMove(MoveImpl.getCastlingType(aMove)).getDef().check(this)) {
                return false;
            }
        } else {
            // regular moves:

            if (MoveImpl.isCapture(aMove)) {
                if (board.get(to) != MoveImpl.getCapturedFigure(aMove)) {
                    return false;
                }
            } else {
                if (!board.isEmpty(to)) {
                    return false;
                }
            }
            long allPieces = board.getPieces();
            if (figureType == FT_BISHOP
                    || figureType == FT_ROOK
                    || figureType == FT_QUEEN) {
                if ((BB.IN_BETWEEN[from][to] & allPieces) != 0) {
                    return false;
                }
            } else if (figureType == FT_PAWN) {
                if (color == WHITE) {
                    if (from > to) {
                        return false;
                    }
                    // 2-move
                    if (to - from == 16 && (allPieces & (1L << (from + 8))) != 0) {
                        return false;
                    }
                } else {
                    if (from < to) {
                        return false;
                    }
                    // 2-move
                    if (from - to == 16 && (allPieces & (1L << (from - 8))) != 0) {
                        return false;
                    }
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

    @Override
    public BoardCastlings getBoardCastlings() {
        return boardCastlings;
    }

    @Override
    public void clearCastlingRights() {
        zobristHash = Zobrist.updateCastling(zobristHash, getCastlingRights());
        castlingRights.clearCastlingRights();
        zobristHash = Zobrist.updateCastling(zobristHash, getCastlingRights());
    }

    @Override
    public boolean isChess960() {
        return chess960;
    }

    @Override
    public void setChess960(boolean chess960) {
        this.chess960 = chess960;
    }

    private CastlingMove getCastlingMove(Move move) {
        return boardCastlings.getCastlingMove(move.getCastlingType());
    }

    /**
     * For an en passant move this returns the position of the captured pawn.
     *
     * @param move
     * @return
     */
    private int getEnPassantCapturePos(Move move) {
        if (move.getFromIndex() < move.getToIndex()) {
            // white en passant:
            return move.getToIndex() - 8;
        } else {
            return move.getToIndex() + 8;
        }
    }
}

