package com.task.vasskob.googlemaps;

import com.task.vasskob.googlemaps.screens.common.model.repository.MarkerRepositories;
import com.task.vasskob.googlemaps.screens.common.model.repository.MarkerRepository;

public class Injection {
    public static MarkerRepository provideMarkerRepository() {
        return MarkerRepositories.getInMemoryRepoInstance();
    }
}
