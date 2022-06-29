package org.mattlang.tuning.data.pgnparser;

import lombok.Getter;

@Getter
public class Word implements Symbol {

    private final String word;

    public Word(String word) {
        this.word = word;
    }
}
