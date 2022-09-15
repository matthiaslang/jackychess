package org.mattlang.jc.engine.tt;

import java.util.Random;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mattlang.jc.StopWatch;

public class IntIntCacheTest {

    private static final int MAX = 10000000;

    static long[] rnd = new long[10000];

    static {
        Random random = new Random(328032020383L);
        for (int i = 0; i < rnd.length; i++) {
            rnd[i] = random.nextLong();
        }
    }

    @Test
    public void testSpeed() {
        IntCache oldCache = new IntCache(20);

        StopWatch watch = new StopWatch();
        watch.start();
        for (int i = 0; i < MAX; i++) {
            oldCache.save(rnd[i % rnd.length], i);
            Assertions.assertThat(oldCache.find(rnd[i % rnd.length])).isEqualTo(i);
        }

        watch.stop();
        System.out.println("time old cache: " + watch.getFormattedDuration());

        IntIntCache newCache = new IntIntCache(20);

        watch.start();
        for (int i = 0; i < MAX; i++) {
            newCache.save(rnd[i % rnd.length], i);
            Assertions.assertThat(newCache.find(rnd[i % rnd.length])).isEqualTo(i);
        }

        watch.stop();
        System.out.println("time new cache: " + watch.getFormattedDuration());

    }

}