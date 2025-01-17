package org.mattlang.tuning.data.pgnparser;

import static org.mattlang.jc.board.Color.WHITE;
import static org.mattlang.jc.board.Tools.fileOf;
import static org.mattlang.jc.board.Tools.rankOf;
import static org.mattlang.jc.moves.MoveImpl.createCastling;

import java.util.List;
import java.util.stream.Collectors;

import org.mattlang.jc.board.*;
import org.mattlang.jc.moves.MoveImpl;
import org.mattlang.jc.util.MoveValidator;

/**
 * Untested so far. Should be used for eigenmann & bratkopec tests to properly compare the expected move which
 * does currently not work very well...
 */
public class AlgebraicNotation {

    private AlgebraicNotation() {
    }

    /**
     * Converts a move in algebraic notation into a long algebraic notation which is used in UCI.
     *
     * @param algNotMove
     * @return
     */
    private static Move convertToMove(BoardRepresentation board, Color color, MoveText algNotMove) {
        MoveValidator moveValidator = new MoveValidator();

        List<MoveImpl> allMoves =
                moveValidator.generateLegalMoves(board, color).extractList();

        List<MoveImpl> matching = allMoves.stream()
                .filter(m -> m.getFigureType() == algNotMove.getFigure().figureCode
                        && m.getToIndex() == algNotMove.getToIdx())
                .collect(Collectors.toList());

        // further match for promotions to filter out the one with the right promoted figure:
        if (algNotMove.isPromotion()) {
            matching = matching.stream()
                    .filter(m -> m.isPromotion()
                            && m.getPromotedFigure().figureType == algNotMove.getPromotedFigureType())
                    .collect(Collectors.toList());
        }

        switch (matching.size()) {
        case 0:
            throw new PgnParserException(
                    "Error Parsing Move " + algNotMove.getText() + ": no legal move seems to match!", algNotMove);
        case 1:
            return matching.get(0);
        default:
            // its not unique, seems multiple same figure types can move to this target place,
            // we need to distinguish by the fromSpec:

            for (MoveImpl move : matching) {
                if (matchesFromSpec(move, algNotMove.getFromSpec())) {
                    return move;
                }
            }
            throw new PgnParserException(
                    "Error Parsing Move " + algNotMove.getText() + ": cant identify move from ambiguous matching ones.."
                            + board.toUniCodeStr(), algNotMove);
        }

    }

    private static boolean matchesFromSpec(MoveImpl move, String fromSpec) {
        return rankOf(move.getFromIndex()) == rankFromSpec(fromSpec)
                || fileOf(move.getFromIndex()) == fileFromSpec(fromSpec)
                || move.getFromIndex() == posFromSpec(fromSpec);
    }

    public static int posFromSpec(String fromSpec) {
        if (fromSpec.length() == 2) {
            return toIndex(fromSpec);
        }
        return -1000;
    }

    public static int fileFromSpec(String fromSpec) {
        if (fromSpec.length() == 1) {
            if (Character.isAlphabetic(fromSpec.charAt(0))) {
                return fileFromChar(fromSpec.charAt(0));
            }
        }
        return -1000;
    }

    public static int rankFromSpec(String fromSpec) {
        if (fromSpec.length() == 1) {
            if (Character.isDigit(fromSpec.charAt(0))) {
                return rankFromChar(fromSpec.charAt(0));
            }
        }
        return -1000;
    }

    private static int rankFromChar(char rank) {
        return Integer.parseInt("" + rank) - 1;
    }

    private static int fileFromChar(char file) {
        return file - 'a';
    }

    public static int toIndex(String to) {
        return IndexConversion.parsePos(to);
    }

    /**
     * Converts a move in algebraic notation into a Move Object.
     *
     * @param algNotMove
     * @return
     */
    public static Move moveFromAN(BoardRepresentation board, Color color, MoveText algNotMove) {
        switch (algNotMove.getType()) {
        case CASTLING_SHORT:
            MoveImpl castlingMove = createCastling(color == WHITE ?
                    board.getBoardCastlings().getCastlingWhiteShort() :
                    board.getBoardCastlings().getCastlingBlackShort());
            if (!board.isvalidmove(color, castlingMove.getMoveInt())) {
                throw new PgnParserException("Invalid Parsed Move " + algNotMove.getText(), algNotMove);
            }
            return castlingMove;
        case CASTLING_LONG:
            castlingMove = createCastling(color == WHITE ?
                    board.getBoardCastlings().getCastlingWhiteLong() :
                    board.getBoardCastlings().getCastlingBlackLong());
            if (!board.isvalidmove(color, castlingMove.getMoveInt())) {
                throw new PgnParserException("Invalid Parsed Move " + algNotMove.getText(), algNotMove);
            }
            return castlingMove;
        case NORMAL:
            return convertToMove(board, color, algNotMove);
        default:
            throw new IllegalArgumentException("unsupported alg not type:" + algNotMove.getType());
        }

    }

    public static FigureType determineFigureType(String algNotMove) {
        char fig = algNotMove.charAt(0);
        return determineFigureType(fig);
    }

    public static FigureType determineFigureType(char fig) {
        if (Character.isUpperCase(fig)) {
            for (FigureType figureType : FigureType.values()) {
                if (figureType.figureChar == fig) {
                    return figureType;
                }
            }
            throw new IllegalStateException("unable to determine Figure Type of " + fig);
        } else {
            return FigureType.Pawn;
        }
    }
}
