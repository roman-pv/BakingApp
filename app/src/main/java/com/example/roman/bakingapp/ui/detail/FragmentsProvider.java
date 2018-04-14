package com.example.roman.bakingapp.ui.detail;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentsProvider {

    @ContributesAndroidInjector()
    abstract RecipeDetailsFragment provideStepsDetailsFragmentFactory();

    @ContributesAndroidInjector()
    abstract StepsFragment provideStepsOverviewFragmentFactory();
}
