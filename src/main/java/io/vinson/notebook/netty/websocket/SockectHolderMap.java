package io.vinson.notebook.netty.websocket;

import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Description:
 * @author: jiangweixin
 * @date: 2019/3/14
 */
public class SockectHolderMap {

    public static final AtomicLong longCount = new AtomicLong(0);

    public static final Map<Long, NioSocketChannel> SOCKET_MAP = new ConcurrentHashMap<>();

    public static Long createNewSocket(NioSocketChannel channel) {
        long id = longCount.addAndGet(1);
        SOCKET_MAP.put(id, channel);
        return id;
    }

    public static int size() {
        return SOCKET_MAP.size();
    }

    public static boolean isEmpty() {
        return SOCKET_MAP.isEmpty();
    }

    public static boolean containsKey(Object key) {
        return SOCKET_MAP.containsKey(key);
    }

    public static boolean containsValue(Object value) {
        return SOCKET_MAP.containsValue(value);
    }

    public static NioSocketChannel get(Object key) {
        return SOCKET_MAP.get(key);
    }

    public static NioSocketChannel put(Long key, NioSocketChannel value) {
        return SOCKET_MAP.put(key, value);
    }

    public static NioSocketChannel remove(Object key) {
        return SOCKET_MAP.remove(key);
    }

    public static void clear() {
        SOCKET_MAP.clear();
    }


    public static Collection<NioSocketChannel> values() {
        return SOCKET_MAP.values();
    }

    public static Set<Map.Entry<Long, NioSocketChannel>> entrySet() {
        return SOCKET_MAP.entrySet();
    }
}
