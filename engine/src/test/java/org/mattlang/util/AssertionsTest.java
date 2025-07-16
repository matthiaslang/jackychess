package org.mattlang.util;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mattlang.jc.BuildConstants;

public class AssertionsTest {

    @Test
    public void testAssertBetween() {
        if (BuildConstants.ASSERTIONS) {
            Assertions.assertionBetween(5, 0, 63);
            AssertionError assertionError = assertThrows(AssertionError.class, () -> {
                Assertions.assertionBetween(64, 0, 63);

            });
            String msg = assertionError.getMessage();
            org.junit.jupiter.api.Assertions.assertTrue(msg.contains("value 64 is not within [0, 63]"));
        }
    }

    @Test
    public void testFigureCode() {
        if (BuildConstants.ASSERTIONS) {
            Assertions.assertFigureCode((byte) 5);
            AssertionError assertionError = assertThrows(AssertionError.class, () -> {
                Assertions.assertFigureCode((byte) 100);
            });
        }
    }
}