package com.example.roman.bakingapp.dagger;

import com.example.roman.bakingapp.ui.detail.RecipeDetailsFragment;
import com.example.roman.bakingapp.ui.detail.StepsFragment;
import com.example.roman.bakingapp.ui.main.RecipesFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentsProvider {

    @ContributesAndroidInjector()
    abstract RecipeDetailsFragment provideRecipeDetailsFragmentFactory();

    @ContributesAndroidInjector()
    abstract StepsFragment provideStepsFragmentFactory();

    @ContributesAndroidInjector()
    abstract RecipesFragment provideRecipesFragmentFactory();
}
