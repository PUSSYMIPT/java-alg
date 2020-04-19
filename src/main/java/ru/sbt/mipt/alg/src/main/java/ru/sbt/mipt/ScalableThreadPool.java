package main.java.ru.sbt.mipt;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class ScalableThreadPool implements ThreadPool {
    private final int min_workers_num;
    private final int max_workers_num;
    private final LinkedBlockingQueue<Runnable> current_execution_queue;
    private ArrayList<ThreadWorker> workers;

    ScalableThreadPool(int min_workers_num, int max_workers_num) {
        this.min_workers_num = min_workers_num;
        this.max_workers_num = max_workers_num;
        this.current_execution_queue = new LinkedBlockingQueue<Runnable>();
        this.workers = new ArrayList<ThreadWorker>(max_workers_num);
    }

    class ThreadWorker extends Thread {
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
        for (int i = 0; i < max_workers_num; ++i) {
            ThreadWorker current_worker = new ThreadWorker();
            workers.add(current_worker);
            current_worker.start();  // calling run() internally
        }
    }

    @Override
    public void execute(Runnable runnable) {
        synchronized (current_execution_queue) {
            current_execution_queue.add(runnable);
            if (workers.size() < max_workers_num){
                ThreadWorker current_worker = new ThreadWorker();
                workers.add(current_worker);
                current_worker.start();  // calling run() internally
            }
            else {
                current_execution_queue.notify();
            }
        }
    }
}
