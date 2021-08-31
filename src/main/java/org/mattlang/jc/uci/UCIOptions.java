package org.mattlang.jc.uci;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Collection of UCI options.
 */
public class UCIOptions {

    private Map<String, UCIOption> options = new LinkedHashMap<>();

    public void put(String name, UCIOption option) {
        if (options.put(name, option) != null) {
            throw new IllegalArgumentException("Duplicate UCI option name " + name);
        }
    }

    public Collection<UCIOption> getAllOptions() {
        return options.values();
    }

    public Map<UCIGroup, List<UCIOption>> getOptionsByGroup() {
        return options.values()
                .stream()
                .collect(groupingBy(UCIOption::getGroup, toList()));
    }

    public UCIOption find(String name) {
        return options.get(name);
    }
}
