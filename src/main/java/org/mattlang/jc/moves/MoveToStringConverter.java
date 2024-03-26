package org.mattlang.jc.moves;

import static org.mattlang.jc.board.IndexConversion.convert;
import static org.mattlang.jc.engine.sorting.MovePicker.mapDebugOrderStr;
import static org.mattlang.jc.moves.MoveImpl.CASTLING_BLACK_LONG;
import static org.mattlang.jc.moves.MoveImpl.CASTLING_WHITE_LONG;

import java.util.ArrayList;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.FigureType;
import org.mattlang.jc.board.Move;
import org.mattlang.jc.engine.MoveList;

/**
 * Methods to convert Moves to String representations.
 */
public class MoveToStringConverter {

    public static String toStr(Move move) {
        String coords = convert(move.getFromIndex()) + convert(move.getToIndex());
        if (move.isPromotion()) {
            char figureChar = Character.toLowerCase(move.getPromotedFigure().figureChar);
            coords += figureChar;
        }
        return coords;
    }

    public static String toUCIString(Move move, BoardRepresentation board) {
        String coords = convert(move.getFromIndex()) + convert(move.getToIndex());
        if (move.isPromotion()) {
            char figureChar = Character.toLowerCase(move.getPromotedFigure().figureChar);
            coords += figureChar;
        }
        // in chess960 we code a castling move as king captures rook to make it distinct.
        // otherwise in some chess960 positions it could not be distinguished between a normal king move.
        if (board.isChess960() && move.isCastling()) {
            CastlingMove castlingMove = board.getBoardCastlings().getCastlingMove(move.getCastlingType());
            coords = convert(castlingMove.getKingFrom()) + convert(castlingMove.getRookFrom());
        }

        return coords;
    }

    /**
     * Returns a long algebraic like representation. Maybe not complete to standard... as it is currently only
     * used for debugging
     *
     * @return
     */
    public static String toLongAlgebraic(Move move) {
        if (move.isCastling()) {
            if (move.getCastlingType() == CASTLING_WHITE_LONG || move.getCastlingType() == CASTLING_BLACK_LONG) {
                return "O-O-O";
            } else {
                return "O-O";
            }
        }
        String coords;
        if (move.isCapture()) {
            coords = convert(move.getFromIndex()) + "x" + convert(move.getToIndex());
        } else {
            coords = convert(move.getFromIndex()) + "-" + convert(move.getToIndex());
        }
        for (FigureType value : FigureType.values()) {
            if (move.getFigureType() == value.figureCode && move.getFigureType() != FigureType.Pawn.figureCode) {
                coords = Character.toUpperCase(value.figureChar) + coords;

            }
        }
        if (move.isPromotion()) {
            char figureChar = Character.toLowerCase(move.getPromotedFigure().figureChar);
            coords += figureChar;
        }
        if (move.isEnPassant()) {
            coords += " e.p";
        }

        return coords;

    }

    public static ArrayList<String> toString(MoveList moveList) {
        return toString(moveList, 0, moveList.size());
    }

    public static ArrayList<String> toString(MoveList moveList, int start, int stop) {
        MoveImpl moveimpl = new MoveImpl("a1a1");
        ArrayList<String> moves = new ArrayList<>();
        for (int i = start; i < stop; i++) {
            int move = moveList.get(i);
            int order = moveList.getOrder(i);
            moveimpl.fromLongEncoded(move);
            String moveDescr = toLongAlgebraic(moveimpl) + ": " + mapDebugOrderStr(order) + ": " + order;
            moves.add(moveDescr);
        }
        return moves;
    }
}
