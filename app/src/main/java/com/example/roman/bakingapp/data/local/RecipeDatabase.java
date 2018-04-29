package com.example.roman.bakingapp.data.local;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.roman.bakingapp.data.model.Ingredient;
import com.example.roman.bakingapp.data.model.RecipeEntity;
import com.example.roman.bakingapp.data.model.Step;

@Database(entities = {RecipeEntity.class, Step.class, Ingredient.class},
        version = 1, exportSchema = false)
public abstract class RecipeDatabase extends RoomDatabase {
    public abstract RecipeDao recipeDao();
}
