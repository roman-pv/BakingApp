package com.example.roman.bakingapp.dagger;

import com.example.roman.bakingapp.widget.IngredientsWidgetService;
import com.example.roman.bakingapp.widget.ListWidgetService;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ServicesBuilder {

        @ContributesAndroidInjector()
        abstract ListWidgetService bindListWidgetService();

        @ContributesAndroidInjector()
        abstract IngredientsWidgetService bindIngredientsWidgetService();

}
