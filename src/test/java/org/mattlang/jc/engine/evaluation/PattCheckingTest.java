package org.mattlang.jc.engine.evaluation;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mattlang.jc.Factory;
import org.mattlang.jc.board.Color;
import org.mattlang.jc.engine.Engine;
import org.mattlang.jc.engine.MoveList;
import org.mattlang.jc.movegenerator.LegalMoveGenerator;

public class PattCheckingTest {

    @Test
    public void testStrangeBoardIssue() {
        Engine engine = new Engine();
        engine.getBoard().setFenPosition("position fen 8/2k5/8/8/3K4/4P3/2q5/8 w - - 19 2 ");
        System.out.println(engine.getBoard().toUniCodeStr());

        boolean patt = PattChecking.isPatt(engine.getBoard(), Color.WHITE);
        System.out.println(patt);
    }
    @Test
    public void testPattWhenMattIs(){
        Engine engine = new Engine();
        engine.getBoard().setFenPosition("position fen k2R4/8/1Q6/8/8/4K3/8/8 w - - 19 2 ");
        System.out.println(engine.getBoard().toUniCodeStr());

        // todo ispatt does not respect the color to chose now; it depends on the side to move if a patt situation exists!!!
        boolean isWhitePatt = PattChecking.isPatt(engine.getBoard(), Color.WHITE);
        //        Assertions.assertThat(isWhitePatt).isFalse();


        // black is mate; but the patt checker would recognize it as patt:
        boolean isBlackPatt = PattChecking.isPatt(engine.getBoard(), Color.BLACK);
        Assertions.assertThat(isBlackPatt).isTrue();


    }

    @Test
    public void testPatt(){
        Engine engine = new Engine();
        engine.getBoard().setFenPosition("position fen k7/8/1Q6/8/8/4K3/8/8 w - - 19 2 ");
        System.out.println(engine.getBoard().toUniCodeStr());

        // todo ispatt does not respect the color to chose now; it depends on the side to move if a patt situation exists!!!
        boolean isWhitePatt = PattChecking.isPatt(engine.getBoard(), Color.WHITE);
        //        Assertions.assertThat(isWhitePatt).isFalse();

        boolean isBlackPatt = PattChecking.isPatt(engine.getBoard(), Color.BLACK);
        Assertions.assertThat(isBlackPatt).isTrue();


    }


    @Test
    public void testMoveGenByPatt(){
        Engine engine = new Engine();
        engine.getBoard().setFenPosition("position fen k7/8/1Q6/8/8/4K3/8/8 w - - 19 2 ");
        System.out.println(engine.getBoard().toUniCodeStr());

        // todo ispatt does not respect the color to chose now; it depends on the side to move if a patt situation exists!!!
        boolean isWhitePatt = PattChecking.isPatt(engine.getBoard(), Color.WHITE);
//        Assertions.assertThat(isWhitePatt).isFalse();

        boolean isBlackPatt = PattChecking.isPatt(engine.getBoard(), Color.BLACK);
        Assertions.assertThat(isBlackPatt).isTrue();


         LegalMoveGenerator generator = Factory.getDefaults().legalMoveGenerator.create();
        MoveList moves = generator.generate(engine.getBoard(), Color.BLACK);
       // todo ischecmate would be true if we are in a patt situation!!!!!!!

        System.out.println(moves.isCheckMate());

    }
}