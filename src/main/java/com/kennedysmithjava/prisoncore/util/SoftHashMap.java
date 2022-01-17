package com.kennedysmithjava.prisoncore.util;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;

public class SoftHashMap<K, V> implements Map<K, V> {
    private static final int DEFAULT_RETENTION_SIZE = 100;
    private final Map<K, SoftValue<V, K>> map;
    private final int RETENTION_SIZE;
    private final Queue<V> strongReferences;
    private final ReentrantLock strongReferencesLock;
    private final ReferenceQueue<? super V> queue;

    public SoftHashMap() {
        this(100);
    }

    public SoftHashMap(int retentionSize) {
        this.RETENTION_SIZE = Math.max(0, retentionSize);
        this.queue = new ReferenceQueue();
        this.strongReferencesLock = new ReentrantLock();
        this.map = new ConcurrentHashMap();
        this.strongReferences = new ConcurrentLinkedQueue();
    }

    public SoftHashMap(Map<K, V> source) {
        this(100);
        this.putAll(source);
    }

    public SoftHashMap(Map<K, V> source, int retentionSize) {
        this(retentionSize);
        this.putAll(source);
    }

    public V get(Object key) {
        this.processQueue();
        V result = null;
        SoftHashMap.SoftValue<V, K> value = (SoftHashMap.SoftValue)this.map.get(key);
        if (value != null) {
            result = value.get();
            if (result == null) {
                this.map.remove(key);
            } else {
                this.addToStrongReferences(result);
            }
        }

        return result;
    }

    private void addToStrongReferences(V result) {
        this.strongReferencesLock.lock();

        try {
            this.strongReferences.add(result);
            this.trimStrongReferencesIfNecessary();
        } finally {
            this.strongReferencesLock.unlock();
        }

    }

    private void trimStrongReferencesIfNecessary() {
        while(this.strongReferences.size() > this.RETENTION_SIZE) {
            this.strongReferences.poll();
        }

    }

    private void processQueue() {
        SoftHashMap.SoftValue sv;
        while((sv = (SoftHashMap.SoftValue)this.queue.poll()) != null) {
            this.map.remove(sv.key);
        }

    }

    public boolean isEmpty() {
        this.processQueue();
        return this.map.isEmpty();
    }

    public boolean containsKey(Object key) {
        this.processQueue();
        return this.map.containsKey(key);
    }

    public boolean containsValue(Object value) {
        this.processQueue();
        Collection values = this.values();
        return values != null && values.contains(value);
    }

    public void putAll(Map<? extends K, ? extends V> m) {
        if (m != null && !m.isEmpty()) {
            Iterator var2 = m.entrySet().iterator();

            while(var2.hasNext()) {
                Entry<? extends K, ? extends V> entry = (Entry)var2.next();
                this.put(entry.getKey(), entry.getValue());
            }

        } else {
            this.processQueue();
        }
    }

    public Set<K> keySet() {
        this.processQueue();
        return this.map.keySet();
    }

    public Collection<V> values() {
        this.processQueue();
        Collection<K> keys = this.map.keySet();
        if (keys.isEmpty()) {
            return Collections.EMPTY_SET;
        } else {
            Collection<V> values = new ArrayList(keys.size());
            Iterator var3 = keys.iterator();

            while(var3.hasNext()) {
                K key = (K) var3.next();
                V v = this.get(key);
                if (v != null) {
                    values.add(v);
                }
            }

            return values;
        }
    }

    public V put(K key, V value) {
        this.processQueue();
        SoftHashMap.SoftValue<V, K> sv = new SoftHashMap.SoftValue(value, key, this.queue);
        SoftHashMap.SoftValue<V, K> previous = (SoftHashMap.SoftValue)this.map.put(key, sv);
        this.addToStrongReferences(value);
        return previous != null ? previous.get() : null;
    }

    public V remove(Object key) {
        this.processQueue();
        SoftHashMap.SoftValue<V, K> raw = (SoftHashMap.SoftValue)this.map.remove(key);
        return raw != null ? raw.get() : null;
    }

    public void clear() {
        this.strongReferencesLock.lock();

        try {
            this.strongReferences.clear();
        } finally {
            this.strongReferencesLock.unlock();
        }

        this.processQueue();
        this.map.clear();
    }

    public int size() {
        this.processQueue();
        return this.map.size();
    }

    public Set<Entry<K, V>> entrySet() {
        this.processQueue();
        Collection<K> keys = this.map.keySet();
        if (keys.isEmpty()) {
            return Collections.EMPTY_SET;
        } else {
            Map<K, V> kvPairs = new HashMap(keys.size());
            Iterator var3 = keys.iterator();

            while(var3.hasNext()) {
                K key = (K) var3.next();
                V v = this.get(key);
                if (v != null) {
                    kvPairs.put(key, v);
                }
            }

            return kvPairs.entrySet();
        }
    }

    private static class SoftValue<V, K> extends SoftReference<V> {
        private final K key;

        private SoftValue(V value, K key, ReferenceQueue<? super V> queue) {
            super(value, queue);
            this.key = key;
        }
    }
}
