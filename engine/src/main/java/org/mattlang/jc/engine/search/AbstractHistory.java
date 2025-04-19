package org.mattlang.jc.engine.search;

public abstract class AbstractHistory {

    public static int HIST_MAX = 8192;
    public static int BONUS_MAX = 1200;

    public static int calcBonus(int depth) {
        return Math.min(200 * depth, BONUS_MAX);
        //        return depth * depth + depth - 1;

        // 300* depth - 250; ??
    }

    public static int clamp(int val, int min, int max) {
        if (val < min)
            return min;
        if (val > max)
            return max;
        return val;
    }
}
