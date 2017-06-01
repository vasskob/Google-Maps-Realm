package com.task.vasskob.googlemaps.app;

import android.app.Application;
import android.content.Context;

import com.raizlabs.android.dbflow.config.DatabaseConfig;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.runtime.DirectModelNotifier;
import com.task.vasskob.googlemaps.R;
import com.task.vasskob.googlemaps.screens.common.model.db.DbFlowDatabase;
import com.task.vasskob.googlemaps.screens.common.model.entity.MarkerIcon;

import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {
    public static final int COUNT_OF_COLUMN = 4;
    private static MyApplication sMyApplication;

    @Override
    public void onCreate() {
        super.onCreate();

        sMyApplication = this;

        FlowManager.init(new FlowConfig.Builder(this)
                .addDatabaseConfig(new DatabaseConfig.Builder(DbFlowDatabase.class)
                        .modelNotifier(DirectModelNotifier.get())
                        .build()).build());
    }

    public static Context getContext() {
        return sMyApplication;
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
