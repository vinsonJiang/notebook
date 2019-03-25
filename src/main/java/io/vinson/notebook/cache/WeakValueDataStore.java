package io.vinson.notebook.cache;

import java.util.concurrent.ConcurrentHashMap;

public class WeakValueDataStore<K, V> implements DataStore<K, V> {

    ConcurrentHashMap<K, ValueHolder<V>> map = new ConcurrentHashMap<K, ValueHolder<V>>();

    @Override
    public ValueHolder<V> get(K key) {
        return map.get(key);
    }

    @Override
    public int put(K key, V value) {
        ValueHolder<V> v = new WeakValueHolder<V>(value);
        map.put(key, v);
        return 1;
    }

    @Override
    public ValueHolder<V> remove(K key) {
        return map.remove(key);
    }

    @Override
    public void clear() {
        map.clear();
    }
}
