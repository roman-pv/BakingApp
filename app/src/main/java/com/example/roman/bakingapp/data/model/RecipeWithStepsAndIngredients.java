package com.example.roman.bakingapp.data.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Relation;

import java.util.List;

public class RecipeWithStepsAndIngredients {
    @Embedded
    public RecipeEntity recipe;
    @Relation(parentColumn = "id", entityColumn = "recipeId", entity = Ingredient.class)
    public List<Ingredient> ingredients;
    @Relation(parentColumn = "id", entityColumn = "recipeId", entity = Step.class)
    public List<Step> steps;

    @Ignore
    public RecipeWithStepsAndIngredients(RecipeEntity recipe, List<Ingredient> ingredients,
                                         List<Step> steps) {
        this.recipe = recipe;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    public RecipeWithStepsAndIngredients() {
    }


    public Integer getId() {
        return recipe.getId();
    }

    public String getName() {
        return recipe.getName();
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public int getServings() {
        return recipe.getServings();
    }

    public String getImage() {
        return recipe.getImage();
    }
}