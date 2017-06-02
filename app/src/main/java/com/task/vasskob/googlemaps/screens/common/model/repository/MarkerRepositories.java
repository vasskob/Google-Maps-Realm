package com.task.vasskob.googlemaps.screens.common.model.repository;


public class MarkerRepositories {

    private MarkerRepositories() {
    }

    private static MarkerRepository repository = null;

    public synchronized static MarkerRepository getInMemoryRepoInstance() {

        if (null == repository) {
            repository = new MarkerRepository();
        }
        return repository;
    }
}
