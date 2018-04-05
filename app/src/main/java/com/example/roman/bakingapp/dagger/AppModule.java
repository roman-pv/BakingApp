package com.example.roman.bakingapp.dagger;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public interface AppModule {

    @Binds
    @Singleton
    Context provideContext(Application application);
}
