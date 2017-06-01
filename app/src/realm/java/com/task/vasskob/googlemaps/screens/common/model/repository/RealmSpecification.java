package com.task.vasskob.googlemaps.screens.common.model.repository;

import io.realm.Realm;

interface RealmSpecification extends Specification {
    void toRealmResults(Realm realm);
    void removeListeners();
}
