package com.example.roman.bakingapp.dagger;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.example.roman.bakingapp.ui.ViewModelFactory;
import com.example.roman.bakingapp.ui.detail.StepsViewModel;
import com.example.roman.bakingapp.ui.main.RecipesViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * Allows Dagger to map the key (class) to a value (object) for ViewModelFactory
 */
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(StepsViewModel.class)
    abstract ViewModel bindStepsViewModel(StepsViewModel stepsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RecipesViewModel.class)
    abstract ViewModel bindRecipesViewModel(RecipesViewModel recipesViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}
