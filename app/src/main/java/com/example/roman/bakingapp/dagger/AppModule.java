package com.example.roman.bakingapp.dagger;

import android.app.Application;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    @Provides
    Context provideContext(Application application) {
        return application;
    }
}
