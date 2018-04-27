package com.example.roman.bakingapp.dagger;

import com.example.roman.bakingapp.ui.detail.RecipeDetailsActivity;
import com.example.roman.bakingapp.ui.detail.StepsActivity;
import com.example.roman.bakingapp.ui.main.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = FragmentsProvider.class)
    abstract MainActivity bindMainActivity();

    @ContributesAndroidInjector(modules = {FragmentsProvider.class})
    abstract StepsActivity bindStepsActivity();

    @ContributesAndroidInjector(modules = {FragmentsProvider.class})
    abstract RecipeDetailsActivity bindRecipeDetailsActivity();

}
