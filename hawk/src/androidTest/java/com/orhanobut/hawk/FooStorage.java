package com.orhanobut.hawk;

import android.util.Pair;

import java.util.HashMap;
import java.util.List;

/**
 * In-memory storage class useful for unit testing. Not really that type-safe at all.
 * Created by bosgood on 2/27/15.
 */
public class FooStorage implements Storage {
    HashMap<String, Object> storage = new HashMap<>();

    @Override
    public <T> boolean put(String key, T value) {
        storage.put(key, value);
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(String key) {
        return (T) storage.get(key);
    }

    @Override
    public boolean remove(String key) {
        Object removedObject = storage.remove(key);
        return removedObject != null;
    }

    @Override
    public boolean put(List<Pair<String, ?>> items) {
        for (Pair<String, ?> pair : items) {
            put(pair.first, pair.second);
        }
        return true;
    }

    @Override
    public boolean remove(String... keys) {
        for (String key : keys) {
            remove(key);
        }
        return true;
    }

    @Override
    public boolean clear() {
        storage.clear();
        return true;
    }

    @Override
    public int count() {
        return storage.size();
    }

    @Override
    public boolean contains(String key) {
        return storage.containsKey(key);
    }
}
