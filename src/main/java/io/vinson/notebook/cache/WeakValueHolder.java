package io.vinson.notebook.cache;

import java.lang.ref.WeakReference;

public class WeakValueHolder<V> implements ValueHolder<V> {

    private WeakReference<V> value;

    public WeakValueHolder(V value) {
        this.value = new WeakReference<V>(value);
    }

    public V value() {
        return this.value.get();
    }
}
