package ru.otus.cache;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sergey
 * created on 14.12.18.
 */
public class MyCache<K, V> implements HwCache<K, V> {

    public static final int INITIAL_CAPACITY = 1000;
    public static final int LISTENER_INITIAL_CAPACITY = 10;
    public static final String PUT_ACTION = "PUT";
    public static final String REMOVE_ACTION = "REMOVE";

    private int   cacheCapacity = INITIAL_CAPACITY;
    private SoftReference<Map<K, V>> storageRef = null;
    private final SoftReference<ArrayList<WeakReference<HwListener<K, V>>>> listenersRef = new SoftReference<>(new ArrayList<>(LISTENER_INITIAL_CAPACITY));

    public MyCache() {
            init();
    }

    public MyCache(int initialCapacity) {
        this.cacheCapacity = initialCapacity;
        init();
    }

    private void init() {
        this.storageRef = new SoftReference<>(new HashMap<>(INITIAL_CAPACITY));
    }

    //Надо реализовать эти методы
    @Override
    public void put(K key, V value) {
        Map<K, V> storage = storageRef.get();
        if (storage != null) {
            // жив еще, курилка. GC еще не удалил storage
            storage.put(key, value);
            notify(key, value, PUT_ACTION);
        }
    }

    @Override
    public void remove(K key) {
        Map<K, V> storage = storageRef.get();
        if (storage != null) {
            V value = storage.get(key);
            storage.remove(key);
            notify(key, value, REMOVE_ACTION);
        }
    }

    @Override
    public V get(K key) {
        Map<K, V> storage = storageRef.get();
        if (storage != null) {
            return storage.get(key);
        }
        return null;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        ArrayList<WeakReference<HwListener<K, V>>> listeners = listenersRef.get();
        if (listeners != null) {
            WeakReference<HwListener<K, V>> listenerWeakReference = new WeakReference(listener);
            listeners.add(listenerWeakReference);
        }
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        ArrayList<WeakReference<HwListener<K, V>>> weakListeners = listenersRef.get();
        if (weakListeners == null) return;

        for(int idx = 0; idx < weakListeners.size(); idx++) {
            HwListener<K, V> hwListener = weakListeners.get(idx).get();
            if(hwListener != null && hwListener.equals(listener)) {
                weakListeners.remove(idx);
            }
        }
    }

    private void notify(K key, V value, String action) {
        ArrayList<WeakReference<HwListener<K, V>>> weakListeners = listenersRef.get();
        if(weakListeners == null) return;

        weakListeners.forEach(weakListener -> {
            HwListener<K, V> listener = weakListener.get();
            if(listener != null) {
                listener.notify(key, value, action);
            }
        });
    }
}
