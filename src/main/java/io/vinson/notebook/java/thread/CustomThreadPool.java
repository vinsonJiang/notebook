package io.vinson.notebook.java.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义线程池实现
 * @author: jiangweixin
 * @date: 2019/5/15
 */
public class CustomThreadPool implements ThreadPool {

    private static final Logger logger = LoggerFactory.getLogger(CustomThreadPool.class);

    private volatile int minSize;

    private volatile int maxSize;

    private long liveTime;

    private AtomicBoolean isShutdown = new AtomicBoolean(false);

    private AtomicInteger totalTaskCount = new AtomicInteger();

    private BlockingQueue<Runnable> workQueue;

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
        if(isShutdown.get()) {
            return;
        }
        totalTaskCount.incrementAndGet();
        if(workers.size() < minSize) {
            addWorker(target);
            return;
        }

        boolean offer = workQueue.offer(target);
        if(!offer) {
            if(workers.size() < maxSize) {
                addWorker(target);
                return;
            } else {
                try {
                    workQueue.put(target);
                } catch (InterruptedException e) {
                    logger.debug("interrupted...");
                }
            }
        }

    }

    @Override
    public void shutdown() {
        isShutdown.set(true);
        tryClose(true);
    }

    @Override
    public void shutdownNow() {
        isShutdown.set(true);
        tryClose(false);
    }

    private void tryClose(boolean isTry) {
        if(isTry) {
            if(isShutdown.get() && totalTaskCount.get() == 0) {
                for(Worker worker : workers) {
                    worker.close();
                }
            }
        } else {
            for(Worker worker : workers) {
                worker.close();
            }
        }
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

        @Override
        public void run() {
            Runnable runnable = null;
            if(isNewTask) {
                task = this.task;
            }
            while(task != null || (task = getTask()) != null) {
                task.run();
            }
        }

    }
    private Runnable getTask() {
        if(isShutdown.get() && totalTaskCount.get() == 0) {
            return null;
        }

        try {
            Runnable task = null;
            if(workers.size() > minSize) {
                task = workQueue.poll(liveTime, TimeUnit.MILLISECONDS);
            } else {
                task = workQueue.take();
            }

            if(task != null) {
                return task;
            }
        } catch (InterruptedException e) {
            return null;
        }
        return null;
    }

}
