package main;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ExecutionManagerImpl implements ExecutionManager {
    private AtomicInteger completedTaskCount;
    private AtomicInteger failedTaskCount;
    private AtomicInteger interruptedTaskCount;
    private boolean isFinished = false;
    private boolean isInterrupted = false;
    private ArrayList<Thread> threadPool;

    @Override
    public Context execute(Runnable callback, Runnable... tasks) {
        Thread executer = new Thread(() -> {
            for (Runnable task : tasks) {
                if (isInterrupted) {
                    interruptedTaskCount.incrementAndGet();
                } else {
                    try {
                        Thread thread = new Thread(task);
                        threadPool.add(thread);
                        thread.start();
                    } catch (RuntimeException e) {
                        failedTaskCount.incrementAndGet();  // no data race due to atomic
                    }
                }
            }
            for (Thread thread : threadPool) {
                try{
                    thread.join();
                    completedTaskCount.incrementAndGet();  // no data race due to atomic
                }catch (InterruptedException e) {
                    interruptedTaskCount.incrementAndGet();  // no data race due to atomic
                }
            }
            callback.run();
            isFinished = true;
        });


        executer.start();

        return new ContextImpl();
    }


    public class ContextImpl implements Context {
        @Override
        public int getCompletedTaskCount() {
            return completedTaskCount.get();
        }

        @Override
        public int getFailedTaskCount() {
            return failedTaskCount.get();
        }

        @Override
        public int getInterruptedTaskCount() {
            return interruptedTaskCount.get();
        }

        @Override
        public void interrupt() {
            isInterrupted = true;
        }

        @Override
        public boolean isFinished() {
            return isFinished;
        }
    }
}
