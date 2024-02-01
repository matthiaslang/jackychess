package org.mattlang.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mattlang.jc.BuildConstants;

public class AssertionsTest {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testAssertBetween() {
        if (BuildConstants.ASSERTIONS) {
            Assertions.assertionBetween(5, 0, 63);
            exceptionRule.expect(AssertionError.class);
            exceptionRule.expectMessage("value 64 is not within [0, 63]");
            Assertions.assertionBetween(64, 0, 63);
        }
    }

    @Test
    public void testFigureCode() {
        if (BuildConstants.ASSERTIONS) {
            Assertions.assertFigureCode((byte) 5);
            exceptionRule.expect(AssertionError.class);
            Assertions.assertFigureCode((byte) 100);
        }
    }
}