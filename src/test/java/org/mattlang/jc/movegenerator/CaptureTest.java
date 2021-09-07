package org.mattlang.jc.movegenerator;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.mattlang.jc.board.Board3;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Figure;

public class CaptureTest {


    @Test
    public void captureQeenV() {

        BoardRepresentation board = new Board3();
        board.setFenPosition("position fen 8/4q3/8/8/4K3/8/8/8 w - - 2 17 ");

        System.out.println(board.toUniCodeStr());

        MoveGeneratorImpl3 generator = new MoveGeneratorImpl3();
        int kingPos = board.findPosOfFigure(Figure.W_King.figureCode);
        assertThat(generator.canFigureCaptured(board, kingPos)).isTrue();

    }


    @Test
    public void captureRookV() {

        BoardRepresentation board = new Board3();
        board.setFenPosition("position fen 8/4r3/8/8/4K3/8/8/8 w - - 2 17 ");

        System.out.println(board.toUniCodeStr());

        MoveGeneratorImpl3 generator = new MoveGeneratorImpl3();
        int kingPos = board.findPosOfFigure(Figure.W_King.figureCode);
        assertThat(generator.canFigureCaptured(board, kingPos)).isTrue();

    }


    @Test
    public void captureQeenH() {

        BoardRepresentation board = new Board3();
        board.setFenPosition("position fen 8/8/8/8/q3K3/8/8/8 w - - 2 17 ");

        System.out.println(board.toUniCodeStr());

        MoveGeneratorImpl3 generator = new MoveGeneratorImpl3();
        int kingPos = board.findPosOfFigure(Figure.W_King.figureCode);
        assertThat(generator.canFigureCaptured(board, kingPos)).isTrue();

    }


    @Test
    public void captureRookH() {

        BoardRepresentation board = new Board3();
        board.setFenPosition("position fen 8/8/8/8/r3K3/8/8/8 w - - 2 17 ");

        System.out.println(board.toUniCodeStr());

        MoveGeneratorImpl3 generator = new MoveGeneratorImpl3();
        int kingPos = board.findPosOfFigure(Figure.W_King.figureCode);
        assertThat(generator.canFigureCaptured(board, kingPos)).isTrue();

    }

    @Test
    public void noCaptureRookQueen() {

        BoardRepresentation board = new Board3();
        board.setFenPosition("position fen 8/8/8/2q2r2/4K3/8/8/8 w - - 2 17 ");

        System.out.println(board.toUniCodeStr());

        MoveGeneratorImpl3 generator = new MoveGeneratorImpl3();
        int kingPos = board.findPosOfFigure(Figure.W_King.figureCode);
        assertThat(generator.canFigureCaptured(board, kingPos)).isFalse();

    }

    @Test
    public void capturePawn() {

        BoardRepresentation board = new Board3();
        board.setFenPosition("position fen 8/8/8/3p1p3/4K3/8/8/8 w - - 2 17 ");

        System.out.println(board.toUniCodeStr());

        MoveGeneratorImpl3 generator = new MoveGeneratorImpl3();
        int kingPos = board.findPosOfFigure(Figure.W_King.figureCode);
        assertThat(generator.canFigureCaptured(board, kingPos)).isTrue();

    }

}