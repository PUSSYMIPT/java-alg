package main.java.ru.sbt.mipt;

import java.util.concurrent.LinkedBlockingQueue;

public class ThreadPoolImpl implements ThreadPool{

    private final int num_workers;
    private final LinkedBlockingQueue<Runnable> current_execution_queue;
    private final ThreadWorkers[] workers;

    ThreadPoolImpl(int num_workers) {
        this.num_workers = num_workers;
        this.current_execution_queue = new LinkedBlockingQueue<Runnable>();
        this.workers = new ThreadWorkers[num_workers];
    }

    class ThreadWorkers extends Thread {
        public void run() {
            Runnable runnable;

            while (true) {
                synchronized (current_execution_queue) {
                    try {
                        current_execution_queue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runnable = current_execution_queue.poll();
                }
                runnable.run();
            }
        }
    }

    @Override
    public void start() {
        for (int i = 0; i < num_workers; ++i) {
            workers[i] = new ThreadWorkers();
            workers[i].start();  // calling run() internally
        }
    }

    @Override
    public void execute(Runnable runnable) {
        synchronized (current_execution_queue) {
            current_execution_queue.add(runnable);
            current_execution_queue.notify();
        }
    }
}
