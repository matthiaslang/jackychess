package org.mattlang.jc.util;

import java.util.ServiceLoader;

/**
 * Common methods to deal with configured services via the SEs ServiceLoader class.
 */
public class ServiceLoaderUtil {

    /**
     * Searches for an implementation of a class via the ServiceLoader.
     * If there are multiple implementations available, it will fail.
     *
     * This is used when we need exaclty one implementation of an interface.
     *
     * @param serviceClass
     * @param <T>
     * @return
     */
    public synchronized static <T> T determineSingleService(Class<T> serviceClass) {
        T service = null;
        for (T currService : ServiceLoader.load(serviceClass)) {
            if (service == null) {
                service = currService;
            } else {
                throw new IllegalStateException(
                        "Bad Configuration! There are multiple implementations of " + serviceClass.getSimpleName()
                                + " available!");
            }
        }
        if (service == null) {
            throw new IllegalStateException(
                    "Bad Configuration! No implementation of " + serviceClass.getSimpleName() + " available!");
        }
        return service;
    }
}
