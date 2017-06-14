package com.task.vasskob.googlemaps.app;

import android.app.Application;

import com.task.vasskob.googlemaps.R;
import com.task.vasskob.googlemaps.app.di.DaggerMyAppComponent;
import com.task.vasskob.googlemaps.app.di.MyAppComponent;
import com.task.vasskob.googlemaps.app.di.MyAppModule;
import com.task.vasskob.googlemaps.screens.common.model.entity.MarkerIcon;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application {
    public static final int COUNT_OF_COLUMN = 4;

    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    private MyAppComponent myAppComponent;

    public MyAppComponent getMyAppComponent() {
        return myAppComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        myAppComponent = initDagger(this);
        instance = this;

        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

    }

    private MyAppComponent initDagger(MyApplication myApplication) {
        return DaggerMyAppComponent.builder()
                .myAppModule(new MyAppModule(myApplication))
                .build();
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
