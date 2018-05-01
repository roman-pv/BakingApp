package com.example.roman.bakingapp.data.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.roman.bakingapp.data.model.Ingredient;
import com.example.roman.bakingapp.data.model.Recipe;
import com.example.roman.bakingapp.data.model.RecipeEntity;
import com.example.roman.bakingapp.data.model.RecipeWithStepsAndIngredients;
import com.example.roman.bakingapp.data.model.Step;

import java.util.ArrayList;
import java.util.List;


/**
 * Some ideas were taken from this post:
 * https://stackoverflow.com/questions/44667160/android-room-insert-relation-entities-using-room
 */
@Dao
public abstract class RecipeDao {

    @Query("SELECT * FROM recipes")
    public abstract LiveData<List<RecipeWithStepsAndIngredients>> getAllRecipes();

    @Query("SELECT * FROM recipes WHERE id = :id")
    public abstract LiveData<RecipeWithStepsAndIngredients> getRecipeById(int id);

    @Query("SELECT * FROM recipes WHERE id = :id")
    public abstract RecipeWithStepsAndIngredients getRecipeByIdOffline(int id);

    public void bulkInsert(List<Recipe> recipes) {

        List<RecipeEntity> recipeEntities = new ArrayList<>();
        for (Recipe recipe : recipes) {
            Integer recipeId = recipe.getId();
            _insertRecipe(new RecipeEntity(recipeId, recipe.getName(),
                    recipe.getServings(), recipe.getImage()));
            insertStepsForRecipe(recipeId, recipe.getSteps());
            insertIngredientsForRecipe(recipeId, recipe.getIngredients());
        }
    }

    public void insertIngredientsForRecipe(Integer recipeId, List<Ingredient> ingredients) {
        for (Ingredient ingredient : ingredients) {
            ingredient.setRecipeId(recipeId);
        }
        _insertIngredients(ingredients);
    }

    public void insertStepsForRecipe(Integer recipeId, List<Step> steps) {
        for (Step step : steps) {
            step.setRecipeId(recipeId);
        }
        _insertSteps(steps);
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void _insertRecipe(RecipeEntity recipe);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void _insertIngredients(List<Ingredient> ingredients);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void _insertSteps(List<Step> steps);

}
