package org.mattlang.jc.uci;

public class UCITimeoutOption extends UCISpinOption {

    public UCITimeoutOption(UCIOptions optionBundle, UCIGroup group) {
        super(optionBundle, group, "thinktime", 1000, 600000, 15000);
    }

}
