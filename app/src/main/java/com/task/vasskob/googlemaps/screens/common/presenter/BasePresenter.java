package com.task.vasskob.googlemaps.screens.common.presenter;

import com.task.vasskob.googlemaps.screens.common.model.repository.MarkerRealmRepository;
import com.task.vasskob.googlemaps.screens.common.model.repository.Repository;

public class BasePresenter {

    protected final Repository realmRepository;

    protected BasePresenter() {
        realmRepository = new MarkerRealmRepository();
    }
}
