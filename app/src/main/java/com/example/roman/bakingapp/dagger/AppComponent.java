package com.example.roman.bakingapp.dagger;

import android.app.Application;

import com.example.roman.bakingapp.BakingApp;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

/**
 * Application component connecting modules for dependency injection.
 */
@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        NetworkModule.class,
        DatabaseModule.class,
        ViewModelModule.class,
        AppModule.class,
        ActivityBuilder.class,
        ServicesBuilder.class})
public interface AppComponent extends AndroidInjector<BakingApp> {

    void inject(BakingApp app);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }
}
