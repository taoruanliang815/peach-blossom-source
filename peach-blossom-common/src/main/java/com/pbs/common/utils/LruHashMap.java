package com.pbs.common.utils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * LRU
 *
 * @author taoruanliang
 * @date 2022/10/9 13:44
 */
public class LruHashMap<K, V> extends LinkedHashMap<K, V> {

    private static final long serialVersionUID = 1L;
    private int maxCapacity;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private final Lock lock = new ReentrantLock();

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public LruHashMap(int maxCapacity) {
        super(maxCapacity, DEFAULT_LOAD_FACTOR, true);
        this.maxCapacity = maxCapacity;
    }

    public LruHashMap(int initialCapacity, int maxCapacity) {
        super(initialCapacity, DEFAULT_LOAD_FACTOR, true);
        this.maxCapacity = maxCapacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > maxCapacity;
    }

    @Override
    public V get(Object key) {
        try {
            lock.lock();
            return super.get(key);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V put(K key, V value) {
        try {
            lock.lock();
            return super.put(key, value);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V remove(Object key) {
        try {
            lock.lock();
            return super.remove(key);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void clear() {
        try {
            lock.lock();
            super.clear();
        } finally {
            lock.unlock();
        }
    }

    public Map<K, V> cloneEntry() {
        Map<K, V> targetMap = new HashMap<K, V>();
        try {
            lock.lock();
            for (Map.Entry<K, V> entry : super.entrySet()) {
                targetMap.put(entry.getKey(), entry.getValue());
            }
        } finally {
            lock.unlock();
        }
        return targetMap;
    }
}
