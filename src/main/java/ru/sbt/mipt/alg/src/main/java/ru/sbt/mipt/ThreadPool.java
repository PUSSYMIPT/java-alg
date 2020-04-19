package main.java.ru.sbt.mipt;

public interface ThreadPool {
    void start();

    void execute(Runnable runnable);
}
