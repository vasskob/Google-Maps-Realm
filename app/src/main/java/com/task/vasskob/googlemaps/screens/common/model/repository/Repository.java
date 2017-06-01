package com.task.vasskob.googlemaps.screens.common.model.repository;


import com.task.vasskob.googlemaps.listeners.db.OnMarkerChangeClickListener;

import java.util.List;

public interface Repository<T> {
    void add(T item);
    void add(List<T> list);
    void update(T item);
    void delete(T item);
    List<T> query(Specification specification);

    void setResultListener(OnMarkerChangeClickListener listener);

    void removeListener();
}