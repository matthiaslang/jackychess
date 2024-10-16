package org.mattlang.jc.engine.evaluation.annotation.configure;

import java.util.ArrayList;
import java.util.List;

/**
 * Filter to selectively filter configuration entries by their qualified name.
 */
public class ConfigFilter {

    private List<String> filterRegEx = new ArrayList<>();

    public ConfigFilter() {
    }

    public ConfigFilter(String filter) {
        addFilter(filter);
    }

    public void addFilter(String filter) {
        filterRegEx.add(filter);
    }

    public boolean filter(String configName) {
        for (String regEx : filterRegEx) {
            if (configName.matches(regEx)) {
                return true;
            }
        }
        return false;
    }
}
