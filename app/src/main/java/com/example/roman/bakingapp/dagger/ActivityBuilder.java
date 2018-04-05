package com.example.roman.bakingapp.dagger;

import com.example.roman.bakingapp.ui.detail.DetailActivity;
import com.example.roman.bakingapp.ui.detail.DetailActivityModule;
import com.example.roman.bakingapp.ui.main.MainActivity;
import com.example.roman.bakingapp.ui.main.MainActivityModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = MainActivityModule.class)
    abstract MainActivity bindMainActivity();

    @ContributesAndroidInjector(modules = DetailActivityModule.class)
    abstract DetailActivity bindDetailActivity();

}
