package org.mattlang.jc.engine.evaluation.parameval.functions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mattlang.jc.engine.evaluation.parameval.ConfigParseException;
import org.mattlang.jc.util.PropertyConfig;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents a term like "2*(mob-14)" .
 */
@AllArgsConstructor
@Getter
public class MobLinFun implements Function {

    /**
     * pattern for a linear formular like "2*(mob-14)"
     */
    private final static String LIN_FUN_PATTERN = "(?<factor>.*)\\*\\(mob(?<koeff>.*)\\)";

    private int factor;
    private int koeff;

    @Override
    public int calc(int mob) {
        return factor * (mob + koeff);
    }

    public static MobLinFun parse(String term) {
        term = term.replace(" ","");

        Matcher matcher = Pattern.compile(LIN_FUN_PATTERN).matcher(term);
        if (matcher.matches()) {

            String strFactor = matcher.group("factor");
            String strKoeff = matcher.group("koeff");
            int factor = PropertyConfig.parseInt(strFactor, "Factor");
            int koeff = PropertyConfig.parseInt(strKoeff, "Koefficient");

            return new MobLinFun(factor, koeff);
        } else {
            throw new ConfigParseException("Cant parse " + term);
        }

    }

}
