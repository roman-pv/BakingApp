package com.example.roman.bakingapp.dagger;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.example.roman.bakingapp.data.local.RecipeDao;
import com.example.roman.bakingapp.data.local.RecipeDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {

    @Provides
    @Singleton
    RecipeDatabase provideDatabase(Context context) {
        return Room.databaseBuilder(context, RecipeDatabase.class, "recipes.db").build();
    }

    @Provides
    @Singleton
    RecipeDao provideDao(RecipeDatabase db) {
        return db.recipeDao();
    }
}
