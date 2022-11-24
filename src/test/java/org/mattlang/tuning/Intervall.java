package org.mattlang.tuning;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Integer Interval from min to max, boundaries inclusive.
 */
@AllArgsConstructor
@Getter
public class Intervall {

    private int min;
    private int max;

    public boolean isInIntervall(int val) {
        return val >= min && val <= max;
    }
}
