package com.example.demo.pool;

public class ExportingTask implements Runnable {

    private Pool<ExportingProcess> pool;

    private int threadNo;

    public ExportingTask(Pool<ExportingProcess> pool, int threadNo) {
        this.pool = pool;
        this.threadNo = threadNo;
    }

    public void run() {
        // get an object from the pool
        ExportingProcess exportingProcess = null;
        try {
            exportingProcess = pool.borrow();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Thread " + threadNo +
                ": Object with process no. " + exportingProcess.getProcessNo() + " was borrowed");

        // do something
        // ...

        // for-loop is just for simulation
//        for (int i = 0; i < 100000; i++) {
//        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // return ExportingProcess instance back to the pool
        try {
            pool.giveBack(exportingProcess);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Thread " + threadNo +
                ": Object with process no. " + exportingProcess.getProcessNo() + " was returned");
    }
}
