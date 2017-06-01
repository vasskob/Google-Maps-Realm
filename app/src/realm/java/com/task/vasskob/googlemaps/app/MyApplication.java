package com.task.vasskob.googlemaps.app;

import android.app.Application;

import com.task.vasskob.googlemaps.R;
import com.task.vasskob.googlemaps.screens.common.model.entity.MarkerIcon;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application {
    public static final int COUNT_OF_COLUMN = 4;

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    public static List<MarkerIcon> getMarkerIconsList() {
        List<MarkerIcon> markerIcons = new ArrayList<>();
        markerIcons.add(new MarkerIcon(1, R.drawable.ic_icon1));
        markerIcons.add(new MarkerIcon(2, R.drawable.ic_icon2));
        markerIcons.add(new MarkerIcon(3, R.drawable.ic_icon3));
        markerIcons.add(new MarkerIcon(4, R.drawable.ic_icon4));
        return markerIcons;
    }
}
