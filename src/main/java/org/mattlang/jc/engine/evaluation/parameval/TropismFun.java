package org.mattlang.jc.engine.evaluation.parameval;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents a term like "2*tropism" .
 */
@AllArgsConstructor
@Getter
public class TropismFun implements Function {

    /**
     * pattern for a linear formular like "2 * tropism"
     */
    private final static String LIN_FUN_PATTERN = "(?<factor>.*)\\*tropism";

    private int factor;

    @Override
    public int calc(int mob) {
        return factor * mob;
    }

    public static TropismFun parse(String term) {
        term = term.replace(" ", "");

        Matcher matcher = Pattern.compile(LIN_FUN_PATTERN).matcher(term);
        if (matcher.matches()) {
            String strFactor = matcher.group("factor");
            int factor = EvalConfig.parseInt(strFactor, "Factor");
            return new TropismFun(factor);
        } else {
            throw new ConfigParseException("Cant parse " + term);
        }

    }

}
