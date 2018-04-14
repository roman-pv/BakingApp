package com.example.roman.bakingapp.dagger;

import com.example.roman.bakingapp.ui.detail.FragmentsProvider;
import com.example.roman.bakingapp.ui.detail.RecipeDetailsActivity;
import com.example.roman.bakingapp.ui.detail.StepsActivity;
import com.example.roman.bakingapp.ui.main.MainActivity;
import com.example.roman.bakingapp.ui.main.MainActivityModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = MainActivityModule.class)
    abstract MainActivity bindMainActivity();

    @ContributesAndroidInjector(modules = {FragmentsProvider.class})
    abstract StepsActivity bindDetailActivity();

    @ContributesAndroidInjector(modules = {FragmentsProvider.class})
    abstract RecipeDetailsActivity bindStepDetailActivity();

}
