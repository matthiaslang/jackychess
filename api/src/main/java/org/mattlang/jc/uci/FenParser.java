package org.mattlang.jc.uci;

import org.mattlang.jc.board.*;
import org.mattlang.jc.moves.CastlingMove;
import org.mattlang.jc.moves.MoveImpl;

import static org.mattlang.jc.board.CastlingType.*;
import static org.mattlang.jc.board.Color.*;
import static org.mattlang.jc.board.Figure.*;
import static org.mattlang.jc.board.FigureConstants.B_PAWN;
import static org.mattlang.jc.board.FigureConstants.W_PAWN;
import static org.mattlang.jc.board.FigureType.Pawn;
import static org.mattlang.jc.board.RochadeType.LONG;
import static org.mattlang.jc.board.RochadeType.SHORT;
import static org.mattlang.jc.moves.CastlingMove.createCastlingMove;
import static org.mattlang.jc.moves.MoveImpl.*;

public class FenParser {

    /**
     * Flag to be more lenient when parsing fens.
     */
    private static boolean lenient = false;

    public static GameState setPosition(String positionStr, BoardRepresentation board) {
        return setPosition(positionStr, board, false);
    }

    public static GameState setPosition(String positionStr, BoardRepresentation board, boolean isChess960) {
        UciStringParser parser = new UciStringParser(positionStr);

        if (!parser.match("position")) {
            throw new FenParseException(
                    "Error Parsing fen position string: Not starting with 'position':" + positionStr);
        }

        if (parser.match("startpos")) {
            board.setStartPosition();

        } else if (parser.match("fen")) {
            // rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1
            // position fen rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 2 moves f1d3 a7a6 g1f3
            String figures = parser.match();
            String siteToMove = parser.match();
            String rochade = parser.match();
            String enpassant = parser.match();
            String noHalfMoves = !"moves".equals(parser.getCurr()) ? parser.match() : "-";
            String nextMoveNum = !"moves".equals(parser.getCurr()) ? parser.match() : "-";

            board.setChess960(isChess960);

            setPosition(board, figures, siteToMove, rochade, enpassant, noHalfMoves, nextMoveNum);

        } else {
            throw new FenParseException("fen position wrong: no 'position startpos' nor 'position fen' found!");
        }

        if (parser.match("moves")) {
            while (parser.hasNext()) {
                String moveStr = parser.match();
                Move move = parseMove(board, moveStr);
                board.domove(move);
            }
        }

        return new GameState(board, positionStr);
    }

    public static Move parseMove(BoardRepresentation board, String moveStr) {
        IndexConversion.MoveFromTo movePos = IndexConversion.parseMoveStr(moveStr);
        Figure fig = board.getFigure(movePos.getFrom());

        if ("e1g1".equals(moveStr) && fig == W_King && board.isCastlingAllowed(WHITE_SHORT)) {
            return MoveImpl.createCastling(board.getBoardCastlings().getCastlingWhiteShort());
        } else if ("e1c1".equals(moveStr) && fig == W_King && board.isCastlingAllowed(WHITE_LONG)) {
            return MoveImpl.createCastling(board.getBoardCastlings().getCastlingWhiteLong());
        } else if ("e8g8".equals(moveStr) && fig == B_King && board.isCastlingAllowed(BLACK_SHORT)) {
            return MoveImpl.createCastling(board.getBoardCastlings().getCastlingBlackShort());
        } else if ("e8c8".equals(moveStr) && fig == B_King && board.isCastlingAllowed(BLACK_LONG)) {
            return MoveImpl.createCastling(board.getBoardCastlings().getCastlingBlackLong());
        } else if (FenConstants.isCastlingShort(moveStr)) {
            switch (board.getSiteToMove()) {
            case WHITE:
                return MoveImpl.createCastling(board.getBoardCastlings().getCastlingWhiteShort());
            case BLACK:
                return MoveImpl.createCastling(board.getBoardCastlings().getCastlingBlackShort());
            }
        } else if (FenConstants.isCastlingLong(moveStr)) {
            switch (board.getSiteToMove()) {
            case WHITE:
                return MoveImpl.createCastling(board.getBoardCastlings().getCastlingWhiteLong());
            case BLACK:
                return MoveImpl.createCastling(board.getBoardCastlings().getCastlingBlackLong());
            }
        } else if (isCastlingByKingCapturesRook(board, movePos)) {
            return castlingByKingCapturesRook(board, movePos);
        } else if (istCastlingByKingToKingMove(board, movePos)) {
            return castlingByKingToKingMove(board, movePos);
        }

        // check target pos & capturing:
        Figure target = board.getFigure(movePos.getTo());
        byte captureFig = target == EMPTY ? (byte) 0 : target.figureCode;

        // check promotions:
        char lastChar = moveStr.charAt(moveStr.length() - 1);
        switch (lastChar) {
        case 'q':
            return createPawnPromotion(movePos, PAWN_PROMOTION_W_QUEEN_TYPE, PAWN_PROMOTION_B_QUEEN_TYPE, captureFig);
        case 'r':
            return createPawnPromotion(movePos, PAWN_PROMOTION_W_ROOK_TYPE, PAWN_PROMOTION_B_ROOK_TYPE, captureFig);
        case 'n':
            return createPawnPromotion(movePos, PAWN_PROMOTION_W_KNIGHT_TYPE, PAWN_PROMOTION_B_KNIGHT_TYPE, captureFig);
        case 'b':
            return createPawnPromotion(movePos, PAWN_PROMOTION_W_BISHOP_TYPE, PAWN_PROMOTION_B_KNIGHT_TYPE, captureFig);
        }

        // en passant:
        if (fig.figureType == Pawn && board.isEnPassantCapturePossible(movePos.getTo())) {
            Color side = board.getFigure(movePos.getFrom()).color;
            byte otherSidePawn = side == WHITE ? B_PAWN : W_PAWN;
            return MoveImpl.createEnPassant(movePos.getFrom(), movePos.getTo(), otherSidePawn);
        }

        // normal move:
        return new MoveImpl(fig.figureType.figureCode, movePos.getFrom(), movePos.getTo(), captureFig);
    }

