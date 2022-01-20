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

    private Map<String, UCIGroup> groups = new LinkedHashMap<>();

    public void put(String name, UCIOption option) {
        if (options.put(name, option) != null) {
            throw new IllegalArgumentException("Duplicate UCI option name " + name);
        }
    }

    public UCIGroup createGroup(String name, String descr) {
        UCIGroup group = new UCIGroup(this, name, descr);
        if (groups.put(name, group) != null) {
            throw new IllegalArgumentException("Duplicate UCI group name " + name);
        }
        return group;
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
