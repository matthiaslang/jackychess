package org.mattlang.jc.uci;

public class UCITimeoutOption extends UCISpinOption {

    public UCITimeoutOption(UCIOptions optionBundle, UCIGroup group) {
        super(optionBundle, group, "thinktime",
                "the think time in milliseconds; this time is used if no other time restrictions are give by the uci go command.",
                1000, 600000, 15000,
                OptionType.UCI);
    }

}
