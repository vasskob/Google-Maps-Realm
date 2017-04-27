package com.task.vasskob.googlemapsrealm.utils;

public interface Mapper<FROM, TO> {
    TO map (FROM data);
}