    private static Move castlingByKingToKingMove(BoardRepresentation board, IndexConversion.MoveFromTo movePos) {
        for (CastlingType castlingType : CastlingType.values()) {
            CastlingMove castlingMove = board.getBoardCastlings().getCastlingMove(castlingType);
            if (castlingMove.getKingFrom() == movePos.getFrom() && castlingMove.getKingTo() == movePos.getTo()) {
                return MoveImpl.createCastling(castlingMove);
            }
        }
        throw new FenParseException(
                "internal error creating castling move from king captures king description!");
    }

    /**
     * frc castling encoded variant, where from ==to == king move for special castlings where the position of the king
     * doesn´t
     * change on castling.
     * cutechess seem to use this encoding on frc (sometimes, not always...).
     *
     * @param board
     * @param movePos
     * @return
     */
    private static boolean istCastlingByKingToKingMove(BoardRepresentation board, IndexConversion.MoveFromTo movePos) {
        Figure figFrom = board.getFigure(movePos.getFrom());
        return figFrom.figureType == FigureType.King
               && movePos.getFrom() == movePos.getTo()
               && figFrom.color == board.getSiteToMove();
    }

    private static Move castlingByKingCapturesRook(BoardRepresentation board, IndexConversion.MoveFromTo movePos) {
        for (CastlingType castlingType : CastlingType.values()) {
            CastlingMove castlingMove = board.getBoardCastlings().getCastlingMove(castlingType);
            if (castlingMove.getKingFrom() == movePos.getFrom() && castlingMove.getRookFrom() == movePos.getTo()) {
                return MoveImpl.createCastling(castlingMove);
            }
        }
        throw new IllegalArgumentException(
                "internal error creating castling move from king captures rook description!");
    }

    private static boolean isCastlingByKingCapturesRook(BoardRepresentation board, IndexConversion.MoveFromTo movePos) {
        Figure figFrom = board.getFigure(movePos.getFrom());
        Figure figTo = board.getFigure(movePos.getTo());
        return figFrom.figureType == FigureType.King
               && figTo.figureType == FigureType.Rook
               && figFrom.color == figTo.color && figFrom.color == board.getSiteToMove();
    }

    private static Move createPawnPromotion(IndexConversion.MoveFromTo parsed, byte wProm, byte bProm,
            byte captureFig) {
        byte promotionSpecialType = parsed.getTo() >= 56 && parsed.getTo() <= 63 ? wProm : bProm;
        return MoveImpl.createPromotion(parsed.getFrom(), parsed.getTo(), captureFig, promotionSpecialType);
    }

