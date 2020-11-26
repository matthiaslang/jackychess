package org.mattlang.jc.board;

import org.junit.Assert;
import org.junit.Test;

import junit.framework.TestCase;

public class MoveTest extends TestCase {

    @Test
    public void testMove() {
        BasicMove move = new BasicMove("e2e4");
        Assert.assertEquals("e2e4", move.toStr());
    }
}