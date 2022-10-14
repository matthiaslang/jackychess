package org.mattlang.jc.uci;

import static org.mattlang.jc.board.CastlingType.*;
import static org.mattlang.jc.board.Color.BLACK;
import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.Figure.*;
import static org.mattlang.jc.board.FigureConstants.B_PAWN;
import static org.mattlang.jc.board.FigureConstants.W_PAWN;
import static org.mattlang.jc.board.FigureType.Pawn;
import static org.mattlang.jc.board.RochadeType.LONG;
import static org.mattlang.jc.board.RochadeType.SHORT;

import org.mattlang.jc.board.*;
import org.mattlang.jc.moves.CastlingMove;
import org.mattlang.jc.moves.MoveImpl;

public class FenParser {

    public GameState setPosition(String positionStr, BoardRepresentation board) {
        if (!positionStr.startsWith("position")) {
            throw new IllegalStateException("Error Parsing fen position string: Not starting with 'position':" + positionStr);
        }
        String[] splitted = positionStr.split(" ");
        String fen = splitted[1];
        int movesSection = 2;
        if ("startpos".equals(fen)) {
            board.setStartPosition();

        } else if ("fen".equals(fen)) {
            // rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1
            // position fen rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 2 moves f1d3 a7a6 g1f3
            String figures = splitted[2];
            String zug = splitted[3];
            String rochade = splitted[4];
            String enpassant = splitted[5];
            String noHalfMoves = splitted[6];
            String nextMoveNum = splitted[7];

            setPosition(board, figures, zug, rochade, enpassant, noHalfMoves, nextMoveNum);

            movesSection = 8;
        } else {
            throw new IllegalArgumentException("fen position wrong: no 'position startpos' nor 'position fen' found!");
        }

        if (splitted.length > movesSection) {
            if ("moves".equals(splitted[movesSection])) {
                for (int moveIndex = movesSection + 1; moveIndex < splitted.length; moveIndex++) {
                    String moveStr = splitted[moveIndex];
                    Move move = parseMove(board, moveStr);
                    board.domove(move);
                }
            }
        }
        return new GameState(board, positionStr);
    }

    public Move parseMove(BoardRepresentation board, String moveStr) {
        if ("e1g1".equals(moveStr) && board.isCastlingAllowed(WHITE, SHORT)) {
            return MoveImpl.createCastling(board.getBoardCastlings().getCastlingWhiteShort());
        } else if ("e1c1".equals(moveStr) && board.isCastlingAllowed(WHITE, LONG)) {
            return MoveImpl.createCastling(board.getBoardCastlings().getCastlingWhiteLong());
        } else if ("e8g8".equals(moveStr) && board.isCastlingAllowed(BLACK, SHORT)) {
            return MoveImpl.createCastling(board.getBoardCastlings().getCastlingBlackShort());
        } else if ("e8c8".equals(moveStr) && board.isCastlingAllowed(BLACK, LONG)) {
            return MoveImpl.createCastling(board.getBoardCastlings().getCastlingBlackLong());
        }

        if (moveStr.endsWith("q")) {
            return createPawnPromotion(moveStr, W_Queen, B_Queen);
        } else if (moveStr.endsWith("r")) {
            return createPawnPromotion(moveStr, W_Rook, B_Rook);
        } else if (moveStr.endsWith("n")) {
            return createPawnPromotion(moveStr, W_Knight, B_Knight);
        } else if (moveStr.endsWith("b")) {
            return createPawnPromotion(moveStr, W_Bishop, B_Bishop);
        }

        // en passant:
        Move tmp = new MoveImpl(moveStr);
        Figure fig = board.getFigure(tmp.getFromIndex());
        Figure target = board.getFigure(tmp.getToIndex());

        if (fig.figureType == Pawn && board.isEnPassantCapturePossible(tmp.getToIndex())) {
            Color side = board.getFigure(tmp.getFromIndex()).color;
            byte otherSidePawn = side == WHITE ? B_PAWN : W_PAWN;
            return MoveImpl.createEnPassant(tmp.getFromIndex(), tmp.getToIndex(), otherSidePawn,
                    board.getEnPassantCapturePos());
        }

        // normal move:
        byte captureFig = target==EMPTY? (byte)0: target.figureCode;
        return new MoveImpl(fig.figureType.figureCode, tmp.getFromIndex(), tmp.getToIndex(), captureFig);
    }