    private static void setPosition(BoardRepresentation board, String figures, String siteToMove, String rochade,
            String enpassant,
            String noHalfMoves,
            String nextMoveNum) {
        // rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1
        board.clearPosition();
        board.setPosition(figures);
        // todo parse and set rest of fen string...

        if (siteToMove == null || siteToMove.trim().length() == 0) {
            return;
        }

        if (!"-".equals(enpassant)) {
            int enpassantOpt = IndexConversion.parsePos(enpassant);
            board.setEnPassantOption(enpassantOpt);
        }
        board.clearCastlingRights();

        if (!"-".equals(rochade)) {
            if (rochade.contains("K")) {
                int wKingPos = board.getKingPos(nWhite);
                long rooks = board.getBoard().getRooks(WHITE);
                try {
                    int rook = searchBiggerRook(wKingPos, rooks);

                    CastlingMove castlingMove = createCastlingMove(WHITE_SHORT, wKingPos, rook);
                    board.setCastlingAllowed(WHITE_SHORT, castlingMove);
                } catch (FenParseException fpe) {
                    if (!lenient) {
                        throw fpe;
                    }
                }
            }
            if (rochade.contains("Q")) {
                int wKingPos = board.getKingPos(nWhite);
                long rooks = board.getBoard().getRooks(WHITE);
                try {
                    int rook = searchSmallerRook(wKingPos, rooks);

                    CastlingMove castlingMove = createCastlingMove(WHITE_LONG, wKingPos, rook);
                    board.setCastlingAllowed(WHITE_LONG, castlingMove);
                } catch (FenParseException fpe) {
                    if (!lenient) {
                        throw fpe;
                    }
                }
            }
            if (rochade.contains("k")) {
                int bKingPos = board.getKingPos(nBlack);
                long rooks = board.getBoard().getRooks(BLACK);
                try {
                    int rook = searchBiggerRook(bKingPos, rooks);

                    CastlingMove castlingMove = createCastlingMove(BLACK_SHORT, bKingPos, rook);
                    board.setCastlingAllowed(BLACK_SHORT, castlingMove);
                } catch (FenParseException fpe) {
                    if (!lenient) {
                        throw fpe;
                    }
                }
            }
            if (rochade.contains("q")) {
                int bKingPos = board.getKingPos(nBlack);
                long rooks = board.getBoard().getRooks(BLACK);
                try {
                    int rook = searchSmallerRook(bKingPos, rooks);

                    CastlingMove castlingMove = createCastlingMove(BLACK_LONG, bKingPos, rook);
                    board.setCastlingAllowed(BLACK_LONG, castlingMove);
                } catch (FenParseException fpe) {
                    if (!lenient) {
                        throw fpe;
                    }
                }
            }
            parseSchredderFenCastlingDef(rochade, board);
        }
        if ("b".equals(siteToMove)) {
            board.switchSiteToMove();
        }
    }

    private static void parseSchredderFenCastlingDef(String rochade, BoardRepresentation board) {
        for (int i = 0; i < rochade.length(); i++) {
            char ch = rochade.charAt(i);
            if (isSchredderCastlingSym(ch)) {
                Color color = Character.isLowerCase(ch) ? BLACK : WHITE;
                long file = BB.fileFromChar(ch);
                int kingPos = Long.numberOfTrailingZeros(board.getBoard().getKings(color));
                long rank = color == WHITE ? BB.rank1 : BB.rank8;
                long rookBB = file & rank;
                int rook = Long.numberOfTrailingZeros(rookBB);

                RochadeType rochadeType = (rook < kingPos) ? LONG : SHORT;
                CastlingType castlingType = of(color, rochadeType);

                CastlingMove castlingMove = createCastlingMove(castlingType, kingPos, rook);
                board.setCastlingAllowed(castlingType, castlingMove);
            }
        }
    }

    private static boolean isSchredderCastlingSym(char ch) {
        char upper = Character.toUpperCase(ch);
        return upper >= 'A' && upper <= 'H';
    }

    private static int searchSmallerRook(int kingPos, long rooks) {
        while (rooks != 0) {
            final int rook = Long.numberOfTrailingZeros(rooks);
            if (rook < kingPos) {
                return rook;
            }
            rooks &= rooks - 1;
        }
        throw new FenParseException("Unable to find smaller Rook pos for Castling!");
    }

    private static int searchBiggerRook(int kingPos, long rooks) {
        while (rooks != 0) {
            final int rook = Long.numberOfTrailingZeros(rooks);
            if (rook > kingPos) {
                return rook;
            }
            rooks &= rooks - 1;
        }
        throw new FenParseException("Unable to find bigger Rook pos for Castling!");
    }

    public static void setLenient(boolean lenient) {
        FenParser.lenient = lenient;
    }

    public static boolean isLenient() {
        return lenient;
    }
}
