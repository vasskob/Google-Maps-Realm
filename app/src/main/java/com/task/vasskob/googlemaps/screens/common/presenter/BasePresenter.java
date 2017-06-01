package com.task.vasskob.googlemaps.screens.common.presenter;

import com.task.vasskob.googlemaps.screens.common.model.repository.MarkerRepository;
import com.task.vasskob.googlemaps.screens.common.model.repository.Repository;

public class BasePresenter {

    protected final Repository repository;

    protected BasePresenter() {
        repository = new MarkerRepository();
    }
}
