package com.example.roman.bakingapp.dagger;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.roman.bakingapp.data.local.RecipeDao;
import com.example.roman.bakingapp.data.local.RecipeDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Provides instances of all classes required access the database
 */
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

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
