package io.vinson.notebook.java.thread;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomThreadPool implements ThreadPool {

    private volatile int minSize;

    private volatile int maxSize;

    private long liveTime;

    private volatile Set<Worker> workers;

    public CustomThreadPool(int minSize, int maxSize, long liveTime, TimeUnit timeUnit) {
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.liveTime = liveTime;
        workers = new ConcurrentHashSet<>();
    }

    @Override
    public void execute(Runnable target) {
        if(target == null) {
            throw new NullPointerException("target must not be null");
        }
        addWorker(target);
    }

    public void addWorker(Runnable target) {
        Worker worker = new Worker(target, true);
        worker.start();
        workers.add(worker);
    }


    private final class ConcurrentHashSet<T> extends AbstractSet<T> {

        ConcurrentHashMap<T, Object> map = new ConcurrentHashMap<>();
        private final Object object = new Object();
        private AtomicInteger count = new AtomicInteger();

        @Override
        public Iterator<T> iterator() {
            return map.keySet().iterator();
        }

        @Override
        public int size() {
            return count.get();
        }

        @Override
        public boolean add(T t) {
            count.incrementAndGet();
            return map.put(t, object) == null;
        }

        @Override
        public boolean remove(Object o) {
            count.decrementAndGet();
            return map.remove(o) == object;
        }

        @Override
        public void clear() {
            map.clear();
        }
    }

    private final class Worker extends Thread {
        private Runnable task;

        private Thread thread;

        private boolean isNewTask;

        public Worker(Runnable task, boolean isNewTask) {
            this.task = task;
            this.isNewTask = isNewTask;
        }

        public void start() {
            thread.start();
        }

        public void close() {
            thread.interrupt();
        }
    }
}
