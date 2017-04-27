package com.task.vasskob.googlemapsrealm.realm;

import android.app.Activity;
import android.app.Application;

import com.task.vasskob.googlemapsrealm.model.Marker;
import com.task.vasskob.googlemapsrealm.model.MarkerIcon;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmController {

    private static RealmController instance;
    private final Realm realm;

    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }


    public static RealmController with(Activity activity) {

        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {

        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance() {

        return instance;
    }

    public Realm getRealm() {

        return realm;
    }

    //Refresh the realm instance
    public void refresh() {

        realm.refresh();
    }

    //clear all objects from Marker.class
    public void clearAll() {

        realm.beginTransaction();
        realm.clear(Marker.class);
        realm.commitTransaction();
    }

    //find all objects in the Marker.class
    public RealmResults<Marker> getMarkers() {

        return realm.where(Marker.class).findAll();
      //  return realm.where(Marker.class).findAllAsync();
    }

    //query a single item with the given id
    public Marker getMarker(long id) {

        return realm.where(Marker.class).equalTo("id", id).findFirst();
    }


    //check if Marker.class is empty
    public boolean hasMarkers() {

        return !realm.allObjects(Marker.class).isEmpty();
    }

    //query example
    public RealmResults<Marker> queryedMarkers() {

        return realm.where(Marker.class)
                .contains("mLabel", "a")
                .or()
                .contains("mIcon", "icon_path")
                .findAll();

    }
}