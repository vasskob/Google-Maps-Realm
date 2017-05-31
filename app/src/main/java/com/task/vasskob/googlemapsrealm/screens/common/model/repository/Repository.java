package com.task.vasskob.googlemapsrealm.screens.common.model.repository;


import java.util.List;

public interface Repository<T> {
    void add(T item);
    void add(List<T> list);

    void update(T item);
    void delete(T item);
    List<T> query(Specification specification);
}