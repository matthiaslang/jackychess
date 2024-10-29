package org.mattlang.jc.board;

import org.junit.Assert;
import org.junit.Test;
import org.mattlang.jc.moves.MoveImpl;

import junit.framework.TestCase;

public class MoveTest extends TestCase {

    @Test
    public void testMove() {
        Move move = new MoveImpl("e2e4");
        Assert.assertEquals("e2e4", move.toStr());
    }
}