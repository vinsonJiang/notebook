package io.vinson.notebook.java.thread;

public interface ThreadPool {
    public void execute(Runnable target);

    public void shutdown();

    public void shutdownNow();
}
