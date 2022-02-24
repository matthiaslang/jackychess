package org.mattlang.jc.movegenerator;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Figure;
import org.mattlang.jc.board.bitboard.BitBoard;

public class CaptureTest {


    @Test
    public void captureQeenV() {

        BoardRepresentation board = new BitBoard();
        board.setFenPosition("position fen 8/4q3/8/8/4K3/8/8/8 w - - 2 17 ");

        System.out.println(board.toUniCodeStr());

        BBMoveGeneratorImpl generator = new BBMoveGeneratorImpl();
        int kingPos = board.findPosOfFigure(Figure.W_King.figureCode);
        assertThat(generator.canFigureCaptured(board, kingPos)).isTrue();

        BBMoveGeneratorImpl bbMoveGenerator=new BBMoveGeneratorImpl();
        assertThat(bbMoveGenerator.canFigureCaptured(board, kingPos)).isTrue();

    }


    @Test
    public void captureRookV() {

        BoardRepresentation board = new BitBoard();
        board.setFenPosition("position fen 8/4r3/8/8/4K3/8/8/8 w - - 2 17 ");

        System.out.println(board.toUniCodeStr());

        BBMoveGeneratorImpl generator = new BBMoveGeneratorImpl();
        int kingPos = board.findPosOfFigure(Figure.W_King.figureCode);
        assertThat(generator.canFigureCaptured(board, kingPos)).isTrue();

        BBMoveGeneratorImpl bbMoveGenerator=new BBMoveGeneratorImpl();
        assertThat(bbMoveGenerator.canFigureCaptured(board, kingPos)).isTrue();

    }


    @Test
    public void captureQeenH() {

        BoardRepresentation board = new BitBoard();
        board.setFenPosition("position fen 8/8/8/8/q3K3/8/8/8 w - - 2 17 ");

        System.out.println(board.toUniCodeStr());

        BBMoveGeneratorImpl generator = new BBMoveGeneratorImpl();
        int kingPos = board.findPosOfFigure(Figure.W_King.figureCode);
        assertThat(generator.canFigureCaptured(board, kingPos)).isTrue();

        BBMoveGeneratorImpl bbMoveGenerator=new BBMoveGeneratorImpl();
        assertThat(bbMoveGenerator.canFigureCaptured(board, kingPos)).isTrue();
    }


    @Test
    public void captureRookH() {

        BoardRepresentation board = new BitBoard();
        board.setFenPosition("position fen 8/8/8/8/r3K3/8/8/8 w - - 2 17 ");

        System.out.println(board.toUniCodeStr());

        BBMoveGeneratorImpl generator = new BBMoveGeneratorImpl();
        int kingPos = board.findPosOfFigure(Figure.W_King.figureCode);
        assertThat(generator.canFigureCaptured(board, kingPos)).isTrue();

        BBMoveGeneratorImpl bbMoveGenerator=new BBMoveGeneratorImpl();
        assertThat(bbMoveGenerator.canFigureCaptured(board, kingPos)).isTrue();
    }

    @Test
    public void noCaptureRookQueen() {

        BoardRepresentation board = new BitBoard();
        board.setFenPosition("position fen 8/8/8/2q2r2/4K3/8/8/8 w - - 2 17 ");

        System.out.println(board.toUniCodeStr());

        BBMoveGeneratorImpl generator = new BBMoveGeneratorImpl();
        int kingPos = board.findPosOfFigure(Figure.W_King.figureCode);
        assertThat(generator.canFigureCaptured(board, kingPos)).isFalse();


        BBMoveGeneratorImpl bbMoveGenerator=new BBMoveGeneratorImpl();
        assertThat(bbMoveGenerator.canFigureCaptured(board, kingPos)).isFalse();
    }

    @Test
    public void captureKnight() {

        BoardRepresentation board = new BitBoard();
        board.setFenPosition("position fen 8/8/8/2n2r2/4K3/8/8/8 w - - 2 17 ");

        System.out.println(board.toUniCodeStr());

        BBMoveGeneratorImpl generator = new BBMoveGeneratorImpl();
        int kingPos = board.findPosOfFigure(Figure.W_King.figureCode);
        assertThat(generator.canFigureCaptured(board, kingPos)).isTrue();


        BBMoveGeneratorImpl bbMoveGenerator=new BBMoveGeneratorImpl();
        assertThat(bbMoveGenerator.canFigureCaptured(board, kingPos)).isTrue();
    }


    @Test
    public void capturePawn() {

        BoardRepresentation board = new BitBoard();
        board.setFenPosition("position fen 8/8/8/3p1p3/4K3/8/8/8 w - - 2 17 ");

        System.out.println(board.toUniCodeStr());

        BBMoveGeneratorImpl generator = new BBMoveGeneratorImpl();
        int kingPos = board.findPosOfFigure(Figure.W_King.figureCode);
        assertThat(generator.canFigureCaptured(board, kingPos)).isTrue();


        BBMoveGeneratorImpl bbMoveGenerator=new BBMoveGeneratorImpl();
        assertThat(bbMoveGenerator.canFigureCaptured(board, kingPos)).isTrue();
    }

}