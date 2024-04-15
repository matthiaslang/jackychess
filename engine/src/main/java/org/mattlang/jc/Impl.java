package org.mattlang.jc;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class Impl<T> {

    private SearchParameter searchParameter;
    private Supplier<T> supplier;
    private T instance = null;

    private List<StatisticsCollector> statCollectorInstances = new ArrayList<>();

    public Impl(SearchParameter searchParameter, Supplier<T> supplier) {
        this.searchParameter = requireNonNull(searchParameter);
        searchParameter.register(this);
        set(supplier);
    }

    /**
     * Get a new instance.
     *
     * @return
     */
    public T create() {
        return internCreate();
    }

    private T internCreate() {
        T t= supplier.get();
        if (t instanceof StatisticsCollector) {
            statCollectorInstances.add((StatisticsCollector) t);
        }
        return t;
    }

    /**
     * Get a shared instance.
     *
     * @return
     */
    public T instance() {
        if (instance == null) {
            instance = internCreate();
        }
        return instance;
    }

    public SearchParameter set(Supplier<T> supplier) {
        this.supplier = requireNonNull(supplier);
        instance = null;
        return searchParameter;
    }

    public void collectStatistics(Map stats) {
        for (StatisticsCollector collector : statCollectorInstances) {
            collector.collectStatistics(stats);
        }
    }
}
