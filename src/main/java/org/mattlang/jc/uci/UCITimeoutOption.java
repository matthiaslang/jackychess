package org.mattlang.jc.uci;

import java.util.Map;

public class UCITimeoutOption extends UCISpinOption {

    public UCITimeoutOption(Map<String, UCIOption> optionBundle) {
        super(optionBundle, "thinktime", 5, 600, 15);
    }

    @Override
    public Integer getValue() {
        return super.getValue() < 1000 ? super.getValue() * 1000 : super.getValue();
    }

    @Override
    public void setValue(Integer newValue) {
        super.setValue(newValue < 1000 ? newValue * 1000 : newValue);
    }

}
