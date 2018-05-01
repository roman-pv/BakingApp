package com.example.roman.bakingapp.ui.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.VisibleForTesting;

import com.example.roman.bakingapp.data.DataRepository;
import com.example.roman.bakingapp.data.model.RecipeWithStepsAndIngredients;

import javax.inject.Inject;

/**
 * Mediates between StepsFragment and RecipeDetailsFragment and Data Repository,
 * fetching necessary data for the Fragments to display
 */
public class StepsViewModel extends ViewModel {

    private DataRepository mRepository;

    @Inject
    public StepsViewModel(DataRepository repository) {
        this.mRepository = repository;
    }

    @VisibleForTesting
    public LiveData<RecipeWithStepsAndIngredients> getRecipe(int id) {
        return mRepository.getRecipeById(id);
    }


}
