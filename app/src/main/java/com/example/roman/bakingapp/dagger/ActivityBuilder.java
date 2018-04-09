package com.example.roman.bakingapp.dagger;

import com.example.roman.bakingapp.ui.detail.DetailActivity;
import com.example.roman.bakingapp.ui.detail.DetailActivityModule;
import com.example.roman.bakingapp.ui.detail.DetailFragmentsProvider;
import com.example.roman.bakingapp.ui.detail.StepDetailActivity;
import com.example.roman.bakingapp.ui.main.MainActivity;
import com.example.roman.bakingapp.ui.main.MainActivityModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = MainActivityModule.class)
    abstract MainActivity bindMainActivity();

    @ContributesAndroidInjector(modules = {DetailActivityModule.class, DetailFragmentsProvider.class})
    abstract DetailActivity bindDetailActivity();

    @ContributesAndroidInjector(modules = {DetailFragmentsProvider.class})
    abstract StepDetailActivity bindStepDetailActivity();

}
