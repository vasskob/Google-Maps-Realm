package com.task.vasskob.googlemapsrealm.screens.common.presenter;

import com.task.vasskob.googlemapsrealm.screens.common.model.db.RealmController;

public class BasePresenter {

    protected final RealmController realmController;

    public BasePresenter() {
        realmController = RealmController.getInstance();
    }
}
