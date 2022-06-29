package org.mattlang.tuning.data.pgnparser;

import lombok.Getter;

@Getter
public class Quote implements Symbol {

    private final String quote;

    public Quote(String quote) {
        this.quote = quote;
    }
}
