package org.mattlang.jc.engine.evaluation.annotation.configure;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.mattlang.jc.engine.evaluation.annotation.EvalConfigParam;

/**
 * Helper class to collect found prefixes for a configuration parameter.
 */
public class PrefixStack {

    private List<String> prefixes = new ArrayList<>();

    public PrefixStack(List<String> prefixes, String prefix) {
        this.prefixes = new ArrayList<>(prefixes);
        addPrefix(prefix);
    }

    public PrefixStack(String prefix) {
        addPrefix(prefix);
    }

    private void addPrefix(String prefix) {
        if (prefix != null && !"".equals(prefix)) {
            prefixes.add(prefix);
        }
    }

    public String getQualifiedName(EvalConfigParam evalConfigParam) {
        return this.with(evalConfigParam.prefix()).with(evalConfigParam.name()).getQualifiedName();
    }

    public String getQualifiedPathName(EvalConfigParam evalConfigParam) {
        return this.with(evalConfigParam.prefix()).with(evalConfigParam.name()).getQualifiedPathName();
    }

    public String getQualifiedName() {
        return prefixes.stream()
                .collect(Collectors.joining("."));
    }

    public String getQualifiedPathName() {
        return prefixes.stream().collect(Collectors.joining("/"));
    }

    public PrefixStack with(String prefix) {
        return new PrefixStack(prefixes, prefix);
    }
}
