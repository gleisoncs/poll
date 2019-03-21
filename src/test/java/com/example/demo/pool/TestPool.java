package com.example.demo.pool;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class TestPool
{
    private Pool<ExportingProcess> pool;

    private AtomicLong processNo = new AtomicLong(0);

    @Before
    public void setUp() {
        // Create a pool of objects of type ExportingProcess. Parameters:
        // 1) Minimum number of special ExportingProcess instances residing in the pool = 4
        // 2) Maximum number of special ExportingProcess instances residing in the pool = 10
        // 3) Time in seconds for periodical checking of minIdle / maxIdle conditions in a separate thread = 5.
        //    When the number of ExportingProcess instances is less than minIdle, missing instances will be created.
        //    When the number of ExportingProcess instances is greater than maxIdle, too many instances will be removed.
        //    If the validation interval is negative, no periodical checking of minIdle / maxIdle conditions
//        //    in a separate thread take place. These boundaries are ignored then.
//        pool = new ObjectPool<ExportingProcess>(1, 3, 1)
//        {
//            protected ExportingProcess createObject() {
//                // create a test object which takes some time for creation
//                return new ExportingProcess("/home/temp/", processNo.incrementAndGet());
//            }
//        };
        List<ExportingProcess> abc = new ArrayList<ExportingProcess>();
        for (int x = 0; x < 3; x++){
            abc.add(new ExportingProcess("", processNo.incrementAndGet()));
        }
        pool = new Pool<ExportingProcess>(abc);
    }

    @After
    public void tearDown() {
        pool = null;
    }

    @Test
    public void testObjectPool() {
        ExecutorService executor = Executors.newFixedThreadPool(6);

        // execute x tasks in separate threads
        for(short x = 0; x < 10; x++){
            executor.execute(new ExportingTask(pool, x));
        }
        executor.shutdown();
        try {
            executor.awaitTermination(180, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
