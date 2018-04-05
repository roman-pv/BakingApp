package com.example.roman.bakingapp.ui.main;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.roman.bakingapp.data.DataRepository;
import com.example.roman.bakingapp.data.model.Recipe;

import java.util.List;

import javax.inject.Inject;

public class MainActivityViewModel extends ViewModel {

    private DataRepository mRepository;
    private Application mApplication;

    @Inject
    public MainActivityViewModel(DataRepository repository, Application application) {
        this.mRepository = repository;
        this.mApplication = application;
    }

    public LiveData<List<Recipe>> getRecipes() {
        return mRepository.getRecipes();
    }


}
