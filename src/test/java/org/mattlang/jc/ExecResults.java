package org.mattlang.jc;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExecResults<T> {

    private Exception e;
    private StopWatch watch;
    private List<T> results = new ArrayList<>();

    public ExecResults(StopWatch watch, List<T> results) {
        this.watch = watch;
        this.results = results;
    }

    public ExecResults(StopWatch watch, List<T> results, Exception e) {
        this.watch = watch;
        this.results = results;
        this.e = e;
    }
}
