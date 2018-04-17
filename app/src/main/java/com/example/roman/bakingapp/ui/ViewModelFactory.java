package com.example.roman.bakingapp.ui;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.example.roman.bakingapp.data.DataRepository;
import com.example.roman.bakingapp.ui.detail.StepsViewModel;
import com.example.roman.bakingapp.ui.main.MainActivityViewModel;

import javax.inject.Inject;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final DataRepository mRepository;
    private final Application mApplication;

    @Inject
    public ViewModelFactory(DataRepository repository, Application application) {
        this.mRepository = repository;
        this.mApplication = application;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainActivityViewModel.class)) {
            return (T) new MainActivityViewModel(mRepository, mApplication);
        } else if (modelClass.isAssignableFrom(StepsViewModel.class)) {
            return (T) new StepsViewModel(mRepository, mApplication);
        }

        throw new IllegalArgumentException(
                modelClass.getName() + " ViewModel is not supported.");

    }

}
