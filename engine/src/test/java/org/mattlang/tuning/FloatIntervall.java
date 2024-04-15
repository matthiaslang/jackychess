package org.mattlang.tuning;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Integer Interval from min to max, boundaries inclusive.
 */
@AllArgsConstructor
@Getter
public class FloatIntervall implements Intervall {

    private float min;
    private float max;

    public boolean isInIntervall(float val) {
        return val >= min && val <= max;
    }

    @Override
    public int getMinIntVal() {
        return (int) (min * 10);
    }

    @Override
    public int getMaxIntVal() {
        return (int) (max * 10);
    }
}
