package com.task.vasskob.googlemapsrealm.screens.common.presenter;

import com.task.vasskob.googlemapsrealm.screens.common.model.repository.MarkerRealmRepository;
import com.task.vasskob.googlemapsrealm.screens.common.model.repository.Repository;

public class BasePresenter {

    protected final Repository realmRepository;

    protected BasePresenter() {
        realmRepository = new MarkerRealmRepository();
    }
}
