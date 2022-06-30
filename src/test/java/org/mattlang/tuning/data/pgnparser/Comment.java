package org.mattlang.tuning.data.pgnparser;

import lombok.Getter;

@Getter
public class Comment implements Symbol {

    private String text;

    public Comment(String text) {
        this.text = text;
    }
}
