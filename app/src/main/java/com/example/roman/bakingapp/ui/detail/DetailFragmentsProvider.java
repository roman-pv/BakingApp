package com.example.roman.bakingapp.ui.detail;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class DetailFragmentsProvider {

    @ContributesAndroidInjector()
    abstract StepsDetailsFragment provideStepsDetailsFragmentFactory();

    @ContributesAndroidInjector()
    abstract StepsOverviewFragment provideStepsOverviewFragmentFactory();
}
