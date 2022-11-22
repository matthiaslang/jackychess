package org.mattlang.jc.engine.evaluation.parameval.functions;

import org.mattlang.jc.engine.evaluation.parameval.ConfigParseException;

public class FunctionParser {

    public static Function parseFunction(String funStr) {
        try {
            return MobLinFun.parse(funStr);
        } catch (ConfigParseException cpe) {
            // ignore
        }
        try {
            return TropismFun.parse(funStr);
        } catch (ConfigParseException cpe) {
            // ignore
        }
        try {
            return KingAttackFun.parse(funStr);
        } catch (ConfigParseException cpe) {
            // ignore
        }

        try {
            return ArrayFunction.parse(funStr);
        } catch (ConfigParseException cpe) {
            throw new ConfigParseException("Function not parseable!", cpe);
        }
    }

}
