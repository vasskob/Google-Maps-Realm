package com.task.vasskob.googlemapsrealm.model.realm;

import com.task.vasskob.googlemapsrealm.model.Marker;
import com.task.vasskob.googlemapsrealm.model.MarkerIcon;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmResults;

public class RealmController {

    private OnChangeListener listener;

    public interface OnChangeListener {
        void onChangeM(RealmResults<Marker> results);
    }
//
//

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
//   realm.refresh();
//   realm.setAutoRefresh(true);

   //  realm.waitForChange();
    }

    public void addMarkerToRealm(final Marker marker) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(marker);
            }
        });
    }

    public void deleteMarkerInRealm(final Marker marker) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                marker.deleteFromRealm();
            }
        });
    }

    public void updateMarkerInRealm(final Marker marker, final String title, final MarkerIcon markerIcon) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                marker.setTitle(title);
                if (markerIcon != null) {
                    MarkerIcon mIcon = realm.createObject(MarkerIcon.class);
                    mIcon.setId(markerIcon.getId());
                    mIcon.setResId(markerIcon.getResId());
                    marker.setMarkerIcon(mIcon);
                }
            }
        });
    }

    public void getAllMarkers() {
        //   return realm.allObjects(Marker.class);
        //   results.addChangeListener(listener);
        RealmResults results=realm.where(Marker.class).findAllAsync();
        //  callback.onChange(null, null);
        results.addChangeListener(callback);


    }

    public Marker getMarker(long id) {
        return realm.where(Marker.class).equalTo("id", id).findFirstAsync();
    }

    public void closeRealm() {
       // realm.close();
    }

    private OrderedRealmCollectionChangeListener<RealmResults<Marker>> callback = new OrderedRealmCollectionChangeListener<RealmResults<Marker>>() {
        @Override
        public void onChange(RealmResults<Marker> collection, OrderedCollectionChangeSet changeSet) {
       //     mMapsView.showMarkers(collection);
        }
    };

}