package com.task.vasskob.googlemaps.utils;

public interface Mapper<FROM, TO> {
    TO map (FROM data);
}
