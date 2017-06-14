package com.task.vasskob.googlemaps.app.di;

import com.task.vasskob.googlemaps.app.DummyData;
import com.task.vasskob.googlemaps.screens.detail.view.MarkerInfoActivity;
import com.task.vasskob.googlemaps.screens.map.presenter.MapsPresenterImpl;
import com.task.vasskob.googlemaps.screens.map.view.MapsActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {MyAppModule.class, RepositoryModule.class})
public interface MyAppComponent {
    void inject(MapsActivity activity);

    void inject(DummyData dummyData);

    void inject(MapsPresenterImpl presenter);
    void inject(MarkerInfoActivity activity);
}
