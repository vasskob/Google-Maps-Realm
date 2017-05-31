package com.task.vasskob.googlemapsrealm.screens.common.model.repository;


import com.task.vasskob.googlemapsrealm.screens.common.model.MarkerIcon;

import java.util.List;

interface Repository<T> {
    void add(T item);
    void add(List<T> list);
    void update(T item, String title, MarkerIcon markerIcon);
    void delete(T item);
    List<T> query(Specification specification);
}