    private Move createPawnPromotion(String moveStr, Figure wProm, Figure bProm) {
        Move parsed = new MoveImpl(moveStr);
        Figure figure = parsed.getToIndex() >= 56 && parsed.getToIndex() <= 63 ? wProm : bProm;
        // todo not correct: we do not care about capture during promotion...!!
        return MoveImpl.createPromotion(parsed.getFromIndex(), parsed.getToIndex(), (byte) 0, figure);
    }

    private void setPosition(BoardRepresentation board, String figures, String zug, String rochade, String enpassant,
                              String noHalfMoves,
                              String nextMoveNum) {
        // rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1
        board.clearPosition();
        String[] rows = figures.split("/");
        board.setPosition(rows);
        // todo parse and set rest of fen string...

        if (zug == null || zug.trim().length() == 0) {
            return ;
        }

        if (!"-".equals(enpassant)) {
            int enpassantOpt = IndexConversion.parsePos(enpassant);
            board.setEnPassantOption(enpassantOpt);
        }
        if (!"-".equals(rochade)) {
            if (rochade.contains("K")) {
                int wKingPos = Long.numberOfTrailingZeros(board.getBoard().getKings(WHITE));
                long rooks = board.getBoard().getRooks(WHITE);
                int rook = searchBiggerRook(wKingPos, rooks);

                CastlingMove castlingMove = CastlingMove.createCastlingMove(WHITE_SHORT, wKingPos, rook);
                board.setCastlingAllowed(WHITE_SHORT, castlingMove);

            }
            if (rochade.contains("Q")) {
                int wKingPos = Long.numberOfTrailingZeros(board.getBoard().getKings(WHITE));
                long rooks = board.getBoard().getRooks(WHITE);
                int rook = searchSmallerRook(wKingPos, rooks);

                CastlingMove castlingMove = CastlingMove.createCastlingMove(WHITE_LONG, wKingPos, rook);
                board.setCastlingAllowed(WHITE_LONG, castlingMove);

            }
            if (rochade.contains("k")) {
                int bKingPos = Long.numberOfTrailingZeros(board.getBoard().getKings(BLACK));
                long rooks = board.getBoard().getRooks(BLACK);
                int rook = searchBiggerRook(bKingPos, rooks);

                CastlingMove castlingMove = CastlingMove.createCastlingMove(BLACK_SHORT, bKingPos, rook);
                board.setCastlingAllowed(BLACK_SHORT, castlingMove);
            }
            if (rochade.contains("q")) {
                int bKingPos = Long.numberOfTrailingZeros(board.getBoard().getKings(BLACK));
                long rooks = board.getBoard().getRooks(BLACK);
                int rook = searchSmallerRook(bKingPos, rooks);

                CastlingMove castlingMove = CastlingMove.createCastlingMove(BLACK_LONG, bKingPos, rook);
                board.setCastlingAllowed(BLACK_LONG, castlingMove);
            }
        }
        if ("b".equals(zug)) {
            board.switchSiteToMove();
        }
    }

    private int searchSmallerRook(int kingPos, long rooks) {
        while (rooks != 0) {
            final int rook = Long.numberOfTrailingZeros(rooks);
            if (rook < kingPos) {
                return rook;
            }
            rooks &= rooks - 1;
        }
        throw new IllegalArgumentException("Unable to find smaller Rook pos for Castling!");
    }

    private int searchBiggerRook(int kingPos, long rooks) {
        while (rooks != 0) {
            final int rook = Long.numberOfTrailingZeros(rooks);
            if (rook > kingPos) {
                return rook;
            }
            rooks &= rooks - 1;
        }
        throw new IllegalArgumentException("Unable to find bigger Rook pos for Castling!");
    }
}
