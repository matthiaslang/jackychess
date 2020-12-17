package org.mattlang.jc;

import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public class Impl<T> {

    private SearchParameter searchParameter;
    private Supplier<T> supplier;
    private T instance = null;

    public Impl(SearchParameter searchParameter, Supplier<T> supplier) {
        this.searchParameter = requireNonNull(searchParameter);
        set(supplier);
    }

    /**
     * Get a new instance.
     *
     * @return
     */
    public T create() {
        return supplier.get();
    }

    /**
     * Get a shared instance.
     *
     * @return
     */
    public T instance() {
        if (instance == null) {
            instance = supplier.get();
        }
        return instance;
    }

    public SearchParameter set(Supplier<T> supplier) {
        this.supplier = requireNonNull(supplier);
        instance = null;
        return searchParameter;
    }
}
