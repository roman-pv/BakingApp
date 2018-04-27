package com.example.roman.bakingapp.ui.main;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.VisibleForTesting;

import com.example.roman.bakingapp.data.DataRepository;
import com.example.roman.bakingapp.data.model.RecipeWithStepsAndIngredients;

import java.util.List;

import javax.inject.Inject;

public class RecipesViewModel extends ViewModel {

    private final DataRepository mRepository;

    @SuppressWarnings("unchecked")
    @Inject
    public RecipesViewModel(DataRepository repository) {
        this.mRepository = repository;
    }

    @VisibleForTesting
    public LiveData<List<RecipeWithStepsAndIngredients>> getRecipes() {
        return mRepository.getRecipes();
    }

    public int getRecipeIdPreference() {
        return mRepository.getRecipeIdPreference();
    }

    public void setRecipeIdPreference(int recipeId) {
        mRepository.setRecipeIdPreference(recipeId);
    }

}
