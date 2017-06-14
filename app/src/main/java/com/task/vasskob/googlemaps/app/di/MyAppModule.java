package com.task.vasskob.googlemaps.app.di;

import android.app.Application;
import android.content.Context;

import com.task.vasskob.googlemaps.utils.Prefs;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MyAppModule {
    private Application application;

    public MyAppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return application;
    }

    @Provides
    @Singleton
    Prefs providePrefs(Context context) {
        return Prefs.with(context);
    }
}

