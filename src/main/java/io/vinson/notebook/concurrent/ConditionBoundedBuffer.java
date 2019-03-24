package io.vinson.notebook.concurrent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionBoundedBuffer<T> {
    protected  final Lock lock = new ReentrantLock();

    private final Condition notFull = lock.newCondition();

    private final Condition notEmpty = lock.newCondition();

    private final T[] items = (T[]) new Object[8];

    private int tail, head, count;

    public void put(T x) throws InterruptedException {
        lock.lock();
        try {
            while(count == items.length) {
                notFull.await();
            }
            items[tail] = x;
            if(++tail == items.length) {
                tail = 0;
            }
            count++;
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public T take() throws InterruptedException {
        lock.lock();
        try {
            while(count == 0) {
                notEmpty.await();
            }
            T x = items[head];
            items[head] = null;
            if(++head == items.length) {
                head = 0;
            }
            count--;
            notFull.signal();
            return x;
        } finally {
            lock.unlock();
        }
    }
}
