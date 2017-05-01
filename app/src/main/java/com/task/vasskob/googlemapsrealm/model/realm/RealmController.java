package com.task.vasskob.googlemapsrealm.model.realm;

import com.task.vasskob.googlemapsrealm.model.Marker;
import com.task.vasskob.googlemapsrealm.model.MarkerIcon;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class RealmController {

//    private OnChangeListener listener;
//
//    public interface OnChangeListener extends RealmChangeListener {
//        void onChange(RealmResults<Marker> results);
//    }

    private OrderedRealmCollectionChangeListener<RealmResults<Marker>> callback = new OrderedRealmCollectionChangeListener<RealmResults<Marker>>() {
        @Override
        public void onChange(RealmResults<Marker> collection, OrderedCollectionChangeSet changeSet) {

        }
    };

    private static RealmController instance;
    private final Realm realm;

    private RealmController() {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController getInstance() {
        if (instance == null) {
            instance = new RealmController();
        }
        return instance;
    }

    public void refresh() {
//        realm.refresh();
    }

    public void addMarkerToRealm(final Marker marker) {
        realm.beginTransaction();
        realm.copyToRealm(marker);
        realm.commitTransaction();
    }

    public void deleteMarkerInRealm(Marker marker) {
        realm.beginTransaction();
//        marker.removeFromRealm();
        realm.commitTransaction();
    }

    public void updateMarkerInRealm(Marker marker, String title, MarkerIcon markerIcon) {
        realm.beginTransaction();
        marker.setTitle(title);
        if (markerIcon != null) {
            MarkerIcon mIcon = realm.createObject(MarkerIcon.class);
            mIcon.setId(markerIcon.getId());
            mIcon.setResId(markerIcon.getResId());
            marker.setMarkerIcon(mIcon);
        }
        realm.commitTransaction();
    }

    public RealmResults<Marker> getAllMarkers() {
        //   return realm.allObjects(Marker.class);
      //   results.addChangeListener(listener);

        callback.onChange(null, null);
        return realm.where(Marker.class).findAllAsync();
    };

    public Marker getMarker(long id) {
        return realm.where(Marker.class).equalTo("id", id).findFirstAsync();
    }

    public void closeRealm() {
        realm.close();
    }

//    public void setOnChangeListener(OnChangeListener listener) {
//        this.listener = listener;
//    }


//    public boolean hasMarkers() {
//        return !realm.allObjects(Marker.class).isEmpty();
//    }
//
//    //query example
//    public RealmResults<Marker> queryedMarkers() {
//        return realm.where(Marker.class)
//                .contains("mLabel", "a")
//                .or()
//                .contains("mIcon", "icon_path")
//                .findAllAsync();
//
//    }
//
//    public void clearAll() {
//        realm.beginTransaction();
//        realm.clear(Marker.class);
//        realm.commitTransaction();
//    }
}