package com.example.roman.bakingapp.ui.detail;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.VisibleForTesting;

import com.example.roman.bakingapp.data.DataRepository;
import com.example.roman.bakingapp.data.model.RecipeWithStepsAndIngredients;

import java.util.List;

import javax.inject.Inject;

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
