package org.mattlang.jc.movegenerator;

import org.junit.Test;
import org.mattlang.jc.board.Board;
import org.mattlang.jc.board.Figure;

import static org.assertj.core.api.Assertions.assertThat;

public class CaptureTest {


    @Test
    public void captureQeenV() {

        Board board = new Board();
        board.setFenPosition("position fen 8/4q3/8/8/4K3/8/8/8 w - - 2 17 ");

        System.out.println(board.toUniCodeStr());

        MoveGeneratorImpl2 generator = new MoveGeneratorImpl2();
        int kingPos = board.findPosOfFigure(Figure.W_King.figureCode);
        assertThat(generator.canFigureCaptured(board, kingPos)).isTrue();

    }


    @Test
    public void captureRookV() {

        Board board = new Board();
        board.setFenPosition("position fen 8/4r3/8/8/4K3/8/8/8 w - - 2 17 ");

        System.out.println(board.toUniCodeStr());

        MoveGeneratorImpl2 generator = new MoveGeneratorImpl2();
        int kingPos = board.findPosOfFigure(Figure.W_King.figureCode);
        assertThat(generator.canFigureCaptured(board, kingPos)).isTrue();

    }


    @Test
    public void captureQeenH() {

        Board board = new Board();
        board.setFenPosition("position fen 8/8/8/8/q3K3/8/8/8 w - - 2 17 ");

        System.out.println(board.toUniCodeStr());

        MoveGeneratorImpl2 generator = new MoveGeneratorImpl2();
        int kingPos = board.findPosOfFigure(Figure.W_King.figureCode);
        assertThat(generator.canFigureCaptured(board, kingPos)).isTrue();

    }


    @Test
    public void captureRookH() {

        Board board = new Board();
        board.setFenPosition("position fen 8/8/8/8/r3K3/8/8/8 w - - 2 17 ");

        System.out.println(board.toUniCodeStr());

        MoveGeneratorImpl2 generator = new MoveGeneratorImpl2();
        int kingPos = board.findPosOfFigure(Figure.W_King.figureCode);
        assertThat(generator.canFigureCaptured(board, kingPos)).isTrue();

    }

    @Test
    public void noCaptureRookQueen() {

        Board board = new Board();
        board.setFenPosition("position fen 8/8/8/2q2r2/4K3/8/8/8 w - - 2 17 ");

        System.out.println(board.toUniCodeStr());

        MoveGeneratorImpl2 generator = new MoveGeneratorImpl2();
        int kingPos = board.findPosOfFigure(Figure.W_King.figureCode);
        assertThat(generator.canFigureCaptured(board, kingPos)).isFalse();

    }

    @Test
    public void capturePawn() {

        Board board = new Board();
        board.setFenPosition("position fen 8/8/8/3p1p3/4K3/8/8/8 w - - 2 17 ");

        System.out.println(board.toUniCodeStr());

        MoveGeneratorImpl2 generator = new MoveGeneratorImpl2();
        int kingPos = board.findPosOfFigure(Figure.W_King.figureCode);
        assertThat(generator.canFigureCaptured(board, kingPos)).isTrue();

    }

}