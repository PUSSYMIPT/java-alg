package main;

public interface ExecutionManager {
    Context execute(Runnable callback, Runnable... tasks);
}
