package io.vinson.notebook.cache;


public interface DataStore<K, V> {

    public ValueHolder<V> get(K key);

    public int put(K key, V value);

    public ValueHolder<V> remove(K key);

    public void clear();
}
