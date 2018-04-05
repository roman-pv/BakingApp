package com.example.roman.bakingapp.ui.main;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.example.roman.bakingapp.data.DataRepository;

import javax.inject.Inject;

public class MainActivityViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final DataRepository mRepository;
    private final Application mApplication;

    @Inject
    public MainActivityViewModelFactory(DataRepository repository, Application application) {
        this.mRepository = repository;
        this.mApplication = application;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {

        return (T) new MainActivityViewModel(mRepository, mApplication);
    }

}
