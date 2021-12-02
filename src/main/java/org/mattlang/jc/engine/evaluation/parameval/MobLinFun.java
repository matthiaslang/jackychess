package org.mattlang.jc.engine.evaluation.parameval;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents a term like "2*(mob-14)" .
 */
@AllArgsConstructor
@Getter
public class MobLinFun {

    /**
     * pattern for a linear formular like "2*(mob-14)"
     */
    private final static String LIN_FUN_PATTERN = "(?<factor>.*)\\*\\(mob(?<koeff>.*)\\)";

    private int factor;
    private int koeff;

    public int calc(int mob) {
        return factor * (mob + koeff);
    }

    public static MobLinFun parse(String term) {
        term = term.replace(" ","");

        Matcher matcher = Pattern.compile(LIN_FUN_PATTERN).matcher(term);
        if (matcher.matches()) {

            String strFactor = matcher.group("factor");
            String strKoeff = matcher.group("koeff");
            int factor = parseInt(strFactor, "Factor");
            int koeff = parseInt(strKoeff, "Koefficient");

            return new MobLinFun(factor, koeff);
        } else {
            throw new IllegalArgumentException("Cant parse " + term);
        }

    }

    private static int parseInt(String str, String name) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException("Cant parse " + name + ": " + str, nfe);
        }
    }
}
