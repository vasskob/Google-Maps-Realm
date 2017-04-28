package com.task.vasskob.googlemapsrealm.presenter;

import com.task.vasskob.googlemapsrealm.model.realm.RealmController;

class BasePresenter {

    final RealmController realmController;

    BasePresenter() {
        realmController = RealmController.getInstance();
    }
}
