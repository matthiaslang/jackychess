package org.mattlang.jc;

import static java.util.concurrent.Executors.newFixedThreadPool;
import static org.mattlang.jc.Constants.MAX_THREADS;

import java.util.concurrent.ExecutorService;

public class JCExecutors {

    /**
     * Executor service used for parallel processing. We use a thread pool with twice as maxthreads defined threads
     * to have enough threads for the parallel search execution and have still some threads for any parallel event
     * activities.
     */
    public static final ExecutorService EXECUTOR_SERVICE = newFixedThreadPool(2 * MAX_THREADS);

}
