package com.task.vasskob.googlemapsrealm.screens.common.presenter;

import com.task.vasskob.googlemapsrealm.screens.common.model.repository.MarkerRealmRepository;

public class BasePresenter {

    // protected final RealmController realmController;
    protected final MarkerRealmRepository realmRepository;

    public BasePresenter() {
        // realmController = RealmController.getInstance();
        realmRepository = new MarkerRealmRepository();
    }
}
