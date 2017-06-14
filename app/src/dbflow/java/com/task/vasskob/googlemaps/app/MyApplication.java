package com.task.vasskob.googlemaps.app;

import android.app.Application;

import com.raizlabs.android.dbflow.config.DatabaseConfig;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.runtime.DirectModelNotifier;
import com.task.vasskob.googlemaps.R;
import com.task.vasskob.googlemaps.app.di.DaggerMyAppComponent;
import com.task.vasskob.googlemaps.app.di.MyAppComponent;
import com.task.vasskob.googlemaps.app.di.MyAppModule;
import com.task.vasskob.googlemaps.screens.common.model.db.DbFlowDatabase;
import com.task.vasskob.googlemaps.screens.common.model.entity.MarkerIcon;

import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {
    public static final int COUNT_OF_COLUMN = 4;
    private static MyApplication instance;

    private MyAppComponent myAppComponent;

    public MyAppComponent getMyAppComponent() {
        return myAppComponent;
    }

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        myAppComponent = initDagger(this);

        FlowManager.init(new FlowConfig.Builder(this)
                .addDatabaseConfig(new DatabaseConfig.Builder(DbFlowDatabase.class)
                        .modelNotifier(DirectModelNotifier.get())
                        .build()).build());
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
