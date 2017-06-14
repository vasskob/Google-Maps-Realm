package com.task.vasskob.googlemaps.app.di;

import com.task.vasskob.googlemaps.screens.common.model.repository.MarkerRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
class RepositoryModule {

    @Provides
    @Singleton
    MarkerRepository provideMarkerRepository() {
        return new MarkerRepository();
    }
}
